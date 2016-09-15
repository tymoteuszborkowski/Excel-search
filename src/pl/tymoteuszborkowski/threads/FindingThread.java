package pl.tymoteuszborkowski.threads;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.GridPane;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import pl.tymoteuszborkowski.services.PoiService;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

public class FindingThread implements Runnable {


    private static final String STEP_1 = "creating list of sheets";
    private static final String STEP_2 = "creating list of cells";
    private static final String STEP_3 = "searching files. this step may take even several hours";
    private static final String STEP_4 = "creating list of duplicated file names";
    private static final String STEP_5 = "generating workbook with founded files";
    private static final String LAST_STEP = "complete";

    private PoiService service = new PoiService();
    private String excelFilePath;
    private File folderLocalization;
    private String date;
    private ProgressBar progressBar;
    private GridPane layout;
    private Label endWorkLabel;

    public FindingThread(String excelFilePath, File folderLocalization, String date, ProgressBar progressBar, GridPane layout, Label endWorkLabel) throws IOException, InvalidFormatException {
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
                endWorkLabel.setText(STEP_1);

            });
            sheetList = service.createSheetListByPath(excelFilePath);


            // creating list of cells from B column from all of sheets
            Platform.runLater(() -> {
                progressBar.setProgress(0.2);
                endWorkLabel.setText(STEP_2);
            });
            List<List<String>> stringCells = service.getCellsFromColumnB(sheetList);


            //deleting duplicated files names from list
            List<List<String>> stringFromCellsWithoutDupl = service.cellNamesWithoutDuplications(stringCells);



            // searching files by names and returning two lists: found files[0], not found files[1]
            Platform.runLater(() -> {
                progressBar.setProgress(0.4);
                endWorkLabel.setText(STEP_3);

            });


            CopyOnWriteArrayList<CopyOnWriteArrayList<String>> foundedAndNotFounded = new CopyOnWriteArrayList<>();
            foundedAndNotFounded.add(0, new CopyOnWriteArrayList<>());
            foundedAndNotFounded.add(1, new CopyOnWriteArrayList<>());


            // if number of sheets is divisible by 2 create '(number of sheet)/2' threads which searching files

            if(((stringFromCellsWithoutDupl.size() % 2) == 0) && (stringFromCellsWithoutDupl.size() <= 8)){
                for (int i = 0; i < stringFromCellsWithoutDupl.size(); i += 2) {
                    List<String> cleanerFilenames = stringFromCellsWithoutDupl.get(i);
                    List<String> cleanerFilenames2 = stringFromCellsWithoutDupl.get(i+1);
                    List<String> names = new ArrayList<>(cleanerFilenames);

                    names.addAll(cleanerFilenames2);
                    threadList.add(new Thread(new SearchingThread(names, folderLocalization, foundedAndNotFounded)));
                }
            }else{
                ArrayList<String> everyFilenames = new ArrayList<>();
                for(int i = 0; i < stringFromCellsWithoutDupl.size(); i++){
                    stringFromCellsWithoutDupl.forEach(everyFilenames::addAll);
                }

                Set<String> tmpList = new HashSet<>(everyFilenames);
                List<String> cleanerFilenames = new ArrayList<>(tmpList);
                threadList.add(new Thread(new SearchingThread(cleanerFilenames, folderLocalization, foundedAndNotFounded)));
            }

            //start every of these threads
            threadList.forEach(Thread::start);


            // wait for end of every thread
                for(Thread thread : threadList){
                    thread.join();
                }

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
                endWorkLabel.setText(STEP_4);
            });
            ArrayList<String> duplicatedFileNames = service.duplicatedFileNames(stringCells);

            // creating new workbook and saves info about founded, not founded, duplicated files
            Platform.runLater(() -> {
                progressBar.setProgress(0.9);
                endWorkLabel.setText(STEP_5);

            });
            service.createNewWorkBook(foundFiles, notFoundFiles, duplicatedFileNames, date);

            Platform.runLater(() -> {
                progressBar.setProgress(1.0);
                endWorkLabel.setText(LAST_STEP);

            });

        } catch (IOException | InvalidFormatException | InterruptedException e) {
            e.printStackTrace();
        }


    }
}
