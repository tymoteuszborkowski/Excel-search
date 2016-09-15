package pl.tymoteuszborkowski;


import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;


class AlertBoxes {

    private static final String CONFIRMATION_LABEL = "Confirmation";
    private static final String CONFIRMATION_TEXT = "Are you sure you have done everything?";
    private static final String CONFIRMATION_YES = "Yes";
    private static final String CONFIRMATION_NO = "No";
    private static final String OK = "Ok";

    private static boolean statement;

    static void popUpAlert(String title, String text){
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);

        Label label = new Label();
        label.setText(text);
        Button closeButton = new Button(OK);

        closeButton.setOnAction(e -> window.close());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, closeButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }

    static boolean closeWindow() {
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(CONFIRMATION_LABEL);
        window.setMinWidth(250);

        Label label = new Label();
        label.setText(CONFIRMATION_TEXT);
        Button yesButton = new Button(CONFIRMATION_YES);
        Button noButton = new Button(CONFIRMATION_NO);


        yesButton.setOnAction(e -> {
            statement = true;
            window.close();
        });

        noButton.setOnAction(e ->{
            statement = false;
            window.close();
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, yesButton, noButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();

        return statement;
    }

}
