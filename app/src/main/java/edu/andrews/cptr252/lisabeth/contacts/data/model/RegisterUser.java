package edu.andrews.cptr252.lisabeth.contacts.data.model;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class RegisterUser {

    private String userId;
    private String displayName;

    public RegisterUser(String userId, String displayName) {
        this.userId = userId;
        this.displayName = displayName;
    }

    public String getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }
}