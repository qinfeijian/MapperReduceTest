package com.yunforge.mapreduce.hbase;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.update.UpdateRequestBuilder;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.PageFilter;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * 
 * 作者:覃飞剑
 * 日期:2018年6月27日
 * 说明:mapper读取hbase数据，然后存入elasticsearch中
 */
public class Hbase2ES {
	
	private static final Log LOG = LogFactory.getLog(Hbase2ES.class);
	
	public static BulkRequestBuilder bulkRequest = null;
	public static TransportClient client = null;
	
	public static String tableName = "data_quarter";
	public static String columnFamily = "info";
	public static String es_ip = "192.168.1.81";
	public static Integer es_port = 9300;
	public static String index = "agrdata_v2";
	public static String type = "sum";
	
	public static class HbaseMap extends TableMapper<NullWritable, NullWritable> {
		
		@Override
	    protected void setup(Context context) throws IOException, InterruptedException {
			Settings setting = Settings.builder().put("cluster.name", "elasticsearch").build();
			client = new PreBuiltTransportClient(setting)
					.addTransportAddresses(new InetSocketTransportAddress(InetAddress
					.getByName(es_ip), es_port));
			bulkRequest = client.prepareBulk();
		}
		@Override
	    protected void map(ImmutableBytesWritable key, Result values, Context context) throws IOException, InterruptedException {
			byte[] row = values.getRow();
			List<Cell> ceList = values.listCells();
			try {
				if (ceList != null && ceList.size() > 0) {
					for (Cell cell : ceList) {
						// 获取值
						String json = Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength());
						// 获取列
//						String quali = Bytes.toString(cell.getQualifierArray(), cell.getQualifierOffset(),
//								cell.getQualifierLength());
//						System.out.println(quali);
						JsonElement  je = new JsonParser().parse(json);
						try {
							String rowkey = je.getAsJsonObject().get("rowkey").getAsString();
							String date = je.getAsJsonObject().get("date").getAsString();
							String divName = je.getAsJsonObject().get("divName").getAsString();
							String indexCode = je.getAsJsonObject().get("indexCode").getAsString();
							String indexName = je.getAsJsonObject().get("indexName").getAsString();
							String value = je.getAsJsonObject().get("value").getAsString();
							String unit = je.getAsJsonObject().get("unit").getAsString();
							String column_source = je.getAsJsonObject().get("column_source").getAsString();
							String id = rowkey + "-" + indexCode;
							String title = date + divName + indexName;
							String content = date + divName + indexName + value + unit;
							String data_source = column_source;
							
							
							
							Map<String, String> insertMap = new HashMap<String, String>();
							insertMap.put("type", "1");
							insertMap.put("title", title);
							insertMap.put("content", content);
							insertMap.put("data_source", data_source);
							
							addUpdateBuilderToBulk(client.prepareUpdate(index, type, id).setDocAsUpsert(true)
									.setDoc(insertMap));
						} catch (Exception e) {
							e.printStackTrace();
							System.out.println(je.toString());
						}
						
					}
				}
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		@Override
	    protected void cleanup(Context context) throws IOException, InterruptedException {
	        if (0 < bulkRequest.numberOfActions()) {
	            try {
	                bulkRequest();
	            } catch (Exception ex) {
	                LOG.error("Bulk index error :" + ex.getMessage());
	            }
	        }
	        //关闭es
	        client.close();
	    }
		/**
		 *add prepare update date  to builder
		 * @param builder
		 */
		private synchronized void addUpdateBuilderToBulk(UpdateRequestBuilder builder) {
			try {
				if (bulkRequest.numberOfActions() != 0 && (bulkRequest.numberOfActions() % 100000 == 0)) {
					bulkRequest();
				}
				bulkRequest.add(builder);
			} catch (Exception ex) {
				LOG.error("Bulk agrdata_v2 index error :" + ex.getMessage());
			}
		}
		/**
		 * execute bulk process
		 * @throws Exception
		 */
		private void bulkRequest() throws Exception {
			BulkResponse bulkResponse = bulkRequest.execute().actionGet();
			if (!bulkResponse.hasFailures()) {
				bulkRequest = client.prepareBulk();
			}
		}
		
	}
	
	
//	public static void main(String args[]) throws Exception {
//		//创建Configuration  
//        Configuration configuration = new Configuration();  
//        configuration.set("hbase.zookeeper.quorum", "dn1.yfbd.com,dn2.yfbd.com,dn3.yfbd.com");  
//        configuration.set("hbase.zookeeper.property.clientPort", "2181");  
//        configuration.set("zookeeper.znode.parent", "/hbase-unsecure");
//        Job job = Job.getInstance(configuration, "ESInit ");
//        Scan scan = new Scan();
////        PageFilter pageFilter = new PageFilter(1);
////        scan.setFilter(pageFilter);
//        scan.setCaching(500); // 这里是设置hbase的缓存大小。 scan没有加任何条件，所以说scan的全量的数据，但是如果全量数据量很大，怎么办？所以我们要设置缓冲500，setCaching设置的值为每次rpc的请求记录数（每次从服务器端读取的行数，默认为配置文件中设置的值），默认是1，cache大可以优化性能，但是太大了会花费很长的时间进行一次传输。
//        scan.setCacheBlocks(false); // don't set to true for MR jobs
//     // 设置map过程
//        TableMapReduceUtil.initTableMapperJob(
//                tableName,      // input table
//                scan,              // Scan instance to control CF and attribute selection
//                HbaseMap.class,   // mapper class
//                null,              // mapper output key
//                null,              // mapper output value
//                job);
//        TableMapReduceUtil.initTableReducerJob(
//        		tableName,      // output table
//                null,             // reducer class
//                job);
//        job.setNumReduceTasks(0);
//        boolean b = job.waitForCompletion(true);
//        if (!b) {
//            throw new IOException("error with job!");
//        }
//    }
}
