package com.edugo.edugo_tcc.event;

import java.util.UUID;

import com.edugo.edugo_tcc.dto.AlunoDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlunoExcluidoEvent {

    private AlunoDTO aluno;

}