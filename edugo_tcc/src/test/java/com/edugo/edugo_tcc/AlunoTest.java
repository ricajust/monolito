package com.edugo.edugo_tcc;

import org.junit.jupiter.api.Test;

import com.edugo.edugo_tcc.model.Aluno;
import com.edugo.edugo_tcc.model.Disciplina;
import com.edugo.edugo_tcc.model.Matricula;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

class AlunoTest {

    private Aluno aluno;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        aluno = new Aluno();
    }

    @Test
    void testAlunoConstructorAndGetters() {
        List<Matricula> matriculas = new ArrayList<>();
        aluno = new Aluno(matriculas);

        assertNotNull(aluno);
        assertEquals(matriculas, aluno.getMatricula());
    }

    @Test
    void testSetters() {
        List<Matricula> matriculas = new ArrayList<>();
        aluno.setMatricula(matriculas);

        assertEquals(matriculas, aluno.getMatricula());
    }

    @Test
    void testRealizaMatricula() {
        Disciplina disciplina = new Disciplina();
        disciplina.setNome("Matemática");
        Matricula matricula = aluno.realizaMatricula(disciplina);
    
        assertNotNull(matricula);
        assertEquals(disciplina, matricula.getDisciplina());
    }

    @Test
    void testNomeNotBlank() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Aluno aluno = new Aluno(); // Cria um aluno com o nome em branco
        aluno.setNome("");

        Set<ConstraintViolation<Aluno>> violations = validator.validate(aluno);

        assertFalse(violations.isEmpty()); // Verifica se há violações de restrição
    }

    @Test
    void testNomeNotBlankValido() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Aluno aluno = new Aluno();
        aluno.setNome("Nome Válido");

        Set<ConstraintViolation<Aluno>> violations = validator.validate(aluno);

        assertTrue(violations.isEmpty());
    }

    @Test
    void testAlunoCreationAndAttributes() {
        aluno.setNome("João da Silva");
        aluno.setDataNascimento(LocalDate.of(2000, 1, 1));
        aluno.setEmail("joao@email.com");
        aluno.setMatricula(new ArrayList<>());
        aluno.setCpf("123.456.789-00");
        aluno.setTelefone("123456789");
        aluno.setEndereco("Rua A");
        aluno.setBairro("Centro");
        aluno.setCidade("São Paulo");
        aluno.setUf("SP");
        aluno.setCep("12345678");
        aluno.setSenha("senha123");

        assertThat(aluno.getNome()).isEqualTo("João da Silva");
        assertThat(aluno.getDataNascimento()).isEqualTo(LocalDate.of(2000, 1, 1));
        assertThat(aluno.getEmail()).isEqualTo("joao@email.com");
        assertThat(aluno.getMatricula()).isNotNull();
        assertThat(aluno.getCpf()).isEqualTo("123.456.789-00");
        assertThat(aluno.getTelefone()).isEqualTo("123456789");
        assertThat(aluno.getEndereco()).isEqualTo("Rua A");
        assertThat(aluno.getBairro()).isEqualTo("Centro");
        assertThat(aluno.getCidade()).isEqualTo("São Paulo");
        assertThat(aluno.getUf()).isEqualTo("SP");
        assertThat(aluno.getCep()).isEqualTo("12345678");
        assertThat(aluno.getSenha()).isEqualTo("senha123");
    }
    
    @Test
    void testNoArgsConstructor() {
        Aluno alunoSemArgs = new Aluno();
        assertNotNull(alunoSemArgs);
    }

    @Test
    void testAllArgsConstructor() {
        List<Matricula> matriculas = new ArrayList<>();
        Aluno alunoComArgs = new Aluno(matriculas);

        assertNotNull(alunoComArgs);
        assertEquals(matriculas, alunoComArgs.getMatricula());
    }
}