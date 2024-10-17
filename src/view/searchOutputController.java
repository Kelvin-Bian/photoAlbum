
package view;

import java.io.IOException;

import app.Photos;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Album;
import model.Photo;
import model.User;
/**
 * The searchOutputController class is a controller for displaying search results in the photo management system.
 * It handles the user interface for displaying a grid of photos, allowing users to open photos, create albums, and navigate to the home screen.
 * 
 * The controller is associated with an FXML file that defines the layout of the search results screen.
 * It includes buttons for creating albums, navigating home, and logging out. Additionally, it displays a grid of photos with relevant information.
 * 
 * The displayPhotos method populates the photo grid with photos from the associated album.
 * 
 * The addPhotoToGrid method adds a single photo to the grid, including its image, date, caption, and an "Open" button.
 * 
 * The initialize method initializes the controller with the provided album and user information, displaying the photos on the screen.
 * 
 * The createAlbumFromResults method is triggered when the user wants to create a new album from the search results.
 * It adds the selected album to the user's albums and renames it if necessary.
 * 
 * The home method navigates the user to the home screen.
 * 
 * The openPhoto method is triggered when the user wants to open a specific photo. It creates a new stage for displaying the photo and information.
 * 
 * @author Jessica Luo 
 * @author Kelvin Bian
 */
public class searchOutputController extends Controller {

    @FXML // fx:id="caption"
    private Label caption; // Value injected by FXMLLoader

    @FXML // fx:id="createAlbumButton"
    private Button createAlbumButton; // Value injected by FXMLLoader

    @FXML // fx:id="homeButton"
    private Button homeButton; // Value injected by FXMLLoader

    @FXML // fx:id="logoutButton"
    private Button logoutButton; // Value injected by FXMLLoader

    @FXML // fx:id="openPhotoButton"
    private Button openPhotoButton; // Value injected by FXMLLoader

    @FXML // fx:id="photoGrid"
    private GridPane photoGrid; // Value injected by FXMLLoader

    @FXML // fx:id="quitButton"
    private Button quitButton; // Value injected by FXMLLoader

    @FXML // fx:id="nameOfAlbum"
    private TextField nameOfAlbum; // Value injected by FXMLLoader

    private Album album;

    private User user;

    // for populating photo grid
    private int currentColumn = 0;
    private int currentRow = 0;
    private final int maxColumns = 3;

    /**
     * Creates a new album from the search results. Adds the selected album to the user's albums and renames it if necessary.
     * Serializes the updated admin data and navigates to the home screen.
     *
     * @param event The ActionEvent triggered by the user's action.
     */
    @FXML
    void createAlbumFromResults(ActionEvent event) {
        // System.out.println("going to add new album");
        user.addAlbum(album);
        if(nameOfAlbum.getText() == null || nameOfAlbum.getText().equals("")) {
            showAlert("Invalid Request", "No name entered.");
        }
        else if (!user.renameAlbum(album, nameOfAlbum.getText())) {
            showAlert("Invalid Request", "Album named " + nameOfAlbum.getText() + " already exists");
            user.deleteAlbum(album.getName());
        } else {
            Photos.serializeAdmin(Photos.admin);
            home(event);
        }
    }

    /**
     * Navigates the user to the home screen.
     *
     * @param event The ActionEvent triggered by the user's action.
     */
    @FXML
    void home(ActionEvent event) {
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
        try {
            Stage primaryStage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/fxml/home.fxml"));
            Parent root = loader.load();
            homeController c = loader.getController();
            c.initialize(user);
            primaryStage.setTitle("Home");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Displays photos on the screen by populating the photo grid with photos from the associated album.
     */
    public void displayPhotos() {
        for (Photo p : album.getPhotos()) {
            addPhotoToGrid(p);
        }
    }

    /**
     * Adds a single photo to the grid, including its image, date, caption, and an "Open" button.
     *
     * @param photo The Photo instance to be added to the grid.
     */
    void addPhotoToGrid(Photo photo) {
        ImageView imageView = new ImageView();
        imageView.setPickOnBounds(true);
        imageView.setPreserveRatio(true);
        imageView.setImage(photo.getImage());
        imageView.setFitHeight(85);
        imageView.setFitWidth(230);

        Label dateLabel = new Label(photo.getDate().toString());

        Button selectButton = new Button("Open");
        selectButton.setId(photo.getPath());
        selectButton.getStyleClass().add("photo-grid-button");
        selectButton.setOnAction(event -> openPhoto(event));

        HBox captionBox = new HBox();
        String captionstr = photo.getCaption();
        VBox vbox;
        Label captionLabel = new Label();
        if (captionstr != null) {
            captionLabel.setText(captionstr);
            captionLabel.getStyleClass().add("label-on-bg");
            captionLabel.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        } else {
            captionLabel.setText("no caption");
            captionLabel.getStyleClass().add("no-caption");
        }
        captionBox.getChildren().addAll(captionLabel);
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
     * Adds a new row to the grid pane if it does not already exist.
     *
     * @param rowIndex The index of the row to be added.
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
     * Retrieves the associated album.
     *
     * @return The Album instance representing the associated album.
     */
    public Album getAlbum() {
        return album;
    }

    /**
     * Retrieves the associated user.
     *
     * @return The User instance representing the associated user.
     */
    public User getUser() {
        return user;
    }

    /**
     * Opens a specific photo. Creates a new stage for displaying the photo and information using the simplePhotoViewer.
     *
     * @param event The ActionEvent triggered by the user's action.
     */
    @FXML
    void openPhoto(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/simplePhotoViewer.fxml"));
            Parent root = loader.load();

            simplePhotoViewerController photoViewerController = loader.getController();
            photoViewerController.initialize(album.findPhoto(((Button) event.getSource()).getId()));
            photoViewerController.setMainWindow(this);

            // Create a new stage for displaying the photo and information
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Photo Display");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initializes the controller with the provided album and user information, displaying the photos on the screen.
     *
     * @param a The Album instance associated with the search results.
     * @param u The User instance associated with the search results.
     */
    public void initialize(Album a, User u) {
        user = u;
        album = a;
        displayPhotos();
    }
}
