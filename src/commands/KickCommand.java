package commands;

import enums.Permission;
import models.Command;
import models.User;
import server.ClientHandler;

public class KickCommand extends Command{

    public KickCommand() {
        super("kick", "kick the selected user", Permission.MODERATOR);
    }

    @Override
    public String execute(User whoCalled, String[] args) {
        if(args.length < 1) {
            return "Usage: /kick <userId>";
        }

        if(!whoCalled.getPermission().isGreaterOrEqual(getRequiredPermission())){
            return "You don't have the permission to use this command";
        }

        
        int userId; 
        try{
            userId = Integer.parseInt(args[0]);

            if(userId < 0) {
                return "Invalid userId";
            }
        }catch(NumberFormatException e){
            return "The userId must be a number";
        }

        User user; 
        
        try{
            user = ClientHandler.clients.get(userId).user;
        }catch(Exception e){
            return "Invalid userId";
        }

        //if(user.getPermission().isGreaterOrEqual(whoCalled.getPermission())) {
        //    return "You can't kick a moderator or admin!";
        //}

        if(user.getId() == whoCalled.getId()) {
            return "You can't kick yourself!";
        }

        ClientHandler.stop(userId);
        return "Successfully kicked " + user.getUsername();
    }
}
