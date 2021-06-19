package controller;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.RecordOperations;
import model.entity.StandardRoom;
import model.entity.SuiteRoom;
import model.entity.Room;
import model.entity.RentalRecord;
import model.Utilities.DateTime;

public class RoomOperations implements Initializable{

	@FXML Button buttonConfirm;
	@FXML Button buttonCancel;
	@FXML DatePicker datePickerRentDate;
	@FXML DatePicker datePickerActualRenturnDate;
	@FXML DatePicker datePickerEstimatedRenturnDate;
	@FXML TextField textFieldLateFee;
	@FXML TextField textFieldRentFee;
	@FXML TextField textFieldPropertyId;
	@FXML GridPane gridPaneButtons;
	@FXML TextField textFieldRecordId;
	@FXML TextField textFieldCustomerId;
	
	private Stage stage;
	private SingleRoomController singlePropertyController;
	private GridViewController gridViewController;

	private Room rentalProperty;
	private RentalRecord record;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		datePickerRentDate.setValue(LocalDate.now());
		
		final Callback<DatePicker, DateCell> dayCellFactory=new Callback<DatePicker, DateCell>() {

			@Override
			public DateCell call(DatePicker arg0) {
				return new DateCell() {
					@Override
					public void updateItem(LocalDate item,boolean empty) {
						if(datePickerRentDate==null) {
							return;
						}
						if(item.isBefore(datePickerRentDate.getValue().plusDays(1))) {
							setDisable(true);
							setStyle("-fx-background-color:#EEEEEE");
						}
					}
				};
			}
		};
		
		datePickerEstimatedRenturnDate.setDayCellFactory(dayCellFactory);
		datePickerActualRenturnDate.setDayCellFactory(dayCellFactory);
		
		buttonConfirm.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				setButtonConfirmAction();
				stage.close();
				
			}
		});
		
		buttonCancel.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				stage.close();
			}
		});
		datePickerActualRenturnDate.valueProperty().addListener(new ChangeListener<LocalDate>() {

			@Override
			public void changed(ObservableValue<? extends LocalDate> arg0, LocalDate arg1, LocalDate arg2) {
				if(record==null) {
					return;
				}
				record.setActualReturnDateAndFees(new DateTime(arg2.format(DateTimeFormatter.ofPattern("ddMMyyyy"))));
				
				textFieldRentFee.setText(String.format("%.2f", record.getRentalFee()));
				textFieldLateFee.setText(String.format("%.2f", record.getLateFee()));
			}
		});
		textFieldPropertyId.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				setRecordId();
			}
		});
		textFieldCustomerId.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				setRecordId();
			}
		});
		datePickerRentDate.valueProperty().addListener(new ChangeListener<LocalDate>() {

			@Override
			public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue,
					LocalDate newValue) {
				setRecordId();
			}
		});
		
	}

	//update observable while renting
	public void bookRentalProperty(Room rentalProperty) {
		this.rentalProperty=rentalProperty;
		textFieldPropertyId.setText(rentalProperty.getRoomId());
		datePickerActualRenturnDate.setDisable(true);
		textFieldRecordId.setDisable(true);
		textFieldRentFee.setDisable(true);
		textFieldLateFee.setDisable(true);
		
	}

	//update observable while returning
	public void returnRentalProperty(Room rentalProperty) {
		this.rentalProperty=rentalProperty;
		record=RecordOperations.findLatestRecordByProperty(rentalProperty);
		textFieldRecordId.setText(record.getRecordId());
		textFieldRecordId.setDisable(true);
		textFieldPropertyId.setText(record.getHotelRoom().getRoomId());
		textFieldPropertyId.setDisable(true);
		textFieldCustomerId.setText(record.getCustomerId());
		textFieldCustomerId.setDisable(true);
		textFieldRentFee.setEditable(false);
		textFieldLateFee.setEditable(false);
		datePickerRentDate.setValue(LocalDate.parse(record.getRentDate().toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		datePickerRentDate.setDisable(true);
		datePickerEstimatedRenturnDate.setValue(LocalDate.parse(record.getEstimatedReturnDate().toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		datePickerEstimatedRenturnDate.setDisable(true);
		datePickerActualRenturnDate.setValue(LocalDate.now());
	}

	public void setStage(Stage stage) {
		this.stage=stage;
	}
	
	private void setRecordId() {
		if(textFieldPropertyId.getText()==null||datePickerRentDate.getValue()==null||textFieldCustomerId.getText()==null) {
			return;
		}
		textFieldRecordId.setText(rentalProperty.getRoomId()+"_"+textFieldCustomerId.getText()+"_"+new DateTime(datePickerRentDate.getValue().format(DateTimeFormatter.ofPattern("ddMMyyyy"))).getEightDigitDate());
	}
	
	public void setSinglePropertyController(SingleRoomController singlePropertyController) {
		this.singlePropertyController = singlePropertyController;
	}

	public void setGridViewController(GridViewController gridViewController) {
		this.gridViewController = gridViewController;
	}
	
	protected void setButtonConfirmAction() {
		try {
			if(record==null) {
				RentalRecord record=new RentalRecord();
				record.setRecordId(textFieldRecordId.getText());
				record.setHotelRoom(rentalProperty);
				record.setRentDate(new DateTime(datePickerRentDate.getValue().format(DateTimeFormatter.ofPattern("ddMMyyyy"))));
				record.setEstimatedReturnDate(new DateTime(datePickerEstimatedRenturnDate.getValue().format(DateTimeFormatter.ofPattern("ddMMyyyy"))));
				record.setCustomerId(textFieldCustomerId.getText());

				if(rentalProperty.getRoomType().equals("standard_room")) {
					((StandardRoom)rentalProperty).rent(record.getCustomerId(), record.getRentDate(), record.getEstimatedReturnDate());
				}else if(rentalProperty.getRoomType().equals("premium_suite")) {
					((SuiteRoom)rentalProperty).rent(record.getCustomerId(), record.getRentDate(), record.getEstimatedReturnDate());
				}
				
				this.record=record;
				Alert alert=new Alert(AlertType.INFORMATION);
				alert.setContentText("Property has been rented successfully");
				alert.showAndWait();
			}else {
				record.setActualReturnDateAndFees(new DateTime(datePickerActualRenturnDate.getValue().format(DateTimeFormatter.ofPattern("ddMMyyyy"))));
				if(rentalProperty.getRoomType().equals("standard_room")) {
					((StandardRoom)rentalProperty).returnRoom(record.getActualReturnDate());
				}else if(rentalProperty.getRoomType().equals("premium_suite")) {
					((SuiteRoom)rentalProperty).returnRoom(record.getActualReturnDate());
				}
				Alert alert=new Alert(AlertType.INFORMATION);
				alert.setContentText("Property has been returned succesfully");
				alert.showAndWait();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			Alert alert = new Alert(AlertType.WARNING);
			alert.setHeaderText(e.getMessage());
			alert.setContentText(e.toString());
			alert.showAndWait();

		} finally {
			if(gridViewController !=null) {
				gridViewController.refresh();
			}
			if(singlePropertyController !=null) {
				singlePropertyController.refresh();
			}
		}
	}
}
