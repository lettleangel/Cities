package ru.project.naten.cities;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
            Parent root = FXMLLoader.load(getClass().getResource("/resources/fxml/cities.fxml"));
			Scene scene = new Scene(root,400,400);
            scene.getStylesheets().add(getClass().getResource("/resources/css/application.css").toExternalForm());

			primaryStage.setMinHeight(400);
			primaryStage.setMinWidth(400);

			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
