module com.example.javafx_vibe.javafx_vibe {
    requires javafx.controls;
    requires javafx.fxml;
            
                            
    opens com.example.javafx_vibe.javafx_vibe to javafx.fxml;
    exports com.example.javafx_vibe.javafx_vibe;
}