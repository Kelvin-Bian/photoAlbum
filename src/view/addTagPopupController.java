package view;

/**
 * Controller class for the Add Tag popup in the application.
 * This class handles the actions to add a new tag to a photo.
 * @author Kelvin Bian
 * @author Jessica Luo
 */

import app.Photos;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Photo;
import model.Tag;
/**
 * Controller class for the Add Tag popup in the application.
 * This class handles the actions to add a new tag to a photo.
 */
public class addTagPopupController extends popupController {

    @FXML
    private Button addTagButton;

    @FXML
    private Button cancelButton;

    @FXML
    private TextField name;

    @FXML
    private TextField value;

    private photoViewerController photoViewerController;
    private Photo p;


    /**
     * Sets the main window controller for this popup.
     * Links this controller to the photoViewerController and the current photo.
     * 
     * @param c The controller of the main window, casted to photoViewerController.
     */
    void setMainWindow(Controller c){
        photoViewerController = (photoViewerController) c;
        p = photoViewerController.getPhoto();
    }


    /**
     * Adds a new tag to the photo. Shows an alert if the tag name-value pair already exists
     * or if the tag name or value is not entered. Closes the popup on successful addition.
     *
     * @param event The event that triggered the method.
     */
    @FXML
    void addTag(ActionEvent event) {
        String tagName = name.getText();
        String tagValue = value.getText();
        if(tagName == null || tagValue == null){
            showAlert("Invalid Tag", "Please enter a tag name and a tag value.");
        }
        else if( p.tagExists(tagName, tagValue)){
            showAlert("Invalid Tag", "This tag name-value pair already exists.");
        }
        else{
            Tag t = new Tag(tagName, tagValue);
            p.addTag(t);
            photoViewerController.getTagList().add(t);
            photoViewerController.getUser().updateSamePhotoRefs(p);
            Photos.serializeAdmin(Photos.admin);
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
        }
    }


}
