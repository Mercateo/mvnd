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
import java.lang.reflect.Field;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.spi.LocationAwareLogger;

import com.google.common.collect.Sets;

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
		// TODO use proper cmdline parser lib and add handler for -? and help
		if (Sets.newHashSet(args).contains("--debug"))
			try {// This is actually a MavenSimpleLogger, but due to various classloader issues,
					// can't work with the directly.
				log.info("Setting debug loglevel");
				Logger l = LoggerFactory.getLogger(MVNDaemon.class.getCanonicalName());
				Field f = l.getClass().getSuperclass().getDeclaredField("currentLogLevel");
				f.setAccessible(true);
				f.set(l, LocationAwareLogger.DEBUG_INT);
				f.set(LoggerFactory.getLogger(MVNDaemonService.class.getCanonicalName()),LocationAwareLogger.DEBUG_INT);
				
			} catch (Exception e) {
				log.warn("Failed to reset the log level", e);
			}
		int portFromCmdLine = portFromCmdLine(args);
		MVNDaemon daemon = new MVNDaemon(portFromCmdLine);
		daemon.start();
		daemon.blockUntilShutdown();
	}

	private static int portFromCmdLine(String[] args) {
		return Arrays.stream(args).filter(MVNDaemon::isPort).mapToInt(s -> Integer.parseInt(s.trim().substring(7)))
				.findFirst().orElse(DEFAULT_PORT);
	}

	private static boolean isPort(String arg) {
		return arg != null && arg.trim().startsWith("--port=");
	}
}
