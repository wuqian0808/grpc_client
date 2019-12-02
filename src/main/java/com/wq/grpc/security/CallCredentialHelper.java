package com.wq.grpc.security;

import com.wq.grpc.channel.StubTransformer;
import io.grpc.CallCredentials;
import io.grpc.Metadata;

import java.util.Base64;
import java.util.concurrent.Executor;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.requireNonNull;

public class CallCredentialHelper {
    public static final Metadata.Key<String> AUTHORIZATION_HEADER = Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER);
    public static final String BASIC_AUTH_PREFIX = "Basic ";
    public static final String BEARER_AUTH_PREFIX = "Bearer ";

    public static StubTransformer fixedCredentialsStubTransformer(final CallCredentials credentials) {
        requireNonNull(credentials, "credentials");
        return (name, stub) -> stub.withCallCredentials(credentials);
    }

    private CallCredentialHelper() {}

    public static CallCredentials basicAuth(final String username, final String password) {
        final Metadata extraHeaders = new Metadata();
        extraHeaders.put(AUTHORIZATION_HEADER, encodeBasicAuth(username, password));
        return new StaticSecurityHeaderCallCredentials(extraHeaders);
    }

    public static String encodeBasicAuth(final String username, final String password) {
        requireNonNull(username, "username");
        requireNonNull(password, "password");
        final String auth = username + ':' + password;
        byte[] encoded;
        try {
            encoded = Base64.getEncoder().encode(auth.getBytes(UTF_8));
        } catch (final IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to encode basic authentication token", e);
        }
        return BASIC_AUTH_PREFIX + new String(encoded, UTF_8);
    }

    private static final class StaticSecurityHeaderCallCredentials extends CallCredentials {

        private final Metadata extraHeaders;

        StaticSecurityHeaderCallCredentials(final Metadata extraHeaders) {
            this.extraHeaders = requireNonNull(extraHeaders, "extraHeaders");
        }

        @Override
        public void applyRequestMetadata(final RequestInfo requestInfo, final Executor appExecutor,
                                         final MetadataApplier applier) {
            applier.apply(this.extraHeaders);
        }

        @Override
        public void thisUsesUnstableApi() {} // API evolution in progress

        @Override
        public String toString() {
            return "StaticSecurityHeaderCallCredentials [extraHeaders=" + this.extraHeaders + "]";
        }

    }
}
