module com.example.javafx_vibe.javafx_vibe {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fazecast.jSerialComm;


    opens com.example.javafx_vibe.javafx_vibe to javafx.fxml;
    exports com.example.javafx_vibe.javafx_vibe;
}