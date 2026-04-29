module org.atpprojectpartc {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires ATPProjectJAR;
    requires javafx.media;

    exports org.atpprojectpartc;              // For MazeDisplayer
    exports org.atpprojectpartc.Model;        // For model classes
    exports org.atpprojectpartc.View;         // For controllers
    exports org.atpprojectpartc.ViewModel;    // For view models

    opens org.atpprojectpartc to javafx.fxml;
    opens org.atpprojectpartc.View to javafx.fxml;  // For controller injection
}