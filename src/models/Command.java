package models;

import enums.Permission;
import interfaces.ICommand;

/**
 * The Command class is used to represent a command.
 */
public abstract class Command implements ICommand{

    protected String name;
    protected String description;
    protected Permission requiredPermission;
    
    /**
     * Create a new instance of Command
     * @param name The name of the command
     * @param description The description of the command
     * @param requiredPermission The required permission to use the command
     */
    public Command(String name, String description, Permission requiredPermission) {
        this.name = name;
        this.description = description;
        this.requiredPermission = requiredPermission;
    }

    /**
     * Execute the command
     * @param whoCalled The user who called the command
     * @param args The arguments of the command
     * @return The result of the command
     */
    public abstract String execute(User whoCalled, String[] args);

    /**
     * Get the name of the command
     * @return The name of the command
     */
    public String getName() {
        return name;
    }

    /**
     * Get the description of the command
     * @return The description of the command
     */
    public String getDescription() {
        return description;
    }

    /**
     * Get the required permission to use the command
     * @return The required permission to use the command
     */
    public Permission getRequiredPermission() {
        return requiredPermission;
    }
}