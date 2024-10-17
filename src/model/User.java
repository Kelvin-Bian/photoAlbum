/**
 * The User class represents a user in the photo management system, storing albums and related functionality.
 * It implements the Serializable interface for object serialization.
 * 
 * Each user has a name and a HashMap of albums.
 * The class provides methods for retrieving information about the user, such as their name, albums, and specific album references.
 * Methods for adding, deleting, and updating albums, as well as searching for photos within albums, are also available.
 * 
 * Users are initially created with a default "stock" album containing predefined photos.
 * 
 * @author Jessica Luo 
 * @author Kelvin Bian
 */
package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/**
 * The User class represents a user in the photo management system, storing albums and related functionality.
 * It implements the Serializable interface for object serialization.
 * 
 * Each user has a name and a HashMap of albums.
 * The class provides methods for retrieving information about the user, such as their name, albums, and specific album references.
 * Methods for adding, deleting, and updating albums, as well as searching for photos within albums, are also available.
 * 
 * Users are initially created with a default "stock" album containing predefined photos.
 *
 */
public class User implements Serializable {
    private HashMap<String, Album> albums;
    private String name;

    /**
     * Constructs a User object with the given name.
     * Initializes the albums HashMap 
     *
     * @param name The name of the user.
     */
    public User(String name) {
        this.name = name;
        albums = new HashMap<>();
        if (name.equals("stock")) {
            Album a = new Album("stock");
            a.addPhoto(new Photo("data/saitama.gif"));
            a.addPhoto(new Photo("data/violet.jpeg"));
            a.addPhoto(new Photo("data/starry sky.jpg"));
            a.addPhoto(new Photo("data/greenland.bmp"));
            a.addPhoto(new Photo("data/final flyer 1.png"));
            addAlbum(a);
        }
    }

    /**
     * Retrieves the name of the user.
     *
     * @return The name of the user.
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves a list of albums associated with the user.
     *
     * @return An ArrayList containing the user's albums.
     */
    public ArrayList<Album> getAlbums() {
        return new ArrayList<>(albums.values());
    }

    /**
     * Finds and retrieves a specific album by name.
     *
     * @param n The name of the album to be found.
     * @return The Album object if found, null otherwise.
     */
    public Album findAlbum(String n) {
        return albums.get(n);
    }

    /**
     * Adds a new album with the given name to the user's albums.
     *
     * @param n The name of the new album.
     * @return true if the album was added successfully, false if an album with the same name already exists.
     */
    public boolean addAlbum(String n) {
        if (albums.get(n) != null) return false;
        albums.put(n, new Album(n));
        return true;
    }

    /**
     * Deletes the album with the given name from the user's albums.
     *
     * @param n The name of the album to be deleted.
     * @return true if the album was deleted successfully, false if the album does not exist.
     */
    public boolean deleteAlbum(String n) {
        if (albums.get(n) == null) return false;
        albums.remove(n);
        return true;
    }

    /**
     * Updates the metadata of photos with the same path in all albums.
     *
     * @param photo The Photo object containing updated metadata.
     */
    public void updateSamePhotoRefs(Photo photo) {
        for (Photo p : searchPhoto(photo.getPath())) {
            p.setCaption(photo.getCaption());
            p.setTags(photo.getTags());
        }
    }

    /**
     * Adds a new album to the user's albums.
     *
     * @param a The Album object to be added.
     */
    public void addAlbum(Album a) {
        albums.put(a.getName(), a);
    }

    /**
     * Updates the metadata of a new photo by copying attributes from an existing photo with the same path.
     *
     * @param p The new Photo object to be updated.
     */
    public void updateNewPhoto(Photo p) {
        Photo photo = searchPhoto(p.getPath()).get(0);
        if (photo != null) {
            p.setCaption(photo.getCaption());
            p.setTags(photo.getTags());
        }
    }

    /**
     * Searches for a photo with a specific path in all albums.
     *
     * @param path The path of the photo to be searched.
     * @return An ArrayList containing all instances of the photo found in different albums.
     */
    public ArrayList<Photo> searchPhoto(String path) {
        ArrayList<Photo> result = new ArrayList<Photo>();
        Photo foundPhoto;
        List<Album> allAlbums = new ArrayList<>(albums.values());
        for (Album a : allAlbums) {
            foundPhoto = a.findPhoto(path);
            if (foundPhoto != null) result.add(foundPhoto);
            foundPhoto = null;
        }
        return result;
    }

    /**
     * Searches for a photo with a specific path in a given album.
     *
     * @param album The Album object in which to search for the photo.
     * @param path  The path of the photo to be searched.
     * @return The Photo object if found, null otherwise.
     */
    public Photo searchPhoto(Album album, String path) {
        ArrayList<Photo> albumPhotos = album.getPhotos();
        for (int i = 0; i < albumPhotos.size(); i++) {
            if (path.equals(albumPhotos.get(i).getPath())) {
                return albumPhotos.get(i);
            }
        }
        return null;
    }

    /**
     * Renames an album with a new name.
     *
     * @param a The Album object to be renamed.
     * @param n The new name for the album.
     * @return true if the album was renamed successfully, false if an album with the new name already exists.
     */
    public boolean renameAlbum(Album a, String n) {
        if (findAlbum(n) != null) return false;
        String oldName = a.getName();
        albums.get(oldName).setName(n);
        albums.remove(oldName);
        albums.put(n, a);
        return true;
    }

    /**
     * Provides a string representation of the user.
     *
     * @return The name of the user.
     */
    public String toString() {
        return name;
    }

    /**
     * Checks if two User objects are equal by comparing their names.
     *
     * @param obj The object to compare with.
     * @return true if the objects are equal, false otherwise.
     */
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        User u = (User) obj;
        return u.getName().equals(name);
    }
}
