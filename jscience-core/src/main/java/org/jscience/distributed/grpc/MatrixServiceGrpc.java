/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.jscience.distributed.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(value = "by gRPC proto compiler (version 1.60.0)", comments = "Source: linear_algebra_service.proto")
@io.grpc.stub.annotations.GrpcGenerated
@SuppressWarnings("all")
public final class MatrixServiceGrpc {

  private MatrixServiceGrpc() {
  }

  public static final java.lang.String SERVICE_NAME = "org.jscience.computing.remote.MatrixService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<org.jscience.distributed.grpc.MatrixRequest, org.jscience.distributed.grpc.MatrixResponse> getMatrixMultiplyMethod;

  @io.grpc.stub.annotations.RpcMethod(fullMethodName = SERVICE_NAME + '/'
      + "MatrixMultiply", requestType = org.jscience.distributed.grpc.MatrixRequest.class, responseType = org.jscience.distributed.grpc.MatrixResponse.class, methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.jscience.distributed.grpc.MatrixRequest, org.jscience.distributed.grpc.MatrixResponse> getMatrixMultiplyMethod() {
    io.grpc.MethodDescriptor<org.jscience.distributed.grpc.MatrixRequest, org.jscience.distributed.grpc.MatrixResponse> getMatrixMultiplyMethod;
    if ((getMatrixMultiplyMethod = MatrixServiceGrpc.getMatrixMultiplyMethod) == null) {
      synchronized (MatrixServiceGrpc.class) {
        if ((getMatrixMultiplyMethod = MatrixServiceGrpc.getMatrixMultiplyMethod) == null) {
          MatrixServiceGrpc.getMatrixMultiplyMethod = getMatrixMultiplyMethod = io.grpc.MethodDescriptor.<org.jscience.distributed.grpc.MatrixRequest, org.jscience.distributed.grpc.MatrixResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "MatrixMultiply"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.jscience.distributed.grpc.MatrixRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.jscience.distributed.grpc.MatrixResponse.getDefaultInstance()))
              .setSchemaDescriptor(new MatrixServiceMethodDescriptorSupplier("MatrixMultiply"))
              .build();
        }
      }
    }
    return getMatrixMultiplyMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static MatrixServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<MatrixServiceStub> factory = new io.grpc.stub.AbstractStub.StubFactory<MatrixServiceStub>() {
      @java.lang.Override
      public MatrixServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
        return new MatrixServiceStub(channel, callOptions);
      }
    };
    return MatrixServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output
   * calls on the service
   */
  public static MatrixServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<MatrixServiceBlockingStub> factory = new io.grpc.stub.AbstractStub.StubFactory<MatrixServiceBlockingStub>() {
      @java.lang.Override
      public MatrixServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
        return new MatrixServiceBlockingStub(channel, callOptions);
      }
    };
    return MatrixServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the
   * service
   */
  public static MatrixServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<MatrixServiceFutureStub> factory = new io.grpc.stub.AbstractStub.StubFactory<MatrixServiceFutureStub>() {
      @java.lang.Override
      public MatrixServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
        return new MatrixServiceFutureStub(channel, callOptions);
      }
    };
    return MatrixServiceFutureStub.newStub(factory, channel);
  }

  /**
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
  public interface AsyncService {

    /**
     */
    default void matrixMultiply(org.jscience.distributed.grpc.MatrixRequest request,
        io.grpc.stub.StreamObserver<org.jscience.distributed.grpc.MatrixResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getMatrixMultiplyMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service MatrixService.
   */
  public static abstract class MatrixServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override
    public final io.grpc.ServerServiceDefinition bindService() {
      return MatrixServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service
   * MatrixService.
   */
  public static final class MatrixServiceStub
      extends io.grpc.stub.AbstractAsyncStub<MatrixServiceStub> {
    private MatrixServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MatrixServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new MatrixServiceStub(channel, callOptions);
    }

    /**
     */
    public void matrixMultiply(org.jscience.distributed.grpc.MatrixRequest request,
        io.grpc.stub.StreamObserver<org.jscience.distributed.grpc.MatrixResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getMatrixMultiplyMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service MatrixService.
   */
  public static final class MatrixServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<MatrixServiceBlockingStub> {
    private MatrixServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MatrixServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new MatrixServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public org.jscience.distributed.grpc.MatrixResponse matrixMultiply(
        org.jscience.distributed.grpc.MatrixRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getMatrixMultiplyMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service
   * MatrixService.
   */
  public static final class MatrixServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<MatrixServiceFutureStub> {
    private MatrixServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MatrixServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new MatrixServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.jscience.distributed.grpc.MatrixResponse> matrixMultiply(
        org.jscience.distributed.grpc.MatrixRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getMatrixMultiplyMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_MATRIX_MULTIPLY = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_MATRIX_MULTIPLY:
          serviceImpl.matrixMultiply((org.jscience.distributed.grpc.MatrixRequest) request,
              (io.grpc.stub.StreamObserver<org.jscience.distributed.grpc.MatrixResponse>) responseObserver);
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

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
            getMatrixMultiplyMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
                new MethodHandlers<org.jscience.distributed.grpc.MatrixRequest, org.jscience.distributed.grpc.MatrixResponse>(
                    service, METHODID_MATRIX_MULTIPLY)))
        .build();
  }

  private static abstract class MatrixServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    MatrixServiceBaseDescriptorSupplier() {
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return org.jscience.distributed.grpc.LinearAlgebraServiceProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("MatrixService");
    }
  }

  private static final class MatrixServiceFileDescriptorSupplier
      extends MatrixServiceBaseDescriptorSupplier {
    MatrixServiceFileDescriptorSupplier() {
    }
  }

  private static final class MatrixServiceMethodDescriptorSupplier
      extends MatrixServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    MatrixServiceMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (MatrixServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new MatrixServiceFileDescriptorSupplier())
              .addMethod(getMatrixMultiplyMethod())
              .build();
        }
      }
    }
    return result;
  }
}


