package com.wq.grpc.nameresolver;

import com.wq.grpc.prop.BPMGrpcChannelProperty;
import io.grpc.EquivalentAddressGroup;
import io.grpc.NameResolver;
import io.grpc.NameResolverProvider;
import lombok.ToString;

import java.net.InetSocketAddress;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;

import static java.util.Objects.requireNonNull;

@ToString
public class BPMNameResolverProvider extends NameResolverProvider {

    public static final String STATIC_SCHEME = "static";
    public static final URI STATIC_DEFAULT_URI = URI.create("static://localhost:9090");
    public static final Function<String, URI> STATIC_DEFAULT_URI_MAPPER = clientName -> STATIC_DEFAULT_URI;

    private static final Pattern PATTERN_COMMA = Pattern.compile(",");

    public BPMNameResolverProvider(){}
//    @Nullable
//    @Override
//    @Deprecated
//    // TODO: Update this to grpc-java 1.21 in v2.6.0
    public NameResolver newNameResolver(final URI targetUri, final io.grpc.NameResolver.Helper args) {
        if (STATIC_SCHEME.equals(targetUri.getScheme())) {
            return of(targetUri.getAuthority(), args.getDefaultPort());
        }
        return null;
    }

    private NameResolver of(final String targetAuthority, int defaultPort) {
        requireNonNull(targetAuthority, "targetAuthority");
        // Determine target ips
        final String[] hosts = PATTERN_COMMA.split(targetAuthority);
        final List<EquivalentAddressGroup> targets = new ArrayList<>(hosts.length);
        for (final String host : hosts) {
            final URI uri = URI.create("//" + host);
            int port = uri.getPort();
            if (port == -1) {
                port = defaultPort;
            }
            targets.add(new EquivalentAddressGroup(new InetSocketAddress(uri.getHost(), port)));
        }
        if (targets.isEmpty()) {
            throw new IllegalArgumentException("Must have at least one target, but was: " + targetAuthority);
        }
        return new BPMNameResolver(targetAuthority, targets);
    }

    @Override
    public String getDefaultScheme() {
        return STATIC_SCHEME;
    }

    @Override
    protected boolean isAvailable() {
        return true;
    }

    @Override
    protected int priority() {
        return 4; // Less important than DNS
    }

}

