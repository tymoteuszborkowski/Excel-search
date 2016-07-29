package sample;


import org.apache.commons.io.FileUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
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

    List<String> createStringsFromCells(List<Cell> cellList){
        List<String> stringList = new ArrayList<>();

        for(int i  = 0; i < cellList.size(); i++){
            Cell cell = cellList.get(i);
            String stringCell = cell.getRichStringCellValue().getString();
            stringList.add(stringCell);
        }

        return stringList;
    }

    void searchFiles(File root, List<String> filesNames){
        Collection files;
        for(int i = 0; i < filesNames.size(); i++){
            try {
                files = FileUtils.listFiles(root, null, true);

                for (Iterator iterator = files.iterator(); iterator.hasNext();) {
                    File file = (File) iterator.next();
                    if (file.getName().equals(filesNames.get(i)))
                        System.out.println(file.getAbsolutePath());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }



}



