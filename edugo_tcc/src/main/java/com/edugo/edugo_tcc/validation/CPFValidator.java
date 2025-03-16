package com.edugo.edugo_tcc.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CPFValidator implements ConstraintValidator<CPFValido, String> {

    @Override
    public void initialize(CPFValido constraintAnnotation) {}

    @Override
    public boolean isValid(String cpf, ConstraintValidatorContext context) {
        if (cpf == null || cpf.isEmpty()) {
            return true; // permite cpf nulo ou vazio
        }

        cpf = cpf.replaceAll("[^\\d]+", ""); //limpa o cpf de todos os caracteres não numéricos
        if (cpf.length() != 11) {
            return false; // cpf deve ter 11 dígitos
        }

        //implementação da validação do cpf
        char digito10, digito11;
        int sm, resto, num, peso;

        try {
            sm = 0;
            peso = 10;
            for (int i = 0; i < 9; i++) {
                num = (int) (cpf.charAt(i) - 48); //os dígitos numéricos '0' a '9' são representados pelos valores decimais 48 a 57 na tabela ASCII
                sm = sm + (num * peso); //soma os dígitos do cpf multiplicados pelo peso para verificar a validade do número (dígito verificador do CPF)
                peso = peso - 1;
            }

            resto = 11 - (sm % 11);
            digito10 = (resto == 10) || (resto == 11) ? '0' : (char) (resto + 48);

            sm = 0;
            peso = 11;
            for (int i = 0; i < 10; i++) {
                num = (int) (cpf.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso - 1;
            }

            resto = 11 - (sm % 11);
            digito11 = (resto == 10) || (resto == 11) ? '0' : (char) (resto + 48);

            return (digito10 == cpf.charAt(9)) && (digito11 == cpf.charAt(10));
        } catch (Exception erro) {
            return false;
        }

    }
}