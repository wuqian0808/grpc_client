package com.wq.grpc.client;

import com.google.protobuf.Any;
import com.google.protobuf.StringValue;
import com.wq.grpc.model.Greet;
import com.wq.grpc.model.GreetRpcServiceGrpc.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataClient implements ClientInterface{
    private GreetRpcServiceBlockingStub stub;

    public DataClient(GreetRpcServiceBlockingStub stub){
        this.stub = stub;
    }

    public void greet(String msg){
        StringValue msgValue = StringValue.of(msg);
        Greet.GreetRequest request = Greet.GreetRequest.newBuilder().setName(Any.pack(msgValue)).build();
        log.info(stub.sayHello(request).getMessage().toString());
    }
}
