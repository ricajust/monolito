package com.edugo.edugo_tcc.service.impl;

import com.edugo.edugo_tcc.dto.CobrancaDTO;
import com.edugo.edugo_tcc.dto.CobrancaResponseDTO; // Importe CobrancaResponseDTO
import com.edugo.edugo_tcc.model.Cobranca;
import com.edugo.edugo_tcc.model.Pagamento;
import com.edugo.edugo_tcc.repository.CobrancaRepository;
import com.edugo.edugo_tcc.repository.PagamentoRepository;
import com.edugo.edugo_tcc.service.CobrancaService;
import com.edugo.edugo_tcc.util.ConversorGenericoDTO;
import com.edugo.edugo_tcc.util.ConversorGenericoEntidade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CobrancaServiceImpl implements CobrancaService {

    private final CobrancaRepository cobrancaRepository;
    private final PagamentoRepository pagamentoRepository;
    private final ConversorGenericoDTO conversorGenericoDTO;
    private final ConversorGenericoEntidade conversorGenericoEntidade;

    @Autowired
    public CobrancaServiceImpl(CobrancaRepository cobrancaRepository, PagamentoRepository pagamentoRepository, ConversorGenericoDTO conversorGenericoDTO, ConversorGenericoEntidade conversorGenericoEntidade) {
        this.cobrancaRepository = cobrancaRepository;
        this.pagamentoRepository = pagamentoRepository;
        this.conversorGenericoDTO = conversorGenericoDTO;
        this.conversorGenericoEntidade = conversorGenericoEntidade;
    }

    @Override
    public CobrancaDTO criarCobranca(CobrancaDTO cobrancaDTO) {
        try {
            Cobranca cobranca = conversorGenericoEntidade.converterParaEntidade(cobrancaDTO, Cobranca.class);

            Pagamento pagamento = pagamentoRepository.findById(cobrancaDTO.getPagamento().getId())
                    .orElseThrow(() -> new RuntimeException("Pagamento não encontrado com ID: " + cobrancaDTO.getPagamento().getId()));

            cobranca.setPagamento(pagamento);

            Cobranca cobrancaSalva = cobrancaRepository.save(cobranca);
            return conversorGenericoDTO.converterParaDTO(cobrancaSalva, CobrancaDTO.class);
        } catch (Exception error) {
            throw new RuntimeException("Erro ao criar cobrança: " + error.getMessage(), error);
        }
    }

    @Override
    public CobrancaResponseDTO buscarCobrancaPorId(UUID id) {
        try {
            Cobranca cobranca = cobrancaRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Cobrança não encontrada com ID: " + id));
            return converterParaCobrancaResponseDTO(cobranca); // Use o método de conversão para o DTO de resposta
        } catch (Exception error) {
            throw new RuntimeException("Erro ao buscar cobrança por ID: " + error.getMessage(), error);
        }
    }

    @Override
    public List<CobrancaResponseDTO> buscarTodasCobrancas() {
        try {
            List<Cobranca> cobrancas = cobrancaRepository.findAll();
            return cobrancas.stream()
                    .map(this::converterParaCobrancaResponseDTO) // Use o método de conversão para o DTO de resposta
                    .collect(Collectors.toList());
        } catch (Exception error) {
            throw new RuntimeException("Erro ao buscar todas as cobranças: " + error.getMessage(), error);
        }
    }

    @Override
    public CobrancaDTO atualizarCobranca(UUID id, CobrancaDTO cobrancaDTO) {
        try {
            Cobranca cobrancaExistente = cobrancaRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Cobrança não encontrada com ID: " + id));

            Cobranca cobrancaAtualizada = conversorGenericoEntidade.converterParaEntidade(cobrancaDTO, Cobranca.class);
            cobrancaAtualizada.setId(cobrancaExistente.getId());
            cobrancaAtualizada.setPagamento(cobrancaExistente.getPagamento());
            cobrancaAtualizada = cobrancaRepository.save(cobrancaAtualizada);
            return conversorGenericoDTO.converterParaDTO(cobrancaAtualizada, CobrancaDTO.class);
        } catch (Exception error) {
            throw new RuntimeException("Erro ao atualizar cobrança: " + error.getMessage(), error);
        }
    }

    @Override
    public CobrancaDTO excluirCobranca(UUID id) {
        try {
            Cobranca cobranca = cobrancaRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Cobrança não encontrada com ID: " + id));
            cobrancaRepository.delete(cobranca);
            return conversorGenericoDTO.converterParaDTO(cobranca, CobrancaDTO.class);
        } catch (Exception error) {
            throw new RuntimeException("Erro ao excluir cobrança: " + error.getMessage(), error);
        }
    }

    // Método auxiliar para converter Cobranca para CobrancaResponseDTO
    private CobrancaResponseDTO converterParaCobrancaResponseDTO(Cobranca cobranca) {
        CobrancaResponseDTO response = new CobrancaResponseDTO();
        response.setId(cobranca.getId());
        response.setDataPagamento(cobranca.getDataPagamento());
        response.setMetodoPagamento(cobranca.getMetodoPagamento());
        response.setIdPagamento(cobranca.getPagamento().getId()); // Apenas o ID do pagamento
        return response;
    }
}