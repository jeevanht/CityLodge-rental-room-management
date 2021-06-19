package controller;

import javafx.scene.control.Alert;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.StandardRoomOperations;
import model.SuiteRoomOperations;
import model.RoomOperations;
import model.RecordOperations;
import model.entity.StandardRoom;
import model.entity.SuiteRoom;
import model.entity.Room;
import model.entity.RentalRecord;

import java.io.*;
import java.util.List;

public class ExportController {

    //export data from db to text files
    protected void exportData() {
        try {
            DirectoryChooser chooser = new DirectoryChooser();
            chooser.setTitle("Choose location To Save Report");
            File chosenFile = null;

            chosenFile = chooser.showDialog(null);

            File file = new File(chosenFile + "export_data.txt");

            BufferedOutputStream outputStream = null;
            List<Room> rentalProperties = RoomOperations.exportData();
            List<RentalRecord> rentalRecords = RecordOperations.getAllRecord();
            outputStream = new BufferedOutputStream(new FileOutputStream(file));

            outputStream.write("#rental_property#\n".getBytes("UTF-8"));
            for (Room rentalProperty : rentalProperties) {
                String temp = rentalProperty.getRoomId() + "::" + rentalProperty.getRoomFloor() + "::"
                        + rentalProperty.getRoomNumber() + "::" + rentalProperty.getNumberOfBeds() + "::"
                        + rentalProperty.getBeds() + "::" + rentalProperty.getRoomType() + "::"
                        + rentalProperty.getStatus() + "::" + rentalProperty.getImageName() + "::"
                        + rentalProperty.getDescription() + "::" + rentalProperty.getCreateTime() + "\n";
                outputStream.write(temp.getBytes("UTF-8"));
            }

            outputStream.write("#rental_record#\n".getBytes("UTF-8"));
            for (RentalRecord rentalRecord : rentalRecords) {
                String temp = rentalRecord.getRecordId() + "::";
                temp = temp + rentalRecord.getHotelRoom().getRoomId() + "::";
                temp = temp + rentalRecord.getCustomerId() + "::" + rentalRecord.getRentDate().toString() + "::";
                temp = temp + rentalRecord.getEstimatedReturnDate().toString() + "::";
                temp = temp + (rentalRecord.getActualReturnDate() == null ? "none"
                        : rentalRecord.getActualReturnDate().toString()) + "::";
                temp = temp + rentalRecord.getRentalFee() + "::" + rentalRecord.getLateFee() + "\n";
                outputStream.write(temp.getBytes("UTF-8"));
            }
            outputStream.write("#rental_record#\n".getBytes("UTF-8"));

            outputStream.flush();
            outputStream.close();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("File has been exported successfully");

            alert.showAndWait();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(e.toString());
            alert.setContentText(e.getMessage());

            alert.showAndWait();
        }
    }
}
