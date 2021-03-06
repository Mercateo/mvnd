/*
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

import io.grpc.Server;
import io.grpc.ServerBuilder;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MVNDaemon {
	private static int DEFAULT_PORT = 1971;

	private final int port;
	private Server server;

	private void start() throws IOException {
		System.out.println("mvnd binding to localhost:" + port);
		server = ServerBuilder.forPort(port).addService(new MVNDaemonService()).build().start();
		System.out.println("mvnd listening on " + port);
		registerShutdownHook();
	}

	private void registerShutdownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				System.err.println("");
				System.out.println("shutting down gRPC server since JVM is shutting down");
				MVNDaemon.this.stop();
				System.out.println("server shut down complete");
			}
		});
	}

	private synchronized void stop() {
		if (server != null) {
			server.shutdown();
			server = null;
		}
	}

	private void blockUntilShutdown() throws InterruptedException {
		if (server != null) {
			server.awaitTermination();
			System.out.println("gRPC Server terminated");
		}
	}

	public static void main(String[] args) throws IOException, InterruptedException {

		if (args != null && args.length > 0) {
			new CommandLineHandler(args).run();
		} else {
			MVNDaemon daemon = new MVNDaemon(portFromEnv(args));
			daemon.start();
			daemon.blockUntilShutdown();
		}
	}



	

	private static int portFromEnv(String[] args) {
		String port = System.getenv("MVND_PORT");
		if (port != null) {
			int p = Integer.parseInt(port);
			if (p > 0)
				return p;
		}
		return DEFAULT_PORT;
	}

}
