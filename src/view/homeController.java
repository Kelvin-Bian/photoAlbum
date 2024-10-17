
package view;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import app.Photos;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Album;
import model.Photo;
import model.User;
import model.Tag;
import javafx.scene.Node;

/**
 * The controller for the home view in a photo management application.
 * This class handles functionalities such as opening albums, creating new albums, 
 * selecting albums for further actions, and searching photos based on date or tags.
 *
 * @author Kelvin Bian
 * @author Jessica Luo
 */

public class homeController extends Controller implements Serializable{
    @FXML
    private Label selectedAlbumDisplay;
    @FXML
    private Label username;
    @FXML 
    private Button openAlbumButton; // Value injected by FXMLLoader

    // @FXML // fx:id="albumName"
    // private Label albumName; // Value injected by FXMLLoader

    @FXML // fx:id="createAlbumButton"
    private Button createAlbumButton; // Value injected by FXMLLoader

    // @FXML // fx:id="dateRange"
    // private Label dateRange; // Value injected by FXMLLoader

    @FXML // fx:id="deleteAlbumButton"
    private Button deleteAlbumButton; // Value injected by FXMLLoader

    @FXML // fx:id="endDay"
    private ComboBox<Integer> endDay; // Value injected by FXMLLoader

    @FXML // fx:id="endMonth"
    private ComboBox<String> endMonth; // Value injected by FXMLLoader

    @FXML // fx:id="endYear"
    private TextField endYear; // Value injected by FXMLLoader

    @FXML // fx:id="logoutButton"
    private Button logoutButton; // Value injected by FXMLLoader

    // @FXML // fx:id="numPhotos"
    // private Label numPhotos; // Value injected by FXMLLoader

    @FXML // fx:id="photoGrid"
    private GridPane photoGrid; // Value injected by FXMLLoader

    @FXML // fx:id="quitButton"
    private Button quitButton; // Value injected by FXMLLoader

    @FXML // fx:id="searchButton"
    private Button searchButton; // Value injected by FXMLLoader

    @FXML // fx:id="startDay"
    private ComboBox<Integer> startDay; // Value injected by FXMLLoader

    @FXML // fx:id="startMonth"
    private ComboBox<String> startMonth; // Value injected by FXMLLoader

    @FXML // fx:id="startYear"
    private TextField startYear; // Value injected by FXMLLoader

    @FXML // fx:id="tag1type"
    private ComboBox<String> tag1type; // Value injected by FXMLLoader

    @FXML // fx:id="tag1value"
    private TextField tag1value; // Value injected by FXMLLoader

    @FXML // fx:id="tag2type"
    private ComboBox<String> tag2type; // Value injected by FXMLLoader

    @FXML // fx:id="tag2value"
    private TextField tag2value; // Value injected by FXMLLoader

    @FXML
    private Button andButton;

    @FXML // fx:id="tagSearchButton"
    private Button tagSearchButton; // Value injected by FXMLLoader

    @FXML // fx:id="dateSearchButton"
    private Button dateSearchButton; // Value injected by FXMLLoader

    @FXML
    private Button orButton;

    private Button lastSelectedButton;

    private User user;

    private Album selectedAlbum;

    // for searching purposes
    private Album temporaryAlbum;
    private boolean andSearch;
    private boolean dateSearch;
    private boolean searchTypeSelected;

    private int currentColumn = 0;
    private int currentRow = 0;
    private final int maxColumns = 3; 

    private Album editNameAlbum;
    private TextField lastNameTextField;
  
    /**
     * Sets the current user and updates the view accordingly.
     *
     * @param u The user to set.
     */
    public void setUser(User u){
        this.user = u;
        username.setText(u.getName());
        displayAlbums();
    }

    /**
     * Opens the selected album in a new view.
     *
     * @param event The action event that triggered this method.
     */
    @FXML
    void openAlbum(ActionEvent event) {
        if(selectedAlbum!=null){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/album.fxml"));
            Parent albumRoot = loader.load();

            albumController c = loader.getController();
            c.initialize(selectedAlbum, user);
            Stage albumStage = new Stage();
            albumStage.setTitle("Album");
            Scene albumScene = new Scene(albumRoot);
            albumStage.setScene(albumScene);

            albumStage.show();

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        }
        else{
            showAlert("Invalid Request", "Select an album first.");
        }

    }

    /**
     * Displays a popup for creating a new album.
     *
     * @param event The action event that triggered this method.
     */
    @FXML
    void createAlbumPopup(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/createAlbumPopup.fxml"));
            Parent root = loader.load();
            
            createAlbumPopupController popupController = loader.getController();
            popupController.setMainWindow(this);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Create Album");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Returns the current User.
     * 
     * @return the current user.
     */
    public User getUser(){
        return user;
    }

    /**
     * Selects an album for further actions based on the user's choice.
     *
     * @param event The action event that triggered this method.
     */
    @FXML
    void selectAlbum(ActionEvent event){

        Button select = (Button) event.getSource();
        selectedAlbum = user.findAlbum(select.getId());
        selectedAlbumDisplay.setText("Actions on Selected Album: "+selectedAlbum.getName());
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
     * Creates and returns a new edit button for editing album names.
     *
     * @return A new edit button.
     */
   private Button makeEditButton(){
        Button editButton = new Button();
        editButton.setMnemonicParsing(false);
        editButton.setMaxHeight(12.0);
        editButton.setMaxWidth(13.0);
        editButton.setOnAction(event -> editName(event));
        editButton.getStyleClass().add("edit-button");

        ImageView icon = new ImageView(new Image(getClass().getResourceAsStream("/view/imgs/edit icon.png")));
        icon.setFitHeight(12.0);
        icon.setFitWidth(13.0);
        icon.setPickOnBounds(true);
        icon.setPreserveRatio(true);

        editButton.setGraphic(icon);
        return editButton;
    }

    /**
     * Adds a new album to the user's album list and updates the view.
     *
     * @param a The album to add.
     */
    public void addAlbum(Album a){
        Button selectButton = new Button("Select");
        selectButton.setId(a.getName());
        selectButton.getStyleClass().add("photo-grid-button");
        selectButton.setOnAction(event -> selectAlbum(event));
        
        String namestr= a.getName();
        Label albumName = new Label(namestr);
        HBox nameBox = new HBox();
        Button editButton = makeEditButton();
        nameBox.getChildren().addAll(albumName, editButton);
        nameBox.setSpacing(5);
        nameBox.setAlignment(javafx.geometry.Pos.CENTER);

        VBox vbox;

        String dateRangestr = a.getDateRange();
        if(dateRangestr!=null){
            Label dateLabel = new Label(dateRangestr);
            dateLabel.getStyleClass().add("label-on-bg");
            dateLabel.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
            String numPhotostr = a.numPhotos()+" photo";
            numPhotostr+= (a.numPhotos()>1)? "s":"";
            Label numberOfPhotos = new Label(numPhotostr);
            numberOfPhotos.getStyleClass().add("label-on-bg");
            vbox = new VBox(nameBox, dateLabel, numberOfPhotos, selectButton);
        }
        else{
            String numPhotostr = a.numPhotos()+" photos";
            Label numberOfPhotos = new Label(numPhotostr);
            numberOfPhotos.getStyleClass().add("label-on-bg");
            vbox = new VBox(nameBox, numberOfPhotos, selectButton);
        }
        vbox.setAlignment(javafx.geometry.Pos.CENTER);
        vbox.setSpacing(10); 

        AnchorPane photoPane = new AnchorPane(vbox);
        AnchorPane.setTopAnchor(vbox, 5.0);
        AnchorPane.setBottomAnchor(vbox, 5.0);
        AnchorPane.setLeftAnchor(vbox, 5.0);
        AnchorPane.setRightAnchor(vbox, 5.0);

        if (currentColumn >= maxColumns) {
            currentColumn = 0;
            currentRow++;
            addRowToGridPane(currentRow);
        }

        photoGrid.add(photoPane, currentColumn, currentRow);
        currentColumn++;
   }

    /**
     * Deletes the selected album from the user's album list and updates the view.
     *
     * @param event The action event that triggered this method.
     */
   @FXML
   void deleteAlbum(ActionEvent event) {
        if (selectedAlbum == null) {
            showAlert("Invalid Request", "No photo selected to delete");
            return;
        }
        user.deleteAlbum(selectedAlbum.getName());
        selectedAlbumDisplay.setText("Actions on Selected Album: ");
        selectedAlbum = null;
        // Clear the existing album display
        photoGrid.getChildren().clear();
        currentColumn = 0;
        currentRow = 0;
        displayAlbums();
        Photos.serializeAdmin(Photos.admin);
   }

    /**
     * Handles the action of editing the name of an album.
     *
     * @param event The action event that triggered this method.
     */
   @FXML
    public void editName(ActionEvent event){
        if(editNameAlbum!= null){ //scenario: click edit name on one photo, did not enter, and clicked edit on another photo
            //change prev edit name to not editing state
            HBox nameBox = (HBox) lastNameTextField.getParent();
            Button enterButton = (Button) nameBox.getChildren().get(1);
            nameBox.getChildren().removeAll(lastNameTextField, enterButton);
            Label name = new Label(editNameAlbum.getName());
            
            nameBox.getChildren().addAll(name,makeEditButton());
            editNameAlbum = null;
        }
        Button editButton = (Button) event.getSource();
        HBox nameBox = (HBox) editButton.getParent();
        Label nameLabel = (Label) nameBox.getChildren().get(0);
        editNameAlbum = user.findAlbum(nameLabel.getText());
        nameBox.getChildren().removeAll(editButton, nameLabel);
        TextField enternameField = new TextField();
        enternameField.setPromptText("enter a name");
        enternameField.requestFocus();
        lastNameTextField = enternameField;
        Button enterButton = new Button("Enter");
        enterButton.setOnAction(e -> savename(e));
        nameBox.getChildren().addAll(enternameField, enterButton);
        
    }

    /**
     * Saves the new name of an album after editing.
     *
     * @param event The action event that triggered this method.
     */
    @FXML
    void savename(ActionEvent event){
        Button enterButton = (Button) event.getSource();
        HBox nameBox = (HBox) enterButton.getParent();
        TextField nameField = (TextField) nameBox.getChildren().get(0);
        String newName = nameField.getText();
        if(newName== null || newName.equals("")){
            showAlert("Invalid Request", "No name entered.");
        }
        else{
            boolean success = user.renameAlbum(editNameAlbum, nameField.getText());
            if(!success)
                showAlert("Invalid Request", "Album named "+newName+" already exists");
            else{
                nameBox.getChildren().removeAll(nameField, enterButton);
                Label name = new Label(editNameAlbum.getName());
                nameBox.getChildren().addAll(name,makeEditButton());
                editNameAlbum = null;
                lastNameTextField = null;
                Photos.serializeAdmin(Photos.admin);
        }
        }
    }

    /**
     * Adds a new row to the GridPane for displaying albums.
     *
     * @param rowIndex The index of the new row to be added.
     */
   private void addRowToGridPane(int rowIndex) {
        if (photoGrid.getRowConstraints().size() <= rowIndex) {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setMinHeight(300);
            rowConstraints.setPrefHeight(300);
            rowConstraints.setVgrow(javafx.scene.layout.Priority.ALWAYS); 
            photoGrid.getRowConstraints().add(rowConstraints);
        }
    }

    /**
     * Displays all albums of the current user.
     */
    public void displayAlbums(){
        for(Album a: user.getAlbums()){
            addAlbum(a);
        }
    }
    
    /**
     * Sets the search mode to 'AND' for tag searching.
     *
     * @param event The action event that triggered this method.
     */
    @FXML
    void andSearch(ActionEvent event) {
        andSearch = true;
        andButton.getStyleClass().add("selected");
        orButton.getStyleClass().remove("selected");
    }

    /**
     * Sets the search mode to 'OR' for tag searching.
     *
     * @param event The action event that triggered this method.
     */
    @FXML
    void orSearch(ActionEvent event) {
        andSearch = false;
        orButton.getStyleClass().add("selected");
        andButton.getStyleClass().remove("selected");
    }

    /**
     * Activates tag-based search mode.
     *
     * @param event The action event that triggered this method.
     */
    @FXML
    void tagSearch(ActionEvent event) {
        dateSearch = false;
        tagSearchButton.getStyleClass().add("selected");
        dateSearchButton.getStyleClass().remove("selected");
        searchTypeSelected = true;
    }

    /**
     * Activates date-based search mode.
     *
     * @param event The action event that triggered this method.
     */
    @FXML
    void dateSearch(ActionEvent event) {
        dateSearch = true;
        dateSearchButton.getStyleClass().add("selected");
        tagSearchButton.getStyleClass().remove("selected");
        searchTypeSelected = true;
    }


    /**
     * Initiates the search functionality based on the input criteria.
     *
     * @param event The action event that triggered this method.
     */
    @FXML
    void searchPhotos(ActionEvent event) {
        temporaryAlbum = new Album("Search Results");
        ArrayList<Album> userAlbums = user.getAlbums();
        if(!searchTypeSelected) {
            showAlert("Error", "Please Select Search Type");
            return;
        }
        // Date search
        if (dateSearch) {
            LocalDate startDate = null;
            LocalDate endDate = null;
            try {
                startDate = LocalDate.of(Integer.parseInt(startYear.getText()),
                startMonth.getSelectionModel().getSelectedIndex() + 1,
                startDay.getSelectionModel().getSelectedIndex() + 1);
                endDate = LocalDate.of(Integer.parseInt(endYear.getText()),
                endMonth.getSelectionModel().getSelectedIndex() + 1,
                endDay.getSelectionModel().getSelectedIndex() + 1);
                dateSearchButton.getStyleClass().remove("selected");
            } catch (NumberFormatException e) {
                showAlert("Invalid Input", "Please enter valid dates.");
                return;
            }

            for (Album album : userAlbums) {
                for (Photo photo : album.getPhotos()) {
                    if (isDateInRange(photo.getDate(), startDate, endDate)) {
                        temporaryAlbum.addPhoto(photo);
                    }
                }
            }
        } 
        // Tag search
        else {
            String tag1Type = tag1type.getSelectionModel().getSelectedItem();
            String tag1Value = tag1value.getText();
            String tag2Type = tag2type.getSelectionModel().getSelectedItem();
            String tag2Value = tag2value.getText();
    
            boolean tag1Filled = tag1Type != null && !tag1Type.isEmpty() && tag1Value != null && !tag1Value.isEmpty();
            boolean tag2Filled = tag2Type != null && !tag2Type.isEmpty() && tag2Value != null && !tag2Value.isEmpty();
            
            for (Album album : userAlbums) {
                for (Photo photo : album.getPhotos()) {
                    boolean matchesTag1 = tag1Filled && doesPhotoMatchSingleTag(photo, tag1Type, tag1Value);
                    boolean matchesTag2 = tag2Filled && doesPhotoMatchSingleTag(photo, tag2Type, tag2Value);
    
                    if (tag1Filled && tag2Filled) {
                        // If both tags are filled, apply AND/OR logic
                        if (andSearch ? (matchesTag1 && matchesTag2) : (matchesTag1 || matchesTag2)) {
                            temporaryAlbum.addPhoto(photo);
                        }
                    } else if (tag1Filled || tag2Filled) {
                        // If only one tag is filled
                        if (matchesTag1 || matchesTag2) {
                            temporaryAlbum.addPhoto(photo);
                        }
                    }
                }
            }
        }
        tagSearchButton.getStyleClass().remove("selected");
        andButton.getStyleClass().remove("selected");
        orButton.getStyleClass().remove("selected");
        displaySearchResults();
    }

    /**
     * Displays the search results in a new view.
     */
    private void displaySearchResults() {
        try {
            // Load the searchOutput.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/searchOutput.fxml"));
            Parent searchOutputRoot = loader.load();
    
            // Get the controller for the searchOutput.fxml file
            searchOutputController controller = loader.getController();
    
            // Initialize the controller with the search results and user information
            controller.initialize(temporaryAlbum, user);
    
            // Create a new stage for the search output view
            Stage searchOutputStage = new Stage();
            searchOutputStage.setTitle("Search Results");
    
            // Set the scene to the searchOutput FXML
            Scene searchOutputScene = new Scene(searchOutputRoot);
            searchOutputStage.setScene(searchOutputScene);
    
            // Show the search output stage
            searchOutputStage.show();
    
            // close the current stage
            Stage currentStage = (Stage) photoGrid.getScene().getWindow();
            currentStage.close();
    
        } catch (IOException e) {
            e.printStackTrace();
            // System.out.println("Error loading search results view");
        }
    }

    /**
     * Checks if a given date is within a specified date range.
     *
     * @param date The date to check.
     * @param startDate The start date of the range.
     * @param endDate The end date of the range.
     * @return True if the date is within the range, false otherwise.
     */
    private boolean isDateInRange(LocalDate date, LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) return true;
        return !(date.isBefore(startDate) || date.isAfter(endDate));
    }

    /**
     * Checks if a photo matches specified tag criteria.
     *
     * @param photo The photo to check.
     * @param tagType The type of tag to check for.
     * @param tagValue The value of the tag to check for.
     * @return True if the photo matches the tag criteria, false otherwise.
     */
    private boolean doesPhotoMatchSingleTag(Photo photo, String tagType, String tagValue) {
        if ((tagType == null || tagType.isEmpty()) || (tagValue == null || tagValue.isEmpty())) {
            // If either the type or value is empty, return false (no match).
            return false;
        } else {
            // Check if the photo has the tag.
            return photo.tagExists(tagType, tagValue);
        }
    }
    
    /**
     * Populates the ComboBoxes used for tag searching with available tag names.
     */
    private void populateTagComboboxes() {
        Set<String> tagNames = new HashSet<>();
        for (Album album : user.getAlbums()) {
            for (Photo photo : album.getPhotos()) {
                for (Tag tag : photo.getTags()) {
                    tagNames.add(tag.getName());
                }
            }
        }
        tag1type.getItems().addAll(tagNames);
        tag2type.getItems().addAll(tagNames);
    }

    /**
     * Populates the ComboBoxes used for date searching with dates.
     */
    private void populateDateComboboxes() {
        // Populating Month ComboBoxes
        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        startMonth.getItems().addAll(months);
        endMonth.getItems().addAll(months);
    
        // Populating Day ComboBoxes
        for (int day = 1; day <= 31; day++) {
            startDay.getItems().add(day);
            endDay.getItems().add(day);
        }
    }
    
    /**
     * Initializes the controller with the given user and populates the date and tag ComboBoxes.
     *
     * @param u The user to initialize with.
     */
    @FXML
    public void initialize(User u) {
        setUser(u);
        populateDateComboboxes();
        populateTagComboboxes();
    }
}
