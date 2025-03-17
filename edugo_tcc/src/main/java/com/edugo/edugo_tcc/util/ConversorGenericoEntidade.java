package com.edugo.edugo_tcc.util;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ConversorGenericoEntidade {
    private final ModelMapper modelMapper = new ModelMapper();

    public <T> T converterParaEntidade(Object dto, Class<T> classeEntidade) {
        return modelMapper.map(dto, classeEntidade);
    }
}
