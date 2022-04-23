package commands;

import enums.Permission;
import models.Command;
import models.User;
import server.ClientHandler;

public class PoPCommand extends Command{

    public PoPCommand() {
        super("pop", "Show the player list!", Permission.USER);
    }

    @Override
    public String execute(User whoCalled, String[] args) {
        if(!whoCalled.getPermission().isGreaterOrEqual(getRequiredPermission())){
            return "You don't have the permission to use this command";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Players list ("+ClientHandler.clients.size()+"): ");
        for (ClientHandler client : ClientHandler.clients) {
            sb.append("("+client.user.getId()+")["+client.user.getPermission() + "]" + client.user.getUsername()).append(" ");
        }
        return sb.toString();
    }
    
}
