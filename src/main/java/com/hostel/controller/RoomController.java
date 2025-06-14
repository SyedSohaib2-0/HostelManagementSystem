package com.hostel.controller;

import com.hostel.database.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.fxml.FXMLLoader;

import java.sql.*;

public class RoomController {

    @FXML private TextField roomNumberField;
    @FXML private TextField capacityField;
    @FXML private TextField searchRoomNumberField;
    @FXML private TextArea roomDetailsArea;
    @FXML private ChoiceBox<String> availabilityChoiceBox;

    private final Connection conn = DatabaseConnection.getConnection();

    @FXML
    public void initialize() {
        availabilityChoiceBox.getItems().addAll("Available", "Occupied");
        availabilityChoiceBox.setValue("Available");
    }

    public void handleAddRoom() {
        String sql = "INSERT INTO rooms (room_number, capacity, availability) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, roomNumberField.getText());
            pstmt.setInt(2, Integer.parseInt(capacityField.getText()));
            pstmt.setString(3, availabilityChoiceBox.getValue());
            pstmt.executeUpdate();
            showAlert(Alert.AlertType.INFORMATION, "Room added successfully.");
            clearFields();
        } catch (SQLException | NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error adding room: " + e.getMessage());
        }
    }

    public void handleUpdateRoom() {
        String sql = "UPDATE rooms SET capacity=?, availability=? WHERE room_number=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, Integer.parseInt(capacityField.getText()));
            pstmt.setString(2, availabilityChoiceBox.getValue());
            pstmt.setString(3, roomNumberField.getText());
            int updated = pstmt.executeUpdate();
            if (updated > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Room updated.");
            } else {
                showAlert(Alert.AlertType.WARNING, "Room not found.");
            }
            clearFields();
        } catch (SQLException | NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error updating room: " + e.getMessage());
        }
    }

    public void handleDeleteRoom() {
        String sql = "DELETE FROM rooms WHERE room_number=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, roomNumberField.getText());
            int deleted = pstmt.executeUpdate();
            if (deleted > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Room deleted.");
            } else {
                showAlert(Alert.AlertType.WARNING, "Room not found.");
            }
            clearFields();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error deleting room: " + e.getMessage());
        }
    }

    public void handleFetchRoom() {
        String sql = "SELECT * FROM rooms WHERE room_number=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, searchRoomNumberField.getText());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                StringBuilder sb = new StringBuilder();
                sb.append("Room Number: ").append(rs.getString("room_number")).append("\n");
                sb.append("Capacity: ").append(rs.getInt("capacity")).append("\n");
                sb.append("Availability: ").append(rs.getString("availability")).append("\n");
                roomDetailsArea.setText(sb.toString());
                // Also populate the fields for editing
                roomNumberField.setText(rs.getString("room_number"));
                capacityField.setText(String.valueOf(rs.getInt("capacity")));
                availabilityChoiceBox.setValue(rs.getString("availability"));
            } else {
                roomDetailsArea.setText("No room found.");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error fetching room: " + e.getMessage());
        }
    }

    private void clearFields() {
        roomNumberField.clear();
        capacityField.clear();
        searchRoomNumberField.clear();
        roomDetailsArea.clear();
        availabilityChoiceBox.setValue("Available");
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
