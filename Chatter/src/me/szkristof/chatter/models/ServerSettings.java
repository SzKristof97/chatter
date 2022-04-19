package me.szkristof.chatter.models;

public class ServerSettings {

    //#region Propertes
    private String serverName;
    private String serverPort;
    //#endregion

    //#region Constructors
    /**
     * Creates a new instance of ServerSettings
     * @param serverName The server name
     * @param serverPort The server port
     */
    public ServerSettings(String serverName, String serverPort) {
        this.serverName = serverName;
        this.serverPort = serverPort;
    }

    //#endregion

    //#region Methods

    /**
     * Get the server name
     * @return {@link #serverName}
     */
    public String GetServerName() {
        return this.serverName;
    }

    /**
     * Get the server port
     * @return {@link #serverPort}
     */
    public String GetServerPort() {
        return this.serverPort;
    }
    
    //#endregion
}
