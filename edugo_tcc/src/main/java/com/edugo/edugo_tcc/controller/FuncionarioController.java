package com.edugo.edugo_tcc.controller;

import com.edugo.edugo_tcc.dto.FuncionarioDTO;
import com.edugo.edugo_tcc.dto.FuncionarioResponseDTO;
import com.edugo.edugo_tcc.service.FuncionarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/funcionarios")
public class FuncionarioController {

    private final FuncionarioService funcionarioService;

    @Autowired
    public FuncionarioController(FuncionarioService funcionarioService) {
        this.funcionarioService = funcionarioService;
    }

    /**
     * Método responsável por criar funcionário
     * @param funcionarioDTO
     * @return FuncionarioResponseDTO
     */
    @PostMapping
    public ResponseEntity<FuncionarioResponseDTO> criarFuncionario(@RequestBody FuncionarioDTO funcionarioDTO) {
        FuncionarioDTO funcionarioCriado = funcionarioService.criarFuncionario(funcionarioDTO);
        FuncionarioResponseDTO funcionarioResponseDTO = converterParaResponseDTO(funcionarioCriado);
        return ResponseEntity.status(HttpStatus.CREATED).body(funcionarioResponseDTO);
    }

    /**
     * Método responsável por buscar funcionário por id
     * @param id
     * @return FuncionarioResponseDTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<FuncionarioResponseDTO> buscarFuncionarioPorId(@PathVariable UUID id) {
        FuncionarioDTO funcionarioDTO = funcionarioService.buscarFuncionarioPorId(id);
        if (funcionarioDTO != null) {
            FuncionarioResponseDTO funcionarioResponseDTO = converterParaResponseDTO(funcionarioDTO);
            return ResponseEntity.ok(funcionarioResponseDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Método responsável por buscar todos os funcionários
     * 
     * @return List<FuncionarioResponseDTO>
     */
    @GetMapping
    public ResponseEntity<List<FuncionarioResponseDTO>> buscarTodosFuncionarios() {
        List<FuncionarioDTO> funcionariosDTO = funcionarioService.buscarTodosFuncionarios();
        List<FuncionarioResponseDTO> funcionariosResponseDTO = funcionariosDTO.stream()
                .map(this::converterParaResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(funcionariosResponseDTO);
    }

    /**
     * Método responsável por atualizar um funcionário
     * 
     * @param id
     * @param funcionarioDTO
     * @return FuncionarioResponseDTO
     */
    @PutMapping("/{id}")
    public ResponseEntity<FuncionarioResponseDTO> atualizarFuncionario(@PathVariable UUID id, @RequestBody FuncionarioDTO funcionarioDTO) {
        FuncionarioDTO funcionarioAtualizado = funcionarioService.atualizarFuncionario(id, funcionarioDTO);
        if (funcionarioAtualizado != null) {
            FuncionarioResponseDTO funcionarioResponseDTO = converterParaResponseDTO(funcionarioAtualizado);
            return ResponseEntity.ok(funcionarioResponseDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Método responsável por excluir um funcionário
     * 
     * @param id
     * @return FuncionarioResponseDTO
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<FuncionarioResponseDTO> excluirFuncionario(@PathVariable UUID id) {
        FuncionarioDTO funcionarioExcluidoDTO = funcionarioService.excluirFuncionario(id);
        if (funcionarioExcluidoDTO != null) {
            FuncionarioResponseDTO funcionarioResponseDTO = converterParaResponseDTO(funcionarioExcluidoDTO);
            return ResponseEntity.ok(funcionarioResponseDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //Métodos da Classe
    /**
     * Método responsável por converter um FuncionarioDTO para um FuncionarioResponseDTO
     * 
     * @param dto
     * @return FuncionarioResponseDTO
     */
    private FuncionarioResponseDTO converterParaResponseDTO(FuncionarioDTO dto) {
        FuncionarioResponseDTO responseDTO = new FuncionarioResponseDTO();
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
        responseDTO.setDataContratacao(dto.getDataContratacao());
        responseDTO.setNivelAcesso(dto.getNivelAcesso());
        return responseDTO;
    }
}