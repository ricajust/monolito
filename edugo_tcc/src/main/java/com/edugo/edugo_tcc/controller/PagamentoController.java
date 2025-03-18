package com.edugo.edugo_tcc.controller;

import com.edugo.edugo_tcc.dto.AlunoInfoDTO;
import com.edugo.edugo_tcc.dto.PagamentoDTO;
import com.edugo.edugo_tcc.dto.PagamentoResponseDTO;
import com.edugo.edugo_tcc.service.PagamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/pagamentos")
public class PagamentoController {

    private final PagamentoService pagamentoService;

    @Autowired
    public PagamentoController(PagamentoService pagamentoService) {
        this.pagamentoService = pagamentoService;
    }

    @PostMapping("/gerar/{alunoId}") // Novo endpoint para gerar pagamento por alunoId
    public ResponseEntity<PagamentoResponseDTO> gerarPagamentoParaAluno(@PathVariable UUID alunoId) {
        PagamentoResponseDTO pagamentoGerado = pagamentoService.gerarPagamentoParaAluno(alunoId);
        if (pagamentoGerado != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(pagamentoGerado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagamentoResponseDTO> buscarPagamentoPorId(@PathVariable UUID id) {
        PagamentoResponseDTO pagamentoResponseDTO = pagamentoService.buscarPagamentoPorId(id);
        if (pagamentoResponseDTO != null) {
            return ResponseEntity.ok(pagamentoResponseDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<PagamentoResponseDTO>> buscarTodosPagamentos() {
        List<PagamentoResponseDTO> pagamentosResponseDTO = pagamentoService.buscarTodosPagamentos();
        return ResponseEntity.ok(pagamentosResponseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PagamentoDTO> atualizarPagamento(@PathVariable UUID id, @RequestBody PagamentoDTO pagamentoDTO) {
        PagamentoDTO pagamentoAtualizado = pagamentoService.atualizarPagamento(id, pagamentoDTO);
        if (pagamentoAtualizado != null) {
            return ResponseEntity.ok(pagamentoAtualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirPagamento(@PathVariable UUID id) {
        pagamentoService.excluirPagamento(id);
        return ResponseEntity.noContent().build();
    }

    //Métodos da Classe
    /**
     * Método para converter PagamentoDTO para PagamentoResponseDTO
     * 
     * @param pagamentoDTO
     * @return PagamentoResponseDTO
     */
    private PagamentoResponseDTO converterParaPagamentoResponseDTO(PagamentoDTO pagamentoDTO) {
        PagamentoResponseDTO responseDTO = new PagamentoResponseDTO();
        responseDTO.setId(pagamentoDTO.getId());
        responseDTO.setValorTotal(pagamentoDTO.getValor());
        responseDTO.setDataVencimento(pagamentoDTO.getDataVencimento());
        responseDTO.setStatus(pagamentoDTO.getStatus());

        if (pagamentoDTO.getAluno() != null) {
            AlunoInfoDTO alunoInfo = new AlunoInfoDTO();
            alunoInfo.setId(pagamentoDTO.getAluno().getId());
            alunoInfo.setNome(pagamentoDTO.getAluno().getNome());
            responseDTO.setAluno(alunoInfo);
        }
        return responseDTO;
    }
}