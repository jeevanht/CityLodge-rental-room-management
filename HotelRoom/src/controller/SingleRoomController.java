package controller;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.RecordOperations;
import model.entity.SuiteRoom;
import model.entity.Room;
import model.entity.RentalRecord;
import model.Exceptions.CompleteMaintenanceException;
import model.Exceptions.PerformMaintenanceException;
import model.Utilities.DateTime;

public class SingleRoomController implements Initializable{

	@FXML GridPane gridPaneButtons;
	@FXML Label labelStreetName;
	@FXML Button buttonRent;
	@FXML TextArea textAreaDescription;
	@FXML ImageView imageViewMain;
	@FXML TableView<RentalRecord> tableViewRecords;
	@FXML TableColumn<RentalRecord,String> tableColumnId;
	@FXML TableColumn<RentalRecord,String> tableColumnCustomer;
	@FXML TableColumn<RentalRecord,String> tableColumnRentDate;
	@FXML TableColumn<RentalRecord,String> tableColumnEstimatedReturnDate;
	@FXML TableColumn<RentalRecord,String> tableColumnActualReturnDate;
	@FXML TableColumn<RentalRecord,Double> tableColumnRentalFee;
	@FXML TableColumn<RentalRecord,Double> tableColumnLateFee;
	@FXML Button buttonReturn;
	@FXML Button buttonMaintenance;
	@FXML Button buttonComplete;
	
	GridViewController gridViewController;

	Room rentalProperty;
	List<RentalRecord> rentalRecords;
	ObservableList<RentalRecord> observableListRecords;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		buttonRent.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				setButtonRentAction();
			}
		});
		
		buttonReturn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				setButtonReturnAction();
			}
		});
		
		buttonMaintenance.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				setButtonMaintenanceAction(rentalProperty);
				refresh();
			}
		});
		
		buttonComplete.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				setButtonCompleteAction(rentalProperty);
				refresh();
			}
		});
	}

	public void initial(Room rentalProperty) {
		this.rentalProperty=rentalProperty;
		this.rentalRecords=RecordOperations.getAllRecordByProperty(rentalProperty);

		labelStreetName.setText(rentalProperty.getRoomNumber());
		textAreaDescription.setText(rentalProperty.getRoomFloor()+" "+rentalProperty.getRoomNumber()+" "+rentalProperty.getNumberOfBeds()+"\n\n"+rentalProperty.getDescription());
		imageViewMain.setImage(new Image("file://../images/"+rentalProperty.getImageName()));
		observableListRecords=FXCollections.observableArrayList(rentalRecords);

		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("recordId"));
		tableColumnCustomer.setCellValueFactory(new PropertyValueFactory<>("customerId"));
		tableColumnRentDate.setCellValueFactory(new PropertyValueFactory<>("rentDate"));
		tableColumnEstimatedReturnDate.setCellValueFactory(new PropertyValueFactory<>("estimatedReturnDate"));
		tableColumnActualReturnDate.setCellValueFactory(new PropertyValueFactory<>("actualReturnDate"));
		tableColumnRentalFee.setCellValueFactory(new PropertyValueFactory<>("rentalFee"));
		tableColumnLateFee.setCellValueFactory(new PropertyValueFactory<>("lateFee"));

		tableViewRecords.setItems(observableListRecords);
		
		setButtonIsDisable(rentalProperty);
	}
	
	private void setButtonRentAction() {
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
			stage.setTitle("Book");
			stage.setScene(new Scene(vbox));
			stage.setResizable(false);
			stage.show();
			propertyOperations.setSinglePropertyController(this);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void setButtonReturnAction() {
		URL location=getClass().getResource("/view/RoomOperationsWindow.fxml");
		FXMLLoader fxmlLoader=new FXMLLoader();
		fxmlLoader.setLocation(location);
		fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
		try {
			VBox vbox = fxmlLoader.load();
			RoomOperations propertyOperations =fxmlLoader.getController();
			propertyOperations.returnRentalProperty(rentalProperty);
			propertyOperations.setSinglePropertyController(this);
			Stage stage=new Stage();
			propertyOperations.setStage(stage);
			stage.setTitle("Return");
			stage.setScene(new Scene(vbox));
			stage.setResizable(false);
			stage.show();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected void setButtonCompleteAction(Room rentalProperty) {
		try {
			((SuiteRoom)rentalProperty).completeMaintenance(new DateTime(new DateTime().getTime()));
			Alert alert=new Alert(AlertType.INFORMATION);
			alert.setContentText("Property has completed Maintenance succesfully");
			alert.showAndWait();
			
		} catch (CompleteMaintenanceException|PerformMaintenanceException e) {
			e.printStackTrace();
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Warning");
			alert.setHeaderText(e.getMessage());
			alert.setContentText(e.toString());
			alert.showAndWait();
		}finally {
			gridViewController.refresh();
		}
	}
	
	protected void setButtonMaintenanceAction(Room rentalProperty2) {
		try {
			((SuiteRoom)rentalProperty).performMaintenance();
			Alert alert=new Alert(AlertType.INFORMATION);
			alert.setContentText("Maintenance has been performed succesfully.");
			alert.showAndWait();	
		} catch (PerformMaintenanceException e) {
			e.printStackTrace();
			Alert alert = new Alert(AlertType.WARNING);
			alert.setHeaderText(e.getMessage());
			alert.setContentText(e.toString());
			alert.showAndWait();
		}finally {
			gridViewController.refresh();
		}
	}
	
	
	public void setGridViewController(GridViewController gridViewController) {
		this.gridViewController = gridViewController;
	}

	//refresh the grid view based on the operations
	public void refresh() {
		this.rentalRecords=RecordOperations.getAllRecordByProperty(rentalProperty);
		observableListRecords.clear();
		observableListRecords.addAll(rentalRecords);
		setButtonIsDisable(rentalProperty);
		gridViewController.refresh();
	}

	//handle button disable conditions for various scenarios
	private void setButtonIsDisable(Room rentalProperty) {
		if(rentalProperty.getStatus().equals("currently available for rent")) {
			buttonRent.setDisable(false);
		}else {
			buttonRent.setDisable(true);
		}
		if(rentalProperty.getStatus().equals("being rented")) {
			buttonReturn.setDisable(false);
		}else {
			buttonReturn.setDisable(true);
		}
		
		if(rentalProperty.getRoomType().equals("premium_suite")) {
            switch (rentalProperty.getStatus()) {
				case "under maintenance":
                    buttonMaintenance.setDisable(true);
                    buttonComplete.setDisable(false);
                    break;
                case "being rented":
                    buttonMaintenance.setDisable(true);
                    buttonComplete.setDisable(true);
                    break;
                case "currently available for rent":
                    buttonMaintenance.setDisable(false);
                    buttonComplete.setDisable(true);
                    break;
            }
		}else {
			buttonMaintenance.setVisible(false);
			buttonComplete.setVisible(false);
		}
	}
}
