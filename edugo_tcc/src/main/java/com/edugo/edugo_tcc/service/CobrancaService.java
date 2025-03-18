package com.edugo.edugo_tcc.service;

import java.util.List;
import java.util.UUID;
import com.edugo.edugo_tcc.dto.CobrancaDTO;
import com.edugo.edugo_tcc.dto.CobrancaResponseDTO; 

public interface CobrancaService {
    CobrancaDTO criarCobranca(CobrancaDTO cobrancaDTO);
    CobrancaResponseDTO buscarCobrancaPorId(UUID id);
    List<CobrancaResponseDTO> buscarTodasCobrancas();
    CobrancaDTO atualizarCobranca(UUID id, CobrancaDTO cobrancaDTO);
    CobrancaDTO excluirCobranca(UUID id);
}