
package view;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import model.Album;
import model.User;
import javafx.scene.Node;
import javafx.util.StringConverter;
/**
 * The selectAlbumPopupController class is a controller for the album selection popup in the photo management system.
 * It handles the interaction with the user for selecting an album from a ComboBox.
 * 
 * This controller is associated with an FXML file that defines the layout of the album selection popup.
 * It includes buttons for canceling and entering the selection, and a ComboBox for displaying the list of available albums.
 * 
 * The controller is designed to work with the albumController as the main controller, and it sets the selected album
 * when the user chooses one from the ComboBox. The selected album can then be retrieved using the getSelectedAlbum method.
 * 
 * The initializeAlbumList method initializes the ComboBox with the list of albums associated with the provided user.
 * It uses a StringConverter to display the album names in the ComboBox.
 * 
 * The setUser method sets the user for whom the album selection is being made.
 * 
 * @author Kelvin Bian 
 * @author Jessica Luo
 */
public class selectAlbumPopupController extends popupController {

    @FXML
    private Button cancelButton;

    @FXML
    private Button enterButton;

    @FXML
    private ComboBox<Album> albumComboBox;

    private albumController mainController;
    private User user;
    private Album selectedAlbum;

    /**
     * Sets the main window controller (albumController) associated with this album selection popup.
     *
     * @param c The albumController instance.
     */
    @Override
    void setMainWindow(Controller c) {
        mainController = (albumController) c;
    }

    /**
     * Sets the user for whom the album selection is being made and initializes the album list in the ComboBox.
     *
     * @param user The User instance for whom the album selection is being made.
     */
    public void setUser(User user) {
        this.user = user;
        initializeAlbumList();
    }

    /**
     * Initializes the album list in the ComboBox with the albums associated with the current user.
     * Uses a StringConverter to display album names.
     */
    private void initializeAlbumList() {
        albumComboBox.setItems(FXCollections.observableArrayList(user.getAlbums()));  // Assuming getAlbums returns a list of albums

        // Setting up a StringConverter for displaying album names
        albumComboBox.setConverter(new StringConverter<Album>() {
            @Override
            public String toString(Album album) {
                // Use the getName method to get the album name
                return album != null ? album.getName() : "";
            }

            @Override
            public Album fromString(String string) {
                // This is not needed for a ComboBox as it's not editable
                return null;
            }
        });
    }

    /**
     * Handles the action when the user selects an album from the ComboBox.
     * Sets the selected album and closes the popup.
     *
     * @param event The ActionEvent triggered by the user's action.
     */
    @FXML
    void selectAlbum(ActionEvent event) {
        selectedAlbum = albumComboBox.getValue();
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }

    /**
     * Retrieves the selected album.
     *
     * @return The Album instance representing the selected album.
     */
    public Album getSelectedAlbum() {
        return selectedAlbum;
    }
}
