package com.wq.grpc.nameresolver;

import com.google.common.collect.ImmutableList;
import io.grpc.Attributes;
import io.grpc.EquivalentAddressGroup;
import io.grpc.NameResolver;
import lombok.ToString;

import java.util.Collection;
import java.util.List;

import static java.util.Objects.requireNonNull;

@ToString
public class BPMNameResolver extends NameResolver {

    private final String authority;
    private final List<EquivalentAddressGroup> targets;


    public BPMNameResolver(final String authority, final EquivalentAddressGroup target) {
        this(authority, ImmutableList.of(requireNonNull(target, "target")));
    }

    public BPMNameResolver(final String authority, final Collection<EquivalentAddressGroup> targets) {
        this.authority = requireNonNull(authority, "authority");
        if (requireNonNull(targets, "targets").isEmpty()) {
            throw new IllegalArgumentException("Must have at least one target");
        }
        this.targets = ImmutableList.copyOf(targets);
    }

    @Override
    public String getServiceAuthority() {
        return this.authority;
    }

    @Override
    public void start(final Listener listener) {
        listener.onAddresses(this.targets, Attributes.EMPTY);
    }

    @Override
    public void refresh() {
        // Does nothing
    }

    @Override
    public void shutdown() {
        // Does nothing
    }


}

