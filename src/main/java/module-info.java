module com.example.herkansing2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fazecast.jSerialComm;


    opens com.example.herkansing2 to javafx.fxml;
    exports com.example.herkansing2;
}