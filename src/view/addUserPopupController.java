package view;

import app.Photos;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.User;

/**
 * Controller class for the Add User popup in the application.
 * This class handles the actions to add a new user.
 * 
 * @author Kelvin Bian
 * @author Jessica Luo
 */
public class addUserPopupController extends popupController {

    @FXML
    private Button cancelButton;

    @FXML
    private Button enterButton;

    @FXML
    private TextField name;

    private adminController c;

    /**
     * Sets the main window controller for this popup.
     * 
     * @param c The controller of the main window, casted to adminController.
     */
    public void setMainWindow(Controller c) {
        this.c = (adminController) c;
    }

    /**
     * Handles the action to enter a new username and create a new user.
     * Validates the username and shows an alert if the username already exists
     * or if no username is entered. Closes the popup on successful creation.
     *
     * @param event The event that triggered the method.
     */
    @FXML
    public void enterUsername(ActionEvent event) {
        String n = name.getText();
        if (n != null && n != "") {
            User u = new User(n);
            boolean success = Photos.admin.addUser(u);
            if (!success) {
                showAlert("Invalid Username", "Username exists already. Please enter a new username.");
            } else {
                c.getUsers().add(u);
                Photos.serializeAdmin(Photos.admin);
                Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                currentStage.close();
            }
        } else {
            if (n == null || n.equals("")) showAlert("Invalid Username", "No username entered.");
            else showAlert("Invalid Username", "Cannot add another admin user.");
        }
    }
}
