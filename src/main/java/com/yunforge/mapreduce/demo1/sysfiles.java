package com.yunforge.mapreduce.demo1;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Base64.Encoder;
import java.util.List;

import org.apache.tika.Tika;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.pdf.PDFParser;
import org.apache.tika.sax.BodyContentHandler;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.*;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.xml.sax.ContentHandler;
import sun.misc.BASE64Encoder;
/**
 * 
 * 作者:覃飞剑
 * 日期:2018年6月27日
 * 说明:系统文件写入elasticsearch
 */
public class sysfiles {
	
//	public static void main(String[] args) {
//		try {
//			sys();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

	public static void sys() throws Exception {
		List<InetSocketTransportAddress> addressList = new ArrayList<InetSocketTransportAddress>();
		addressList.add(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
		Settings setting = Settings.builder().put("cluster.name", "elasticsearch").build();
		TransportClient client = new PreBuiltTransportClient(setting)
				.addTransportAddresses(addressList.toArray(new InetSocketTransportAddress[addressList.size()]));
		String data64 = encodeBase64File("D://111.pdf");
		XContentBuilder source = XContentFactory.jsonBuilder().startObject().field("file", data64).field("text", data64)
				.endObject();
		String id = "file" + 11;
		IndexResponse idxResp = client.prepareIndex().setIndex("test").setType("person").setId(id).setSource(source)
				.execute().actionGet();
		System.out.println(idxResp);
		client.close();
	}

	/**
	 * 将文件转成base64 字符串
	 * 
	 * @param path文件路径
	 * @return *
	 * @throws Exception
	 */

	public static String encodeBase64File(String path) throws Exception {
		File file = new File(path);
		;
		FileInputStream inputFile = new FileInputStream(file);
		byte[] buffer = new byte[(int) file.length()];
		inputFile.read(buffer);
		inputFile.close();
		return new BASE64Encoder().encode(buffer);

	}
}
