package com.wq.grpc.client;

import com.wq.grpc.channel.BPMGrpcChannelConfigurer;
import com.wq.grpc.channel.BPMNettyChannelFactory;
import com.wq.grpc.channel.StubTransformer;
import com.wq.grpc.interceptor.BPMGrpcClientInterceptorRegistry;
import com.wq.grpc.nameresolver.BPMNameResolverProvider;
import com.wq.grpc.nameresolver.MappedNameResolverFactory;
import com.wq.grpc.prop.BPMGrpcChannelProperty;
import com.wq.grpc.prop.BPMGrpcChannelsProperty;
import com.wq.grpc.security.CallCredentialsHelper;
import com.wq.grpc.util.PropertyUtil;
import com.wq.grpc.util.ReflectionUtils;
import io.grpc.CallCredentials;
import io.grpc.Channel;
import io.grpc.ClientInterceptor;
import io.grpc.stub.AbstractStub;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Slf4j
public class ClientBuilder {
    private BPMGrpcChannelsProperty channelsProperty;
    private BPMNettyChannelFactory channelFactory;
    public static final String GLOBAL_PROPERTIES_KEY = "GLOBAL";
    private StubTransformer stubTransformer;

    public ClientBuilder(BPMGrpcChannelsProperty channelsProperty){
        this.channelsProperty = channelsProperty;
    }

    public ClientInterface buildClient(String name) throws Exception {
        //check if property contains name, if not, retun null. Can't be null.
        if (!channelsProperty.getClient().containsKey(name))
            return null;
        /**init channelfactory and stub and create client**/
        /**TODO, How to get all interceptor?**/
        List<ClientInterceptor> interceptors = new LinkedList<>();
        Channel channel = getChannelFactory().createChannel(name, interceptors);
        BPMGrpcChannelProperty property = PropertyUtil.getPropertiesForChannel(this.channelsProperty, name);
        /**create client**/
        return injectStubIntoClient(name, property, channel);
    }

    private BPMNettyChannelFactory getChannelFactory(){
        if(channelFactory == null){
            MappedNameResolverFactory nameResolverFactory = new MappedNameResolverFactory(channelsProperty,
                    BPMNameResolverProvider.STATIC_DEFAULT_URI_MAPPER);
            /**TODO, How to get from registry**/
            BPMGrpcClientInterceptorRegistry registry = new BPMGrpcClientInterceptorRegistry();
            List<BPMGrpcChannelConfigurer> channelConfigurers = Collections.emptyList();
            channelFactory = new BPMNettyChannelFactory(channelsProperty, nameResolverFactory,
                            registry, channelConfigurers);
        }
        return channelFactory;
    }

    private ClientInterface injectStubIntoClient(String name, BPMGrpcChannelProperty property, Channel channel) throws Exception{
        String clientClazz = property.getClientClass();
        Object client = Class.forName(clientClazz).newInstance();
        for (Field field : client.getClass().getDeclaredFields()) {
            if (field.toString().contains("Stub")) {
                ReflectionUtils.makeAccessible(field);
                Class<?> clazz = field.getType();
                final Class<? extends AbstractStub<?>> stubClass =
                        (Class<? extends AbstractStub<?>>) clazz.asSubclass(AbstractStub.class);
                final Constructor<? extends AbstractStub<?>> constructor =
                        ReflectionUtils.accessibleConstructor(stubClass, Channel.class);
                AbstractStub<?> stub = constructor.newInstance(channel);
                if(getStubTransFormer() != null)
                    stub = stubTransformer.transform(name, stub);
                ReflectionUtils.setField(field, client, stub);
                return (ClientInterface)client;
            }
        }
        throw new Exception("Can not init client :: " + name);
    }

    private StubTransformer getStubTransFormer(){
        if(stubTransformer == null) {
            BPMGrpcChannelProperty globalChannel = PropertyUtil.getPropertiesForChannel(channelsProperty, GLOBAL_PROPERTIES_KEY);
            if (globalChannel.getSecurity().getBasicAuthEnabled()) {
                CallCredentials callCredentials = CallCredentialsHelper.basicAuth(globalChannel.getSecurity().getUser(), globalChannel.getSecurity().getPwd());
                stubTransformer =  CallCredentialsHelper.fixedCredentialsStubTransformer(callCredentials);
            }
        }
        return stubTransformer;
    }

}
