package com.edugo.edugo_tcc.service;

import java.util.Optional;
import com.edugo.edugo_tcc.dto.UsuarioDTO;

public interface UsuarioService {
    Optional<UsuarioDTO> buscarUsuarioPorCpf(String cpf);
}
