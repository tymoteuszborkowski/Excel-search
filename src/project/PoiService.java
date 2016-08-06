package project;


import org.apache.commons.io.FilenameUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class PoiService {


    //1

    public List<Sheet> createSheetListByPath(String filePath) throws IOException, InvalidFormatException {
        final List<Sheet> sheetList = new ArrayList<>();
        Workbook workbook = WorkbookFactory.create(new File(filePath));

        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            Sheet sheet = workbook.getSheetAt(i);
            sheetList.add(sheet);
        }


        return sheetList;
    }


    //2


    public List<List<String>> getCellsFromColumnB(List<Sheet> sheetList) {

        List<List<String>> filenames = new ArrayList<>();


        for (int i = 0; i < sheetList.size(); i++) {
            Sheet sheet = sheetList.get(i);
            filenames.add(new ArrayList<>());
            for (int j = 0; j < sheet.getLastRowNum(); j++) {
                Row row = sheet.getRow(j);
                Cell cell = row.getCell(CellReference.convertColStringToIndex("B"));
                if (cell != null) {
                    String stringCellValue = cell.getStringCellValue();
                    filenames.get(i).add(stringCellValue);
                }

            }
        }

            return filenames;
    }


    public List<List<String>> cellNamesWithoutDuplications(List<List<String>> listsOfStringCells) {
        Set<String> setFilesNames = new HashSet<>();
        List<List<String>> listsOfCleanerStrings = new ArrayList<>();

        for (int i = 0; i < listsOfStringCells.size(); i++) {
            List<String> stringCells = listsOfStringCells.get(i);
            for (int j = 0; j < stringCells.size(); j++) {
                setFilesNames.add(stringCells.get(j));
            }

            List<String> cleanerFilesNames = new ArrayList<>(setFilesNames);
            listsOfCleanerStrings.add(cleanerFilesNames);
            setFilesNames.clear();

        }


        return listsOfCleanerStrings;
    }


    public ArrayList<String> duplicatedFileNames(List<List<String>> listOfFilenames) {

        final ArrayList<String> listToReturn = new ArrayList<>();
        final Set<String> set1 = new HashSet<>();

        for (List<String> list : listOfFilenames) {
            for (String fileName : list) {
                if (!set1.add(fileName)) {
                    listToReturn.add(fileName);
                }
            }
        }
        return listToReturn;
    }


    public void createNewWorkBook(ArrayList<String> foundFiles, ArrayList<String> notFoundFiles, ArrayList<String> duplicatedFileNames, String workbookName) throws IOException {

        Workbook workbook = new XSSFWorkbook();
        //style GREEN
        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.GREEN.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);

        //style RED
        CellStyle redStyle = workbook.createCellStyle();
        redStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
        redStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);

        //style orange
        CellStyle orangeStyle = workbook.createCellStyle();
        orangeStyle.setFillForegroundColor(IndexedColors.ORANGE.getIndex());
        orangeStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);


        FileOutputStream fileOutputStream = new FileOutputStream("created workbooks/" + workbookName + ".xlsx");


        CreationHelper creationHelper = workbook.getCreationHelper();
        Sheet foundSheet = workbook.createSheet("found files");
        Sheet notFoundSheet = workbook.createSheet("not found files");
        Sheet duplicatedSheet = workbook.createSheet("duplicated file names");


        // FOUND SHEET

        for (int i = 0; i < foundFiles.size(); i++) {
            Row row = foundSheet.createRow((short) i);

            Cell cellFile = row.createCell(0);
            cellFile.setCellValue(creationHelper.createRichTextString("File:"));

            Cell cellFileName = row.createCell(2);
            cellFileName.setCellValue(creationHelper.createRichTextString(FilenameUtils.getName(foundFiles.get(i))));

            Cell cellDir = row.createCell(4);
            cellDir.setCellValue(creationHelper.createRichTextString("directory:"));

            Cell cellFilePath = row.createCell(6);
            cellFilePath.setCellValue(creationHelper.createRichTextString(foundFiles.get(i)));

            Cell cellFound = row.createCell(8);
            cellFound.setCellValue(creationHelper.createRichTextString("FOUND"));
            cellFound.setCellStyle(style);

        }

        // NOT FOUND SHEET
        if (!notFoundFiles.isEmpty()) {

            for (int i = 0; i < notFoundFiles.size(); i++) {
                Row row = notFoundSheet.createRow((short) i);

                Cell cellFile = row.createCell(0);
                cellFile.setCellValue(creationHelper.createRichTextString("File:"));

                Cell cellFileName = row.createCell(2);
                cellFileName.setCellValue(creationHelper.createRichTextString(notFoundFiles.get(i)));

                Cell cellNotFound = row.createCell(4);
                cellNotFound.setCellValue(creationHelper.createRichTextString("NOT FOUND"));
                cellNotFound.setCellStyle(redStyle);
            }
        }


        // IF THERE ARE DUPLICATED ELEMENTS THIS SHEET IS CREATED AND FILLED

        if (!duplicatedFileNames.isEmpty()) {

            for (int i = 0; i < duplicatedFileNames.size(); i++) {
                Row row = duplicatedSheet.createRow((short) i);

                Cell cellFile = row.createCell(0);
                cellFile.setCellValue(creationHelper.createRichTextString("File:"));

                Cell cellFileName = row.createCell(2);
                cellFileName.setCellValue(creationHelper.createRichTextString(duplicatedFileNames.get(i)));

                Cell cellNotFound = row.createCell(4);
                cellNotFound.setCellValue(creationHelper.createRichTextString("DUPLICATED"));
                cellNotFound.setCellStyle(orangeStyle);

            }
        }


        // auto size cells from sheet1
        if (workbook.getSheetAt(0).getRow(0) != null) {
            Row row1 = workbook.getSheetAt(0).getRow(0);

            for (int colNum = 0; colNum < row1.getLastCellNum(); colNum++)
                workbook.getSheetAt(0).autoSizeColumn(colNum);
        }
        // auto size cells from sheet2
        if (workbook.getSheetAt(1).getRow(0) != null) {
            Row row2 = workbook.getSheetAt(1).getRow(0);
            for (int colNum = 0; colNum < row2.getLastCellNum(); colNum++)
                workbook.getSheetAt(1).autoSizeColumn(colNum);

        }
        // auto size cells from sheet3 if exists

        if (workbook.getSheetAt(2).getRow(0) != null) {
            Row row3 = workbook.getSheetAt(2).getRow(0);

            for (int colNum = 0; colNum < row3.getLastCellNum(); colNum++)
                workbook.getSheetAt(2).autoSizeColumn(colNum);
        }


        workbook.write(fileOutputStream);
        fileOutputStream.close();
    }

}




