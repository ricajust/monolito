package com.edugo.edugo_tcc.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edugo.edugo_tcc.dto.UsuarioDTO;
import com.edugo.edugo_tcc.repository.UsuarioRepository;
import com.edugo.edugo_tcc.service.UsuarioService;
import com.edugo.edugo_tcc.util.ConversorGenericoDTO;

@Service
public class UsuarioServiceImpl implements UsuarioService{
    private final UsuarioRepository usuarioRepository;
    private final ConversorGenericoDTO conversorGenericoDTO;

    @Autowired
    public UsuarioServiceImpl(
        UsuarioRepository usuarioRepository, 
        ConversorGenericoDTO conversorGenericoDTO) 
        {
            this.usuarioRepository = usuarioRepository;
            this.conversorGenericoDTO = conversorGenericoDTO;
        }

    /**
     * Método responsável por buscar usuário por cpf
     * 
     * @param cpf
     * @return UsuarioDTO 
     */
    @Override
    public Optional<UsuarioDTO> buscarUsuarioPorCpf(String cpf) {
        return usuarioRepository
            .findByCpf(cpf)
            .map(usuario -> conversorGenericoDTO.converterParaDTO(usuario, UsuarioDTO.class));
    }
}
