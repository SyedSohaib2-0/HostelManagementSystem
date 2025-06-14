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
import java.time.LocalDate;

public class PaymentsController {

    @FXML private TextField studentIdField;
    @FXML private TextField amountField;
    @FXML private DatePicker paymentDatePicker;
    @FXML private TextField searchStudentIdField;
    @FXML private TextArea paymentDetailsArea;

    private final Connection conn = DatabaseConnection.getConnection();

    public void handleRecordPayment() {
        String sql = "INSERT INTO payments (student_id, amount, payment_date) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, Integer.parseInt(studentIdField.getText()));
            pstmt.setBigDecimal(2, new java.math.BigDecimal(amountField.getText()));
            pstmt.setDate(3, Date.valueOf(paymentDatePicker.getValue()));
            pstmt.executeUpdate();
            showAlert(Alert.AlertType.INFORMATION, "Payment recorded.");
            clearFields();
        } catch (SQLException | NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error: " + e.getMessage());
        }
    }

    public void handleFetchPayments() {
        String sql = "SELECT * FROM payments WHERE student_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, Integer.parseInt(searchStudentIdField.getText()));
            ResultSet rs = pstmt.executeQuery();
            StringBuilder sb = new StringBuilder();
            while (rs.next()) {
                sb.append("Payment ID: ").append(rs.getInt("payment_id")).append("\n");
                sb.append("Amount: ").append(rs.getBigDecimal("amount")).append("\n");
                sb.append("Date: ").append(rs.getDate("payment_date")).append("\n\n");
            }
            paymentDetailsArea.setText(sb.length() > 0 ? sb.toString() : "No payments found.");
        } catch (SQLException | NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error: " + e.getMessage());
        }
    }

    private void clearFields() {
        studentIdField.clear();
        amountField.clear();
        paymentDatePicker.setValue(null);
        searchStudentIdField.clear();
        paymentDetailsArea.clear();
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
