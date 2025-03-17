package com.edugo.edugo_tcc.controller;

import com.edugo.edugo_tcc.dto.AlunoInfoDTO;
import com.edugo.edugo_tcc.dto.MatriculaDTO;
import com.edugo.edugo_tcc.dto.MatriculaResponseDTO;
import com.edugo.edugo_tcc.service.MatriculaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/matriculas")
public class MatriculaController {

    private final MatriculaService matriculaService;

    @Autowired
    public MatriculaController(MatriculaService matriculaService) {
        this.matriculaService = matriculaService;
    }

    /**
     * Método responsável por criar uma matricula
     * 
     * @param matriculaDTO
     * @return MatriculaResponseDTO
     */
    @PostMapping
    public ResponseEntity<MatriculaResponseDTO> criarMatricula(@RequestBody MatriculaDTO matriculaDTO) {
        MatriculaDTO matriculaCriada = matriculaService.criarMatricula(matriculaDTO);
        MatriculaResponseDTO matriculaResponseDTO = converterParaMatriculaResponseDTO(matriculaCriada);
        return ResponseEntity.status(HttpStatus.CREATED).body(matriculaResponseDTO);
    }

    /**
     * Método responsável por buscar uma matrícula por ID
     * 
     * @param id
     * @return MatriculaDTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<MatriculaDTO> buscarMatriculaPorId(@PathVariable Long id) {
        MatriculaDTO matriculaDTO = matriculaService.buscarMatriculaPorId(id);
        if (matriculaDTO != null) {
            return ResponseEntity.ok(matriculaDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Método responsável por buscar todas as matrículas
     * 
     * @return List<MatriculaResponseDTO>
     */
    @GetMapping
    public ResponseEntity<List<MatriculaResponseDTO>> buscarTodasMatriculas() {
        List<MatriculaDTO> matriculasDTO = matriculaService.buscarTodasMatriculas();
        List<MatriculaResponseDTO> matriculasResponseDTO = matriculasDTO
            .stream()
            .map(this::converterParaMatriculaResponseDTO)
            .toList();
        return ResponseEntity.ok(matriculasResponseDTO);
    }

    /**
     * Método responsável por atualizar uma matrícula
     * 
     * @param id
     * @param matriculaDTO
     * @return MatriculaDTO
     */
    @PutMapping("/{id}")
    public ResponseEntity<MatriculaDTO> atualizarMatricula(@PathVariable Long id, @RequestBody MatriculaDTO matriculaDTO) {
        MatriculaDTO matriculaAtualizada = matriculaService.atualizarMatricula(id, matriculaDTO);
        if (matriculaAtualizada != null) {
            return ResponseEntity.ok(matriculaAtualizada);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Método responsável por excluir uma matrícula
     * 
     * @param id
     * @return 
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirMatricula(@PathVariable Long id) {
        matriculaService.excluirMatricula(id);
        return ResponseEntity.noContent().build();
    }

    //Métodos da Classe
    /**
     * Método responsável por converter o MatriculaDTO em MatriculaResponseDTO
     * 
     * @param dto
     * @return MatriculaResponseDTO
     */
    private MatriculaResponseDTO converterParaMatriculaResponseDTO(MatriculaDTO dto) {
        MatriculaResponseDTO response = new MatriculaResponseDTO();
        response.setId(dto.getId());
        response.setDataMatricula(dto.getDataMatricula());
        response.setStatus(dto.getStatus());

        if (dto.getAluno() != null) {
            AlunoInfoDTO alunoInfo = new AlunoInfoDTO();
            alunoInfo.setId(dto.getAluno().getId());
            alunoInfo.setNome(dto.getAluno().getNome());
            response.setAluno(alunoInfo);
        }

        if (dto.getDisciplina() != null) {
            response.setDisciplinaId(dto.getDisciplina().getId());
            response.setDisciplinaNome(dto.getDisciplina().getNome());
            response.setDisciplinaValor(dto.getDisciplina().getValor());
            response.setProfessorNome(dto.getDisciplina().getProfessor().getNome());
        }

        return response;
    }
}