/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controller.Admin;

import Model.Category;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import ConnectDB.Connect;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class EditCategoryController implements Initializable {
    @FXML
    private TextField categoryNameField;
    @FXML
    private Button saveButton;
    
    private Category categoryToEdit;

    public void setCategoryToEdit(Category category) {
        categoryToEdit = category;
        
        categoryNameField.setText(categoryToEdit.getCategoryName());
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        saveButton.setOnAction(event -> {
            String editedCategoryName = categoryNameField.getText();
            
            boolean updated = updateCategory(categoryToEdit.getCategory_id(), editedCategoryName);
            if (updated) {
                Stage currentStage = (Stage) saveButton.getScene().getWindow();
                currentStage.close();

                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Admin/Category.fxml"));
                    Parent root = loader.load();
                    Scene scene = new Scene(root);
                    Stage newStage = new Stage();
                    newStage.setScene(scene);
                    newStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                // Xử lý lỗi khi cập nhật danh mục
            }
        });
    }
    
    private boolean updateCategory(int categoryID, String editedCategoryName) {
        try {
            Connection conn = Connect.connect();
            String updateQuery = "UPDATE category SET category_name = ? WHERE category_id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(updateQuery);
            preparedStatement.setString(1, editedCategoryName);
            preparedStatement.setInt(2, categoryID);
            
            int rowsUpdated = preparedStatement.executeUpdate();
            
            conn.close();
            
            return rowsUpdated > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
