package ilya.shin.auth_microservice.api.model;

public enum Role {
    CUSTOMER("customer"),
    CHEF("chef"),
    MANAGER("manager");

    private final String role;
    Role(String s) {
        role = s;
    }

    public String getRole() {
        return role;
    }
}
