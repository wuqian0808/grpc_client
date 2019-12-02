package com.wq.grpc.stub;

import io.grpc.stub.AbstractStub;

/**create channel and stub for client**/
public class BPMGrpcStubBuilder {

    private BPMGrpcStubBuilder instance = null;

    private BPMGrpcStubBuilder(){}

    /**singleton**/
    public BPMGrpcStubBuilder getInstance(){
        if(instance == null){
            synchronized(this) {
                instance = new BPMGrpcStubBuilder();
            }
        }
        return instance;
    }

//    public AbstractStub getGrpcClient
}
