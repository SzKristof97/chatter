package enums;

public enum Permission {
    USER (0),
    MODERATOR (1),
    ADMIN (2);

    private int value;

    private Permission(int value) {
        this.value = value;
    }

    /**
     * Get the value of the permission
     * @return The value of the permission
     */
    public int getValue() {
        return value;
    }

    /**
     * Get the permission by the value
     * @param value The value of the permission
     * @return The permission
     */
    public static Permission getPermission(int value) {
        for(Permission p : Permission.values()) {
            if(p.getValue() == value) return p;
        }
        return null;
    }

    /**
     * Get the permission by the name
     * @param name The name of the permission
     * @return The permission
     */
    public static Permission getPermission(String name) {
        for(Permission p : Permission.values()) {
            if(p.name().equalsIgnoreCase(name)) return p;
        }
        return null;
    }

    /**
     * Check if the permission is greater or equal to the given permission
     * @param permission The permission to check
     * @return True if the permission is greater or equal to the given permission, false if it is not
     */
    public boolean isGreaterOrEqual(Permission permission) {
        return this.getValue() >= permission.getValue();
    }

}
