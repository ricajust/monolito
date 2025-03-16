package com.edugo.edugo_tcc.dto;

import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true) // Mantido!
public class AlunoDTO extends UsuarioDTO {

    private List<MatriculaDTO> matricula;

}