package managers;

import java.util.ArrayList;
import java.util.List;

import interfaces.ICommand;
import models.User;

/**
 * The CommandManager class is used to manage all the commands.
 */
public class CommandManager {

    private static CommandManager instance;
    public static CommandManager getInstance() {
        if(instance == null) {
            instance = new CommandManager();
        }
        return instance;
    }

    private List<ICommand> commands;

    /**
     * Create the CommandManager.
     */
    private CommandManager() {
        commands = new ArrayList<ICommand>();
    }
    
    /**
     * Register commnad
     * @param command The command to register
     * @return True if the command was registered, false otherwise
     */
    public boolean registerCommand(ICommand command) {
        if(command == null) {
            return false;
        }
        if(commands.contains(command)) {
            return false;
        }
        commands.add(command);
        return true;
    }

    /**
     * Unregister command
     * @param command The command to unregister
     * @return True if the command was unregistered, false otherwise
     */
    public boolean unregisterCommand(ICommand command) {
        if(command == null) {
            return false;
        }
        if(!commands.contains(command)) {
            return false;
        }
        commands.remove(command);
        return true;
    }

    /**
     * Execute command
     * @param whoCalled The user who called the command
     * @param command The command to execute
     * @param args The arguments of the command
     * @return null if the commmand was not executed, otherwise the result of the command
     */
    public String executeCommand(User user, String command, String[] args) {
        if(user == null) {
            return null;
        }

        if(command == null || command.isEmpty()) {
            return null;
        }

        for(ICommand c : commands) {
            if(c.getName().equalsIgnoreCase(command.toLowerCase().trim())) {
                return c.execute(user, args);
            }
        }
        return null;
    }
}
