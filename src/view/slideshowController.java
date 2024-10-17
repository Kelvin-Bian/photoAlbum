
package view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import model.Album;
import model.Photo;
/**
 * The slideshowController class is a controller for the slideshow view in the photo management system.
 * It handles the display of photos, captions, and date information in a slideshow format.
 * 
 * This controller is associated with an FXML file that defines the layout of the slideshow view.
 * It includes buttons for navigating to the next and previous photos, labels for displaying captions and dates,
 * and an image view for displaying the photos.
 * 
 * The controller keeps track of the current album and photo index to manage the slideshow functionality.
 * It provides methods for updating the displayed photo, handling next and previous photo actions,
 * and setting the current album for the slideshow.
 * 
 * @author Jessica Luo
 * @author Kelvin Bian
 */
public class slideshowController {

    @FXML
    private Label caption;

    @FXML
    private Label dateDisplay;

    @FXML
    private ImageView image;

    @FXML
    private Button nextButton;

    @FXML
    private Button previousButton;

    private Album a;

    private int photoIndex;

    /**
     * Handles the action event when the "Next" button is clicked.
     * Updates the photo index and displays the next photo in the slideshow.
     *
     * @param event The ActionEvent triggered by clicking the "Next" button.
     */
    @FXML
    void nextPhoto(ActionEvent event) {
        photoIndex = (photoIndex + 1) % a.getPhotos().size();
        displayPhoto(a.getPhotos().get(photoIndex));
    }

    /**
     * Handles the action event when the "Previous" button is clicked.
     * Updates the photo index and displays the previous photo in the slideshow.
     *
     * @param event The ActionEvent triggered by clicking the "Previous" button.
     */
    @FXML
    void prevPhoto(ActionEvent event) {
        if (photoIndex == 0) {
            photoIndex = a.getPhotos().size() - 1;
        } else
            photoIndex--;
        displayPhoto(a.getPhotos().get(photoIndex));
    }

    /**
     * Sets the current album for the slideshow and initializes the photo index.
     * Displays the first photo in the album.
     *
     * @param a The Album object representing the current album.
     */
    void setAlbum(Album a) {
        this.a = a;
        photoIndex = 0;
        displayPhoto(a.getPhotos().get(photoIndex));
        if (a.getPhotos().size() == 1) {
            nextButton.setDisable(true);
            previousButton.setDisable(true);
            nextButton.setVisible(false);
            previousButton.setVisible(false);
        } else {
            nextButton.setDisable(false);
            previousButton.setDisable(false);
            nextButton.setVisible(true);
            previousButton.setVisible(true);
        }
    }

    /**
     * Displays the provided photo in the slideshow view.
     * Updates the image, caption, and date information based on the photo.
     *
     * @param p The Photo object to be displayed.
     */
    void displayPhoto(Photo p) {
        image.setImage(p.getImage());
        image.setPreserveRatio(true);
        String c = p.getCaption();
        if (c != null && !c.isEmpty()) {
            caption.setText(c);
            caption.setVisible(true);
        } else {
            caption.setVisible(false);
        }
        dateDisplay.setText(p.getDate().toString());
    }
}
