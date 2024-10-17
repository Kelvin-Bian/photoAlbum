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
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.User;

/**
 * Controller class for handling the login functionality in a photo management application.
 * This class manages user authentication and redirection to the appropriate view based on the user type.
 *
 * @author Kelvin Bian
 * @author Jessica Luo
 */

public class loginController extends Controller{
    @FXML
    private Button quitButton;

    @FXML 
    private Button enterButton;

    @FXML
    private TextField username;


    /**
     * Handles the 'Quit' action. Closes the current stage.
     *
     * @param event The action event that triggered this method.
     */
    @FXML
    void quit(ActionEvent event){
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }

    /**
     * Processes the entered username and navigates to the corresponding user interface.
     * If the username is 'admin', navigates to the admin view. Otherwise, checks for the
     * existence of the user and navigates to the user's home view if the user exists.
     *
     * @param event The action event that triggered this method.
     */
    @FXML
    void enterUsername(ActionEvent event){
        String name = username.getText();
        String path = "fxml/";
        boolean user=false , admin = false, stock = false;
        if(name.equals("admin")){
            path+="admin.fxml";
            admin = true;
            try { 
                    FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
                    Parent root = loader.load();
                    Controller c = loader.getController();
                    ((adminController) c).displayUsers();
          
                    Stage homeStage = new Stage();
                    homeStage.setTitle("Home");

                    Scene homeScene = new Scene(root);
                    homeStage.setScene(homeScene);

           
                    homeStage.show();
                    Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    currentStage.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    
                }
        }
        else{
            User u = Photos.admin.getUser(name);
            if(u!= null){
                    path+="home.fxml";
                    user = true;
                try { 
                    FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
                    Parent root = loader.load();
                    Controller c = loader.getController();
                    if (user || stock){
                        ((homeController)c).initialize(u);
                    }

                    Stage homeStage = new Stage();
                    homeStage.setTitle("Home");

                    Scene homeScene = new Scene(root);
                    homeStage.setScene(homeScene);

                    homeStage.show();
                    Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    currentStage.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    
                }
            }
            else{
                showAlert("Invalid Username", "Please enter a valid username.");
            }
        }
        
    }
       
}
