package models;

import enums.Permission;

public class User {

    //#region Properties
    private int id;
    private String username;
    private Permission permission;
    //#endregion

    //#region Constructors
    /**
     * Create a new instance of User
     * @param username The username of the user
     * @param permission The permission of the user
     */
    public User(int id, String username, Permission permission) {
        this.id = id;
        this.username = username;
        this.permission = permission;
    }

    /**
     * Create a new instance of User
     * The default Permission is set to USER
     * @param username The username of the user
     */
    public User(int id, String username) {
        this(id, username, Permission.USER);
    }
    //#endregion

    //#region Methods
    /**
     * Get the username of the user
     * @return The username of the user
     */
    public String getUsername() {
        return username;
    }

    /**
     * Set the username of the user
     * @param username The new username of the user
     * @return True if the username was set, false if it was not
     */
    public boolean setUsername(String username) {
        if(username == null || username.isEmpty()) return false;

        this.username = username;
        return true;
    }
    
    /**
     * Get the permission of the user
     * @return The permission of the user
     */
    public Permission getPermission() {
        return permission;
    }

    /**
     * Sets the permission of the user
     * @param permission The permission to set
     * @return True if the permission was set, false if it was not
     */
    public boolean setPermission(Permission permission) {
        if(permission == null || this.permission == permission) return false;

        this.permission = permission;
        return true;
    }
    
    /**
     * Get the id of the user
     */
    public int getId() {
        return id;
    }
    //#endregion
}
