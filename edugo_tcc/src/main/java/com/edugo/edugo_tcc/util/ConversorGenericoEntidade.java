package com.edugo.edugo_tcc.util;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.edugo.edugo_tcc.dto.AlunoDTO;
import com.edugo.edugo_tcc.model.Aluno;

@Component
public class ConversorGenericoEntidade {
    private final ModelMapper modelMapper = new ModelMapper();

    public <T> T converterParaEntidade(Object dto, Class<T> classeEntidade) {
        T entidade = modelMapper.map(dto, classeEntidade);
        if (dto instanceof AlunoDTO && entidade instanceof Aluno) {
            AlunoDTO alunoDTO = (AlunoDTO) dto;
            Aluno aluno = (Aluno) entidade;
            if (alunoDTO.getId() != null) {
                aluno.setId(alunoDTO.getId());
            }
        }
        return entidade;
    }
}
