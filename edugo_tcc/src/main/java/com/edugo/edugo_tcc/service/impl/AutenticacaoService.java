package com.edugo.edugo_tcc.service.impl;

import com.edugo.edugo_tcc.model.Usuario;
import com.edugo.edugo_tcc.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service("autenticacaoService") // Dê um nome específico ao bean para evitar conflitos
public class AutenticacaoService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o email: " + email));

        // Aqui você pode adicionar lógica para buscar as roles do usuário se tiver um sistema de roles
        // Por enquanto, estamos criando um UserDetails com apenas o usuário, senha e uma lista vazia de authorities
        return new User(usuario.getEmail(), usuario.getSenha(), new ArrayList<>());
    }
}