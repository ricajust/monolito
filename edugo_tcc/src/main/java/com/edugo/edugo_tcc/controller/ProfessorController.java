package com.edugo.edugo_tcc.controller;

import com.edugo.edugo_tcc.dto.ProfessorDTO;
import com.edugo.edugo_tcc.dto.ProfessorResponseDTO;
import com.edugo.edugo_tcc.service.ProfessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/professores")
public class ProfessorController {

    private final ProfessorService professorService;

    @Autowired
    public ProfessorController(ProfessorService professorService) {
        this.professorService = professorService;
    }

    /**
     * Método responsável por criar professor
     * 
     * @param professorDTO
     * @return ProfessorResponseDTO
     */
    @PostMapping
    public ResponseEntity<ProfessorResponseDTO> criarProfessor(@RequestBody ProfessorDTO professorDTO) {
        ProfessorDTO professorCriado = professorService.criarProfessor(professorDTO);
        ProfessorResponseDTO professorResponseDTO = converterParaResponseDTO(professorCriado);

        return ResponseEntity.status(HttpStatus.CREATED).body(professorResponseDTO);
    }

    /**
     * Método responsável por buscar um professor por ID
     * 
     * @param id
     * @return ProfessorResponseDTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProfessorResponseDTO> buscarProfessorPorId(@PathVariable UUID id) {
        ProfessorDTO professorDTO = professorService.buscarProfessorPorId(id);
        if (professorDTO != null) {
            ProfessorResponseDTO professorResponseDTO = converterParaResponseDTO(professorDTO);

            return ResponseEntity.ok(professorResponseDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Método responsável por buscar todos os professores
     * 
     * @return List<ProfessorResponseDTO>
     */
    @GetMapping
    public ResponseEntity<List<ProfessorResponseDTO>> buscarTodosProfessores() {
        List<ProfessorDTO> professoresDTO = professorService.buscarTodosProfessores();
        List<ProfessorResponseDTO> professoresResponseDTO = professoresDTO.stream()
                .map(this::converterParaResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(professoresResponseDTO);
    }

    /**
     * Método responsável por atualizar um professor
     * 
     * @param id
     * @param professorDTO
     * @return ProfessorResponseDTO
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProfessorResponseDTO> atualizarProfessor(@PathVariable UUID id, @RequestBody ProfessorDTO professorDTO) {
        ProfessorDTO professorAtualizado = professorService.atualizarProfessor(id, professorDTO);
        if (professorAtualizado != null) {
            ProfessorResponseDTO professorResponseDTO = converterParaResponseDTO(professorAtualizado);
            return ResponseEntity.ok(professorResponseDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Método responsável por excluir um professor
     * 
     * @param id
     * @return ProfessorResponseDTO
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ProfessorResponseDTO> excluirProfessor(@PathVariable UUID id) {
        ProfessorDTO professorExcluidoDTO = professorService.excluirProfessor(id);
        if (professorExcluidoDTO != null) {
            ProfessorResponseDTO professorResponseDTO = converterParaResponseDTO(professorExcluidoDTO);
            return ResponseEntity.ok(professorResponseDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //Métodos da Classe
    /**
     * Método responsável por converter um ProfessorDTO para um ProfessorResponseDTO
     * 
     * @param dto
     * @return ProfessorResponseDTO
     */
    private ProfessorResponseDTO converterParaResponseDTO(ProfessorDTO dto) {
        ProfessorResponseDTO responseDTO = new ProfessorResponseDTO();
        responseDTO.setId(dto.getId());
        responseDTO.setNome(dto.getNome());
        responseDTO.setCpf(dto.getCpf());
        responseDTO.setDataNascimento(dto.getDataNascimento());
        responseDTO.setEmail(dto.getEmail());
        responseDTO.setTelefone(dto.getTelefone());
        responseDTO.setEndereco(dto.getEndereco());
        responseDTO.setBairro(dto.getBairro());
        responseDTO.setCidade(dto.getCidade());
        responseDTO.setUf(dto.getUf());
        responseDTO.setCep(dto.getCep());
        responseDTO.setFormacao(dto.getFormacao());
        responseDTO.setEspecialidade(dto.getEspecialidade());
        return responseDTO;
    }
}