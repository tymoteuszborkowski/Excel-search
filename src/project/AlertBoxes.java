package project;


import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class AlertBoxes {

    static boolean statement;

    static void notSelectedAlert() {
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Warning!");
        window.setMinWidth(250);

        Label label = new Label();
        label.setText("Select Excel file and folder destination!");
        Button closeButton = new Button("Ok");

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
        window.setTitle("Confirmation");
        window.setMinWidth(250);

        Label label = new Label();
        label.setText("Are you sure you have done everything?");
        Button yesButton = new Button("Yes");
        Button noButton = new Button("No");


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



    static void errorBox() {
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Error!");
        window.setMinWidth(250);

        Label label = new Label();
        label.setText("Error! Pisz do Tymka!");
        Button closeButton = new Button("Ok");

        closeButton.setOnAction(e -> window.close());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, closeButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }

}
