 /* @author Kelvin Bian
 * @author Jessica Luo
 */

package view;

import java.io.IOException;
import app.Photos;
import model.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.layout.RowConstraints;

/**
 * Controller class for managing album views and interactions in a photo album application.
 * This class handles various actions like adding, deleting, copying, moving photos,
 * editing captions, and opening slideshows.
 */
public class albumController extends Controller {

    @FXML // fx:id="addPhotoButton"
    private Button addPhotoButton; // Value injected by FXMLLoader

    @FXML // fx:id="addTagButton"
    private Button addTagButton; // Value injected by FXMLLoader

    @FXML // fx:id="albumList"
    private ListView<Album> albumList; // Value injected by FXMLLoader

    @FXML // fx:id="albumName"
    private Label albumName; // Value injected by FXMLLoader

    @FXML // fx:id="copyPhotoButton"
    private Button copyPhotoButton; // Value injected by FXMLLoader

    @FXML // fx:id="deletePhotoButton"
    private Button deletePhotoButton; // Value injected by FXMLLoader

    @FXML // fx:id="deleteTagButton"
    private Button deleteTagButton; // Value injected by FXMLLoader

    @FXML // fx:id="homeButton"
    private Button homeButton; // Value injected by FXMLLoader

    @FXML // fx:id="logoutButton"
    private Button logoutButton; // Value injected by FXMLLoader

    @FXML // fx:id="movePhotoButton"
    private Button movePhotoButton; // Value injected by FXMLLoader

    @FXML // fx:id="openButton"
    private Button openButton; // Value injected by FXMLLoader

    @FXML // fx:id="photoGrid"
    private GridPane photoGrid; // Value injected by FXMLLoader

    @FXML // fx:id="quitButton"
    private Button quitButton; // Value injected by FXMLLoader

    @FXML // fx:id="slideshowButton"
    private Button slideshowButton; // Value injected by FXMLLoader

    private TextField lastCaptionTextField;
    
    private Button lastSelectedButton;

    private Album album;

    private Photo photo;

    private Photo editCaptionPhoto;

    private User user;

    private int currentColumn = 0;
    private int currentRow = 0;
    private final int maxColumns = 3; 

    /**
     * Navigates back to the home view.
     *
     * @param event The action event that triggered this method.
     */
    @FXML
    public void home(ActionEvent event){
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
        try{
            Stage primaryStage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/fxml/home.fxml"));
            Parent root = loader.load();
            homeController c = loader.getController();
            c.initialize(user);
            primaryStage.setTitle("Home");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Initializes the controller with specified album and user.
     *
     * @param a The album to be managed.
     * @param u The user currently using the application.
     */
    public void initialize(Album a, User u){
        setAlbum(a);
        user = u;
        albumList.setItems(FXCollections.observableArrayList(user.getAlbums()));
    }

    /**
     * Sets the current album and updates the view accordingly.
     *
     * @param album The album to set.
     */
    public void setAlbum(Album album){
        this.album = album;
        albumName.setText(album.getName());
        displayPhotos();
    }

    /**
     * Gets the currently set album.
     *
     * @return The current album.
     */
    public Album getAlbum(){
        return album;
    }

    /**
     * Gets the current user.
     *
     * @return The current user.
     */
    public User getUser(){
        return user;
    }
    
    /**
     * Handles adding a new photo to the album.
     *
     * @param event The action event that triggered this method.
     */
    @FXML
    void addPhoto(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/addPhotoPopup.fxml"));
            Parent root = loader.load();
            
            // Access the controller of the loaded FXML layout
            addPhotoPopupController popupController = loader.getController();
            popupController.initialize(user);
            popupController.setMainWindow(this);
            

            // Create a new stage for displaying the photo and information
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Add Photo");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a photo to the photo grid in the UI.
     *
     * @param photo The photo to add.
     */
    void addPhotoToGrid(Photo photo) {

        ImageView imageView = new ImageView();
        imageView.setPickOnBounds(true);
        imageView.setPreserveRatio(true);
        imageView.setImage(photo.getImage());
        imageView.setFitHeight(85);
        imageView.setFitWidth(230);
        
        Label dateLabel = new Label(photo.getDate().toString());
        Button selectButton = new Button("Select");
        selectButton.setId(photo.getPath());
        selectButton.getStyleClass().add("photo-grid-button");
        selectButton.setOnAction(event -> selectPhoto(event));
        
        HBox captionBox = new HBox();
        String captionstr= photo.getCaption();
        VBox vbox;
        Label captionLabel = new Label();
        if(captionstr!=null){
            captionLabel.setText(captionstr);
            captionLabel.getStyleClass().add("label-on-bg");
            captionLabel.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        }
        else{
            captionLabel.setText("no caption");
            captionLabel.getStyleClass().add("no-caption");
        }
        Button editButton = makeEditButton();
        captionBox.getChildren().addAll(captionLabel, editButton);
        captionBox.setSpacing(5);
        captionBox.setAlignment(javafx.geometry.Pos.CENTER);
        vbox = new VBox(imageView, dateLabel, captionBox, selectButton);
        vbox.setAlignment(javafx.geometry.Pos.TOP_CENTER);
        vbox.setSpacing(10);
        
        VBox.setVgrow(imageView, javafx.scene.layout.Priority.SOMETIMES);

        if (currentColumn >= maxColumns) {
            currentColumn = 0;
            currentRow++;
            addRowToGridPane(currentRow);
        }

        photoGrid.add(vbox, currentColumn, currentRow);
        currentColumn++;
    }
    
    /**
     * Creates and returns a new edit button.
     *
     * @return A new edit button.
     */
    private Button makeEditButton(){
         Button editButton = new Button();
        editButton.setMnemonicParsing(false);
        editButton.setMaxHeight(12.0);
        editButton.setMaxWidth(13.0);
        editButton.setOnAction(event -> editCaption(event));
        editButton.getStyleClass().add("edit-button");
        // Create an ImageView with an Image
        ImageView icon = new ImageView(new Image(getClass().getResourceAsStream("/view/imgs/edit icon.png")));
        icon.setFitHeight(12.0);
        icon.setFitWidth(13.0);
        icon.setPickOnBounds(true);
        icon.setPreserveRatio(true);

        // Set the ImageView as the graphic for the Button
        editButton.setGraphic(icon);
        return editButton;
    }

    /**
     * Displays the photos in the current album.
     */
    public void displayPhotos(){
        for(Photo p: album.getPhotos()){
            addPhotoToGrid(p);
        }
    }

    /**
     * Adds a new row to the GridPane.
    *
    * @param rowIndex The index of the new row.
    */
    private void addRowToGridPane(int rowIndex) {
        // Only add a new row if it does not already exist
        if (photoGrid.getRowConstraints().size() <= rowIndex) {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setMinHeight(300);
            rowConstraints.setPrefHeight(300);
            rowConstraints.setVgrow(javafx.scene.layout.Priority.ALWAYS);
            photoGrid.getRowConstraints().add(rowConstraints);
        }
    }

    /**
     * Edits the caption of a photo.
     *
     * @param event The action event that triggered this method.
     */
   @FXML
    public void editCaption(ActionEvent event){
        if(editCaptionPhoto!= null){ //scenario: click edit caption on one photo, did not enter, and clicked edit on another photo
            //change prev edit caption to not editing state
            HBox captionBox = (HBox) lastCaptionTextField.getParent();
            Button enterButton = (Button) captionBox.getChildren().get(1);
            captionBox.getChildren().removeAll(lastCaptionTextField, enterButton);
            Label caption = new Label(editCaptionPhoto.getCaption());
            if(caption.getText().equals("")){
                caption.setText("no caption");
                caption.getStyleClass().add("no-caption");
            }
            captionBox.getChildren().addAll(caption,makeEditButton());
            editCaptionPhoto = null;
        }
        Button editButton = (Button) event.getSource();
        HBox captionBox = (HBox) editButton.getParent();
        Label captionLabel = (Label) captionBox.getChildren().get(0);
        captionBox.getChildren().removeAll(editButton, captionLabel);
        TextField enterCaptionField = new TextField();
        enterCaptionField.setPromptText("enter a caption");
        enterCaptionField.requestFocus();
        lastCaptionTextField = enterCaptionField;
        Button enterButton = new Button("Enter");
        enterButton.setOnAction(e -> saveCaption(e));
        captionBox.getChildren().addAll(enterCaptionField, enterButton);
        VBox gridCell = (VBox) captionBox.getParent();
        Button selecButton = (Button) gridCell.getChildren().get(3);
        editCaptionPhoto = album.findPhoto(selecButton.getId());
    }

    /**
     * Saves the edited caption of a photo.
     *
     * @param event The action event that triggered this method.
     */
    @FXML
    void saveCaption(ActionEvent event){
        Button enterButton = (Button) event.getSource();
        HBox captionBox = (HBox) enterButton.getParent();
        TextField captionField = (TextField) captionBox.getChildren().get(0);
        editCaptionPhoto.setCaption(captionField.getText());
        user.updateSamePhotoRefs(editCaptionPhoto);
        captionBox.getChildren().removeAll(captionField, enterButton);
        Label caption = new Label(editCaptionPhoto.getCaption());
        if(caption.getText().equals("")){
            caption.setText("no caption");
            caption.getStyleClass().add("no-caption");
        }
        captionBox.getChildren().addAll(caption,makeEditButton());
        editCaptionPhoto = null;
        lastCaptionTextField = null;
    }

    /**
     * Updates the caption label of the currently selected photo.
     */
    void updateCaptionLabel(){
        VBox gridcell = (VBox) lastSelectedButton.getParent();
        HBox captionBox = (HBox) gridcell.getChildren().get(2);
        Label captionLabel = (Label) captionBox.getChildren().get(0);
        captionLabel.setText(photo.getCaption());
    }

    /**
     * Selects a photo from the photo grid.
     *
     * @param event The action event that triggered this method.
     */
    @FXML
    void selectPhoto(ActionEvent event) {
        Button select = (Button) event.getSource();
        photo = album.findPhoto(select.getId());
        if(lastSelectedButton!= null){
            lastSelectedButton.setText("Select");
            lastSelectedButton.getStyleClass().remove("selected");
            lastSelectedButton.getStyleClass().add("photo-grid-button");
        }
        lastSelectedButton = select;
        lastSelectedButton.getStyleClass().remove("photo-grid-button");
        lastSelectedButton.getStyleClass().add("selected");
        lastSelectedButton.setText("Selected");
    }

    /**
     * Gets the currently selected photo.
     *
     * @return The currently selected photo.
     */
    public Photo getPhoto(){
        return photo;
    }
    
    /**
     * Opens a selected photo for detailed view.
     *
     * @param event The action event that triggered this method.
     */
    @FXML
    void openPhoto(ActionEvent event) {
        if (photo != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/photoViewer.fxml"));
                Parent root = loader.load();
                photoViewerController photoViewerController = loader.getController();
                photoViewerController.setMainWindow(this);
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setTitle("Photo Display");
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            showAlert("Invalid Request", "Please select a photo first.");
        }
    }

    /**
     * Copies the selected photo to another album.
     *
     * @param event The action event that triggered this method.
     */
    @FXML
    void copyPhoto(ActionEvent event) {
        if (photo == null) {
            showAlert("Invalid Request", "No photo selected to copy");
            return;
        }
    
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/selectAlbumPopup.fxml"));
            Parent root = loader.load();
            
            selectAlbumPopupController popupController = loader.getController();
            popupController.setMainWindow(this);
            popupController.setUser(user);
    
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Select Album");
            stage.setScene(new Scene(root));
            stage.showAndWait();
    
            Album selectedAlbum = popupController.getSelectedAlbum();
            if (selectedAlbum != null) {
                if(selectedAlbum.equals(album)) {
                    showAlert("Invalid Action", "Cannot copy photo to the same album.");
                    return;
                }
                if(!selectedAlbum.addPhoto(photo)){
                    showAlert("Invalid Action", "Photo already exists in "+selectedAlbum.getName());
                }
                Photos.serializeAdmin(Photos.admin);
            }
            else{
                showAlert("Invalid Action", "No album selected");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Moves the selected photo to another album.
     *
     * @param event The action event that triggered this method.
     */
    @FXML
    void movePhoto(ActionEvent event) {
        if (photo == null) {
            showAlert("Invalid Request", "No photo selected to move");
            return;
        }
    
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/selectAlbumPopup.fxml"));
            Parent root = loader.load();
            
            selectAlbumPopupController popupController = loader.getController();
            popupController.setMainWindow(this);
            popupController.setUser(user); 
    
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Select Album");
            stage.setScene(new Scene(root));
            stage.showAndWait();
    
            Album selectedAlbum = popupController.getSelectedAlbum();
            if (selectedAlbum != null) {
                if(selectedAlbum.equals(album)) {
                    showAlert("Invalid Action", "Cannot move photo to the same album.");
                    return;
                }
                boolean success = selectedAlbum.addPhoto(photo);
                if(success) album.deletePhoto(photo); 
                else showAlert("Invalid Action", "Photo already exists in "+selectedAlbum.getName());
                photoGrid.getChildren().clear();
                currentColumn = 0;
                currentRow = 0;
                for (Photo p : album.getPhotos()) {
                    addPhotoToGrid(p);
                }
                // Reset the current photo selection
                photo = null;
                if (lastSelectedButton != null) {
                    lastSelectedButton.getStyleClass().remove("selected");
                    lastSelectedButton.getStyleClass().add("photo-grid-button");
                    lastSelectedButton.setText("Select");
                    lastSelectedButton = null;
                }
                Photos.serializeAdmin(Photos.admin);
            }
            else{
                showAlert("Invalid Action", "No album selected");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes the selected photo from the current album.
     *
     * @param event The action event that triggered this method.
     */
    @FXML
    void deletePhoto(ActionEvent event) {
        if (photo == null) {
            // Handle the case where no photo is selected
            showAlert("Invalid Request", "No photo selected to delete");
            return;
        }
    
        album.deletePhoto(photo);
    
        photoGrid.getChildren().clear();
        currentColumn = 0;
        currentRow = 0;
        for (Photo p : album.getPhotos()) {
            addPhotoToGrid(p);
        }
        photo = null;
        if (lastSelectedButton != null) {
            lastSelectedButton.getStyleClass().remove("selected");
            lastSelectedButton.getStyleClass().add("photo-grid-button");
            lastSelectedButton.setText("Select");
            lastSelectedButton = null;
        }
        Photos.serializeAdmin(Photos.admin);
    }

    /**
     * Opens a slideshow of photos in the current album.
     *
     * @param event The action event that triggered this method.
     */
    @FXML
    public void openSlideshow(ActionEvent event){
        if(album.getPhotos().size()==0){
            showAlert("Invalid Request", "No photos to make a slideshow.");
        }
        else{
            try{
                Stage primaryStage = new Stage();
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/view/fxml/slideshow.fxml"));
                Parent root;
                
                root = loader.load();
                slideshowController c = loader.getController();
                c.setAlbum(album);
                primaryStage.setTitle("Slideshow");
                primaryStage.setScene(new Scene(root));
                primaryStage.show();
            }
            catch(Exception e){
                showAlert("Error", "Failed to open slideshow.");
            }
        }
    }
}
