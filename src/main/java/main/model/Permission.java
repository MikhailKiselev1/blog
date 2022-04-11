package main.model;

public enum Permission {

    USER("user:write"),
    MODERATE("user:moderate");

    private final String peremission;

    Permission(String peremission) {
        this.peremission = peremission;
    }

    public String getPeremission() {
        return peremission;
    }

}
