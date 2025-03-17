package com.edugo.edugo_tcc.service;

import java.util.List;
import java.util.UUID;
import com.edugo.edugo_tcc.dto.DesempenhoDTO;

public interface DesempenhoService {
    DesempenhoDTO criarDesempenho(DesempenhoDTO desempenhoDTO);
    DesempenhoDTO buscarDesempenhoPorId(UUID id);
    List<DesempenhoDTO> buscarTodosDesempenhos();
    DesempenhoDTO atualizarDesempenho(UUID id, DesempenhoDTO desempenhoDTO);
    DesempenhoDTO excluirDesempenho(UUID id);
}