package vt.passit.Modules;

import vt.passit.Role;

public class SessionManager {
    private static SessionManager instance;
    private User currentUser;

    private SessionManager() {

    }

    public static synchronized SessionManager getInstance() {
        if(instance == null){
            instance = new SessionManager();
        }
        return instance;

    }

    public void login(User user){
        this.currentUser = user;
        System.out.println("Користувач '" + user.getName() + "' авторизований. Роль: " + user.getRole());
    }

    public void logout() {
        if (currentUser != null) {
            System.out.println("Користувач '" + currentUser.getName() + "' вийшов з системи.");
        }
        this.currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public boolean isAdmin() {
        return isLoggedIn() && currentUser.getRole() == Role.SUPERVISOR;
    }
    public boolean isUser() {
        return isLoggedIn() && currentUser.getRole() == Role.USER;
    }
}
