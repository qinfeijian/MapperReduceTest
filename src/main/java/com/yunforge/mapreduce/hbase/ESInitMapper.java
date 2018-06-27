package com.yunforge.mapreduce.hbase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.NullWritable;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.update.UpdateRequestBuilder;
import org.elasticsearch.client.Client;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * ES init mapper
 */
public class ESInitMapper extends TableMapper<NullWritable, NullWritable> {

    private Client client;
    private static final Log LOG = LogFactory.getLog(ESInitMapper.class);
    private BulkRequestBuilder bulkRequestBuilder;
    private String index;
    private String columnFamily;

    /**
     * init ES
     * @param context
     * @throws IOException
     * @throws InterruptedException
     */
    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        try {
            String clusterName = context.getConfiguration().get("es.cluster.name");
            String host = context.getConfiguration().get("es.cluster.host");
            int port = Integer.parseInt(context.getConfiguration().get("es.cluster.port"));
            client = ESClient.getEsClient(clusterName,host,port);
            bulkRequestBuilder = client.prepareBulk();
            index = context.getConfiguration().get("index");
            columnFamily = context.getConfiguration().get("columnFamily");
        } catch (Exception ex) {
            LOG.error("init ES Client error:" + ex.getMessage());
        }
    }

    @Override
    protected void map(ImmutableBytesWritable key, Result values, Context context) throws IOException, InterruptedException {
        Map<String, Object> json = new HashMap<String, Object>();
        Map<String, Object> infoJson = new HashMap<String, Object>();
        String rowKey = Bytes.toString(values.getRow());
        for (KeyValue kv : values.raw()) {
            String keyHbase = Bytes.toString(kv.getQualifier());
            String valueHbase = Bytes.toString(kv.getValue());
            json.put(keyHbase, valueHbase);
        }
        // set Family(you can do not set or change zhe Family name)
        infoJson.put(columnFamily, json);
        addUpdateBuilderToBulk(client.prepareUpdate(index, index, rowKey).setDocAsUpsert(true).setDoc(infoJson));
    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        if (0 < bulkRequestBuilder.numberOfActions()) {
            try {
                bulkRequest();
            } catch (Exception ex) {
                LOG.error("Bulk " + index + "index error :" + ex.getMessage());
            }
        }
        //关闭es
        client.close();
    }

    /**
     * execute bulk process
     * @throws Exception
     */
    private void bulkRequest() throws Exception {
        BulkResponse bulkResponse = bulkRequestBuilder.execute().actionGet();
        if (!bulkResponse.hasFailures()) {
            bulkRequestBuilder = client.prepareBulk();
        }
    }

    /**
     *add prepare update date  to builder
     * @param builder
     */
    private synchronized void addUpdateBuilderToBulk(UpdateRequestBuilder builder) {
        try {
            if (bulkRequestBuilder.numberOfActions() != 0 && (bulkRequestBuilder.numberOfActions() % 500 == 0)) {
                bulkRequest();
            }
            bulkRequestBuilder.add(builder);
        } catch (Exception ex) {
            LOG.error("Bulk" + index + "index error :" + ex.getMessage());
        }
    }
}