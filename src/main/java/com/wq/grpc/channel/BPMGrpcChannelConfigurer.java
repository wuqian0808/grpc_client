package com.wq.grpc.channel;

import io.grpc.ManagedChannelBuilder;

import java.util.function.BiConsumer;

import static java.util.Objects.requireNonNull;

/**TODO:: Read**/
public interface BPMGrpcChannelConfigurer extends BiConsumer<ManagedChannelBuilder<?>, String> {

    @Override
    default BPMGrpcChannelConfigurer andThen(final BiConsumer<? super ManagedChannelBuilder<?>, ? super String> after) {
        requireNonNull(after, "after");
        return (l, r) -> {
            accept(l, r);
            after.accept(l, r);
        };
    }

}

