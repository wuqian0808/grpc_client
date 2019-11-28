package com.wq.grpc.interceptor;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import io.grpc.ClientInterceptor;

import javax.annotation.PostConstruct;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BPMGrpcClientInterceptorRegistry {

    private final List<ClientInterceptor> clientInterceptors = Lists.newArrayList();
//    private ImmutableList<ClientInterceptor> sortedClientInterceptors;

    @PostConstruct
    public void init() {
        /**TODO, Scan from annota or other interface by reflection**/
        final Map<String, BPMGrpcClientInterceptorRegistry> map = new LinkedHashMap<>();
        //invoke this to add interceptors
//        for (final BPMGrpcClientInterceptorRegistry globalClientInterceptorConfigurer : map.values()) {
//            globalClientInterceptorConfigurer.addClientInterceptors(this);
//        }
    }

    public BPMGrpcClientInterceptorRegistry addClientInterceptors(final ClientInterceptor interceptor) {
//        this.sortedClientInterceptors = null;
        this.clientInterceptors.add(interceptor);
        return this;
    }


    public ImmutableList<ClientInterceptor> getClientInterceptors() {
//        if (this.sortedClientInterceptors == null) {
//            List<ClientInterceptor> temp = Lists.newArrayList(this.clientInterceptors);
//            this.sortedClientInterceptors = ImmutableList.copyOf(temp);
//        }
//        return this.sortedClientInterceptors;
        return ImmutableList.copyOf(this.clientInterceptors);
    }


}
