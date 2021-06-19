package view;
	
import java.net.URL;

import controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class ApplicationStartup extends Application {
	@Override
	//main startup class
	public void start(Stage primaryStage) {
		try {
			URL location=getClass().getResource("/view/MainWindow.fxml");
			FXMLLoader fxmlLoader=new FXMLLoader();
			fxmlLoader.setLocation(location);
			fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
			Parent root = fxmlLoader.load();
			
			primaryStage.setTitle("Welcome to City Lodge Main Window");
			primaryStage.setScene(new Scene(root));

			MainController mainController=fxmlLoader.getController();
			mainController.loadTilePane(primaryStage);
			
			primaryStage.show();

		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
