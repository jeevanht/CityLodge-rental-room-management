package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class AddRoomController implements Initializable {
    @FXML
    ComboBox propertytypebox;
    @FXML
    ComboBox bedrrombox;
    @FXML
    TextField streetnoarea;
    @FXML
    TextField suburbarea;
    @FXML
    TextField streetnamearea;
    @FXML
    TextArea descriptionarea;
    @FXML
    DatePicker datechooser;

    ObservableList<Integer> bedrooms = FXCollections.observableArrayList();
    ObservableList<String> propertyType = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            setViewValues();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setViewValues(){
        propertyType.addAll("standard_room", "SuiteRoom");
        propertytypebox.setItems(propertyType);

        bedrooms.addAll(1, 2);
        bedrrombox.setItems(bedrooms);

        setBedrooms();
    }

    // use listerner to get the various field values
    public void setBedrooms() {
        propertytypebox.getSelectionModel().selectedItemProperty().addListener((e -> {
            String propertyType = propertytypebox.getSelectionModel().getSelectedItem().toString();

            if (propertyType.equalsIgnoreCase("SuiteRoom")) {
                bedrooms.removeAll(1,2,4);
                bedrooms.addAll(6);
                bedrrombox.setItems(bedrooms);
                datechooser.setDisable(false);
            }else{
                bedrooms.clear();
                bedrooms.addAll(1,2,4);
                bedrrombox.setItems(bedrooms);
                bedrrombox.getSelectionModel().selectFirst();
                datechooser.setDisable(true);
            }
        }));
    }

    //check if any fields are empty
    private boolean checkFields(){
        if(streetnoarea.getText().length() ==0 ||
                streetnamearea.getText().length() ==0 ||
                bedrrombox.getSelectionModel().getSelectedItem().toString().length() == 0 ||
                suburbarea.getText().length() ==0 ||
                descriptionarea.getText().length() ==0 ||
                bedrrombox.getSelectionModel().getSelectedItem().toString().length() ==0){

            return false;
        }
        return true;
    }

    public void addRoom(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Property added successfully");

        alert.showAndWait();
    }
}
