package com.edugo.edugo_tcc.controller;

import com.edugo.edugo_tcc.dto.PagamentoDTO;
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

    @PostMapping
    public ResponseEntity<PagamentoDTO> criarPagamento(@RequestBody PagamentoDTO pagamentoDTO) {
        PagamentoDTO pagamentoCriado = pagamentoService.criarPagamento(pagamentoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(pagamentoCriado);
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
}