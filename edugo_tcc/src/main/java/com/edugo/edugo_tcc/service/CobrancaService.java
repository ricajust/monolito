package com.edugo.edugo_tcc.service;

import java.util.List;
import java.util.UUID;
import com.edugo.edugo_tcc.dto.CobrancaDTO;

public interface CobrancaService {
    CobrancaDTO criarCobranca(CobrancaDTO cobrancaDTO);
    CobrancaDTO buscarCobrancaPorId(UUID id);
    List<CobrancaDTO> buscarTodasCobrancas();
    CobrancaDTO atualizarCobranca(UUID id, CobrancaDTO cobrancaDTO);
    CobrancaDTO excluirCobranca(UUID id);
}