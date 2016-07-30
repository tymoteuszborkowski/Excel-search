package sample;


import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.formula.functions.Index;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

public class PoiService {


    //1

    List<Sheet> createSheetListByPath(String filePath) throws IOException, InvalidFormatException {
        List<Sheet> sheetList = new ArrayList<>();
        Workbook workbook = WorkbookFactory.create(new File(filePath));

        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            Sheet sheet = workbook.getSheetAt(i);
            sheetList.add(sheet);
        }


        return sheetList;
    }

    //2

    List<Cell> getCellsFromColumnB(List<Sheet> sheetList) {
        List<Cell> listOfCells = new ArrayList<>();

        for (int i = 0; i < sheetList.size(); i++) {
            for (Row row : sheetList.get(i)) {
                Cell cell = row.getCell(CellReference.convertColStringToIndex("B"));
                if (cell != null) {
                    listOfCells.add(cell);
                }

            }

        }

        return listOfCells;
    }

    //3

    List<String> createStringsFromCells(List<Cell> cellList) {
        List<String> stringList = new ArrayList<>();

        for (int i = 0; i < cellList.size(); i++) {
            Cell cell = cellList.get(i);
            String stringCell = cell.getRichStringCellValue().getString();
            String stringCellWithoutExt = FilenameUtils.removeExtension(stringCell);
            stringList.add(stringCellWithoutExt);
        }

        return stringList;
    }

    List<ArrayList<String>> searchFiles(File folderLocalization, List<String> filesNames) {
        Collection files;

        ArrayList<String> foundFiles = new ArrayList<>();
        ArrayList<String> notFoundFiles = new ArrayList<>();

        //temporary list to store only names of founded files
        ArrayList<String> tempList = new ArrayList<>();

        for (int i = 0; i < filesNames.size(); i++) {
            try {
                files = FileUtils.listFiles(folderLocalization, null, true);

                for (Iterator iterator = files.iterator(); iterator.hasNext(); ) {
                    File file = (File) iterator.next();
                    if (FilenameUtils.getBaseName(file.getName()).equals(filesNames.get(i))){
                        foundFiles.add(file.getAbsolutePath());
                        tempList.add(FilenameUtils.getBaseName(file.getName()));
                    }

                }

                if(!tempList.contains(filesNames.get(i)))
                    notFoundFiles.add(filesNames.get(i));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        List<ArrayList<String>> listOfLists = new ArrayList<>();
        listOfLists.add(foundFiles);
        listOfLists.add(notFoundFiles);

        return listOfLists;
    }

    ArrayList<String> duplicatedFileNames(List<String> fileNames){

        final ArrayList<String> listToReturn = new ArrayList<>();
        final Set<String> set1 = new HashSet<>();

        for (String fileName : fileNames) {
            if (!set1.add(fileName)) {
                listToReturn.add(fileName);
            }
        }
        return listToReturn;
    }


    void createNewWorkBook(ArrayList<String> foundFiles, ArrayList<String> notFoundFiles, ArrayList<String> duplicatedFileNames, String workbookName) throws IOException {

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


        // IF THERE ARE DUPLICATED ELEMENTS THIS SHEET IS CREATED AND FILLED

        if(!duplicatedFileNames.isEmpty()){
            Sheet duplicatedSheet = workbook.createSheet("duplicated file names");

            for(int i = 0; i < duplicatedFileNames.size(); i++){
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
        Row row1 = workbook.getSheetAt(0).getRow(0);

        for(int colNum = 0; colNum<row1.getLastCellNum();colNum++)
            workbook.getSheetAt(0).autoSizeColumn(colNum);

        // auto size cells from sheet2
        Row row2 = workbook.getSheetAt(1).getRow(0);

        for(int colNum = 0; colNum<row2.getLastCellNum();colNum++)
            workbook.getSheetAt(1).autoSizeColumn(colNum);

        // auto size cells from sheet3 if exists

        if(workbook.getNumberOfSheets() == 3){
            Row row3 = workbook.getSheetAt(2).getRow(0);

            for(int colNum = 0; colNum<row3.getLastCellNum();colNum++)
                workbook.getSheetAt(2).autoSizeColumn(colNum);
        }



        workbook.write(fileOutputStream);
        fileOutputStream.close();
    }

}




