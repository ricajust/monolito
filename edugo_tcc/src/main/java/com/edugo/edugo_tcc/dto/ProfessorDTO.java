package com.edugo.edugo_tcc.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProfessorDTO extends UsuarioDTO{
    private String formacao;
    private String especialidade;
}
