package com.yunforge.mapreduce.excel;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

/** 
* @ProjectName HandleExcelPhone
* @PackageName com.buaa
* @ClassName ExcelParser
* @Description 解析excel
* @Author 刘吉超
* @Date 2016-04-24 16:59:28
* 首先，输入文件是Excel格式，我们可以借助poi来解析Excel文件，我们先来实现一个Excel的解析类（ExcelParser）
*/
public class ExcelParser {
    private static final Log logger = LogFactory.getLog(ExcelParser.class);

    /**
     * 解析is
     * 
     * @param is 数据源
     * @return String[]
     */
    public static String[] parseExcelData(InputStream is) {
        // 结果集
        List<String> resultList = new ArrayList<String>();
        
        try {
            // 获取Workbook
            HSSFWorkbook workbook = new HSSFWorkbook(is);
            // 获取sheet
            HSSFSheet sheet = workbook.getSheetAt(0);
            
            Iterator<Row> rowIterator = sheet.iterator();
            
            while (rowIterator.hasNext()) {
                // 行
                Row row = rowIterator.next();
                // 字符串
                StringBuilder rowString = new StringBuilder();
                
                Iterator<Cell> colIterator = row.cellIterator();
                while (colIterator.hasNext()) {
                    Cell cell = colIterator.next();

                    switch (cell.getCellType()) {
                        case Cell.CELL_TYPE_BOOLEAN:
                            rowString.append(cell.getBooleanCellValue() + "\t");
                            break;
                        case Cell.CELL_TYPE_NUMERIC:
                            rowString.append(cell.getNumericCellValue() + "\t");
                            break;
                        case Cell.CELL_TYPE_STRING:
                            rowString.append(cell.getStringCellValue() + "\t");
                            break;
                    }
                }
                
                resultList.add(rowString.toString());
            }
        } catch (IOException e) {
            logger.error("IO Exception : File not found " + e);
        }
        System.out.println(resultList.toString());
        return resultList.toArray(new String[0]);
    }
}