package com.hostel.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.stage.Stage;

import java.io.IOException;

public class MainDashboardController {

    private void loadModule(String fxmlFile, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openStudentModule(ActionEvent event) {
        loadModule("StudentManagement.fxml", event);
    }

    public void openStaffModule(ActionEvent event) {
        loadModule("StaffManagement.fxml", event);
    }

    public void openRoomModule(ActionEvent event) {
        loadModule("RoomManagement.fxml", event);
    }

    public void openAllocationModule(ActionEvent event) {
        loadModule("AllocationManagement.fxml", event);
    }

    public void openPaymentModule(ActionEvent event) {
        loadModule("PaymentManagement.fxml", event);
    }

    public void openMaintenanceModule(ActionEvent event) {
        loadModule("MaintenanceManagement.fxml", event);
    }
}
