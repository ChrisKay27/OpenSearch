module com.search.search {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires org.jsoup;
    requires java.sql;
    requires robots;

    opens com.search.search to javafx.fxml;
    exports com.search.search;
}