
/**
 * @author Kelvin Bian 
 * @author Jessica Luo
 */

package app;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Admin;
import model.User;
import view.Controller;

/**
 * The main class representing the Photos application, which extends the JavaFX Application.
 * This class initializes the application, manages the Admin object, and handles serialization.
 */
public class Photos extends Application implements Serializable {

    /**
     * The Admin instance representing the administrator of the application.
     */
    public static Admin admin;


    /**
     * The filename for serialized Admin data.
     */
    private static final String ADMIN_FILE = "admin.ser";

    /**
     * The main entry point for the application.
     *
     * @param primaryStage The primary stage for the application.
     * @throws Exception If an exception occurs during application startup.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        admin = deserializeAdmin();
        if (admin == null || admin.getUsers().size() == 0) {
            admin = new Admin();
            addUsers();
        }
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/fxml/login.fxml"));
            Parent root = loader.load();
            primaryStage.setTitle("Photos");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * The method called when the application is closing. It serializes the Admin object.
     *
     * @throws Exception If an exception occurs during application shutdown.
     */
    @Override
    public void stop() throws Exception {
        // Serialize Admin object when the application is closing
        serializeAdmin(admin);
        super.stop();
    }

    /**
     * Serializes the Admin object and writes it to a file.
     *
     * @param admin The Admin object to be serialized.
     */
    public static void serializeAdmin(Admin admin) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(ADMIN_FILE))) {
            outputStream.writeObject(admin);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deserializes the Admin object from a file.
     *
     * @return The deserialized Admin object.
     */
    private Admin deserializeAdmin() {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(ADMIN_FILE))) {
            return (Admin) inputStream.readObject();
        } catch (Exception e) {
            // If there's an error (e.g., file not found), return a new Admin
            return new Admin();
        }
    }

    /**
     * Serializes a list of users and writes it to a file.
     *
     * @param users    The list of users to be serialized.
     * @param filename The name of the file to write to.
     */
    public static void serializeUsers(List<User> users, String filename) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(filename))) {
            // Serialize the list of users
            outputStream.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds users to the Admin object. (Currently commented out for demonstration purposes.)
     */
    public void addUsers() {
        // for(int i = 1; i< 3; i++){
        //     boolean success = admin.addUser(Integer.toString(i));
        //     if(!success) System.out.println("failed to add user");
        //     else{
        //         System.out.println("added user");
        //     }
        // }
        admin.addUser("stock");
    }

    /**
     * The main method, which launches the application.
     *
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }

}
