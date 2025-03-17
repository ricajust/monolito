package com.edugo.edugo_tcc.service;

import java.util.List;
import java.util.UUID;
import com.edugo.edugo_tcc.dto.FuncionarioDTO;

public interface FuncionarioService {
    FuncionarioDTO criarFuncionario(FuncionarioDTO funcionarioDTO);
    FuncionarioDTO buscarFuncionarioPorId(UUID id);
    List<FuncionarioDTO> buscarTodosFuncionarios();
    FuncionarioDTO atualizarFuncionario(UUID id, FuncionarioDTO funcionarioDTO);
    FuncionarioDTO excluirFuncionario(UUID id);
}
