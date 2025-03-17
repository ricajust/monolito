package com.edugo.edugo_tcc.controller;

import com.edugo.edugo_tcc.dto.AlunoDTO;
import com.edugo.edugo_tcc.dto.AlunoResponseDTO;
import com.edugo.edugo_tcc.dto.ProfessorResponseDTO;
import com.edugo.edugo_tcc.service.AlunoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/alunos")
public class AlunoController {

    private final AlunoService alunoService;

    @Autowired
    public AlunoController(AlunoService alunoService) {
        this.alunoService = alunoService;
    }

    /**
     * Método responsável por criar um aluno
     * 
     * @param alunoDTO
     * @return AlunoResponseDTO
     */
    @PostMapping
    public ResponseEntity<AlunoResponseDTO> criarAluno(@RequestBody AlunoDTO alunoDTO) {
        AlunoDTO alunoCriado = alunoService.criarAluno(alunoDTO);
        AlunoResponseDTO alunoResponseDTO = converterParaResponseDTO(alunoCriado);

        return ResponseEntity.status(HttpStatus.CREATED).body(alunoResponseDTO);
    }

    /**
     * Método responsável por buscar um aluno por ID
     * 
     * @param id
     * @return AlunoResponseDTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlunoResponseDTO> buscarAlunoPorId(@PathVariable UUID id) {
        AlunoDTO alunoDTO = alunoService.buscarAlunoPorId(id);
        if (alunoDTO != null) {
            AlunoResponseDTO alunoResponseDTO = converterParaResponseDTO(alunoDTO);

            return ResponseEntity.ok(alunoResponseDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Método responsável por buscar todos os alunos
     * 
     * @return List<AlunoResponseDTO>
     */
    @GetMapping
    public ResponseEntity<List<AlunoResponseDTO>> buscarTodosAlunos() {
        List<AlunoDTO> alunosDTO = alunoService.buscarTodosAlunos();
        List<AlunoResponseDTO> alunoResponseDTOS = alunosDTO.stream()
            .map(alunoDTO -> {
                AlunoResponseDTO alunoResponseDTO = converterParaResponseDTO(alunoDTO);

                return alunoResponseDTO;
            })
            .collect(Collectors.toList());
        return ResponseEntity.ok(alunoResponseDTOS);
    }

    /**
     * Método responsável por atualizar um aluno
     * 
     * @param id
     * @param alunoDTO
     * @return AlunoResponseDTO
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlunoResponseDTO> atualizarAluno(@PathVariable UUID id, @RequestBody AlunoDTO alunoDTO) {
        AlunoDTO alunoAtualizado = alunoService.atualizarAluno(id, alunoDTO);
        if (alunoAtualizado != null) {
            AlunoResponseDTO alunoResponseDTO = converterParaResponseDTO(alunoAtualizado);

            return ResponseEntity.ok(alunoResponseDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Método responsável por excluir um aluno
     * 
     * @param id
     * @return AlunoResponseDTO
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<AlunoResponseDTO> excluirAluno(@PathVariable UUID id) {
        AlunoDTO alunoExcluidoDTO = alunoService.excluirAluno(id);
        if (alunoExcluidoDTO != null) {
            AlunoResponseDTO alunoResponseDTO = converterParaResponseDTO(alunoExcluidoDTO);
            return ResponseEntity.ok(alunoResponseDTO);
        } else {
            return ResponseEntity.notFound().build();
        }       
    }

    //Métodos da Classe
    /**
     * Método responsável por converter um AlunoDTO para um AlunoResponseDTO
     * 
     * @param dto
     * @return AlunoResponseDTO
     */
    private AlunoResponseDTO converterParaResponseDTO(AlunoDTO dto) {
        AlunoResponseDTO responseDTO = new AlunoResponseDTO();
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
        return responseDTO;
    }
}