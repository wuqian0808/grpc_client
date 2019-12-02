package com.wq.grpc.prop;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**TODO, How to init Global**/
@Data
@EqualsAndHashCode
public class BPMGrpcChannelsProperty {
    public static final String GLOBAL_PROPERTIES_KEY = "GLOBAL";
    private Map<String, BPMGrpcChannelProperty> client = new ConcurrentHashMap();

    public void setGlobalProperty(BPMGrpcChannelProperty channelProperty){
        client.put(GLOBAL_PROPERTIES_KEY, channelProperty);
    }

    public void addChannel(String name, BPMGrpcChannelProperty property){
        client.put(name, property);
    }

    public Map<String, BPMGrpcChannelProperty> getClient(){
        return this.client;
    }

    public BPMGrpcChannelProperty getGloabalChannel(){
        return this.getInitChannel(GLOBAL_PROPERTIES_KEY);
    }

    public BPMGrpcChannelProperty getChannel(String name){
        BPMGrpcChannelProperty channelProperty = getInitChannel(name);
        channelProperty.copyAllValueIfNullFrom(this.getGloabalChannel());
        return channelProperty;
    }

    private BPMGrpcChannelProperty getInitChannel(String name){
        return (BPMGrpcChannelProperty) this.client.computeIfAbsent(name, (key) -> {return  new BPMGrpcChannelProperty();});

    }
}
