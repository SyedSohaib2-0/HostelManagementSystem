package com.hostel.controller;

import com.hostel.database.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.*;
import java.time.LocalDate;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.fxml.FXMLLoader;

public class AllocationsController {

    @FXML private TextField studentIdField;
    @FXML private TextField roomIdField;
    @FXML private DatePicker allocationDatePicker;
    @FXML private TextField searchStudentIdField;
    @FXML private TextArea allocationDetailsArea;

    private final Connection conn = DatabaseConnection.getConnection();

    public void handleAllocateRoom() {
        String sql = "INSERT INTO allocations (student_id, room_id, allocation_date) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, Integer.parseInt(studentIdField.getText()));
            pstmt.setInt(2, Integer.parseInt(roomIdField.getText()));
            pstmt.setDate(3, Date.valueOf(allocationDatePicker.getValue()));
            pstmt.executeUpdate();
            showAlert(Alert.AlertType.INFORMATION, "Room allocated.");
            clearFields();
        } catch (SQLException | NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error: " + e.getMessage());
        }
    }

    public void handleFetchAllocations() {
        String sql = "SELECT * FROM allocations WHERE student_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, Integer.parseInt(searchStudentIdField.getText()));
            ResultSet rs = pstmt.executeQuery();
            StringBuilder sb = new StringBuilder();
            while (rs.next()) {
                sb.append("Allocation ID: ").append(rs.getInt("allocation_id")).append("\n");
                sb.append("Room ID: ").append(rs.getInt("room_id")).append("\n");
                sb.append("Date: ").append(rs.getDate("allocation_date")).append("\n\n");
            }
            allocationDetailsArea.setText(sb.length() > 0 ? sb.toString() : "No allocations found.");
        } catch (SQLException | NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error: " + e.getMessage());
        }
    }

    private void clearFields() {
        studentIdField.clear();
        roomIdField.clear();
        allocationDatePicker.setValue(null);
        searchStudentIdField.clear();
        allocationDetailsArea.clear();
    }

    private void showAlert(Alert.AlertType type, String msg) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
    @FXML
    public void handleBack(javafx.event.ActionEvent event) {
        try {
            // Adjust path if needed
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/MainDashboard.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Main Dashboard");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not load Main Dashboard.");
            alert.showAndWait();
        }
    }

}
