package view;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * Abstract base controller class providing common functionalities like quitting,
 * logging out, and showing alerts for different views in the application.
 * This class can be extended by other controllers requiring these basic operations.
 *
 * @author Kelvin Bian
 * @author Jessica Luo
 */

public abstract class Controller {

    @FXML
    private Button homeButton;
    @FXML
    private Button logoutButton;
    @FXML
    private Button quitButton;

    /**
     * Handles the 'Quit' action. Closes the current stage.
     *
     * @param event The action event that triggered this method.
     */
    @FXML
    void quit(ActionEvent event){
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }

    /**
     * Handles the 'Logout' action. Closes the current stage and opens the login view.
     *
     * @param event The action event that triggered this method.
     */
    @FXML
    void logout(ActionEvent event){
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
        try{
            Stage primaryStage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/fxml/login.fxml"));
            Parent root = loader.load();
            primaryStage.setTitle("Photos");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Displays an alert dialog with a specified title and content.
     * This static method can be called by any subclass.
     *
     * @param title   The title of the alert dialog.
     * @param content The content text to be displayed in the alert.
     */
    static void showAlert(String title, String content) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Cancels the current operation and closes the current stage.
     *
     * @param event The action event that triggered this method.
     */
    @FXML
    public void cancel(ActionEvent event){
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }
}
