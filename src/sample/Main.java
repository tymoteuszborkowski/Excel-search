package sample;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Main extends Application {

    Stage mainWindow;
    Scene mainScene;

    private File excelFile;
    private File folderLocalization;


    @Override
    public void start(Stage primaryStage) throws Exception {
        mainWindow = primaryStage;
        GridPane layout = new GridPane();
        layout.setPadding(new Insets(10, 10, 10, 10));
        layout.setVgap(10);
        layout.setVgap(10);

        PoiService service = new PoiService();


        //labels
        Label excelFileLabel = new Label("Choose excel file, where You have stored names of files: ");
        excelFileLabel.setFont(new Font("Arial", 12));
        GridPane.setConstraints(excelFileLabel, 0, 0);

        Label labelSelectedDirectory = new Label();
        labelSelectedDirectory.setFont(new Font("Arial", 12));
        labelSelectedDirectory.setWrapText(true);
        GridPane.setConstraints(labelSelectedDirectory, 0, 2);

        Label labelSearchFolder = new Label("Where you want to search? Choose folder.");
        labelSearchFolder.setFont(new Font("Arial", 12));
        GridPane.setConstraints(labelSearchFolder, 0, 3);

        Label selectedFolderLabel = new Label();
        selectedFolderLabel.setFont(new Font("Arial", 12));
        selectedFolderLabel.setWrapText(true);
        GridPane.setConstraints(selectedFolderLabel, 0, 5);

        //buttons
        Button selectExceLFileButton = new Button("Select");
        GridPane.setConstraints(selectExceLFileButton, 0, 1);

        Button checkButton = new Button("Check");
        GridPane.setConstraints(checkButton, 0, 6);

        Button selectFolderButton = new Button("Select");
        GridPane.setConstraints(selectFolderButton, 0, 4);

        //BUTTONS EVENTS

        selectExceLFileButton.setOnAction(e -> {

            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel files", "*.xlsx", "*.xls", "*.xlt", "*.xlm", "*.xlsm", "*.xltx", "*.xltm"));
            excelFile = fileChooser.showOpenDialog(mainWindow);

            if (excelFile == null) {
                labelSelectedDirectory.setText("No directory selected");
                labelSelectedDirectory.setTextFill(Color.web("#AA0114"));
            } else {
                labelSelectedDirectory.setText("File path: " + excelFile.getAbsolutePath());
                labelSelectedDirectory.setTextFill(Color.web("#3FAB4F"));
            }
        });

        selectFolderButton.setOnAction(e -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            folderLocalization = directoryChooser.showDialog(mainWindow);

            if (folderLocalization == null) {
                selectedFolderLabel.setText("No folder selected!");
                selectedFolderLabel.setTextFill(Color.web("#AA0114"));
            } else {
                selectedFolderLabel.setText("Chosen folder: " + folderLocalization.getAbsolutePath());
                selectedFolderLabel.setTextFill(Color.web("#3FAB4F"));
            }
        });


        checkButton.setOnAction(e -> {
            String excelFilePath = excelFile.getAbsolutePath();
            String date = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss").format(new Date());
            try {
                //creating list of sheets from selected excel file
                List<Sheet> sheetList = service.createSheetListByPath(excelFilePath);
                // creating list of cells from B column from all of sheets
                List<Cell> cells = service.getCellsFromColumnB(sheetList);
                // changing cells to names of searching files
                List<String> filesNames = service.createStringsFromCells(cells);
                // searching files by names and returning two lists: found files[0], not found files[1]
                List<ArrayList<String>> listOfLists = service.searchFiles(folderLocalization, filesNames);
                ArrayList<String> foundFiles = listOfLists.get(0);
                ArrayList<String> notFoundFiles = listOfLists.get(1);
                // creating list of duplicated file names
                ArrayList<String> duplicatedFileNames = service.duplicatedFileNames(filesNames);

                // creating new workbook and saves info about founded, not founded, duplicated files
                service.createNewWorkBook(foundFiles, notFoundFiles, duplicatedFileNames, date);

            } catch (IOException | InvalidFormatException e1) {
                e1.printStackTrace();
            }


        });

        //layout
        layout.getChildren().addAll(excelFileLabel, selectExceLFileButton, labelSelectedDirectory, labelSearchFolder, selectFolderButton, selectedFolderLabel, checkButton);
        mainScene = new Scene(layout, 500, 500);


        mainWindow.setScene(mainScene);
        mainWindow.setTitle("Excel search");
        mainWindow.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
