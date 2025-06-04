module com.arboles.arbolbinariofx {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;

    opens com.arboles.id3fx to javafx.fxml;
    exports com.arboles.id3fx;
}