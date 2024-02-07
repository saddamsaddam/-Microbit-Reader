package com.example.herkansing2;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.paint.Color;
import javafx.stage.StageStyle;

import com.fazecast.jSerialComm.SerialPort;

import java.nio.charset.StandardCharsets;

public class HelloApplication extends Application {
    private Label measurementLabel;
    private GridPane gridPane;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        gridPane = new GridPane();
        gridPane.setMinSize(600, 600);
        gridPane.setPadding(new Insets(15, 15, 15, 15));
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        gridPane.setAlignment(Pos.CENTER);

        measurementLabel = new Label("Microbit Measurement: ");
        measurementLabel.setTextFill(Color.CRIMSON); 
        gridPane.add(measurementLabel, 0, 0);
        gridPane.setStyle("-fx-background-color: black;"); 

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(10), event -> readFromMicrobit()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        Scene scene = new Scene(gridPane, 800, 600);
        scene.setFill(Color.BLACK);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Microbit Reader");
      //  primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();
    }

    private void readFromMicrobit() {
        // Gebruik een nieuwe thread voor de communicatie met de micro:bit
        Thread microbitThread = new Thread(() -> {
            SerialPort microbitPort = SerialPort.getCommPort("COM4");
            microbitPort.openPort();

            if (microbitPort.isOpen()) {
                microbitPort.setBaudRate(115200);
                microbitPort.setNumDataBits(8);

                byte[] readBuffer = new byte[1024];
                int bytesRead = microbitPort.readBytes(readBuffer, readBuffer.length);

                if (bytesRead > 0) {
                    String receivedData = new String(readBuffer, 0, bytesRead);
                    Platform.runLater(() -> updateMeasurementLabel("Stappen: " + receivedData + " /8000"));
                }
            }

            microbitPort.closePort();
        });

        microbitThread.start();
    }

    private void updateMeasurementLabel(String text) {
        measurementLabel.setText(text);
    }

    private void showAlert(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
}
