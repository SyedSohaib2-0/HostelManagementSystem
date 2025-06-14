package com.hostel.controller;

import com.hostel.database.DatabaseConnection;
import com.hostel.model.Staff;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.sql.*;

public class StaffController {

    @FXML private TextField nameField;
    @FXML private TextField roleField;
    @FXML private TextField phoneField;
    @FXML private TextField emailField;
    @FXML private TableView<Staff> staffTable;
    @FXML private TableColumn<Staff, String> nameColumn;
    @FXML private TableColumn<Staff, String> roleColumn;
    @FXML private TableColumn<Staff, String> phoneColumn;
    @FXML private TableColumn<Staff, String> emailColumn;
    @FXML private Button addButton;

    private ObservableList<Staff> staffList = FXCollections.observableArrayList();
    private Staff selectedStaff = null;

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getName()));
        roleColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getRole()));
        phoneColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getPhone()));
        emailColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getEmail()));

        loadStaffData();

        staffTable.setOnMouseClicked(this::handleRowClick);
    }

    private void loadStaffData() {
        staffList.clear();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM staff");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Staff staff = new Staff(
                        rs.getInt("staff_id"),
                        rs.getString("name"),
                        rs.getString("role"),
                        rs.getString("phone"),
                        rs.getString("email")
                );
                staffList.add(staff);
            }

            staffTable.setItems(staffList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleAddStaff() {
        if (selectedStaff == null) {
            addStaff();
        } else {
            updateStaff();
        }
    }

    private void addStaff() {
        String insertQuery = "INSERT INTO staff (name, role, phone, email) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(insertQuery)) {

            stmt.setString(1, nameField.getText());
            stmt.setString(2, roleField.getText());
            stmt.setString(3, phoneField.getText());
            stmt.setString(4, emailField.getText());
            stmt.executeUpdate();

            loadStaffData();
            clearFields();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateStaff() {
        String updateQuery = "UPDATE staff SET name = ?, role = ?, phone = ?, email = ? WHERE staff_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(updateQuery)) {

            stmt.setString(1, nameField.getText());
            stmt.setString(2, roleField.getText());
            stmt.setString(3, phoneField.getText());
            stmt.setString(4, emailField.getText());
            stmt.setInt(5, selectedStaff.getStaffId());
            stmt.executeUpdate();

            loadStaffData();
            clearFields();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleDeleteStaff() {
        if (selectedStaff != null) {
            String deleteQuery = "DELETE FROM staff WHERE staff_id = ?";

            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(deleteQuery)) {

                stmt.setInt(1, selectedStaff.getStaffId());
                stmt.executeUpdate();

                loadStaffData();
                clearFields();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleRowClick(MouseEvent event) {
        selectedStaff = staffTable.getSelectionModel().getSelectedItem();
        if (selectedStaff != null) {
            nameField.setText(selectedStaff.getName());
            roleField.setText(selectedStaff.getRole());
            phoneField.setText(selectedStaff.getPhone());
            emailField.setText(selectedStaff.getEmail());
            addButton.setText("Update Staff");
        }
    }

    private void clearFields() {
        nameField.clear();
        roleField.clear();
        phoneField.clear();
        emailField.clear();
        selectedStaff = null;
        addButton.setText("Add Staff");
    }

    @FXML
    public void handleBack(javafx.event.ActionEvent event) {
        try {
            // Correct path assuming src/main/resources/fxml/MainDashboard.fxml
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
