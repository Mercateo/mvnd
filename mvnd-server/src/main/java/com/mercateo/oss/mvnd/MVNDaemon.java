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

import io.grpc.Server;
import io.grpc.ServerBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class MVNDaemon {
	private static int DEFAULT_PORT = 1971;

	private final int port;
	private Server server;

	private void start() throws IOException {
		log.info("binding to localhost:" + port);
		server = ServerBuilder.forPort(port).addService(new MVNDaemonService()).build().start();
		log.info("listening on " + port);
		registerShutdownHook();
	}

	private void registerShutdownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				System.err.println("");
				log.info("shutting down gRPC server since JVM is shutting down");
				MVNDaemon.this.stop();
				log.info("server shut down complete");
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
			log.info("gRPC Server terminated");
		}
	}

	public static void main(String[] args) throws IOException, InterruptedException {

		if (args != null && args.length > 0) {
			if (args.length == 2)
				if (args[0].trim().toLowerCase().startsWith("--install"))
					install(args);

			help();
		} else {
			MVNDaemon daemon = new MVNDaemon(portFromEnv(args));
			daemon.start();
			daemon.blockUntilShutdown();
		}
	}

	private static void help() {
		System.out.println("Usage: ");

		System.out.println("");
		System.out.println(" mvnd --install /path/to/apache/maven");
		System.out.println("    Patch a fresh maven installation to use MVND");
		System.out.println("");
		System.out.println(" mvnd");
		System.out.println(
				"    Run mvnd and accept connections from mvnc on port 1971 (or the port defined by MVND_PORT environment variable.");
		System.out.println("");

		System.exit(2);
	}

	private static void install(String[] args) {
		if (args.length != 2)
			help();
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
