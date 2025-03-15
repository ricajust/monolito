package com.edugo.edugo_tcc.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class Pagamento {
    //propriedades
    public UUID id;
    public String aluno;
    public BigDecimal valor;
    public LocalDate dataVencimento;
    public String status;

    //metodos
    public String gerarBoletos() {
        return "boletos";
    }

    public void enviarEmailCobranca() {
        
    }
}
