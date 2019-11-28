package com.wq.grpc;

import com.wq.grpc.client.ClientFactory;
import com.wq.grpc.client.DataClient;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App {
    public static void main(String[] args){
        try {
            log.info("start");
            DataClient dataClient = (DataClient) ClientFactory.getInstance().getClient("data");
            dataClient.greet("test");
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
    }
}
