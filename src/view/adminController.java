package view;

import java.io.IOException;
import app.Photos;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionModel;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.User;

/**
 * Controller class for the admin functionalities in the application.
 * Handles user management tasks including adding and deleting users.
 * 
 * @author Kelvin Bian
 * @author Jessica Luo
 */
public class adminController extends Controller {

    @FXML
    private Button addUserButton;

    @FXML
    private Button deleteUserButton;

    @FXML
    private Button logoutButton;

    @FXML
    private Button quitButton;

    @FXML
    private ListView<User> userList;

    private ObservableList<User> users;

    /**
     * Retrieves the list of users.
     * 
     * @return An ObservableList of users.
     */
    public ObservableList<User> getUsers() {
        return users;
    }

    /**
     * Displays the list of users in the user interface.
     */
    public void displayUsers() {
        users = FXCollections.observableArrayList(Photos.admin.getUsers());
        userList.setItems(users);
    }

    /**
     * Handles the action to add a new user.
     * Opens a new window for user input.
     */
    @FXML
    public void addUser() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/addUserPopup.fxml"));
            Parent root = loader.load();
            
            addUserPopupController popupController = loader.getController();
            popupController.setMainWindow(this);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Add User");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the action to delete a selected user.
     * Shows an alert if 'admin' is attempted to be deleted or no user is selected.
     *
     * @param e The event that triggered the method.
     */
    @FXML
    public void deleteUser(ActionEvent e) {
        SelectionModel<User> selection = userList.getSelectionModel();
        User u = selection.getSelectedItem();
        if (u != null) {
            if (u.getName().equals("admin")) {
                showAlert("Invalid Request", "You cannot delete admin.");
            } else {
                Photos.admin.deleteUser(u.getName());
                users.remove(u);
            }
        } else {
            showAlert("Invalid Request", "No user selected.");
        }
    }
}
