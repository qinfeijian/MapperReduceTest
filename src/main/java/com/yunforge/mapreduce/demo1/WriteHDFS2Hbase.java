package com.yunforge.mapreduce.demo1;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;

/**
 * 
 * 作者:覃飞剑
 * 日期:2018年6月8日
 * 说明:从hdfs读取气象数据文件存入hbase
 */
public class WriteHDFS2Hbase {

	public static class MyMapper extends Mapper<LongWritable, Text, ImmutableBytesWritable, Put>{  
        ImmutableBytesWritable rowkey = new ImmutableBytesWritable();  
  
        @Override  
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {  
        	// 打印样本: Before Mapper: 0, 2000010115
            System.out.println("Before Mapper: " + key + ", " + value);
            String line = value.toString();
            String year = line.substring(0, 8);
            String temperature = line.substring(8);
            
            rowkey.set(Bytes.toBytes(year));  
  
            
            
            Put put = new Put(Bytes.toBytes(year));  
            put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("temperature"), Bytes.toBytes(temperature));  
  
            context.write(rowkey, put);  
            System.out.println("======" + "After Mapper:" + new Text(year) + ", " + new Text(temperature));
        }  
    }  
//	public static void main(String[] args) throws Exception{  
//        //创建Configuration  
//        Configuration configuration = new Configuration();  
//        configuration.set("hbase.zookeeper.quorum", "dn1.yfbd.com");  
//        configuration.set("hbase.zookeeper.property.clientPort", "2181");  
//        configuration.set("zookeeper.znode.parent", "/hbase-unsecure");
//        //创建job  
//        Job job = Job.getInstance(configuration, "WriteHDFS2Hbase");  
//        //设置job的处理类  
//        job.setJarByClass(WriteHDFS2Hbase.class);  
//  
//        job.setMapperClass(MyMapper.class);  
//        job.setMapOutputKeyClass(ImmutableBytesWritable.class);  
//        job.setMapOutputValueClass(Put.class); 
//        String dst = "webhdfs://192.168.1.91:50070/user/test/input.txt";
//        FileInputFormat.addInputPath(job, new Path(dst));  
//  
//        TableMapReduceUtil.initTableReducerJob(  
//                "hdfs2hbase",        // output table  
//               null,    // reducer class  
//                job);  
//        job.setNumReduceTasks(1);   // at least one, adjust as required  
//  
//        boolean b = job.waitForCompletion(true);  
//        if (!b) {  
//            throw new IOException("error with job!");  
//        }  
//    }  

}
