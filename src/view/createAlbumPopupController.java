package view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Album;
import model.User;

/**
 * Controller class for the 'Create Album' popup in a photo management application.
 * This class manages the creation of new albums.
 *
 * @author Kelvin Bian
 * @author Jessica Luo
 */

public class createAlbumPopupController extends popupController {

    @FXML
    private Button cancelButton;

    @FXML
    private Button enterButton;

    @FXML
    private TextField name;

    private homeController homeController;


    /**
     * Sets the main window controller that this popup is associated with.
     *
     * @param c The controller of the main window.
     */
    void setMainWindow(Controller c){
        homeController = (homeController) c;
    }

    /**
     * Creates a new album based on the input provided in the 'name' text field.
     * Shows an alert if the album name is empty or the album already exists.
     *
     * @param event The action event that triggered this method.
     */
    @FXML
    void createAlbum(ActionEvent event){
        String namestr = name.getText();
        if(namestr!=null && !namestr.equals("")){
            User u = homeController.getUser();
            Album a = new Album(namestr);
            if(!u.addAlbum(namestr)){
                showAlert("Invalid Request", "Album named "+namestr+" already exists");
            }
            else{
                homeController.addAlbum(a);
                Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                currentStage.close();
            }
        }
        else
        showAlert("Invalid Request", "No name entered.");
        
    }

}
