package com.wq.grpc.channel;

import com.wq.grpc.interceptor.BPMGrpcClientInterceptorRegistry;
import com.wq.grpc.prop.BPMGrpcChannelProperty;
import com.wq.grpc.prop.BPMGrpcChannelsProperty;
import com.wq.grpc.prop.NegotiationType;
import com.wq.grpc.prop.Security;
import com.wq.grpc.util.PropertyUtil;
import io.grpc.NameResolver;
import io.grpc.netty.shaded.io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import io.grpc.netty.shaded.io.netty.handler.ssl.SslContextBuilder;

import javax.net.ssl.SSLException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * create builder and configure security
 */
public class BPMNettyChannelFactory extends AbstractChannelFactory<NettyChannelBuilder>{
    private final NameResolver.Factory nameResolverFactory;

    public BPMNettyChannelFactory(final BPMGrpcChannelsProperty properties,
                                     final NameResolver.Factory nameResolverFactory,
                                     final BPMGrpcClientInterceptorRegistry globalClientInterceptorRegistry,
                                     final List<BPMGrpcChannelConfigurer> channelConfigurers) {
        super(properties, globalClientInterceptorRegistry, channelConfigurers);
        this.nameResolverFactory = requireNonNull(nameResolverFactory, "nameResolverFactory");
    }

    @Override
    protected NettyChannelBuilder newChannelBuilder(final String name) {
        return NettyChannelBuilder.forTarget(name)
                .defaultLoadBalancingPolicy(PropertyUtil.getPropertiesForChannel(this.properties, name).getDefaultLoadBalancingPolicy())
                .nameResolverFactory(this.nameResolverFactory);
    }

    @Override
    protected void configureSecurity(final NettyChannelBuilder builder, final String name) {
        final BPMGrpcChannelProperty property = PropertyUtil.getPropertiesForChannel(this.properties, name);

        final NegotiationType negotiationType = property.getNegotiationType();
        builder.negotiationType(of(negotiationType));

        if (negotiationType == NegotiationType.TLS) {
            final Security security = property.getSecurity();

            final String authorityOverwrite = security.getAuthorityOverride();
            if (authorityOverwrite != null && !authorityOverwrite.isEmpty()) {
                builder.overrideAuthority(authorityOverwrite);
            }

            final SslContextBuilder sslContextBuilder = GrpcSslContexts.forClient();

            if (security.isClientAuthEnabled()) {
                final String certificateChain =
                        requireNonNull(security.getCertificateChain(), "certificateChain not configured");
                final String privateKey = requireNonNull(security.getPrivateKey(), "privateKey not configured");
                try {
                    InputStream certificateChainStream = new FileInputStream(new File(certificateChain));
                    InputStream privateKeyStream = new FileInputStream(new File(privateKey));
                    sslContextBuilder.keyManager(certificateChainStream,privateKeyStream,
                                security.getPrivateKeyPassword());
                } catch (IOException | RuntimeException e) {
                    throw new IllegalArgumentException("Failed to create SSLContext (PK/Cert)", e);
                }
            }

            final String trustCertCollection = security.getTrustCertCollection();
            if (trustCertCollection != null) {
                try {
                    InputStream trustCertCollectionStream = new FileInputStream(new File(trustCertCollection));
                    sslContextBuilder.trustManager(trustCertCollectionStream);
                } catch (IOException | RuntimeException e) {
                    throw new IllegalArgumentException("Failed to create SSLContext (TrustStore)", e);
                }
            }

            if (security.getCiphers() != null && !security.getCiphers().isEmpty()) {
                sslContextBuilder.ciphers(security.getCiphers());
            }

            if (security.getProtocols() != null && security.getProtocols().length > 0) {
                sslContextBuilder.protocols(security.getProtocols());
            }

            try {
                builder.sslContext(sslContextBuilder.build());
            } catch (final SSLException e) {
                throw new IllegalStateException("Failed to create ssl context for grpc client", e);
            }
        }
    }

    protected static io.grpc.netty.shaded.io.grpc.netty.NegotiationType of(final NegotiationType negotiationType) {
        switch (negotiationType) {
            case PLAINTEXT:
                return io.grpc.netty.shaded.io.grpc.netty.NegotiationType.PLAINTEXT;
            case PLAINTEXT_UPGRADE:
                return io.grpc.netty.shaded.io.grpc.netty.NegotiationType.PLAINTEXT_UPGRADE;
            case TLS:
                return io.grpc.netty.shaded.io.grpc.netty.NegotiationType.TLS;
            default:
                throw new IllegalArgumentException("Unsupported NegotiationType: " + negotiationType);
        }
    }
}
