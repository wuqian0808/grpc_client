package com.wq.grpc.util;

import com.wq.grpc.prop.BPMGrpcChannelProperty;
import com.wq.grpc.prop.BPMGrpcChannelsProperty;

public class PropertyUtil {
    public static BPMGrpcChannelProperty getPropertiesForChannel(BPMGrpcChannelsProperty property, final String name) {
        return property.getChannel(name);
    }
}
