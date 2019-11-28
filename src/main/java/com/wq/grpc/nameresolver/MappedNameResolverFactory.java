package com.wq.grpc.nameresolver;

import com.wq.grpc.prop.BPMGrpcChannelsProperty;
import io.grpc.NameResolver;
import io.grpc.NameResolverProvider;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

/**resove uri for specific client**/
@Slf4j
@ToString
public class MappedNameResolverFactory extends NameResolver.Factory {

    private final BPMGrpcChannelsProperty config;
    private final NameResolver.Factory delegate;
    private final Function<String, URI> defaultUriMapper;

//    @SuppressWarnings("deprecation")
    public MappedNameResolverFactory(final BPMGrpcChannelsProperty config,
                                           Function<String, URI> defaultUriMapper) {
        this(config, NameResolverProvider.asFactory(), defaultUriMapper);
    }

    public MappedNameResolverFactory(final BPMGrpcChannelsProperty config, final NameResolver.Factory delegate,
                                           Function<String, URI> defaultUriMapper) {
        this.config = requireNonNull(config, "config");
        this.delegate = requireNonNull(delegate, "delegate");
        this.defaultUriMapper = requireNonNull(defaultUriMapper, "defaultUriMapper");
    }

    @Override
    public String getDefaultScheme() {
        // The config does not use schemes at all
        return "";
    }

}

