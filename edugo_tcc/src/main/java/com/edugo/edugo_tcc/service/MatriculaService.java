package com.edugo.edugo_tcc.service;

import java.util.List;
import com.edugo.edugo_tcc.dto.MatriculaDTO;

public interface MatriculaService {
    MatriculaDTO criarMatricula(MatriculaDTO matriculaDTO);
    MatriculaDTO buscarMatriculaPorId(Long id);
    List<MatriculaDTO> buscarTodasMatriculas();
    MatriculaDTO atualizarMatricula(Long id, MatriculaDTO matriculaDTO);
    MatriculaDTO excluirMatricula(Long id);
}
