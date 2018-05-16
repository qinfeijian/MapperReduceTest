package com.yunforge.mapreduce;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.hadoop.fs.HdfsResourceLoader;
import org.springframework.test.context.junit4.SpringRunner;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;  

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class MapReduceTestApplicationTests {
	
	@Autowired
	private Configuration hadoopConfiguration;

	@Autowired
	private HdfsResourceLoader hadoopResourceLoader;
	
	public static String uri = "hdfs://192.168.1.91:50070";  
	
	@Test
	public void contextLoads() throws IOException {
		
		String folder = "/user/test";
		FileSystem fs = hadoopResourceLoader.getFileSystem();
		FileStatus[] filesStatus = fs.listStatus(new Path(folder));
		for (FileStatus file : filesStatus) {
			String fileName = file.getPath().getName();
			String filePath = file.getPath().toUri().getPath();
			boolean directory = file.isDirectory();
			
		}
		
	}
	

}
