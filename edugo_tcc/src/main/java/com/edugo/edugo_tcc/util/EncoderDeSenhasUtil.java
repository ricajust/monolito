package com.edugo.edugo_tcc.util;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class EncoderDeSenhasUtil {
    public static void main(String[] args) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode("123");
        System.out.println("Senha criptografada: " + encodedPassword);
        // Use este valor para atualizar a senha no seu banco de dados
    }
}
