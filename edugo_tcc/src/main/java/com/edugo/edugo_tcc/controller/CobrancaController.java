package com.edugo.edugo_tcc.controller;

import com.edugo.edugo_tcc.dto.CobrancaDTO;
import com.edugo.edugo_tcc.service.CobrancaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/cobrancas")
public class CobrancaController {

    private final CobrancaService cobrancaService;

    @Autowired
    public CobrancaController(CobrancaService cobrancaService) {
        this.cobrancaService = cobrancaService;
    }

    @PostMapping
    public ResponseEntity<CobrancaDTO> criarCobranca(@RequestBody CobrancaDTO cobrancaDTO) {
        CobrancaDTO cobrancaCriada = cobrancaService.criarCobranca(cobrancaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(cobrancaCriada);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CobrancaDTO> buscarCobrancaPorId(@PathVariable UUID id) {
        CobrancaDTO cobrancaDTO = cobrancaService.buscarCobrancaPorId(id);
        if (cobrancaDTO != null) {
            return ResponseEntity.ok(cobrancaDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<CobrancaDTO>> buscarTodasCobrancas() {
        List<CobrancaDTO> cobrancasDTO = cobrancaService.buscarTodasCobrancas();
        return ResponseEntity.ok(cobrancasDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CobrancaDTO> atualizarCobranca(@PathVariable UUID id, @RequestBody CobrancaDTO cobrancaDTO) {
        CobrancaDTO cobrancaAtualizada = cobrancaService.atualizarCobranca(id, cobrancaDTO);
        if (cobrancaAtualizada != null) {
            return ResponseEntity.ok(cobrancaAtualizada);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirCobranca(@PathVariable UUID id) {
        cobrancaService.excluirCobranca(id);
        return ResponseEntity.noContent().build();
    }
}