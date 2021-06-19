package controller;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import model.RoomOperations;
import model.entity.Room;

public class MainController implements Initializable {

	//declare all fxml variabes
	@FXML
	ScrollPane scrollPanePropertyList;
	@FXML
	TilePane tilePanePropertyItemList;
	@FXML
	MenuItem menuItemExport;
	@FXML
	MenuItem menuItemImport;
	@FXML
	MenuItem menuItemQuit;
	@FXML
	RadioButton radioButtonAll;
	@FXML
	RadioButton radioButtonApartment;
	@FXML
	RadioButton radioButtonPremiumSuite;
	@FXML
	ChoiceBox<String> choiceBoxBedroomNumber;
	@FXML
	ChoiceBox<String> choiceBoxStatus;
	

	final ToggleGroup group = new ToggleGroup();
	Stage stage;
	List<Room> rentalProperties;


	//set all the filtering options , buttons , combobox values and their respective binding handlers
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		rentalProperties = RoomOperations.getAllHotelRooms();

		radioButtonAll.setUserData("(1=1)");
		radioButtonAll.setToggleGroup(group);
		radioButtonApartment.setUserData("(room_type='standard_room')");
		radioButtonApartment.setToggleGroup(group);
		radioButtonPremiumSuite.setUserData("(room_type='premium_suite')");
		radioButtonPremiumSuite.setToggleGroup(group);
		
		
		group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			@Override
			public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
				if (group.getSelectedToggle() != null) {
					String bedroomsNumberCondition = "(beds='" + choiceBoxBedroomNumber.getValue() + "')";
					if (choiceBoxBedroomNumber.getValue().equals("All")) {
						bedroomsNumberCondition = "(1=1)";
					}
					String statusCondition = "(room_status='" + choiceBoxStatus.getValue() + "')";
					if (choiceBoxStatus.getValue().equals("All")) {
						statusCondition = "(1=1)";
					}

					String propertyTypeCondition = (String) group.getSelectedToggle().getUserData();


					rentalProperties = RoomOperations
							.filterRooms(propertyTypeCondition + " and " + bedroomsNumberCondition
									+ " and " + statusCondition );
					loadTilePane(stage);
					System.out.println(propertyTypeCondition + " and " + bedroomsNumberCondition + " and "
							+ statusCondition );
				}
			}
		});

		choiceBoxBedroomNumber.setItems(FXCollections.observableArrayList("All", "1", "2", "4", "6"));
		choiceBoxBedroomNumber.setValue("All");
		handleBedroombindings();

		choiceBoxStatus.setItems(FXCollections.observableArrayList("All", "currently available for rent",
				"being rented", "under maintenance"));
		choiceBoxStatus.setValue("All");
		handleComboBoxBindings();


		//click handlers
		menuItemImport.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				ImportController imp = new ImportController();
				imp.importData();
			}
		});
		menuItemExport.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				ExportController ecp = new ExportController();
				ecp.exportData();
			}
		});
		menuItemQuit.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				stage.close();
			}
		});
	}


	//load all the frid vew data
	public void loadTilePane(Stage stage) {
		this.stage=stage;
		tilePanePropertyItemList.getChildren().clear();
		if (rentalProperties == null) {
			return;
		}
		for (int i = 0; i < rentalProperties.size(); i++) {
			Room rentalProperty = rentalProperties.get(i);
			URL location = getClass().getResource("/view/RoomGridView.fxml");
			FXMLLoader fxmlLoader = new FXMLLoader();
			fxmlLoader.setLocation(location);
			fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
			try {
				GridPane gridPaneRentalPropertyItem = fxmlLoader.load();
				GridViewController gridViewController = fxmlLoader.getController();
				gridViewController.initial(rentalProperty);
				tilePanePropertyItemList.getChildren().add(gridPaneRentalPropertyItem);
				TilePane.setMargin(gridPaneRentalPropertyItem, new Insets(10));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	private void handleBedroombindings(){
		choiceBoxBedroomNumber.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			//binding handler for bedroom number values
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				String propertyTypeCondition = group.getSelectedToggle().getUserData().toString();

				String bedroomsNumberCondition = newValue.equals("All") ? "(1=1)"
						: "(beds='" + newValue + "')";

				String statusCondition = "(room_status='" + choiceBoxStatus.getValue() + "')";
				if (choiceBoxStatus.getValue().equals("All")) {
					statusCondition = "(1=1)";
				}

				rentalProperties = RoomOperations.filterRooms(propertyTypeCondition + " and "
						+ bedroomsNumberCondition + " and " + statusCondition );
				loadTilePane(stage);
			}
		});
	}

	private void handleComboBoxBindings(){
		choiceBoxStatus.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				String bedroomsNumberCondition = "(beds='" + choiceBoxBedroomNumber.getValue() + "')";
				if (choiceBoxBedroomNumber.getValue().equals("All")) {
					bedroomsNumberCondition = "(1=1)";
				}

				String propertyTypeCondition = group.getSelectedToggle().getUserData().toString();

				String statusCondition = "(room_status='" + newValue + "')";
				if (newValue.equals("All")) {
					statusCondition = "(1=1)";
				}

				rentalProperties = RoomOperations.filterRooms(propertyTypeCondition + " and "
						+ bedroomsNumberCondition + " and " + statusCondition );
				loadTilePane(stage);
			}
		});
	}



	public void addRoom(ActionEvent event) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/AddRoom.fxml"));
		Parent root1 =  fxmlLoader.load();
		Stage stage = new Stage();
		stage.setScene(new Scene(root1));
		stage.setTitle("Add Room");
		stage.show();
	}
}
