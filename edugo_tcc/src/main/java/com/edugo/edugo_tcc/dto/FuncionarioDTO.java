package com.edugo.edugo_tcc.dto;

import java.time.LocalDate;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class FuncionarioDTO extends UsuarioDTO{
    private LocalDate dataContratacao;
    private int nivelAcesso;
}
