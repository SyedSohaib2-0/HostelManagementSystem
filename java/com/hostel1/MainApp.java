package com.hostel1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class MainApp extends Application {

	@Override
	public void start(Stage stage) throws IOException {
	    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainDashboard.fxml"));
	    Scene scene = new Scene(loader.load(), 800, 600);  // Set width and height here
	    scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
	    
	    stage.setScene(scene);
	    stage.setTitle("Hostel Management System");
	    stage.show();
	}


    public static void main(String[] args) {
        launch(args);
    }

}
