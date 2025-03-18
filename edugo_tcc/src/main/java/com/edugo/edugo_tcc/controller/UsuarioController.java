package com.edugo.edugo_tcc.controller;

import com.edugo.edugo_tcc.dto.UsuarioDTO;
import com.edugo.edugo_tcc.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * Método responsável por buscar um usuário por cpf
     * 
     * @param cpf
     * @return UsuarioDTO
     */
    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<UsuarioDTO> buscarUsuarioPorCpf(@PathVariable String cpf) {
        Optional<UsuarioDTO> usuarioDTO = usuarioService.buscarUsuarioPorCpf(cpf);
        return usuarioDTO
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }
}