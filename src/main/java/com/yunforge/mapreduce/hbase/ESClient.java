package com.yunforge.mapreduce.hbase;

import java.net.InetAddress;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

/**
 * ES Client Class
 */
public class ESClient {

    private static TransportClient client;

    @SuppressWarnings("resource")
    public static synchronized Client getEsClient(String clusterName,String host, int port) throws Exception {
        if(null == client){
            Settings settings = Settings.builder()
    				.put("cluster.name", clusterName).build();
            client = new PreBuiltTransportClient(settings)
    				.addTransportAddresses(new InetSocketTransportAddress(InetAddress
    						.getByName(host), port));
        }
        return client;
    }
}
