module com.example.lab6_part2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;
    requires java.sql;
    requires kotlin.stdlib.jdk7;


    opens com.example.lab6_part2 to javafx.fxml;
    exports com.example.lab6_part2;
}