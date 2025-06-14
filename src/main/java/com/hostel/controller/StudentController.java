package com.hostel.controller;

import com.hostel.database.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentController {

    @FXML private TextField regNoField;
    @FXML private TextField nameField;
    @FXML private ChoiceBox<String> genderChoice;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TextField roomIdField;      // New Room ID field
    @FXML private TextField courseField;
    @FXML private DatePicker joinDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private TextField searchRegNoField;
    @FXML private TextArea studentDetailsArea;

    private final Connection conn = DatabaseConnection.getConnection();

    @FXML
    public void initialize() {
        genderChoice.getItems().addAll("Male", "Female", "Other");
    }

    public void handleAddStudent() {
        // Validate Room ID exists
        String roomIdText = roomIdField.getText();
        int roomId;
        try {
            roomId = Integer.parseInt(roomIdText);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Room ID must be a number.");
            return;
        }

        String checkRoomSql = "SELECT COUNT(*) FROM rooms WHERE room_id = ?";
        try (PreparedStatement checkStmt = conn.prepareStatement(checkRoomSql)) {
            checkStmt.setInt(1, roomId);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) == 0) {
                showAlert(Alert.AlertType.ERROR, "Room ID does not exist. Cannot add student.");
                return;
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error checking Room ID: " + e.getMessage());
            return;
        }

        String sql = "INSERT INTO students (reg_no, name, gender, email, phone, room_id, course, join_date, end_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, regNoField.getText());
            pstmt.setString(2, nameField.getText());
            pstmt.setString(3, genderChoice.getValue());
            pstmt.setString(4, emailField.getText());
            pstmt.setString(5, phoneField.getText());
            pstmt.setInt(6, roomId);
            pstmt.setString(7, courseField.getText());
            pstmt.setDate(8, java.sql.Date.valueOf(joinDatePicker.getValue()));
            pstmt.setDate(9, java.sql.Date.valueOf(endDatePicker.getValue()));
            pstmt.executeUpdate();
            showAlert(Alert.AlertType.INFORMATION, "Student added successfully.");
            clearFields();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Failed to add student: " + e.getMessage());
        }
    }

    public void handleUpdateStudent() {
        String roomIdText = roomIdField.getText();
        int roomId;
        try {
            roomId = Integer.parseInt(roomIdText);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Room ID must be a number.");
            return;
        }

        String checkRoomSql = "SELECT COUNT(*) FROM rooms WHERE room_id = ?";
        try (PreparedStatement checkStmt = conn.prepareStatement(checkRoomSql)) {
            checkStmt.setInt(1, roomId);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) == 0) {
                showAlert(Alert.AlertType.ERROR, "Room ID does not exist. Cannot update student.");
                return;
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error checking Room ID: " + e.getMessage());
            return;
        }

        String sql = "UPDATE students SET name=?, gender=?, email=?, phone=?, room_id=?, course=?, join_date=?, end_date=? WHERE reg_no=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nameField.getText());
            pstmt.setString(2, genderChoice.getValue());
            pstmt.setString(3, emailField.getText());
            pstmt.setString(4, phoneField.getText());
            pstmt.setInt(5, roomId);
            pstmt.setString(6, courseField.getText());
            pstmt.setDate(7, java.sql.Date.valueOf(joinDatePicker.getValue()));
            pstmt.setDate(8, java.sql.Date.valueOf(endDatePicker.getValue()));
            pstmt.setString(9, regNoField.getText());
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Student updated successfully.");
            } else {
                showAlert(Alert.AlertType.WARNING, "Student not found.");
            }
            clearFields();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Failed to update student: " + e.getMessage());
        }
    }

    public void handleDeleteStudent() {
        String sql = "DELETE FROM students WHERE reg_no=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, regNoField.getText());
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Student deleted.");
            } else {
                showAlert(Alert.AlertType.WARNING, "Student not found.");
            }
            clearFields();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Failed to delete student: " + e.getMessage());
        }
    }

    public void handleFetchStudent() {
        String sql = "SELECT * FROM students WHERE reg_no=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, searchRegNoField.getText());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                StringBuilder sb = new StringBuilder();
                sb.append("Reg No: ").append(rs.getString("reg_no")).append("\n")
                  .append("Name: ").append(rs.getString("name")).append("\n")
                  .append("Gender: ").append(rs.getString("gender")).append("\n")
                  .append("Email: ").append(rs.getString("email")).append("\n")
                  .append("Phone: ").append(rs.getString("phone")).append("\n")
                  .append("Room ID: ").append(rs.getInt("room_id")).append("\n")
                  .append("Course: ").append(rs.getString("course")).append("\n")
                  .append("Join Date: ").append(rs.getDate("join_date")).append("\n")
                  .append("End Date: ").append(rs.getDate("end_date"));
                studentDetailsArea.setText(sb.toString());
            } else {
                studentDetailsArea.setText("Student not found.");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Failed to fetch student: " + e.getMessage());
        }
    }

    @FXML
    public void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainDashboard.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) regNoField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Main Dashboard");
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Failed to load dashboard: " + e.getMessage());
        }
    }

    private void clearFields() {
        regNoField.clear();
        nameField.clear();
        genderChoice.setValue(null);
        emailField.clear();
        phoneField.clear();
        roomIdField.clear(); // clear the new Room ID field
        courseField.clear();
        joinDatePicker.setValue(null);
        endDatePicker.setValue(null);
        searchRegNoField.clear();
        studentDetailsArea.clear();
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
