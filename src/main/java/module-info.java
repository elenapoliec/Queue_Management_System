module com.example.assignment2 {
    requires javafx.controls;
    requires javafx.fxml;

    requires java.desktop;



    opens com.example.assignment2 to javafx.fxml;
    exports com.example.assignment2;
    exports GUI;
    opens GUI to javafx.fxml;
    exports MODEL;
    opens MODEL to javafx.fxml;
    exports BUSINESS;
    opens BUSINESS to javafx.fxml;
}