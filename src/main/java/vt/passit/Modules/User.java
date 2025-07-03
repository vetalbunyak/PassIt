package vt.passit.Modules;

import vt.passit.Role;

import java.sql.Timestamp;

public class User {
    private int id;
    private String name;
    private String last_name;
    private String image_url;
    private String passwordHash;
    private String email;
    private Role role;
    private Timestamp createdAt;

    public User(int id, String name, String passwordHash, String email, String last_name, String image_url, Role role, Timestamp createdAt) {
        this.id = id;
        this.name = name;
        this.passwordHash = passwordHash;
        this.email = email;
        this.role = role;
        this.last_name = last_name;
        this.image_url = image_url;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getEmail() {
        return email;
    }

    public Role getRole() {
        return role;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public boolean isSupervisor() {
        return this.role == Role.SUPERVISOR;
    }

    public boolean isUser() {
        return this.role == Role.USER;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                ", createdAt=" + createdAt +
                '}';
    }
}
