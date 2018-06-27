package com.yunforge.mapreduce.demo1;

import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.elasticsearch.hadoop.mr.EsInputFormat;
/**
 * 
 * 作者:覃飞剑
 * 日期:2018年6月27日
 * 说明:elasticsearch中的数据写入HDFS
 */
public class ESWriteHdfsTest {
	public static class ESMap extends Mapper<Writable, Writable, NullWritable, Text> {
        @Override
        public void map(Writable key, Writable value, Mapper<Writable, Writable, NullWritable, Text>.Context context)
                throws IOException, InterruptedException {
            // 假如我这边只是想导出数据到HDFS
            Text docVal = new Text();
            docVal.set(value.toString());
            context.write(NullWritable.get(), docVal);
        }
    }

//    public static void main(String[] args) throws Exception {
//
//        long start_time = System.currentTimeMillis();
//        Configuration conf = new Configuration();
//        conf.set("es.nodes", "192.168.1.81:9200");
//        conf.set("es.resource", "test1/sum");
//
//        Job job = Job.getInstance(conf,"hadoop elasticsearch");
//
//        // 指定自定义的Mapper阶段的任务处理类
//        job.setMapperClass(ESMap.class);
//        job.setNumReduceTasks(0);
//        // 设置map输出格式
//        job.setMapOutputKeyClass(NullWritable.class);
//        job.setMapOutputValueClass(Text.class);  
//        // 设置输入格式
//        job.setInputFormatClass(EsInputFormat.class);
//        // 设置输出路径
//        FileOutputFormat.setOutputPath(job, new Path("webhdfs://192.168.1.91:50070/user/test/output"));
//        // 运行MR程序
//        job.waitForCompletion(true);
//        System.out.println(System.currentTimeMillis()-start_time);
//    }
}
