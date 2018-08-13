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

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.13.2)",
    comments = "Source: mvnd.proto")
public final class MVNDServiceGrpc {

  private MVNDServiceGrpc() {}

  public static final String SERVICE_NAME = "mvnd.MVNDService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.mercateo.oss.mvnd.MVNDProto.InvokeRequest,
      com.mercateo.oss.mvnd.MVNDProto.InvokeResponse> getInvokeMethod;

  public static io.grpc.MethodDescriptor<com.mercateo.oss.mvnd.MVNDProto.InvokeRequest,
      com.mercateo.oss.mvnd.MVNDProto.InvokeResponse> getInvokeMethod() {
    io.grpc.MethodDescriptor<com.mercateo.oss.mvnd.MVNDProto.InvokeRequest, com.mercateo.oss.mvnd.MVNDProto.InvokeResponse> getInvokeMethod;
    if ((getInvokeMethod = MVNDServiceGrpc.getInvokeMethod) == null) {
      synchronized (MVNDServiceGrpc.class) {
        if ((getInvokeMethod = MVNDServiceGrpc.getInvokeMethod) == null) {
          MVNDServiceGrpc.getInvokeMethod = getInvokeMethod = 
              io.grpc.MethodDescriptor.<com.mercateo.oss.mvnd.MVNDProto.InvokeRequest, com.mercateo.oss.mvnd.MVNDProto.InvokeResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(
                  "mvnd.MVNDService", "invoke"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.mercateo.oss.mvnd.MVNDProto.InvokeRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.mercateo.oss.mvnd.MVNDProto.InvokeResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new MVNDServiceMethodDescriptorSupplier("invoke"))
                  .build();
          }
        }
     }
     return getInvokeMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static MVNDServiceStub newStub(io.grpc.Channel channel) {
    return new MVNDServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static MVNDServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new MVNDServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static MVNDServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new MVNDServiceFutureStub(channel);
  }

  /**
   */
  public static abstract class MVNDServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void invoke(com.mercateo.oss.mvnd.MVNDProto.InvokeRequest request,
        io.grpc.stub.StreamObserver<com.mercateo.oss.mvnd.MVNDProto.InvokeResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getInvokeMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getInvokeMethod(),
            asyncServerStreamingCall(
              new MethodHandlers<
                com.mercateo.oss.mvnd.MVNDProto.InvokeRequest,
                com.mercateo.oss.mvnd.MVNDProto.InvokeResponse>(
                  this, METHODID_INVOKE)))
          .build();
    }
  }

  /**
   */
  public static final class MVNDServiceStub extends io.grpc.stub.AbstractStub<MVNDServiceStub> {
    private MVNDServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private MVNDServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MVNDServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new MVNDServiceStub(channel, callOptions);
    }

    /**
     */
    public void invoke(com.mercateo.oss.mvnd.MVNDProto.InvokeRequest request,
        io.grpc.stub.StreamObserver<com.mercateo.oss.mvnd.MVNDProto.InvokeResponse> responseObserver) {
      asyncServerStreamingCall(
          getChannel().newCall(getInvokeMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class MVNDServiceBlockingStub extends io.grpc.stub.AbstractStub<MVNDServiceBlockingStub> {
    private MVNDServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private MVNDServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MVNDServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new MVNDServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public java.util.Iterator<com.mercateo.oss.mvnd.MVNDProto.InvokeResponse> invoke(
        com.mercateo.oss.mvnd.MVNDProto.InvokeRequest request) {
      return blockingServerStreamingCall(
          getChannel(), getInvokeMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class MVNDServiceFutureStub extends io.grpc.stub.AbstractStub<MVNDServiceFutureStub> {
    private MVNDServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private MVNDServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MVNDServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new MVNDServiceFutureStub(channel, callOptions);
    }
  }

  private static final int METHODID_INVOKE = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final MVNDServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(MVNDServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_INVOKE:
          serviceImpl.invoke((com.mercateo.oss.mvnd.MVNDProto.InvokeRequest) request,
              (io.grpc.stub.StreamObserver<com.mercateo.oss.mvnd.MVNDProto.InvokeResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class MVNDServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    MVNDServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.mercateo.oss.mvnd.MVNDProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("MVNDService");
    }
  }

  private static final class MVNDServiceFileDescriptorSupplier
      extends MVNDServiceBaseDescriptorSupplier {
    MVNDServiceFileDescriptorSupplier() {}
  }

  private static final class MVNDServiceMethodDescriptorSupplier
      extends MVNDServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    MVNDServiceMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (MVNDServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new MVNDServiceFileDescriptorSupplier())
              .addMethod(getInvokeMethod())
              .build();
        }
      }
    }
    return result;
  }
}
