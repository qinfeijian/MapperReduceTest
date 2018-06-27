package com.yunforge.mapreduce.demo1;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.language.LanguageIdentifier;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.pdf.PDFParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;
/**
 * 
 * 作者:覃飞剑
 * 日期:2018年6月27日
 * 说明:Tika解析文件内容的demo
 */
public class TikaDemo {

//	public static void main(final String[] args) {
//		try {
//			getMetadata();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (TikaException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (SAXException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

	public static void parseToString() throws IOException, TikaException {
		// Assume sample.txt is in your current directory
		File file = new File("D://111.json");

		// Instantiating Tika facade class
		Tika tika = new Tika();
		String filecontent = tika.parseToString(file);
		System.out.println("Extracted Content: " + filecontent);
	}

	public static void parse() throws IOException, TikaException, SAXException {
		// Assume sample.txt is in your current directory
		File file = new File("D:\\Documents\\广西农业数据管理平台项目_ClouderaManager.docx");

		// parse method parameters
		Parser parser = new AutoDetectParser();
		BodyContentHandler handler = new BodyContentHandler();
		Metadata metadata = new Metadata();
		FileInputStream inputstream = new FileInputStream(file);
		ParseContext context = new ParseContext();

		// parsing the file
		parser.parse(inputstream, handler, metadata, context);
		System.out.println("File content : " + handler.toString());
	}

	public static void getMetadata() throws IOException, TikaException, SAXException {
		// Assume that boy.jpg is in your current directory
		File file = new File("D:\\222.json");

		// Parser method parameters
		Parser parser = new AutoDetectParser();
		BodyContentHandler handler = new BodyContentHandler();
		Metadata metadata = new Metadata();
		FileInputStream inputstream = new FileInputStream(file);
		ParseContext context = new ParseContext();

		parser.parse(inputstream, handler, metadata, context);

		// 添加新的元数据
		metadata.add("作者", "qinfj");

		System.out.println("handler to String");
		System.out.println(handler.toString());

		// getting the list of all meta data elements
		String[] metadataNames = metadata.names();
		System.out.println("metadata for each");
		for (String name : metadataNames) {
			System.out.println(name + ": " + metadata.get(name));
		}
	}

	/**
	 * 
	 * 作者:覃飞剑 日期:2018年5月8日
	 * 
	 * @throws IOException
	 * @throws TikaException
	 * @throws SAXException
	 *             返回:void 说明:文档语言检测
	 */
	public static void documentLanguageDetection() throws IOException, TikaException, SAXException {
		LanguageIdentifier identifier = new LanguageIdentifier("你好 ");
		String language = identifier.getLanguage();
		System.out.println("Language of the given content is : " + language);
	}

	public static void PdfParse() throws IOException, TikaException, SAXException {
		BodyContentHandler handler = new BodyContentHandler();
		Metadata metadata = new Metadata();
		FileInputStream inputstream = new FileInputStream(new File("D:\\111.pdf"));
		ParseContext pcontext = new ParseContext();

		// parsing the document using PDF parser
		PDFParser pdfparser = new PDFParser();
		pdfparser.parse(inputstream, handler, metadata, pcontext);

		// getting the content of the document
		System.out.println("Contents of the PDF :" + handler.toString());

		// getting metadata of the document
		System.out.println("Metadata of the PDF:");
		String[] metadataNames = metadata.names();

		for (String name : metadataNames) {
			System.out.println(name + " : " + metadata.get(name));
		}

	}
}
