package com.wq.grpc.security;

import com.wq.grpc.channel.StubTransformer;
import io.grpc.CallCredentials;

import static java.util.Objects.requireNonNull;

public class CallCredentialHelper {

    public static StubTransformer fixedCredentialsStubTransformer(final CallCredentials credentials) {
        requireNonNull(credentials, "credentials");
        return (name, stub) -> stub.withCallCredentials(credentials);
    }

    private CallCredentialHelper() {}

}
