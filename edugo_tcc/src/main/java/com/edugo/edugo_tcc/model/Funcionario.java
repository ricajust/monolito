package com.edugo.edugo_tcc.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Funcionario {
    //propriedades
    public LocalDate dataContratacao;
    public int nivelAcesso;

    //metodos
    public List<Pagamento> calcularCobranca(Aluno aluno) {
        return new ArrayList<Pagamento>();
    }
}
