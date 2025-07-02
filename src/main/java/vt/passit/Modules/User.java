package vt.passit.Modules;

import vt.passit.Role;

import java.sql.Timestamp;

public class User {
    private int id;
    private String username;
    private String passwordHash;
    private String email;
    private Role role;
    private Timestamp createdAt;

    public User(int id, String username, String passwordHash, String email, Role role, Timestamp createdAt) { // <-- Приймає Role enum
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;
        this.role = role;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
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
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                ", createdAt=" + createdAt +
                '}';
    }
}
