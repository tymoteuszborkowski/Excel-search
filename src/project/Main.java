package project;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main extends Application {

    Stage mainWindow;
    Scene mainScene;

    private File excelFile;
    private File folderLocalization;
    private Thread thread;

    @Override
    public void start(Stage primaryStage) throws Exception {
        mainWindow = primaryStage;
        GridPane layout = new GridPane();
        layout.setPadding(new Insets(10, 10, 10, 10));
        layout.setVgap(10);
        layout.setVgap(10);

        PoiService service = new PoiService();

        //progress bar
        double endValue = 1.0;
        ProgressBar progressBar = new ProgressBar();
        progressBar.setPrefSize(300, 30);
        GridPane.setConstraints(progressBar, 0, 7);

        //labels
        Label excelFileLabel = new Label("Choose excel file, where you have stored names of files: ");
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

        Label endWorkLabel = new Label();
        endWorkLabel.setFont(new Font("Arial", 12));
        endWorkLabel.setWrapText(true);
        endWorkLabel.setTextFill(Color.web("#0C4F00"));
        GridPane.setConstraints(endWorkLabel, 0, 9);

        //buttons
        Button selectExceLFileButton = new Button("Select");
        GridPane.setConstraints(selectExceLFileButton, 0, 1);

        Button checkButton = new Button("Generate");
        checkButton.setPrefSize(150, 30);
        GridPane.setConstraints(checkButton, 0, 6);

        Button selectFolderButton = new Button("Select");
        GridPane.setConstraints(selectFolderButton, 0, 4);

        //BUTTONS EVENTS

        mainWindow.setOnCloseRequest(e -> {
            e.consume();
            boolean statement = AlertBoxes.closeWindow();

            if (statement){
                Platform.exit();
                System.exit(0);
            }

        });

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
            if ((excelFile == null) || (folderLocalization == null)) {
                AlertBoxes.notSelectedAlert();
            } else {
                String excelFilePath = excelFile.getAbsolutePath();
                String date = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss").format(new Date());

                try {

                    layout.getChildren().add(progressBar);
                    thread = new Thread(new FindingThread(excelFilePath, folderLocalization, date, progressBar,layout, endWorkLabel ));
                    thread.start();

                } catch (Exception e1) {
                    AlertBoxes.errorBox();
                }

            }



        });

        //layout
        layout.getChildren().addAll(excelFileLabel, selectExceLFileButton, labelSelectedDirectory, labelSearchFolder, selectFolderButton, selectedFolderLabel, checkButton);
        layout.setAlignment(Pos.BASELINE_CENTER);
        mainScene = new Scene(layout, 350, 350);


        mainWindow.setScene(mainScene);
        mainWindow.setTitle("Excel search");
        mainWindow.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
