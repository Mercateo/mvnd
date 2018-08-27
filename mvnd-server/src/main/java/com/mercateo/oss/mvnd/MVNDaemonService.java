/**
 * Copyright © 2018 Mercateo AG (http://www.mercateo.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mercateo.oss.mvnd;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.maven.cli.MavenCli;

import com.mercateo.oss.mvnd.MVNDProto.InvokeRequest;
import com.mercateo.oss.mvnd.MVNDProto.InvokeResponse;
import com.mercateo.oss.mvnd.MVNDProto.InvokeResponse.ResponseType;
import com.mercateo.oss.mvnd.MVNDServiceGrpc.MVNDServiceImplBase;

import io.grpc.stub.StreamObserver;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

public class MVNDaemonService extends MVNDServiceImplBase {
	private final ExecutorService es = Executors.newCachedThreadPool();

	public MVNDaemonService() {
		ThreadLocalSystemPrintStreams.install();
	}

	@Override
	@SneakyThrows
	public void invoke(InvokeRequest request, StreamObserver<InvokeResponse> responseObserver) {
		try {

			String workDir = request.getWorkDir();

			Set<Future<?>> writers = prepareStreams(responseObserver);

			// add some debug loggin

			// todo try to detect reading from System.in and killing the invocation, as a
			// failsafe
			// maybe even restarting adding -B

			// there should be a general setting for using -B as a default

			// alternatively, we could also provide sending input to the protocol. should be
			// easy to do, but is it worth it?

			MavenCli cli = new MavenCli();

			List<String> argList = new LinkedList<>(request.getArgsList());
			String[] args = new String[argList.size()];
			argList.toArray(args);

			try {
				// streams are prepared already, so we pass nulls here
				int exit = cli.doMain(args, workDir, null, null);

				System.out.close();
				System.err.close();

				writers.forEach(t -> {
					try {
						t.get(100, TimeUnit.MILLISECONDS);
					} catch (TimeoutException streamsDidNotClose) {
						System.out.println(
								"streams did not close properly. Unexpected, but not a problem. " + streamsDidNotClose);
					} catch (InterruptedException meh) {
					} catch (ExecutionException e) {
						System.out.println("Exception while writing out to remote. Unexpected, but not a problem." + e);
					}
				});

				terminateCall(responseObserver, exit);

			} catch (Throwable e) {
				// remember, this will be redirected to client and probably should be, as it
				// most likely came from mavenCli itself
				e.printStackTrace(System.err);
				terminateCall(responseObserver, 1);
			} finally {
				releaseStreams();
			}
		} catch (Throwable e) {
			System.err.println("invocation failed: ");
			e.printStackTrace(System.err);
		}
	}

	private void releaseStreams() {
		ThreadLocalSystemPrintStreams.release();
	}

	private void terminateCall(StreamObserver<InvokeResponse> responseObserver, int exit) {
		responseObserver.onNext(InvokeResponse.newBuilder().setErrorCode(exit).setType(ResponseType.EXIT).build());
		responseObserver.onCompleted();
	}

	private Set<Future<?>> prepareStreams(StreamObserver<InvokeResponse> responseObserver) {

		Set<Future<?>> writers = new HashSet<>();

		PrintStream out;
		PrintStream err;

		try {
			PipedInputStream i = new PipedInputStream(8192);
			PipedOutputStream o = new PipedOutputStream(i);
			writers.add(es.submit(new RemoteStreamWriter(i, responseObserver, ResponseType.OUT)));
			out = new PrintStream(o);

			i = new PipedInputStream(8192);
			o = new PipedOutputStream(i);
			writers.add(es.submit(new RemoteStreamWriter(i, responseObserver, ResponseType.ERR)));
			err = new PrintStream(o);

			ThreadLocalSystemPrintStreams.register(out, err);

			return writers;

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
