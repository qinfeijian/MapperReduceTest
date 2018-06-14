package com.yunforge.mapreduce.demo1;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FsStatus;
import org.apache.hadoop.fs.Path;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.junit.Test;

import com.google.gson.Gson;
import com.yunforge.mapreduce.demo1.HdfsUtils;

import lombok.Cleanup;;

/**
 * 
 * 作者:覃飞剑
 * 日期:2018年6月12日
 * 说明:将HDFS的/user/test下的所有文件 经过tika解析内容，把内容解析成json字符串存入file.json文件中
 */
public class HdfsUtilsTest {
	
	public static String uri = "hdfs://192.168.1.91:8020";  
    public String dir = "/user/test";  
    public String write = "/user/test/file.json";
    public Configuration conf = new Configuration();
    
    @Test  
    public void listFile() {  
    	dir = uri + dir;
    	write = uri + write;
    	FSDataInputStream inputStream = null;
    	FileSystem fs = null;
    	FileSystem write_fs = null;
    	FSDataOutputStream os = null;
        try{  
        	fs = FileSystem.get(URI.create(dir), conf);
        	write_fs = FileSystem.get(URI.create(write), conf);
        	
        	os = write_fs.create(new Path(write));
        	
        	FileStatus[] stats = fs.listStatus(new Path(dir));
    		for (int i = 0; i < stats.length; ++i) {
    			
    			
    			if (stats[i].isFile()) {
    				Map<String, Object> map = new HashMap<String, Object>();
    				
    				// regular file
    				inputStream = fs.open(stats[i].getPath());
    				// Parser method parameters
    				Parser parser = new AutoDetectParser();
    				BodyContentHandler handler = new BodyContentHandler();
    				Metadata metadata = new Metadata();
    				ParseContext context = new ParseContext();
    				
    				parser.parse(inputStream, handler, metadata, context);
    				

//    				System.out.println("handler to String");
//    				System.out.println(handler.toString());
    				
    				map.put("content", replaceBlank(handler.toString()));

    				// getting the list of all meta data elements
    				String[] metadataNames = metadata.names();
    				System.out.println("metadata for each");
    				for (String name : metadataNames) {
//    					System.out.println(name + ": " + metadata.get(name));
    					map.put(name, metadata.get(name));
    				}
    				
    				Gson gson = new Gson();
    				String json = gson.toJson(map);
    				os.write(json.getBytes());
    				os.write("\r\n".getBytes());  
    				
    			} else if (stats[i].isDirectory()) {
    				// dir
    				
    			} else if (stats[i].isSymlink()) {
    				// is s symlink in linux
    				
    			}
    			
    		}

    		
        } catch(Exception ex){  
        	ex.printStackTrace(); 
        }  finally {
        	try {
        		os.close();
        		inputStream.close();
        		fs.close();
        		write_fs.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }  
    /**
     * 
     * 作者:覃飞剑
     * 日期:2018年5月10日
     * @param str
     * @return
     * 返回:String
     * 说明:去除字符串中的空格、回车、换行符、制表符
     */
    public static String replaceBlank(String str) {  
        String dest = "";  
        if (str!=null) {  
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");  
            Matcher m = p.matcher(str);  
            dest = m.replaceAll("");  
        }  
        return dest;  
    } 

}
