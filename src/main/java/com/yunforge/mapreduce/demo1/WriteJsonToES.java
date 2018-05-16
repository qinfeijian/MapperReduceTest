package com.yunforge.mapreduce.demo1;

import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.elasticsearch.hadoop.mr.EsOutputFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;  

public class WriteJsonToES {
	
	public static class SomeMapper extends Mapper<Object, Text, NullWritable, BytesWritable> {
		
        private static final Logger LOG = LoggerFactory.getLogger(SomeMapper.class);  

        @Override
        public void map(Object key, Text value, Mapper<Object, Text, NullWritable, BytesWritable>.Context context) throws IOException, InterruptedException {
            byte[] source = value.toString().trim().getBytes();
            BytesWritable jsonDoc = new BytesWritable(source);
            context.write(NullWritable.get(), jsonDoc);
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS","hdfs://192.168.1.91:8020");
        conf.set("hadoop.tmp.dir", "/tmp/hadoop");
        conf.setBoolean("mapred.map.tasks.speculative.execution", false);
        conf.setBoolean("mapred.reduce.tasks.speculative.execution", false);
        conf.set("es.nodes", "192.168.1.119:9200");
        conf.set("es.resource", "m10/sum");
        conf.set("es.input.json", "yes"); 

        Job job = Job.getInstance(conf,"hadoop es write test");
        job.setMapperClass(SomeMapper.class);

        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(EsOutputFormat.class);

        job.setMapOutputKeyClass(NullWritable.class);
        job.setMapOutputValueClass(BytesWritable.class);

        // 设置输入路径
        FileInputFormat.setInputPaths(job, new Path("/user/test/file.json"));

        job.waitForCompletion(true);
    }
	

}
