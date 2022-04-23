package commands;

import enums.Permission;
import models.Command;
import models.User;

public class ClearCommand extends Command {

    public ClearCommand() {
        super("clear", "Clear the console", Permission.USER);
    }

    @Override
    public String execute(User whoCalled, String[] args) {
        if(whoCalled == null) {
            return "You must be logged in to use this command";
        }

        if(!whoCalled.getPermission().isGreaterOrEqual(getRequiredPermission())) {
            return "You do not have permission to use this command";
        }

        return "cmd:clear";
    }

}
