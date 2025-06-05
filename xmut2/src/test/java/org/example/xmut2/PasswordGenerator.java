package org.example.xmut2;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "27545";
        String encoded = encoder.encode(rawPassword);
        System.out.println("加密后的密码: " + encoded);
    }
}

