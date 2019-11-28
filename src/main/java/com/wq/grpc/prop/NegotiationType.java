package com.wq.grpc.prop;

public enum NegotiationType {

    TLS,
    PLAINTEXT_UPGRADE, //include HTTP/1.1 to HTTP2
    PLAINTEXT; //HTTP2

}