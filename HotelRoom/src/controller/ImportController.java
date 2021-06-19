package controller;

import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.StandardRoomOperations;
import model.SuiteRoomOperations;
import model.RoomOperations;
import model.RecordOperations;
import model.Utilities.DateTime;
import model.entity.StandardRoom;
import model.entity.SuiteRoom;
import model.entity.Room;
import model.entity.RentalRecord;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ImportController {

    //get data from file and insert into database
    protected void importData() {
        BufferedReader bufferedReader = null;
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(new File("./"));

            FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
            fileChooser.getExtensionFilters().add(extensionFilter);
            Stage s = new Stage();
            File file = fileChooser.showOpenDialog(s);

            if (file == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("NO FILE SELECTED");
                return;
            }

            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            List<StandardRoom> standard_rooms = new ArrayList<>();
            List<SuiteRoom> premiumSuites = new ArrayList<>();
            List<Room> rentalProperties = new ArrayList<>();
            List<RentalRecord> rentalRecords = new ArrayList<>();
            String temp = null;

            if ((temp = bufferedReader.readLine()) != null && temp.equals("#rental_property#")) {
                while ((temp = bufferedReader.readLine()) != null && !temp.equals("#rental_record#")) {
                    String[] properties = temp.split("::");
                    Room rentalProperty = new Room();
                    rentalProperty.setRoomId(properties[0]);
                    rentalProperty.setRoomFloor(properties[1]);
                    rentalProperty.setRoomNumber(properties[2]);
                    rentalProperty.setNumberOfBeds(properties[3]);
                    rentalProperty.setBeds(Integer.valueOf(properties[4]));
                    rentalProperty.setRoomType(properties[5]);
                    rentalProperty.setStatus(properties[6]);
                    rentalProperty.setImageName(properties[7]);
                    rentalProperty.setDescription(properties[8]);
                    rentalProperty.setCreateTime(properties[9]);

                    rentalProperties.add(rentalProperty);
                }
            }

            if ((temp = bufferedReader.readLine()) != null && temp.equals("#rental_record#")) {
                while ((temp = bufferedReader.readLine()) != null && !temp.equals("#rental_record#")) {
                    String[] properties = temp.split("::");
                    RentalRecord rentalRecord = new RentalRecord();

                    rentalRecord.setRecordId(properties[0]);
                    Room rentalProperty = new Room();
                    rentalProperty.setRoomId(properties[1]);
                    rentalRecord.setHotelRoom(rentalProperty);
                    rentalRecord.setCustomerId(properties[2]);
                    rentalRecord.setRentDate(new DateTime(properties[3]));
                    rentalRecord.setEstimatedReturnDate(new DateTime(properties[4]));
                    rentalRecord.setActualReturnDate(properties[5].equals("none") ? null : new DateTime(properties[5]));
                    rentalRecord.setRentalFee(Double.valueOf(properties[6]));
                    rentalRecord.setLateFee(Double.valueOf(properties[7]));

                    rentalRecords.add(rentalRecord);
                }
            }

         //   StandardRoomOperations.saveAll(standard_rooms);
          //  SuiteRoomOperations.saveToDb(premiumSuites);
            RoomOperations.saveAll(rentalProperties);
            RecordOperations.saveAll(rentalRecords);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Imported Data, Please restart to view the updated dataset");

            alert.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(e.toString());
            alert.setContentText(e.getMessage());

            alert.showAndWait();
        }
    }
}
