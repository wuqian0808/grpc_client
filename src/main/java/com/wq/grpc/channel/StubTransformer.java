package com.wq.grpc.channel;

import io.grpc.stub.AbstractStub;

public interface StubTransformer {

    AbstractStub<?> transform(String name, AbstractStub<?> stub);

}
