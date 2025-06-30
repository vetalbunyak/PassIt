package vt.passit.Modules;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class HashData {
    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private HashData() {

    }

    public static String hashPassword(String pass) {
        if(pass == null || pass.isEmpty()) {
            throw new IllegalArgumentException("Пароль не може бути пустим або null.");
        }
        return passwordEncoder.encode(pass);
    }

    public static boolean verifyPassword(String pass, String encodedPass){
        if(pass == null || pass.isEmpty()){
            return false;
        }
        if(encodedPass == null || encodedPass.isEmpty()){
            return false;
        }
        return passwordEncoder.matches(pass,encodedPass);
    }
}
