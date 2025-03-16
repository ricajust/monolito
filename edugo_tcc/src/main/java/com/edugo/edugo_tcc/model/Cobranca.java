package com.edugo.edugo_tcc.model;

import java.time.LocalDate;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cobranca {
    //propriedades
    public UUID id;
    public Pagamento pagamento;
    public LocalDate dataPagamento;
    public String metodoPagamento;

    //métodos
    public void confirmarPagamentovoid() {
    
    }
    public String gerarBoleto() {
        return "boleto";
    }
}
