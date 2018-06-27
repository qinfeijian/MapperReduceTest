package com.yunforge.mapreduce.excel;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.yunforge.mapreduce.util.HbaseBatchUtils;

/**
 * 
 * 作者:覃飞剑
 * 日期:2018年6月27日
 * 说明:从hdfs上获取Excel文件通过poi解析，存入hbase
 */
public class HDFSAndPOIGetExcelToHbase {

	public static String uri = "hdfs://192.168.1.91:8020";
	public static String read = "/user/test/mytest.xlsx";
	public static Configuration conf = new Configuration();

//	public static void main(String[] args) {
//		List<Map<String, String>> insertList = new ArrayList<Map<String, String>>();
//		String url = uri + read;
//		String substring = read.substring(read.lastIndexOf(".") + 1);
//		System.out.println("substring : ");
//		System.out.println(substring);
//		try {
//			FileSystem fs = FileSystem.get(URI.create(url), conf);
//			Path path = new Path(read);
//			if (fs.exists(path)) {
//				FSDataInputStream is = fs.open(path);
//
//				boolean flag = substring.equals("xlsx") ? true : false;
//				Iterator<Row> rowIterator = null;
//				// 获取sheet
//				if (flag) {
//					XSSFWorkbook workbook = new XSSFWorkbook(is);
//					XSSFSheet sheet = workbook.getSheetAt(0);
//					rowIterator = sheet.iterator();
//				} else {
//					// 获取Workbook
//					HSSFWorkbook workbook = new HSSFWorkbook(is);
//					HSSFSheet sheet = workbook.getSheetAt(0);
//					rowIterator = sheet.iterator();
//				}
//
//				/**
//				 * 遍历每一行数据
//				 */
//				String date = "";
//				String divCode = "";
//				String divName = "";
//				String comefrom = "";
//				/**
//				 * 存对应indexCode所在的列
//				 */
//				Map<Integer, String> indexCodeMap = new HashMap<Integer, String>();
//
//				int row_num = 0;
//				while (rowIterator.hasNext()) {
//					Row row = rowIterator.next();
//					/**
//					 * 第一行为指标信息
//					 */
//					if (row_num++ == 0) {
//						Iterator<Cell> colIterator = row.cellIterator();
//						int i = 0;
//						while (colIterator.hasNext()) {
//							/**
//							 * 从第四列开始取
//							 */
//							if (i < 4) {
//								i++;
//								continue;
//							}
//							Cell cell = colIterator.next();
//							cell.setCellType(Cell.CELL_TYPE_STRING);
//							indexCodeMap.put(i, cell.getStringCellValue());
//							i++;
//						}
//						continue;
//					}
//					
//					
//					
//					// 数据行
//					/**
//					 * 遍历每一个cell
//					 */
//					Iterator<Cell> colIterator = row.cellIterator();
//					
//					int i = 0;
//					while (colIterator.hasNext()) {
//						Cell cell = colIterator.next();
//						cell.setCellType(Cell.CELL_TYPE_STRING);
//						if (i < 4) {
//							
//							switch (i) {
//							case 0:
//								date = cell.getStringCellValue();
//								break;
//							case 1:
//								divCode = cell.getStringCellValue();
//								break;
//							case 2:
//								divName = cell.getStringCellValue();
//								break;
//							case 3:
//								comefrom = cell.getStringCellValue();
//								break;
//							default:
//								break;
//							}
//							i++;
//							continue;
//						}
//
//						Map<String, String> oneData = new HashMap<String, String>();
//
//						String rowkey = date + "-" + StringUtils.rightPad(divCode, 12, "0");
//						String column_source = comefrom;
//						String column = indexCodeMap.get(i);
//						String value = cell.getStringCellValue() == null ? "" : cell.getStringCellValue();
//						oneData.put("rowkey", rowkey);
//						oneData.put("column_source", column_source);
//						oneData.put("column", column);
//						oneData.put("value", value);
//						insertList.add(oneData);
//					}
//
//				}
//
//				is.close();
//			}
//			fs.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} finally {
//
//		}
//		System.out.println("insertList 完成");
//		boolean flag = HbaseBatchUtils.batchInsterDataByListMap(insertList, "testimport");
//		if (flag) {
//			System.out.println("插入成功");
//		} else {
//			System.out.println("插入失败");
//		}
//	}

}
