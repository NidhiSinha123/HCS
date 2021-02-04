package com.cg.healthcaresystemrest.exceldownload;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.cg.healthcaresystemrest.dto.Appointment;

public class ExcelGenerator {
  
  public static ByteArrayInputStream customersToExcel(List<Appointment> appointmentList) throws IOException {
    String[] COLUMNs = {"Appointment Id", "Center Name", "Test Name","Date and Time","Status"};
    try(
        Workbook workbook = new XSSFWorkbook();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
    ){
      CreationHelper createHelper = workbook.getCreationHelper();
   
      Sheet sheet = workbook.createSheet("Appointments");
   
      Font headerFont = workbook.createFont();
      headerFont.setBold(true);
      headerFont.setColor(IndexedColors.BLUE.getIndex());
   
      CellStyle headerCellStyle = workbook.createCellStyle();
      headerCellStyle.setFont(headerFont);
   
      // Row for Header
      Row headerRow = sheet.createRow(0);
   
      // Header
      for (int col = 0; col < COLUMNs.length; col++) {
        Cell cell = headerRow.createCell(col);
        cell.setCellValue(COLUMNs[col]);
        cell.setCellStyle(headerCellStyle);
      }
   
      // CellStyle for Age
      CellStyle ageCellStyle = workbook.createCellStyle();
      ageCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("#"));
   
      int rowIdx = 1;
      for (Appointment appointment : appointmentList) {
        Row row = sheet.createRow(rowIdx++);
   
        row.createCell(0).setCellValue(appointment.getAppointmentId().doubleValue());
        row.createCell(1).setCellValue(appointment.getCenter().getCenterName());
        row.createCell(2).setCellValue(appointment.getTest().getTestName());
        row.createCell(3).setCellValue(appointment.getDateTime().toString());
        if(appointment.getAppointmentStatus()==0) {
        row.createCell(4).setCellValue("Pending");
        }
        else if(appointment.getAppointmentStatus() == 1) {
        	 row.createCell(4).setCellValue("Approved");
        }else {
        	row.createCell(4).setCellValue("Rejected");
        }

      }
   
      workbook.write(out);
      return new ByteArrayInputStream(out.toByteArray());
    }
  }
}
