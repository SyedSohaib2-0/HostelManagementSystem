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

public class MaintenanceController {

    @FXML private TextField roomIdField;
    @FXML private TextArea issueDescriptionField;
    @FXML private DatePicker dateReportedPicker;
    @FXML private TextField searchRoomIdField;
    @FXML private TextArea reportArea;

    private final Connection conn = DatabaseConnection.getConnection();

    public void handleReportIssue() {
        String sql = "INSERT INTO maintenance (room_id, issue_description, date_reported) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, Integer.parseInt(roomIdField.getText()));
            pstmt.setString(2, issueDescriptionField.getText());
            pstmt.setDate(3, Date.valueOf(dateReportedPicker.getValue()));
            pstmt.executeUpdate();
            showAlert(Alert.AlertType.INFORMATION, "Issue reported successfully.");
            clearFields();
        } catch (SQLException | NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error: " + e.getMessage());
        }
    }

    public void handleFetchReports() {
        String sql = "SELECT * FROM maintenance WHERE room_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, Integer.parseInt(searchRoomIdField.getText()));
            ResultSet rs = pstmt.executeQuery();
            StringBuilder sb = new StringBuilder();
            while (rs.next()) {
                sb.append("Maintenance ID: ").append(rs.getInt("maintenance_id")).append("\n");
                sb.append("Issue: ").append(rs.getString("issue_description")).append("\n");
                sb.append("Date Reported: ").append(rs.getDate("date_reported")).append("\n\n");
            }
            reportArea.setText(sb.length() > 0 ? sb.toString() : "No issues found.");
        } catch (SQLException | NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error: " + e.getMessage());
        }
    }

    private void clearFields() {
        roomIdField.clear();
        issueDescriptionField.clear();
        dateReportedPicker.setValue(null);
        searchRoomIdField.clear();
        reportArea.clear();
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
