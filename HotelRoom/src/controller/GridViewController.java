package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.entity.Room;

public class GridViewController implements Initializable{
	
	@FXML TextArea textAreaDescription;
	@FXML Label labelStreetName;
	@FXML Button buttonInfo;
	@FXML ImageView imageViewMain;
	@FXML GridPane gridPaneButtons;
	
	private Room rentalProperty;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		buttonInfo.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				setButtonInfoAction();
				
			}
		});
	}
	
	protected void setButtonInfoAction() {
		URL location=getClass().getResource("/view/SingleRoomView.fxml");
		FXMLLoader fxmlLoader=new FXMLLoader();
		fxmlLoader.setLocation(location);
		fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
		try {
			VBox vbox = fxmlLoader.load();
			SingleRoomController singlePropertyController =fxmlLoader.getController();
			singlePropertyController.initial(rentalProperty);

			Stage stage=new Stage();
			stage.setTitle("Detail");
			stage.setScene(new Scene(vbox));
			stage.setResizable(false);
			stage.show();

			singlePropertyController.setGridViewController(this);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void setButtonRentAction() {
		URL location=getClass().getResource("/view/RoomOperationsWindow.fxml");
		FXMLLoader fxmlLoader=new FXMLLoader();
		fxmlLoader.setLocation(location);
		fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
		try {
			VBox vbox = fxmlLoader.load();
			RoomOperations propertyOperations =fxmlLoader.getController();
			propertyOperations.bookRentalProperty(rentalProperty);
			Stage stage=new Stage();
			propertyOperations.setStage(stage);
			propertyOperations.setGridViewController(this);
			stage.setTitle("Book");
			stage.setScene(new Scene(vbox));
			stage.setResizable(false);
			stage.show();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void initial(Room rentalProperty) {
		if(rentalProperty==null) {
			return;
		}
		this.rentalProperty=rentalProperty;
		
		labelStreetName.setText(rentalProperty.getRoomNumber());
		textAreaDescription.setText(rentalProperty.getRoomFloor()+" "+rentalProperty.getRoomNumber()+" "+rentalProperty.getNumberOfBeds()+"\n\n"+rentalProperty.getDescription());
		imageViewMain.setImage(new Image("file://../images/"+rentalProperty.getImageName()));
		setButtonIsDisable(rentalProperty);
	}

	private void setButtonIsDisable(Room rentalProperty) {
		if(rentalProperty.getStatus().equals("currently available for rent")) {
		}else {
		}
	}

	public void refresh() {
	}

}
