package interfaces;

import enums.Permission;
import models.User;

public interface ICommand {
    public String getName();
    public Permission getRequiredPermission();
    public String getDescription();
    public String execute(User whoCalled, String[] args);
}
