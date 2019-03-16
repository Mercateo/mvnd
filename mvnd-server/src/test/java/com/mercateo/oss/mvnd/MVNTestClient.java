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

import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.Stream;

import com.mercateo.oss.mvnd.MVNDProto.InvokeRequest;
import com.mercateo.oss.mvnd.MVNDProto.InvokeResponse;
import com.mercateo.oss.mvnd.MVNDServiceGrpc.MVNDServiceBlockingStub;
import com.mercateo.oss.mvnd.MVNDServiceGrpc.MVNDServiceStub;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class MVNTestClient {

    public static void main(String[] args) {
        
        
        while(true)
        run();

    }

    private static void run() {
        ManagedChannel c = ManagedChannelBuilder.forAddress("localhost", 1971).usePlaintext().build();
        MVNDServiceBlockingStub stub = MVNDServiceGrpc.newBlockingStub(c);

        InvokeRequest r = InvokeRequest.newBuilder()
                .setWorkDir("/home/usr/work/workspaces/mvnd/mvnd/mvnd-server/")
                .addAllArgs(Arrays.asList("validate"))
                .build();

        for (Iterator<InvokeResponse> iterator = stub.invoke(r); iterator.hasNext();) {
            InvokeResponse res = iterator.next();
            switch (res.getType().getNumber()) {
            case InvokeResponse.ResponseType.ERR_VALUE:
                System.err.println(res.getLine());
                break;

            case InvokeResponse.ResponseType.OUT_VALUE:
                System.out.println(res.getLine());
                break;

            case InvokeResponse.ResponseType.EXIT_VALUE:
                System.out.println(res.getErrorCode());
                break;

            default:
                throw new IllegalArgumentException("WTF?");
                
            }

        }
    }

}
