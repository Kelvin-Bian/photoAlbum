
package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Photo;
import model.Tag;
import model.User;
/**
 * The simplePhotoViewerController class is a controller for the simple photo viewer in the photo management system.
 * It handles the display of a single photo along with its caption, date, and tags.
 * 
 * This controller is associated with an FXML file that defines the layout of the simple photo viewer.
 * It includes labels for displaying the caption and date, an image view for displaying the photo,
 * a list view for displaying the tags, and an HBox and VBox for layout purposes.
 * 
 * The controller initializes and sets data for the displayed photo, including its image, caption, date, and tags.
 * It also provides methods to retrieve the current photo, user, and tag list.
 * 
 * Additionally, the controller supports setting variables and data when associated with the searchOutputController.
 * 
 * @author Kelvin Bian 
 * @author Jessica Luo
 */
public class simplePhotoViewerController {

    @FXML // fx:id="caption"
    private Label caption; // Value injected by FXMLLoader

    @FXML // fx:id="captionBox"
    private HBox captionBox; // Value injected by FXMLLoader

    @FXML // fx:id="dateDisplay"
    private Label dateDisplay; // Value injected by FXMLLoader

    @FXML // fx:id="image"
    private ImageView image; // Value injected by FXMLLoader

    @FXML // fx:id="imageSpace"
    private VBox imageSpace; // Value injected by FXMLLoader

    @FXML // fx:id="tagList"
    private ListView<Tag> tagList; // Value injected by FXMLLoader

    private ObservableList<Tag> tagOList;

    private Photo photo; // Instance of the Photo class

    private User u;

    private searchOutputController searchOutputController;

    /**
     * Sets the main window controller (searchOutputController) associated with this simple photo viewer.
     *
     * @param searchOutputController The searchOutputController instance.
     */
    public void setMainWindow(searchOutputController searchOutputController) {
        this.searchOutputController = searchOutputController;
        setVariables();
    }

    /**
     * Sets the variables for the controller, including the user data.
     */
    public void setVariables() {
        u = searchOutputController.getUser();
        setData();
    }

    /**
     * Retrieves the current photo.
     *
     * @return The Photo instance representing the current photo.
     */
    public Photo getPhoto() {
        return photo;
    }

    /**
     * Retrieves the associated user.
     *
     * @return The User instance representing the associated user.
     */
    public User getUser() {
        return u;
    }

    /**
     * Retrieves the observable list of tags.
     *
     * @return The ObservableList of tags associated with the current photo.
     */
    public ObservableList<Tag> getTagList() {
        return tagOList;
    }

    /**
     * Sets the data for the simple photo viewer, including image, caption, date, and tags.
     */
    public void setData() {
        image.setPreserveRatio(true);
        image.setImage(photo.getImage());
        image.setFitWidth(575);
        image.setFitHeight(400);

        String captionstr = photo.getCaption();
        if (captionstr != null) {
            caption.setText(captionstr);
            caption.getStyleClass().add("label-on-bg");
        } else {
            caption.setText("no caption");
            caption.getStyleClass().add("no-caption");
        }

        dateDisplay.setText(photo.getDate().toString());

        // Set the tags label
        tagOList = FXCollections.observableArrayList(photo.getTags());
        tagList.setItems(tagOList);
    }

    /**
     * Initializes the simple photo viewer with the provided photo.
     *
     * @param p The Photo instance to be displayed in the viewer.
     */
    public void initialize(Photo p) {
        photo = p;
    }
}
