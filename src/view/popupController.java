
package view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.stage.Stage;
/**
 * The popupController class is an abstract controller for handling pop-up windows in the photo management system.
 * It defines common functionality for pop-up windows, such as displaying alerts and handling cancel actions.
 * 
 * Subclasses of popupController should implement the setMainWindow method to set the main window controller when needed.
 * The showAlert method is a static method for displaying warning alerts with a specified title and content.
 * The cancel method is an event handler for cancel actions in pop-up windows. It closes the current pop-up window.
 * 
 * @author Jessica Luo 
 * @author Kelvin Bian
 */
public abstract class popupController {
    @FXML
    private Button cancelButton;

    /**
     * Sets the main window controller for the pop-up window. Subclasses should implement this method as needed.
     *
     * @param c The Controller instance representing the main window controller.
     */
    abstract void setMainWindow(Controller c);

    /**
     * Displays a warning alert with the specified title and content.
     *
     * @param title   The title of the alert.
     * @param content The content text of the alert.
     */
    static void showAlert(String title, String content) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);

        alert.showAndWait();
    }

    /**
     * Handles the cancel action in pop-up windows. Closes the current pop-up window.
     *
     * @param event The ActionEvent triggered by the user's action.
     */
    @FXML
    public void cancel(ActionEvent event) {
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }
}
