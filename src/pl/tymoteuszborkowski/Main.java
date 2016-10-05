package pl.tymoteuszborkowski;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import pl.tymoteuszborkowski.threads.FindingThread;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main extends Application {


    private static final String TITLE = "Excel search";
    private static final String EXCEL_FILE_LABEL = "Choose excel file, where you have stored names of files: ";
    private static final String SEARCH_LOCATION_LABEL = "Where you want to search? Choose folder.";
    private static final String PICK_COLUMN_LABEL = "Pick workbook column with filenames.";
    private static final String SELECT_BTN_LABEL = "Select";
    private static final String GENERATE_BTN_LABEL = "Generate";
    private static final String ARIAL_FONT = "Arial";
    private static final String EXTENSIONS_FILTER_LABEL = "Excel files";
    private static final String GREEN_COLOR = "#0C4F00";
    private static final String RED_COLOR = "#AA0114";
    private static final String NO_DIR_SELECTED = "No directory selected";
    private static final String NO_SEARCH_DIR_SELECTED = "Please select searching directory";
    private static final String FILE_PATH_PREFIX = "File path: ";
    private static final String DATE_FORMAT = "dd-MM-yyyy-HH-mm-ss";
    private static final String WARNING = "Warning!";
    private static final String SELECT_EXCEL_FILE = "Select Excel file and folder destination!";
    private static final String ERROR = "Error!";
    private static final String[] EXTENSIONS = new String[]{"*.xlsx", "*.xls", "*.xlt", "*.xlm", "*.xlsm", "*.xltx", "*.xltm"};
    private static final ObservableList<String> columns = FXCollections
            .observableArrayList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z");


    private Stage mainWindow;

    private File excelFile;
    private File folderLocalization;
    private Thread thread;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        mainWindow = primaryStage;

        final GridPane layout = new GridPane();
            layout.setPadding(new Insets(10, 10, 10, 10));
            layout.setVgap(10);
            layout.setVgap(10);


        //progress bar
        final ProgressBar progressBar = new ProgressBar();
            progressBar.setPrefSize(300, 30);
            GridPane.setConstraints(progressBar, 0, 9);

        //labels
        final Label excelFileLabel = new Label(EXCEL_FILE_LABEL);
            excelFileLabel.setFont(new Font(ARIAL_FONT, 12));
            GridPane.setConstraints(excelFileLabel, 0, 0);

        final Label labelSelectedDirectory = new Label();
            labelSelectedDirectory.setFont(new Font(ARIAL_FONT, 12));
            labelSelectedDirectory.setWrapText(true);
            GridPane.setConstraints(labelSelectedDirectory, 0, 2);

        final Label labelSearchFolder = new Label(SEARCH_LOCATION_LABEL);
            labelSearchFolder.setFont(new Font(ARIAL_FONT, 12));
            GridPane.setConstraints(labelSearchFolder, 0, 3);

        final Label selectedFolderLabel = new Label();
            selectedFolderLabel.setFont(new Font(ARIAL_FONT, 12));
            selectedFolderLabel.setWrapText(true);
            GridPane.setConstraints(selectedFolderLabel, 0, 5);


        final Label columnPickerLabel = new Label(PICK_COLUMN_LABEL);
            columnPickerLabel.setFont(new Font(ARIAL_FONT, 12));
            columnPickerLabel.setWrapText(true);
            GridPane.setConstraints(columnPickerLabel, 0, 6);

        final Label endWorkLabel = new Label();
            endWorkLabel.setFont(new Font(ARIAL_FONT, 12));
            endWorkLabel.setWrapText(true);
            endWorkLabel.setTextFill(Color.web(GREEN_COLOR));
            GridPane.setConstraints(endWorkLabel, 0, 10);

        //BUTTONS
        final Button selectExcelFileButton = new Button(SELECT_BTN_LABEL);
            GridPane.setConstraints(selectExcelFileButton, 0, 1);

        final Button selectFolderButton = new Button(SELECT_BTN_LABEL);
        GridPane.setConstraints(selectFolderButton, 0, 4);

        final Button generateButton = new Button(GENERATE_BTN_LABEL);
            generateButton.setPrefSize(150, 30);
            GridPane.setConstraints(generateButton, 0, 8);


        // COMBO BOX (COLUMN PICKER)

        final ComboBox<String> columnPicker =new ComboBox<> (columns);
            columnPicker.setValue(columns.get(0));
            GridPane.setConstraints(columnPicker, 0, 7);


        //BUTTONS EVENTS

        mainWindow.setOnCloseRequest(e -> {
            e.consume();
            boolean statement = AlertBoxes.closeWindow();

            if (statement){
                Platform.exit();
                System.exit(0);
            }

        });

        selectExcelFileButton.setOnAction(e -> {

            final FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(EXTENSIONS_FILTER_LABEL, EXTENSIONS));
            excelFile = fileChooser.showOpenDialog(mainWindow);

            if (excelFile == null) {
                labelSelectedDirectory.setText(NO_DIR_SELECTED);
                labelSelectedDirectory.setTextFill(Color.web(RED_COLOR));
            } else {
                labelSelectedDirectory.setText(FILE_PATH_PREFIX + excelFile.getAbsolutePath());
                labelSelectedDirectory.setTextFill(Color.web(GREEN_COLOR));
            }
        });

        selectFolderButton.setOnAction(e -> {
            final DirectoryChooser directoryChooser = new DirectoryChooser();
            folderLocalization = directoryChooser.showDialog(mainWindow);

            if (folderLocalization == null) {
                selectedFolderLabel.setText(NO_SEARCH_DIR_SELECTED);
                selectedFolderLabel.setTextFill(Color.web(RED_COLOR));
            } else {
                selectedFolderLabel.setText("Chosen folder: " + folderLocalization.getAbsolutePath());
                selectedFolderLabel.setTextFill(Color.web(GREEN_COLOR));
            }
        });


        generateButton.setOnAction(e -> {
            if ((excelFile == null) || (folderLocalization == null)) {
                AlertBoxes.popUpAlert(WARNING, SELECT_EXCEL_FILE);
            } else {
                final String excelFilePath = excelFile.getAbsolutePath();
                final String column = columnPicker.getValue();
                final String date = new SimpleDateFormat(DATE_FORMAT).format(new Date());

                try {

                    layout.getChildren().add(progressBar);
                    thread = new Thread(new FindingThread(excelFilePath, column, folderLocalization, date, progressBar,layout, endWorkLabel ));
                    thread.start();

                } catch (Exception e1) {
                    AlertBoxes.popUpAlert(ERROR, ERROR);
                }

            }



        });

        //layout
        layout.getChildren().addAll(excelFileLabel,
                selectExcelFileButton,
                labelSelectedDirectory,
                labelSearchFolder,
                selectFolderButton,
                selectedFolderLabel,
                generateButton,
                columnPicker,
                columnPickerLabel);

        layout.setAlignment(Pos.BASELINE_CENTER);
        final Scene mainScene = new Scene(layout, 350, 350);


        mainWindow.setScene(mainScene);
        mainWindow.setTitle(TITLE);
        mainWindow.show();
    }

}
