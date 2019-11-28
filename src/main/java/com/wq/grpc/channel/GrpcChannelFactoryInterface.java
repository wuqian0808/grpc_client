package com.wq.grpc.channel;

import io.grpc.Channel;
import io.grpc.ClientInterceptor;

import java.util.List;

/**Remove sort option**/
public interface GrpcChannelFactoryInterface {

    public Channel createChannel(String name);

    public Channel createChannel(String name, List<ClientInterceptor> interceptors);
}
