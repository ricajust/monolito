package com.edugo.edugo_tcc.controller.auth;

import com.edugo.edugo_tcc.dto.LoginRequestDTO;
import com.edugo.edugo_tcc.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController; // Certifique-se que esta anotação está presente

@RestController
@RequestMapping("/auth")
public class AutenticacaoController {

    private static final Logger logger = LoggerFactory.getLogger(AutenticacaoController.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        logger.info("Tentativa de login para o usuário: {}", loginRequestDTO.getEmail());
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(), loginRequestDTO.getSenha())
            );
            logger.info("Resultado da autenticação: isAuthenticated={}", authentication.isAuthenticated());
            if (authentication.isAuthenticated()) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequestDTO.getEmail());
                String token = jwtUtil.generateToken(userDetails);
                return ResponseEntity.ok(token);
            } else {
                return ResponseEntity.badRequest().body("Credenciais inválidas");
            }
        } catch (Exception e) {
            logger.error("Erro durante a autenticação para o usuário: {}", loginRequestDTO.getEmail(), e);
            return ResponseEntity.status(401).body("Erro de autenticação");
        }
    }
}