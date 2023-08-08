module com.search.search {
    requires javafx.controls;
    requires javafx.fxml;
        requires javafx.web;
            
        requires org.controlsfx.controls;
            requires com.dlsc.formsfx;
                requires org.kordamp.ikonli.javafx;
            requires org.kordamp.bootstrapfx.core;
            requires eu.hansolo.tilesfx;
        
    opens com.search.search to javafx.fxml;
    exports com.search.search;
}