package project;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.GridPane;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

public class FindingThread implements Runnable {

    private PoiService service = new PoiService();
    private String excelFilePath;
    private File folderLocalization;
    private String date;
    private ProgressBar progressBar;
    private GridPane layout;
    private Label endWorkLabel;

    FindingThread(String excelFilePath, File folderLocalization, String date, ProgressBar progressBar, GridPane layout, Label endWorkLabel) throws IOException, InvalidFormatException {
        this.excelFilePath = excelFilePath;
        this.folderLocalization = folderLocalization;
        this.date = date;
        this.progressBar = progressBar;
        this.layout = layout;
        this.endWorkLabel = endWorkLabel;

    }

    @Override
    public void run() {

        List<Sheet> sheetList;
        ArrayList<String> foundFiles;
        ArrayList<String> notFoundFiles;
        List<Thread> threadList = new ArrayList<>();
        try {

            //creating list of sheets from selected excel file
            Platform.runLater(() -> {
                progressBar.setProgress(0.1);
                layout.getChildren().add(endWorkLabel);
                endWorkLabel.setText("creating list of sheets");

            });
            sheetList = service.createSheetListByPath(excelFilePath);


            // creating list of cells from B column from all of sheets
            Platform.runLater(() -> {
                progressBar.setProgress(0.2);
                endWorkLabel.setText("creating list of cells");
            });
            List<List<String>> stringCells = service.getCellsFromColumnB(sheetList);


            //deleting duplicated files names from list
            List<List<String>> stringFromCellsWithoutDupl = service.cellNamesWithoutDuplications(stringCells);




        ////////////////////////////////////////////////////////////////////////////////////////////////////

            // searching files by names and returning two lists: found files[0], not found files[1]
            Platform.runLater(() -> {
                progressBar.setProgress(0.4);
                endWorkLabel.setText("searching files. this step may take even several hours");

            });


            CopyOnWriteArrayList<CopyOnWriteArrayList<String>> foundedAndNotFounded = new CopyOnWriteArrayList<>();
            foundedAndNotFounded.add(0, new CopyOnWriteArrayList<>());
            foundedAndNotFounded.add(1, new CopyOnWriteArrayList<>());


            // if number of sheets is divisible by 2 create '(number of sheet)/2' threads which searching files

            long start = System.currentTimeMillis();
            if((stringFromCellsWithoutDupl.size() % 2) == 0){
                for (int i = 0; i < stringFromCellsWithoutDupl.size(); i += 2) {
                    List<String> cleanerFilenames = stringFromCellsWithoutDupl.get(i);
                    List<String> cleanerFilenames2 = stringFromCellsWithoutDupl.get(i+1);
                    List<String> names = new ArrayList<>(cleanerFilenames);

                    for(String s: cleanerFilenames2)
                        names.add(s);
                    threadList.add(new Thread(new SearchingThread(names, folderLocalization, foundedAndNotFounded)));
                }
            }else{
                ArrayList<String> everyFilenames = new ArrayList<>();
                for(int i = 0; i < stringFromCellsWithoutDupl.size(); i++){
                    for(List<String> list: stringFromCellsWithoutDupl){
                        for(String name : list){
                            everyFilenames.add(name);
                        }
                    }
                }

                Set<String> tmpList = new HashSet<>(everyFilenames);
                List<String> cleanerFilenames = new ArrayList<>(tmpList);
                threadList.add(new Thread(new SearchingThread(cleanerFilenames, folderLocalization, foundedAndNotFounded)));
            }

            //start every of these threads
            for (Thread thread : threadList)
                thread.start();


            // wait for end of every thread
                for(Thread thread : threadList){
                    thread.join();
                }
                long seconds = System.currentTimeMillis()-start;

            // grab lists of founded and not founded files
            CopyOnWriteArrayList<String> tmpListFoundFiles = foundedAndNotFounded.get(0);
            CopyOnWriteArrayList<String> tmpListNotFoundFiles = foundedAndNotFounded.get(1);


            //change them to array lists
            foundFiles = new ArrayList<>(tmpListFoundFiles);
            notFoundFiles = new ArrayList<>(tmpListNotFoundFiles);

        //////////////////////////////////////////////////////////////////////////////////////////////////

            // creating list of duplicated file names
            Platform.runLater(() -> {
                progressBar.setProgress(0.8);
                endWorkLabel.setText("creating list of duplicated file names");
            });
            ArrayList<String> duplicatedFileNames = service.duplicatedFileNames(stringCells);

            // creating new workbook and saves info about founded, not founded, duplicated files
            Platform.runLater(() -> {
                progressBar.setProgress(0.9);
                endWorkLabel.setText("generating workbook with founded files");

            });
            service.createNewWorkBook(foundFiles, notFoundFiles, duplicatedFileNames, date);

            Platform.runLater(() -> {
                progressBar.setProgress(1.0);
                endWorkLabel.setText("complete");

            });

        } catch (IOException | InvalidFormatException | InterruptedException e) {
            e.printStackTrace();
        }


    }
}
