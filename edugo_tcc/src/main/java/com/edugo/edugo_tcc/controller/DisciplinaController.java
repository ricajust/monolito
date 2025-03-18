package com.edugo.edugo_tcc.controller;

import com.edugo.edugo_tcc.dto.DisciplinaDTO;
import com.edugo.edugo_tcc.dto.DisciplinaResponseDTO;
import com.edugo.edugo_tcc.dto.ProfessorInfoDTO;
import com.edugo.edugo_tcc.service.impl.DisciplinaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/disciplinas")
public class DisciplinaController {

    private final DisciplinaServiceImpl disciplinaService;

    @Autowired
    public DisciplinaController(DisciplinaServiceImpl disciplinaService) {
        this.disciplinaService = disciplinaService;
    }

    /**
     * Método responsável por criar uma disciplina
     * 
     * @param disciplinaDTO
     * @return DisciplinaResponseDTO
     */
    @PostMapping
    public ResponseEntity<DisciplinaResponseDTO> criarDisciplina(@RequestBody DisciplinaDTO disciplinaDTO) {
        DisciplinaDTO disciplinaCriada = disciplinaService.criarDisciplina(disciplinaDTO);
        DisciplinaResponseDTO disciplinaResponseDTO = converterParaDisciplinaResponseDTO(disciplinaCriada);
        return ResponseEntity.status(HttpStatus.CREATED).body(disciplinaResponseDTO);
    }

    /**
     * Método responsável por buscar uma disciplina por ID
     * 
     * @param id
     * @return DisciplinaResponseDTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<DisciplinaResponseDTO> buscarDisciplinaPorId(@PathVariable Long id) {
        DisciplinaDTO disciplinaDTO = disciplinaService.buscarDisciplinaPorId(id);
        if (disciplinaDTO != null) {
            return ResponseEntity.ok(converterParaDisciplinaResponseDTO(disciplinaDTO));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Método responsável por buscar todas as disciplinas
     * 
     * @return List<DisciplinaResponseDTO>
     */
    @GetMapping
    public ResponseEntity<List<DisciplinaResponseDTO>> buscarTodasDisciplinas() {
        List<DisciplinaDTO> disciplinasDTO = disciplinaService.buscarTodasDisciplinas();
        List<DisciplinaResponseDTO> disciplinasResponseDTO = disciplinasDTO
            .stream()
            .map(this::converterParaDisciplinaResponseDTO)
            .toList();
        return ResponseEntity.ok(disciplinasResponseDTO);
    }

    /**
     * Método responsável por atualizar uma disciplina
     * 
     * @param id
     * @param disciplinaDTO
     * @return DisciplinaResponseDTO
     */
    @PutMapping("/{id}")
    public ResponseEntity<DisciplinaResponseDTO> atualizarDisciplina(@PathVariable Long id, @RequestBody DisciplinaDTO disciplinaDTO) {
        DisciplinaDTO disciplinaAtualizada = disciplinaService.atualizarDisciplina(id, disciplinaDTO);
        if (disciplinaAtualizada != null) {
            return ResponseEntity.ok(converterParaDisciplinaResponseDTO(disciplinaAtualizada));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Método reponsável por exclior uma disciplina
     * @param id
     * @return DisciplinaResponseDTO
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<DisciplinaResponseDTO> excluirDisciplina(@PathVariable Long id) {
        DisciplinaDTO disciplinaExcluidaDTO = disciplinaService.excluirDisciplina(id);
        if (disciplinaExcluidaDTO != null) {
            return ResponseEntity.ok(converterParaDisciplinaResponseDTO(disciplinaExcluidaDTO));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //Métodos da Classe
    /**
     * Método responsável por converter uma DisciplinaDTO para um DisciplinaResponseDTO
     * 
     * @param dto
     * @return DisciplinaResponseDTO
     */
    private DisciplinaResponseDTO converterParaDisciplinaResponseDTO(DisciplinaDTO dto) {
        DisciplinaResponseDTO responseDTO = new DisciplinaResponseDTO();
        responseDTO.setId(dto.getId());
        responseDTO.setNome(dto.getNome());
        responseDTO.setDescricao(dto.getDescricao());
        responseDTO.setNivel(dto.getNivel());
        responseDTO.setValor(dto.getValor());

        if (dto.getProfessor() != null) {
            ProfessorInfoDTO professorInfo = new ProfessorInfoDTO();
            professorInfo.setId(dto.getProfessor().getId());
            responseDTO.setProfessor(professorInfo);
        }
        return responseDTO;
    }
}