package me.szkristof.chatter.models;

public class User {

    //#region Properties
    private String username;
    //#endregion

    //#region Constructors
    public User(String username) {
        this.username = username;
    }

    //#endregion

    //#region Methods
    /**
     * Get the username
     * @return {@link #username}
     */
    public String GetName(){
        return this.username;
    }

    /**
     * Setting the username
     * @param value The username
     * @return true if the {@link #username} is successfully set, false otherwise
     */
    public boolean SetName(String value){
        if (value == null || value.length() == 0)
            return false;
        
        this.username = value;
        return true;
    }

    //#endregion
}
