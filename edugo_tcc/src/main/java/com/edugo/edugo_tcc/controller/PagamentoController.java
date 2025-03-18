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

    // @PostMapping
    // public ResponseEntity<PagamentoDTO> criarPagamento(@RequestBody PagamentoDTO pagamentoDTO) {
    //     PagamentoDTO pagamentoCriado = pagamentoService.criarPagamento(pagamentoDTO);
    //     return ResponseEntity.status(HttpStatus.CREATED).body(pagamentoCriado);
    // }

    @PostMapping("/gerar/{alunoId}") // Novo endpoint para gerar pagamento por alunoId
    public ResponseEntity<PagamentoResponseDTO> gerarPagamentoParaAluno(@PathVariable UUID alunoId) {
        PagamentoDTO pagamentoGeradoDTO = pagamentoService.gerarPagamentoParaAluno(alunoId);
        if (pagamentoGeradoDTO != null) {
            PagamentoResponseDTO pagamentoResponseDTO = converterParaPagamentoResponseDTO(pagamentoGeradoDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(pagamentoResponseDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagamentoDTO> buscarPagamentoPorId(@PathVariable UUID id) {
        PagamentoDTO pagamentoDTO = pagamentoService.buscarPagamentoPorId(id);
        if (pagamentoDTO != null) {
            return ResponseEntity.ok(pagamentoDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<PagamentoDTO>> buscarTodosPagamentos() {
        List<PagamentoDTO> pagamentosDTO = pagamentoService.buscarTodosPagamentos();
        return ResponseEntity.ok(pagamentosDTO);
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
        responseDTO.setValor(pagamentoDTO.getValor());
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