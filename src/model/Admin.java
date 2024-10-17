
/**
 * @author Kelvin Bian 
 * @author Jessica Luo
 */
package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * The Admin class represents the administrator of the application and manages user-related operations.
 * It implements the Serializable interface for object serialization.
 */
public class Admin implements Serializable {

    /**
     * HashMap to store users, where the key is the username and the value is the corresponding User object.
     */
    private HashMap<String, User> users;

    /**
     * Constructs a new Admin object with an empty user HashMap.
     */
    public Admin() {
        users = new HashMap<>();
    }

    /**
     * Retrieves a User object based on the provided username.
     *
     * @param n The username of the User to retrieve.
     * @return The User object corresponding to the username, or null if not found.
     */
    public User getUser(String n) {
        return users.get(n);
    }

    /**
     * Retrieves a list of all User objects managed by the Admin.
     *
     * @return An ArrayList containing all User objects.
     */
    public ArrayList<User> getUsers() {
        ArrayList<User> usersList = new ArrayList<>(users.values());
        // Uncomment the following loop for debugging or logging purposes
        // for(User u: usersList)
        //     System.out.println(u);
        return usersList;
    }

    /**
     * Adds a User object to the Admin's list of users.
     *
     * @param u The User object to be added.
     * @return true if the User was added successfully, false if the username already exists.
     */
    public boolean addUser(User u) {
        String n = u.getName();
        if (users.get(n) != null) return false;
        users.put(n, u);
        return true;
    }

    /**
     * Adds a new User with the given username to the Admin's list of users.
     *
     * @param n The username of the new User.
     * @return true if the User was added successfully, false if the username already exists.
     */
    public boolean addUser(String n) {
        if (users.get(n) != null) return false;
        User u = new User(n);
        users.put(n, u);
        return true;
    }

    /**
     * Deletes a User with the specified username from the Admin's list of users.
     *
     * @param n The username of the User to be deleted.
     */
    public void deleteUser(String n) {
        users.remove(n);
    }
}
