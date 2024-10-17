
package view;

import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.*;
/**
 * The photoViewerController class is a controller for the photo viewer window in the photo management system.
 * It handles the display of a selected photo, allowing users to view, edit, and manage photo information.
 * 
 * The class includes methods for displaying photo information, adding tags, editing captions, and deleting tags.
 * It also integrates with the addTagPopupController for adding tags via a pop-up window.
 * 
 * The setData method sets the data for the displayed photo, including the image, caption, date, and tags.
 * The addTagPopup method opens a pop-up window for adding tags to the photo.
 * The editCaption method allows users to edit the caption of the photo.
 * The saveCaption method saves the edited caption and updates the user's photo references.
 * The deleteTag method deletes a selected tag from the photo and updates the user's photo references.
 * 
 * The class also provides methods for getting the current photo, user, and tag list.
 * 
 * @author Jessica Luo 
 * @author Kelvin Bian
 */
public class photoViewerController extends Controller{

    @FXML // fx:id="addTagButton"
    private Button addTagButton; // Value injected by FXMLLoader

    @FXML // fx:id="caption"
    private Label caption; // Value injected by FXMLLoader

    @FXML // fx:id="deleteTagButton"
    private Button deleteTagButton; // Value injected by FXMLLoader

    @FXML // fx:id="editButton"
    private Button editButton; // Value injected by FXMLLoader

    @FXML // fx:id="dateDisplay"
    private Label dateDisplay; // Value injected by FXMLLoader

    @FXML // fx:id="image"
    private ImageView image; // Value injected by FXMLLoader

    @FXML // fx:id="tagList"
    private ListView<Tag> tagList; // Value injected by FXMLLoader

    @FXML
    private HBox captionBox;

    @FXML
    private VBox imageSpace;

    private ObservableList<Tag> tagOList;

    private Photo photo; // Instance of the Photo class

    private User u;
    private albumController albumController;

    /**
     * Sets the main window controller for the photo viewer. It initializes the variables and data for the displayed photo.
     *
     * @param albumController The albumController instance representing the main window controller.
     */
    public void setMainWindow(albumController albumController){
        this.albumController = albumController;
        setVariables();
    }

    /**
     * Initializes the variables and data for the displayed photo, including setting the current photo and user.
     */
    public void setVariables() {
        this.photo = albumController.getPhoto();
        u = albumController.getUser();
        setData();
    }

    /**
     * Gets the current photo being displayed.
     *
     * @return The Photo instance representing the displayed photo.
     */
    public Photo getPhoto() {
        return photo;
    }

    /**
     * Gets the current user.
     *
     * @return The User instance representing the current user.
     */
    public User getUser(){
        return u;
    }

    /**
     * Gets the observable list of tags associated with the displayed photo.
     *
     * @return The ObservableList<Tag> representing the list of tags.
     */
    public ObservableList<Tag> getTagList(){
        return tagOList;
    }

    /**
     * Sets the data for the displayed photo, including the image, caption, date, and tags.
     */
    public void setData() {
        image.setPreserveRatio(true);
        image.setImage(photo.getImage());
        image.setFitWidth(575);
        image.setFitHeight(400);

        String captionstr = photo.getCaption();
        if(captionstr != null){
            caption.setText(captionstr);
            caption.getStyleClass().add("label-on-bg");
        }
        else{
            caption.setText("no caption");
            caption.getStyleClass().add("no-caption");
        }

        dateDisplay.setText(photo.getDate().toString());

        // Set the tags label
        tagOList = FXCollections.observableArrayList(photo.getTags());
        tagList.setItems(tagOList);
    }

    /**
     * Opens a pop-up window for adding tags to the photo.
     *
     * @param event The ActionEvent triggered by the user's action.
     */
    @FXML
    void addTagPopup(ActionEvent event){
         try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/addTagPopup.fxml"));
            Parent root = loader.load();
            
            // Access the controller of the loaded FXML layout
            addTagPopupController popupController = loader.getController();
            popupController.setMainWindow(this);
            

            // Create a new stage for displaying the photo and information
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Add Tag");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Allows users to edit the caption of the photo.
     *
     * @param event The ActionEvent triggered by the user's action.
     */
    @FXML
    public void editCaption(ActionEvent event){
        captionBox.getChildren().removeAll(caption, editButton);
        TextField enterCaptionField = new TextField();
        enterCaptionField.setPromptText("enter a caption");
        enterCaptionField.requestFocus();
        Button enterButton = new Button("Enter");
        enterButton.setOnAction(e -> saveCaption(e));
        captionBox.getChildren().addAll(enterCaptionField, enterButton);
        albumController.updateCaptionLabel();
    }

    /**
     * Saves the edited caption and updates the user's photo references.
     *
     * @param event The ActionEvent triggered by the user's action.
     */
    @FXML
    void saveCaption(ActionEvent event){
        Button enterButton = (Button) event.getSource();
        TextField captionField = (TextField) captionBox.getChildren().get(0);

        photo.setCaption(captionField.getText());

        captionBox.getChildren().removeAll(captionField, enterButton);
        caption.setText(photo.getCaption());

        u.updateSamePhotoRefs(photo);
        albumController.updateCaptionLabel();
        if(caption.getText().equals("")){
            caption.setText("no caption");
            caption.getStyleClass().add("no-caption");
        }
        else
            caption.getStyleClass().remove("no-caption");
        captionBox.getChildren().addAll(caption,editButton);
    }

    /**
     * Deletes a selected tag from the photo and updates the user's photo references.
     *
     * @param event The ActionEvent triggered by the user's action.
     */
    @FXML
    void deleteTag(ActionEvent event){
        SelectionModel<Tag> selection = tagList.getSelectionModel();
        Tag t = selection.getSelectedItem();
        if(t == null){
            showAlert("Invalid Request", "Please select a tag first.");
        }
        else{
            photo.deleteTag(t);
            u.updateSamePhotoRefs(photo);
            tagOList.remove(t);
        }
    }
}
