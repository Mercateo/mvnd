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
