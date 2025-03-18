package com.edugo.edugo_tcc.util;

import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

import com.edugo.edugo_tcc.model.Aluno;
import com.edugo.edugo_tcc.dto.AlunoDTO;
import com.edugo.edugo_tcc.model.Usuario;
import com.edugo.edugo_tcc.dto.UsuarioDTO;

@Component
public class ConversorGenericoDTO {
    private final ModelMapper modelMapper = new ModelMapper();

    public ConversorGenericoDTO() {
        configureModelMapper();
    }

    private void configureModelMapper() {
        // Mapeamento de Usuario para UsuarioDTO
        modelMapper.createTypeMap(Usuario.class, UsuarioDTO.class);
    
        // Mapeamento de Aluno para AlunoDTO (incluindo as propriedades da classe base)
        modelMapper.createTypeMap(Aluno.class, AlunoDTO.class)
            .includeBase(Usuario.class, UsuarioDTO.class);
    
        // Mapeamento de UsuarioDTO para Usuario
        modelMapper.createTypeMap(UsuarioDTO.class, Usuario.class);
    
        // Mapeamento de AlunoDTO para Aluno (incluindo as propriedades da classe base)
        modelMapper.createTypeMap(AlunoDTO.class, Aluno.class)
            .includeBase(UsuarioDTO.class, Usuario.class);
    }
    public <T> T converterParaDTO(Object objeto, Class<T> classeDTO) {
        return modelMapper.map(objeto, classeDTO);
    }

    public <T> List<T> converterListaParaDTO(List<?> entidades, Class<T> classeDTO) {
        return entidades
                .stream()
                .map(entidade -> converterParaDTO(entidade, classeDTO))
                .collect(Collectors.toList());
    }
}