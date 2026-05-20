package service;

import exception.Disciplina.DisciplinaNaoEncontradaException;
import exception.PersistenciaException;
import exception.Semestre.SemestreInvalidoException;
import exception.Semestre.SemestreNaoEncontradoException;
import model.Personagem;
import model.Disciplina.Disciplina;
import model.Tempo.Dia;
import model.Tempo.Semestre;
import repository.DisciplinaRepository;
import repository.SemestreRepository;

import java.util.ArrayList;
import java.util.List;

public class SemestreService {

    private final SemestreRepository semestreRepo;
    private final DisciplinaRepository disciplinaRepo;

    public SemestreService(SemestreRepository semestreRepo,
                           DisciplinaRepository disciplinaRepo) {
        this.semestreRepo = semestreRepo;
        this.disciplinaRepo = disciplinaRepo;
    }

    // Inicialização
    /**@throws PersistenciaException se ocorrer falha ao carregar o arquivo*/
    public void carregar() throws PersistenciaException {
        semestreRepo.carregar();
    }

    /**@throws PersistenciaException se ocorrer falha ao salvar o arquivo*/
    public void salvar() throws PersistenciaException {
        semestreRepo.salvar();
    }

    // Leitura
    /**@throws SemestreNaoEncontradoException se não houver semestres para o jogador*/
    public List<Semestre> getSemestresPorJogador(int jogadorId) {
        return semestreRepo.getSemestresPorJogador(jogadorId);
    }

    // Lógica de negócio

    public Semestre criarSemestre() {
        return new Semestre();
    }

    public Semestre iniciarPrimeiroSemestre(int jogadorId) {
        Semestre semestre = criarSemestre();
        semestre.setDisciplinas(disciplinaRepo.buscarDisciplinasIniciais());
        return semestre;
    }

    /**@throws SemestreInvalidoException se o semestre já tiver terminado*/
    public Dia avancarDia(Semestre semestre) {
        if (semestre.terminou()) {
            throw new SemestreInvalidoException("semestre", "já atingiu o número máximo de dias");
        }

        Dia novoDia = new Dia();
        semestre.adicionarDia(novoDia);
        return novoDia;
    }

    /**@throws SemestreInvalidoException se a disciplina já pertencer ao semestre*/
    public void adicionarDisciplina(Semestre semestre, Disciplina disciplina) {
        if (semestre.getDisciplinas().contains(disciplina)) {
            throw new SemestreInvalidoException("disciplina",
                    "já pertence ao semestre");
        }

        semestre.adicionarDisciplinas(disciplina);
    }

    public boolean terminouSemestre(Semestre semestre) {
        return semestre.terminou();
    }

    /**@throws SemestreInvalidoException  se o semestre for nulo
     @throws PersistenciaException       se ocorrer falha ao salvar após encerramento*/
    public Semestre encerrarSemestre(Personagem personagem, Semestre semestre)
            throws PersistenciaException {
        if (semestre == null) {
            throw new SemestreInvalidoException("semestre", "não pode ser nulo");
        }

        if (!semestre.terminou()) {
            return semestre;
        }

        personagem.adicionarSemestre(semestre);
        semestreRepo.adicionarSemestre(personagem.getPersonagemId(), semestre);

        Semestre novoSemestre = new Semestre();
        List<Disciplina> novasDisciplinas = new ArrayList<>();

        for (Disciplina atual : semestre.getDisciplinas()) {
            if (semestre.foiAprovado(atual)) {
                try {
                    Disciplina proxima = disciplinaRepo.proximaDisciplina(
                            atual.getNome(), atual.getCodigo());
                    novasDisciplinas.add(proxima);
                } catch (DisciplinaNaoEncontradaException e) {
                    // disciplina não tem próximo nível — não adiciona ao novo semestre
                }
            } else {
                novasDisciplinas.add(atual);
            }
        }

        novoSemestre.setDisciplinas(novasDisciplinas);
        semestreRepo.adicionarSemestre(personagem.getPersonagemId(), novoSemestre);
        semestreRepo.salvar();

        return novoSemestre;
    }

    /**@throws SemestreInvalidoException se semestre ou disciplina forem nulos*/
    public void definirResultadoDisciplina(Semestre semestre, Disciplina disciplina,
                                           boolean aprovado) {
        if (semestre == null) {
            throw new SemestreInvalidoException("semestre", "não pode ser nulo");
        }
        if (disciplina == null) {
            throw new SemestreInvalidoException("disciplina", "não pode ser nula");
        }

        semestre.registrarResultado(disciplina, aprovado);
    }
}