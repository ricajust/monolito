package com.edugo.edugo_tcc.controller;

import com.edugo.edugo_tcc.dto.DesempenhoDTO;
import com.edugo.edugo_tcc.service.DesempenhoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/desempenhos")
public class DesempenhoController {

    private final DesempenhoService desempenhoService;

    @Autowired
    public DesempenhoController(DesempenhoService desempenhoService) {
        this.desempenhoService = desempenhoService;
    }

    @PostMapping
    public ResponseEntity<DesempenhoDTO> criarDesempenho(@RequestBody DesempenhoDTO desempenhoDTO) {
        DesempenhoDTO desempenhoCriado = desempenhoService.criarDesempenho(desempenhoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(desempenhoCriado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DesempenhoDTO> buscarDesempenhoPorId(@PathVariable UUID id) {
        DesempenhoDTO desempenhoDTO = desempenhoService.buscarDesempenhoPorId(id);
        if (desempenhoDTO != null) {
            return ResponseEntity.ok(desempenhoDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<DesempenhoDTO>> buscarTodosDesempenhos() {
        List<DesempenhoDTO> desempenhosDTO = desempenhoService.buscarTodosDesempenhos();
        return ResponseEntity.ok(desempenhosDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DesempenhoDTO> atualizarDesempenho(@PathVariable UUID id, @RequestBody DesempenhoDTO desempenhoDTO) {
        DesempenhoDTO desempenhoAtualizado = desempenhoService.atualizarDesempenho(id, desempenhoDTO);
        if (desempenhoAtualizado != null) {
            return ResponseEntity.ok(desempenhoAtualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirDesempenho(@PathVariable UUID id) {
        desempenhoService.excluirDesempenho(id);
        return ResponseEntity.noContent().build();
    }
}