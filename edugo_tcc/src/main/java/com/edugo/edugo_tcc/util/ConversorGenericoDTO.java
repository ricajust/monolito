package com.edugo.edugo_tcc.util;

import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ConversorGenericoDTO {
    private final ModelMapper modelMapper = new ModelMapper();

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
