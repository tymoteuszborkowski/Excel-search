package sample;


import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellReference;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    List<File> createFilesFromCells(List<Cell> cellList){
        List<File> fileList = new ArrayList<>();

        for(int i  = 0; i < cellList.size(); i++){
            Cell cell = cellList.get(i);
            String stringCell = cell.getRichStringCellValue().getString();
            File fileCell = new File(stringCell);
            fileList.add(fileCell);
        }

        return fileList;
    }


}



