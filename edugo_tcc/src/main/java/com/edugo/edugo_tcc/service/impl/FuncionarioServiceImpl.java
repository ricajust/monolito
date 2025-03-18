package com.edugo.edugo_tcc.service.impl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.edugo.edugo_tcc.dto.FuncionarioDTO;
import com.edugo.edugo_tcc.model.Funcionario;
import com.edugo.edugo_tcc.repository.FuncionarioRepository;
import com.edugo.edugo_tcc.service.FuncionarioService;
import com.edugo.edugo_tcc.util.ConversorGenericoDTO;
import com.edugo.edugo_tcc.util.ConversorGenericoEntidade;

@Service
public class FuncionarioServiceImpl implements FuncionarioService {

    private final FuncionarioRepository funcionarioRepository;
    private final ConversorGenericoDTO conversorGenericoDTO;
    private final ConversorGenericoEntidade conversorGenericoEntidade;

    @Autowired
    public FuncionarioServiceImpl(
        FuncionarioRepository funcionarioRepository,
        ConversorGenericoDTO conversorGenericoDTO,
        ConversorGenericoEntidade conversorGenericoEntidade) 
        {
            this.funcionarioRepository = funcionarioRepository;
            this.conversorGenericoDTO = conversorGenericoDTO;
            this.conversorGenericoEntidade = conversorGenericoEntidade;
        }

    /**
     * Método responsável por criar um funcionário
     * 
     * @param funcionarioDTO
     * @return FuncionarioDTO
     */
    @Override
    public FuncionarioDTO criarFuncionario(FuncionarioDTO funcionarioDTO) {
        try {
            Funcionario funcionario = conversorGenericoEntidade.converterParaEntidade(funcionarioDTO, Funcionario.class);
            System.out.println("Entidade Funcionario após conversão: " + funcionario.toString()); // Adicionado log
            Funcionario funcionarioSalvo = funcionarioRepository.save(funcionario);
            return conversorGenericoDTO.converterParaDTO(funcionarioSalvo, FuncionarioDTO.class);
        } catch(Exception error) {
            throw new RuntimeException("Erro ao criar funcionário: " + error.getMessage(), error);
        }
    }

    /**
     * Método responsável por buscar um funcionário por ID
     * 
     * @param id
     * @return FuncionarioDTO
     */
    @Override
    public FuncionarioDTO buscarFuncionarioPorId(UUID id) {
        try {
            Funcionario funcionario = funcionarioRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado"));
            return conversorGenericoDTO.converterParaDTO(funcionario, FuncionarioDTO.class);
        }
        catch(Exception error) {
            throw new RuntimeException("Erro ao buscar funcionário por ID: " + error.getMessage(), error);
        }
    }

    /**
     * Método responsável por buscar todos os funcionários
     * 
     * @return List<FuncionarioDTO>
     */
    @Override
    public List<FuncionarioDTO> buscarTodosFuncionarios() {
        try {
            List<Funcionario> funcionarios = funcionarioRepository.findAll();
            return funcionarios
                .stream()
                .map(funcionario -> conversorGenericoDTO.converterParaDTO(funcionario, FuncionarioDTO.class))
                .collect(Collectors.toList());
        } catch(Exception error) {
            throw new RuntimeException("Erro ao buscar todos os funcionários: " + error.getMessage(), error);
        }
    }

    /**
     * Método responsável por atualizar um funcionário
     * 
     * @param id
     * @param funcionarioDTO
     * @return FuncionarioDTO
     */
    @Override
    public FuncionarioDTO atualizarFuncionario(UUID id, FuncionarioDTO funcionarioDTO) {
        try {
            Funcionario funcionarioExistente = funcionarioRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado com o ID: " + id));
            Funcionario funcionarioAtualizado = conversorGenericoEntidade.converterParaEntidade(funcionarioDTO, Funcionario.class);
            funcionarioAtualizado.setId(funcionarioExistente.getId());
                        
            // Atualizar a senha apenas se um novo valor for fornecido
            if(funcionarioDTO.getSenha() != null && !funcionarioDTO.getSenha().isEmpty()) {
                funcionarioAtualizado.setSenha(funcionarioDTO.getSenha());
            } else {
                funcionarioAtualizado.setSenha(funcionarioExistente.getSenha()); // Manter a senha existente
            }

            funcionarioAtualizado = funcionarioRepository.save(funcionarioAtualizado);
            return conversorGenericoDTO.converterParaDTO(funcionarioAtualizado, FuncionarioDTO.class);
        } catch(Exception error) {
            throw new RuntimeException("Erro ao atualizar funcionário: " + error.getMessage(), error);
        }
    }

    /**
     * Método responsável por excluir um funcionário
     * 
     * @param id
     * @return FuncionarioDTO
     */
    @Override
    public FuncionarioDTO excluirFuncionario(UUID id) {
        try {
            Funcionario funcionario = funcionarioRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado com o ID: " + id));
            funcionarioRepository.delete(funcionario);
            return conversorGenericoDTO.converterParaDTO(funcionario, FuncionarioDTO.class);
        }
        catch(Exception error) {
            throw new RuntimeException("Erro ao excluir funcionário: " + error.getMessage(), error);
        }
    }
}
