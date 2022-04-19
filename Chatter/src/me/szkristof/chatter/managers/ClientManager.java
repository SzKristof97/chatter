package me.szkristof.chatter.managers;

import me.szkristof.chatter.models.User;

/**
 * ClientManager
 * Stores the {@link User}
 * and manage the save and load of the {@link User}
 */
public class ClientManager {

    //#region Propertes
    private User user;
    //#endregion

    //#region Constructors
    /**
     * Creates a new instance of the ClientManager class.
     */
    public ClientManager() {}

    //#endregion

    //#region Methods

    /**
     * Saves the {@link User} to the xml file.
     * @return True if the save was successful, false otherwise.
     */
    public boolean Save() {
        if(user == null || user.GetName() == null || user.GetName().isEmpty()) {
            return false;
        }

        boolean isSaved = XmlManager.SaveUser(user);
        return isSaved;
    }

    /**
     * Loads the {@link User} from the xml file.
     * @return true if the {@link User} is loaded, false otherwise.
     */
    public boolean Load() {
        User loadedUser = XmlManager.LoadUser();
        return loadedUser != null;
    }

    /**
     * Create a new {@link User} with the given parameters
     * @param name The name of the user
     * @return true if the {@link User} is created, false otherwise.
     */
    public boolean CreateUser(String name) {
        if(name == null || name.isEmpty()) {
            return false;
        }

        user = new User(name);
        return true;
    }

    /**
     * Gets the {@link User}
     * @return The {@link User}
     */
    public User GetUser() {
        return user;
    }
    //#endregion
}