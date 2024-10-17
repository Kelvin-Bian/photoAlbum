package view;

/**
 * Controller class for the Add Photo popup in the application.
 * This class handles the actions to add a new photo to a user's album.
 * @author Kelvin Bian
 * @author Jessica Luo
 */

import java.io.File;
import app.Photos;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Album;
import model.Photo;
import model.User;
/**
 * Controller class for the Add Photo popup in the application.
 * This class handles the actions to add a new photo to a user's album.
 */
public class addPhotoPopupController extends popupController{

    @FXML
    private Button addPhotoButton;

    @FXML
    private Button cancelButton;

    @FXML
    private TextField caption;

    @FXML
    private Button selectButton;

    private Album album;
    private albumController albumController;
    private File imageFile;
    private User user;

    /**
     * Sets the main window controller for this popup.
     * 
     * @param c The controller of the main window.
     */
    public void setMainWindow(Controller c){
        albumController = (albumController) c;
        album = albumController.getAlbum();
    }

    /**
     * Opens a file explorer to select a photo.
     * Triggered by a user action.
     *
     * @param event The event that triggered the method.
     */
    @FXML
    public void selectPhoto(ActionEvent event){
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp")
        );

        imageFile = fileChooser.showOpenDialog(null);
        
    }

    /**
     * Adds a photo to the user's album. If a caption is provided, it is set for the photo.
     * Shows an alert if the photo already exists or no photo is selected.
     * Closes the window upon successful addition of the photo.
     *
     * @param event The event that triggered the method.
     */
    @FXML
    public void addPhoto(ActionEvent event){
        String path = imageFile.getPath();
        // System.out.println(path);
        if (imageFile != null) {
                Photo p = new Photo(path);
                String c = caption.getText();
                if(c!= null && c!=""){
                    p.setCaption(c);
                }
                if (album.addPhoto(p)) {
                    user.updateSamePhotoRefs(p);
                    Photos.serializeAdmin(Photos.admin);
                    albumController.getUser().updateNewPhoto(p);
                    albumController.addPhotoToGrid(p);
                    Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    currentStage.close();
                }
                else{
                    showAlert("Invalid Request", "Photo already exists.");
                }
                
        }
        else
            showAlert("Invalid Request", "No photo selected.");
    }

    /**
     * Initializes the controller with the given user.
     * 
     * @param u The user to initialize the controller with.
     */
    public void initialize(User u){
        user = u;
    }

}
