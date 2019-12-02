package com.wq.grpc.channel;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.wq.grpc.interceptor.BPMGrpcClientInterceptorRegistry;
import com.wq.grpc.prop.BPMGrpcChannelProperty;
import com.wq.grpc.prop.BPMGrpcChannelsProperty;
import com.wq.grpc.prop.NegotiationType;
import com.wq.grpc.prop.Security;
import com.wq.grpc.util.PropertyUtil;
import io.grpc.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import static java.util.Objects.requireNonNull;

/**
 * create channel and configure
 */
public abstract class AbstractChannelFactory <T extends ManagedChannelBuilder<T>>  implements GrpcChannelFactoryInterface{
    protected final BPMGrpcChannelsProperty properties;
    protected final BPMGrpcClientInterceptorRegistry registry;
    protected final List<BPMGrpcChannelConfigurer> configurers;
    private final Map<String, ManagedChannel> channels = new ConcurrentHashMap<>();
    private boolean shutdown = false;

    public AbstractChannelFactory(final BPMGrpcChannelsProperty properties,
                                  final BPMGrpcClientInterceptorRegistry globalClientInterceptorRegistry,
                                  final List<BPMGrpcChannelConfigurer> channelConfigurers) {
        this.properties = requireNonNull(properties, "properties");
        this.registry =
                requireNonNull(globalClientInterceptorRegistry, "globalClientInterceptorRegistry");
        this.configurers = requireNonNull(channelConfigurers, "channelConfigurers");
    }

    @Override
    public Channel createChannel(String name) {
        return createChannel(name, Collections.EMPTY_LIST);
    }

    @Override
    public Channel createChannel(String name, List<ClientInterceptor> interceptors) {
        final Channel channel;
        synchronized (this) {
            if (this.shutdown) {
                throw new IllegalStateException("GrpcChannelFactory is already closed!");
            }
            channel = this.channels.computeIfAbsent(name, this::newManagedChannel);
        }
        return ClientInterceptors.interceptForward(channel, interceptors);
    }

    protected ManagedChannel newManagedChannel(final String name) {
        final T builder = newChannelBuilder(name);
        configure(builder, name);
        return builder.build();
    }

    protected abstract T newChannelBuilder(String name);

    protected void configure(final T builder, final String name) {
        final BPMGrpcChannelProperty property = PropertyUtil.getPropertiesForChannel(this.properties, name);
        configureSecurity(builder, name);
        final int maxInboundMessageSize = property.getMaxInboundMessageSize();
        if (maxInboundMessageSize > 0 ) {
            builder.maxInboundMessageSize(maxInboundMessageSize);
        }
        if (property.isFullStreamDecompression()) {
            builder.enableFullStreamDecompression();
        }
        for (final BPMGrpcChannelConfigurer channelConfigurer : this.configurers) {
            channelConfigurer.accept(builder, name);
        }
    }

    //not required
    protected void configureSecurity(final T builder, final String name) {
        final BPMGrpcChannelProperty property = PropertyUtil.getPropertiesForChannel(this.properties, name);
        final Security security = property.getSecurity();

        if (property.getNegotiationType() != NegotiationType.TLS // non-default
                || !Strings.isNullOrEmpty(security.getAuthorityOverride())
                || security.getCertificateChain() != null
                || security.getPrivateKey() != null
                || security.getTrustCertCollection() != null) {
            throw new IllegalStateException(
                    "Security is configured but this implementation does not support security!");
        }
    }

}
