package com.edugo.edugo_tcc.mocks;

import com.edugo.edugo_tcc.dto.AlunoDTO;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MockAlunoData {

    private static final String[] PRIMEIROS_NOMES = {
            "Ana", "Beatriz", "Carlos", "Daniel", "Eduardo", "Fernanda", "Gabriel", "Helena", "Igor", "Juliana",
            "Lucas", "Mariana", "Pedro", "Renata", "Thiago", "Vanessa", "William", "Xavier", "Yasmin", "Zeca",
            "Amanda", "Bruno", "Carolina", "Diego", "Erica", "Felipe", "Giovanna", "Hugo", "Isabela", "João"
    };

    private static final String[] SOBRENOMES = {
            "Silva", "Santos", "Oliveira", "Souza", "Pereira", "Rodrigues", "Almeida", "Nascimento", "Costa", "Carvalho",
            "Andrade", "Barbosa", "Castro", "Dias", "Freitas", "Gomes", "Lima", "Machado", "Martins", "Melo",
            "Nunes", "Pinto", "Ribeiro", "Rocha", "Sales", "Teixeira", "Vargas", "Vieira", "Xavier", "Zanetti"
    };

    private static final Random random = new Random();

    public static String gerarCPFValido() {
        String noveDigitos = "";
        for (int i = 0; i < 9; i++) {
            noveDigitos += String.valueOf(random.nextInt(10));
        }

        int soma = 0;
        int peso = 10;
        for (int i = 0; i < 9; i++) {
            soma += (noveDigitos.charAt(i) - '0') * peso--;
        }

        int primeiroDigito = 11 - (soma % 11);
        if (primeiroDigito > 9) {
            primeiroDigito = 0;
        }
        noveDigitos += primeiroDigito;

        soma = 0;
        peso = 11;
        for (int i = 0; i < 10; i++) {
            soma += (noveDigitos.charAt(i) - '0') * peso--;
        }

        int segundoDigito = 11 - (soma % 11);
        if (segundoDigito > 9) {
            segundoDigito = 0;
        }
        noveDigitos += segundoDigito;

        return noveDigitos;
    }

    public static List<AlunoDTO> gerarListaDeAlunos(int quantidade) {
        List<AlunoDTO> alunos = new ArrayList<>();
        for (int i = 0; i < quantidade; i++) {
            String nome = PRIMEIROS_NOMES[random.nextInt(PRIMEIROS_NOMES.length)] + " " + SOBRENOMES[random.nextInt(SOBRENOMES.length)];
            AlunoDTO aluno = new AlunoDTO();
            aluno.setNome(nome);
            aluno.setCpf(gerarCPFValido());
            aluno.setDataNascimento(LocalDate.now().minusYears(18).minusDays(random.nextInt(365 * 5))); // Idade aproximada
            aluno.setEmail(nome.toLowerCase().replace(" ", "") + i + "@email.com");
            aluno.setTelefone("119" + String.format("%08d", random.nextInt(100000000)));
            aluno.setEndereco("Rua Mock, " + random.nextInt(200));
            aluno.setBairro("Bairro Mock");
            aluno.setCidade("Cidade Mock");
            aluno.setUf("SP");
            aluno.setCep(String.format("%08d", random.nextInt(100000000)));
            aluno.setSenha("senha123"); // Senha padrão para todos os mocks
            alunos.add(aluno);
        }
        return alunos;
    }

    public static void main(String[] args) {
        List<AlunoDTO> alunosMock = gerarListaDeAlunos(30);
        for (AlunoDTO aluno : alunosMock) {
            System.out.println("{");
            System.out.println("  \"nome\": \"" + aluno.getNome() + "\",");
            System.out.println("  \"cpf\": \"" + aluno.getCpf() + "\",");
            System.out.println("  \"dataNascimento\": \"" + aluno.getDataNascimento() + "\",");
            System.out.println("  \"email\": \"" + aluno.getEmail() + "\",");
            System.out.println("  \"telefone\": \"" + aluno.getTelefone() + "\",");
            System.out.println("  \"endereco\": \"" + aluno.getEndereco() + "\",");
            System.out.println("  \"bairro\": \"" + aluno.getBairro() + "\",");
            System.out.println("  \"cidade\": \"" + aluno.getCidade() + "\",");
            System.out.println("  \"uf\": \"" + aluno.getUf() + "\",");
            System.out.println("  \"cep\": \"" + aluno.getCep() + "\",");
            System.out.println("  \"senha\": \"" + aluno.getSenha() + "\"");
            System.out.println("},");
        }
    }
}