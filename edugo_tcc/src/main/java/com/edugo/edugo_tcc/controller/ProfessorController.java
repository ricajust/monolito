package com.edugo.edugo_tcc.controller;

import com.edugo.edugo_tcc.dto.ProfessorDTO;
import com.edugo.edugo_tcc.service.ProfessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/professores")
public class ProfessorController {

    private final ProfessorService professorService;

    @Autowired
    public ProfessorController(ProfessorService professorService) {
        this.professorService = professorService;
    }

    @PostMapping
    public ResponseEntity<ProfessorDTO> criarProfessor(@RequestBody ProfessorDTO professorDTO) {
        ProfessorDTO professorCriado = professorService.criarProfessor(professorDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(professorCriado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfessorDTO> buscarProfessorPorId(@PathVariable UUID id) {
        ProfessorDTO professorDTO = professorService.buscarProfessorPorId(id);
        if (professorDTO != null) {
            return ResponseEntity.ok(professorDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<ProfessorDTO>> buscarTodosProfessores() {
        List<ProfessorDTO> professoresDTO = professorService.buscarTodosProfessores();
        return ResponseEntity.ok(professoresDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfessorDTO> atualizarProfessor(@PathVariable UUID id, @RequestBody ProfessorDTO professorDTO) {
        ProfessorDTO professorAtualizado = professorService.atualizarProfessor(id, professorDTO);
        if (professorAtualizado != null) {
            return ResponseEntity.ok(professorAtualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirProfessor(@PathVariable UUID id) {
        professorService.excluirProfessor(id);
        return ResponseEntity.noContent().build();
    }
}