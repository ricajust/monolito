package com.edugo.edugo_tcc.event;

import com.edugo.edugo_tcc.dto.AlunoDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlunoAtualizadoEvent {

    private AlunoDTO aluno;

}