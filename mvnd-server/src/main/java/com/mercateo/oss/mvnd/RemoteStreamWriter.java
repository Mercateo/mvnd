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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicBoolean;

import com.mercateo.oss.mvnd.MVNDProto.InvokeResponse;
import com.mercateo.oss.mvnd.MVNDProto.InvokeResponse.ResponseType;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class RemoteStreamWriter implements Runnable {

	private AtomicBoolean alive = new AtomicBoolean(true);

	private final InputStream i;

	private final StreamObserver<InvokeResponse> responseObserver;

	private final ResponseType type;

	@Override
	public void run() {
		new BufferedReader(new InputStreamReader(i)).lines().forEachOrdered(line -> {
			if (alive.get())
				try {
					responseObserver.onNext(InvokeResponse.newBuilder().setType(type).setLine(line).build());
				} catch (Throwable meh) {
					// call is closed... skip the rest
					alive.set(false);
				}
		});
	}

	public void flush() {
		try {
			// block until no more bytes available
			while (alive.get() && i.available() > 0)
				Thread.sleep(50);
		} catch (Throwable meh) {
		}

	}
}