package com.wq.grpc.prop;

import lombok.EqualsAndHashCode;

import java.net.URI;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

@EqualsAndHashCode
public class BPMGrpcChannelProperty {

    private URI address = null;// Target Address
    private String defaultLoadBalancingPolicy;// defaultLoadBalancingPolicy
    private static final String DEFAULT_DEFAULT_LOAD_BALANCING_POLICY = "round_robin";
    private Boolean enableKeepAlive;// KeepAlive
    private static final boolean DEFAULT_ENABLE_KEEP_ALIVE = false;
    private Duration keepAliveTime;
    private static final Duration DEFAULT_KEEP_ALIVE_TIME = Duration.of(60, ChronoUnit.SECONDS);
    private Duration keepAliveTimeout;
    private static final Duration DEFAULT_KEEP_ALIVE_TIMEOUT = Duration.of(20, ChronoUnit.SECONDS);
    private Boolean keepAliveWithoutCalls;
    private static final boolean DEFAULT_KEEP_ALIVE_WITHOUT_CALLS = false;
    private int maxInboundMessageSize = 0;// Message Transfer
    private NegotiationType negotiationType;
    private static final NegotiationType DEFAULT_NEGOTIATION_TYPE = NegotiationType.TLS;
    private Boolean fullStreamDecompression;
    private static final boolean DEFAULT_FULL_STREAM_DECOMPRESSION = false;
    private final Security security = new Security();
    private String clientClass; //Store client class name

    public URI getAddress() {
        return this.address;
    }

    public void setAddress(final URI address) {
        this.address = address;
    }

    public void setAddress(final String address) {
        this.address = address == null ? null : URI.create(address);
    }

    public String getDefaultLoadBalancingPolicy() {
        return this.defaultLoadBalancingPolicy == null ? DEFAULT_DEFAULT_LOAD_BALANCING_POLICY
                : this.defaultLoadBalancingPolicy;
    }

    public void setDefaultLoadBalancingPolicy(final String defaultLoadBalancingPolicy) {
        this.defaultLoadBalancingPolicy = defaultLoadBalancingPolicy;
    }

    public boolean isEnableKeepAlive() {
        return this.enableKeepAlive == null ? DEFAULT_ENABLE_KEEP_ALIVE : this.enableKeepAlive;
    }

    public void setEnableKeepAlive(final Boolean enableKeepAlive) {
        this.enableKeepAlive = enableKeepAlive;
    }

    public Duration getKeepAliveTime() {
        return this.keepAliveTime == null ? DEFAULT_KEEP_ALIVE_TIME : this.keepAliveTime;
    }

    public void setKeepAliveTime(final Duration keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
    }

    public Duration getKeepAliveTimeout() {
        return this.keepAliveTimeout == null ? DEFAULT_KEEP_ALIVE_TIMEOUT : this.keepAliveTimeout;
    }

    public void setKeepAliveTimeout(final Duration keepAliveTimeout) {
        this.keepAliveTimeout = keepAliveTimeout;
    }

    public boolean isKeepAliveWithoutCalls() {
        return this.keepAliveWithoutCalls == null ? DEFAULT_KEEP_ALIVE_WITHOUT_CALLS : this.keepAliveWithoutCalls;
    }

    public void setKeepAliveWithoutCalls(final Boolean keepAliveWithoutCalls) {
        this.keepAliveWithoutCalls = keepAliveWithoutCalls;
    }

    public int getMaxInboundMessageSize() {
        return this.maxInboundMessageSize;
    }

    public void setMaxInboundMessageSize(final int maxInboundMessageSize) {
        if (maxInboundMessageSize > 0) {
            this.maxInboundMessageSize = maxInboundMessageSize;
        } else if (maxInboundMessageSize == 0) {
            this.maxInboundMessageSize = Integer.MAX_VALUE;
        } else {
            throw new IllegalArgumentException("Unsupported maxInboundMessageSize: " + maxInboundMessageSize);
        }
    }

    public boolean isFullStreamDecompression() {
        return this.fullStreamDecompression == null ? DEFAULT_FULL_STREAM_DECOMPRESSION : this.fullStreamDecompression;
    }

    public void setFullStreamDecompression(final Boolean fullStreamDecompression) {
        this.fullStreamDecompression = fullStreamDecompression;
    }

    public NegotiationType getNegotiationType() {
        return this.negotiationType == null ? DEFAULT_NEGOTIATION_TYPE : this.negotiationType;
    }

    public void setNegotiationType(final NegotiationType negotiationType) {
        this.negotiationType = negotiationType;
    }

    public Security getSecurity() {
        return this.security;
    }

    public  String getClientClass() {
        return clientClass;
    }

    public void setClientClass(String clientClass) {
        this.clientClass = clientClass;
    }

    public void copyAllValueIfNullFrom(final BPMGrpcChannelProperty config) {
        if (this == config) {
            return;
        }
        if (this.address == null) {
            this.address = config.address;
        }
        if (this.defaultLoadBalancingPolicy == null) {
            this.defaultLoadBalancingPolicy = config.defaultLoadBalancingPolicy;
        }
        if (this.enableKeepAlive == null) {
            this.enableKeepAlive = config.enableKeepAlive;
        }
        if (this.keepAliveTime == null) {
            this.keepAliveTime = config.keepAliveTime;
        }
        if (this.keepAliveTimeout == null) {
            this.keepAliveTimeout = config.keepAliveTimeout;
        }
        if (this.keepAliveWithoutCalls == null) {
            this.keepAliveWithoutCalls = config.keepAliveWithoutCalls;
        }
        if (this.maxInboundMessageSize == 0) {
            this.maxInboundMessageSize = config.maxInboundMessageSize;
        }
        if (this.fullStreamDecompression == null) {
            this.fullStreamDecompression = config.fullStreamDecompression;
        }
        if (this.negotiationType == null) {
            this.negotiationType = config.negotiationType;
        }
        if(this.clientClass == null){
            this.clientClass = config.clientClass;
        }
        this.security.copyAllValueIfNullFrom(config.security);
    }
}
