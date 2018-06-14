package com.yunforge.mapreduce.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;

import com.google.gson.Gson;

public class HbaseBatchUtils {

	private static Configuration _conf = HBaseConfiguration.create();
	private static Table _hTable = null;
	private static Connection conn = null;

	static {
		_conf.set("hbase.zookeeper.quorum", "dn1.yfbd.com");
		_conf.set("hbase.zookeeper.property.clientPort", "2181");
		_conf.set("zookeeper.znode.parent", "/hbase-unsecure");
		try {
			conn = ConnectionFactory.createConnection(_conf);
			_hTable = conn.getTable(TableName.valueOf("testimport"));
		} catch (IOException e) {
			System.out.println("[QCH]cann't connect hbase.");
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * 作者:覃飞剑
	 * 日期:2018年6月12日
	 * @param list
	 * @param tableName
	 * @return
	 * 返回:boolean
	 * 说明:批量导入数据到hbase，每一条数据都要有 rowkey value column column_source
	 */
	public static boolean batchInsterDataByListMap(List<Map<String, String>> list, String tableName) {
		try {
			_hTable = conn.getTable(TableName.valueOf(tableName));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return false;
		}
		final List<Put> puts = new ArrayList<Put>();
		list.forEach(map -> {
			String rowkey = map.get("rowkey");
			String value  = map.get("value");
			String column = map.get("column");
			String column_source = map.get("column_source");
			Map<String, String> valueMap = new HashMap<String, String>();
			valueMap.put("value", value);
			valueMap.put("source", column_source);
			Gson gson = new Gson();
			/**
	    	 * 插入数据
	    	 */
	    	Put put = new Put(Bytes.toBytes(rowkey));
	    	put.addColumn(Bytes.toBytes("info"), Bytes.toBytes(column), Bytes.toBytes(gson.toJson(valueMap)));
	    	puts.add(put);
	    	
		});
		try {
			_hTable.put(puts);
		} catch (IOException e) {
	    	System.out.println("[QCH]Insert Error. " + e.getMessage());
			return false;
		}
    	System.out.println("[QCH]Insert Success");
    	return true;
	}

}
