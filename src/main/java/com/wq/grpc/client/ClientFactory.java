package com.wq.grpc.client;

import com.wq.grpc.prop.BPMGrpcChannelProperty;
import com.wq.grpc.prop.BPMGrpcChannelsProperty;
import com.wq.grpc.prop.NegotiationType;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
public class ClientFactory {
    private Map<String, ClientInterface> clients = null;
    private static ClientFactory clientFactory = null;
    public static final String GLOBAL_PROPERTIES_KEY = "GLOBAL";

    private ClientFactory(){}

    public static ClientFactory getInstance() throws Exception{
        /**Read settings from specific path**/
        BPMGrpcChannelsProperty properties = new BPMGrpcChannelsProperty();
        BPMGrpcChannelProperty gloabalChannel = new BPMGrpcChannelProperty();
        gloabalChannel.setEnableKeepAlive(true);
        gloabalChannel.setNegotiationType(NegotiationType.TLS);
        gloabalChannel.getSecurity().setAuthorityOverride("hd001.statestreet.com");
        gloabalChannel.getSecurity().setClientAuthEnabled(true);
        gloabalChannel.getSecurity().setCertificateChain("C:/JAVAProject/IJWorkSpace/grpc/non-spring-grpc-client/src/main/resources/server.crt");
        gloabalChannel.getSecurity().setTrustCertCollection("C:/JAVAProject/IJWorkSpace/grpc/non-spring-grpc-client/src/main/resources/server.crt");
        gloabalChannel.getSecurity().setPrivateKey("C:/JAVAProject/IJWorkSpace/grpc/non-spring-grpc-client/src/main/resources/server.key");
        gloabalChannel.getSecurity().setBasicAuthEnabled(true, "wq", "wq");
        properties.setGlobalProperty(gloabalChannel);
        BPMGrpcChannelProperty dataChannel = new BPMGrpcChannelProperty();
        dataChannel.setClientClass(DataClient.class.getName());
        dataChannel.setAddress("static://localhost:8090");
        properties.addChannel("data", dataChannel);
        BPMGrpcChannelProperty processChannel = new BPMGrpcChannelProperty();
        processChannel.setClientClass(ProcessClient.class.getName());
        processChannel.setAddress("static://localhost:8090");
        properties.addChannel("process", processChannel);
        //init client
        if(clientFactory==null){
            synchronized (clientFactory) {
                clientFactory = new ClientFactory();
                clientFactory.clients = new LinkedHashMap<String, ClientInterface>();
                ClientBuilder clientBuilder = new ClientBuilder(properties);
                //init channel and set stub, exclude global
                Set<String> clientNames = properties.getClient().keySet();
                for(String clientName:clientNames){
                    if(!clientName.equals(GLOBAL_PROPERTIES_KEY)){
                        ClientInterface clientInterface = clientBuilder.buildClient(clientName);
                        clientFactory.clients.put(clientName, clientInterface);
                    }
                }
            }
        }
        return clientFactory;
    }

    public ClientInterface getClient(String name) throws Exception{
        if(clients != null && clients.containsKey(name))
            return clients.get(name);
        throw  new Exception("Can not find client with name :: " + name);
    }
}
