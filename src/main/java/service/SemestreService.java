package service;

import exception.Disciplina.DisciplinaInvalidaException;
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
import java.util.Map;

public class SemestreService {

    private final SemestreRepository semestreRepo;
    private final DisciplinaRepository disciplinaRepo;
    private static final int MAX_DISCIPLINAS_POR_SEMESTRE = 3;
    private static final String NOME_TCC = "TCC";
    private static final String NOME_ESTAGIO = "Estágio";

    public SemestreService(SemestreRepository semestreRepo,
                           DisciplinaRepository disciplinaRepo) {
        this.semestreRepo = semestreRepo;
        this.disciplinaRepo = disciplinaRepo;
    }

    // Inicialização
    /**lança PersistenciaException se ocorrer falha ao carregar o arquivo*/
    public void carregar() throws PersistenciaException {
        semestreRepo.carregar();
    }

    /**lança PersistenciaException se ocorrer falha ao salvar o arquivo*/
    public void salvar() throws PersistenciaException {
        semestreRepo.salvar();
    }

    // Leitura
    /** Retorna lista vazia se o jogador ainda não tiver semestres */
    public List<Semestre> getSemestresPorJogador(int jogadorId) {
        return semestreRepo.getSemestresPorJogador(jogadorId);
    }

    // Lógica de negócio

    public Semestre criarSemestre(Personagem personagem) {
        Semestre semestre = new Semestre();
        int numero = personagem.getSemestres().size() + 1; // próximo número, baseado no histórico já encerrado
        semestre.setNumeroSemestre(numero);
        return semestre;
    }

    public Semestre iniciarSemestreComEscolha(int jogadorId, List<Disciplina> disciplinasEscolhidas, Personagem personagem) {
        if (disciplinasEscolhidas == null || disciplinasEscolhidas.isEmpty()) {
            throw new SemestreInvalidoException("disciplinas", "é necessário escolher ao menos uma disciplina");
        }

        if (disciplinasEscolhidas.size() > MAX_DISCIPLINAS_POR_SEMESTRE) {
            throw new SemestreInvalidoException("disciplinas",
                    "não é possível escolher mais de " + MAX_DISCIPLINAS_POR_SEMESTRE + " disciplinas por semestre");
        }

        List<Disciplina> disponiveis = getDisciplinasDisponiveis(jogadorId);

        for (Disciplina escolhida : disciplinasEscolhidas) {
            if (!disponiveis.contains(escolhida)) {
                throw new DisciplinaInvalidaException(
                        escolhida.getNome(),
                        "não está disponível para escolha neste semestre"
                );
            }
        }

        Semestre semestre = criarSemestre(personagem);
        semestre.setDisciplinas(disciplinasEscolhidas);
        return semestre;
    }

    public List<Disciplina> getDisciplinasDisponiveis(int jogadorId) {
        List<Semestre> historico = semestreRepo.getSemestresPorJogador(jogadorId);
        Map<String, List<Disciplina>> catalogo = disciplinaRepo.carregarDisciplinas();

        List<Disciplina> disponiveis = new ArrayList<>();

        for (Map.Entry<String, List<Disciplina>> entry : catalogo.entrySet()) {
            String nomeArea = entry.getKey();
            List<Disciplina> niveis = entry.getValue();

            // TCC e Estágio exigem checagem especial de elegibilidade, tratados fora do loop principal
            if (nomeArea.equals(NOME_TCC) || nomeArea.equals(NOME_ESTAGIO)) {
                continue;
            }

            float ultimoAprovado = 0;
            for (Disciplina d : niveis) {
                if (foiAprovadaEmAlgumSemestre(historico, d) && d.getCodigo() > ultimoAprovado) {
                    ultimoAprovado = d.getCodigo();
                }
            }

            final float proximoNivel = ultimoAprovado + 1;

            niveis.stream()
                    .filter(d -> d.getCodigo() == proximoNivel)
                    .findFirst()
                    .ifPresent(disponiveis::add);
        }

        if (elegivelParaTcc(jogadorId, historico, catalogo)) {
            catalogo.getOrDefault(NOME_TCC, List.of()).stream()
                    .filter(d -> !foiAprovadaEmAlgumSemestre(historico, d))
                    .findFirst()
                    .ifPresent(disponiveis::add);
        }

        if (elegivelParaEstagio(jogadorId, historico, catalogo)) {
            catalogo.getOrDefault(NOME_ESTAGIO, List.of()).stream()
                    .filter(d -> !foiAprovadaEmAlgumSemestre(historico, d))
                    .findFirst()
                    .ifPresent(disponiveis::add);
        }

        return disponiveis;
    }

    /**
     * TCC só é elegível se todas as disciplinas do catálogo, exceto Estágio1,
     * já tiverem sido aprovadas em algum semestre anterior.
     */
    private boolean elegivelParaTcc(int jogadorId, List<Semestre> historico,
                                    Map<String, List<Disciplina>> catalogo) {
        for (Map.Entry<String, List<Disciplina>> entry : catalogo.entrySet()) {
            String nomeArea = entry.getKey();

            if (nomeArea.equals(NOME_ESTAGIO) || nomeArea.equals(NOME_TCC)) {
                continue;
            }

            for (Disciplina d : entry.getValue()) {
                if (!foiAprovadaEmAlgumSemestre(historico, d)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Estágio só é elegível se todas as disciplinas regulares do catálogo,
     * exceto TCC, já tiverem sido aprovadas em algum semestre anterior.
     */
    private boolean elegivelParaEstagio(int jogadorId, List<Semestre> historico,
                                        Map<String, List<Disciplina>> catalogo) {
        for (Map.Entry<String, List<Disciplina>> entry : catalogo.entrySet()) {
            String nomeArea = entry.getKey();

            if (nomeArea.equals(NOME_ESTAGIO) || nomeArea.equals(NOME_TCC)) {
                continue;
            }

            for (Disciplina d : entry.getValue()) {
                if (!foiAprovadaEmAlgumSemestre(historico, d)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean foiAprovadaEmAlgumSemestre(List<Semestre> historico, Disciplina d) {
        return historico.stream().anyMatch(s -> s.foiAprovado(d));
    }
    /**lança SemestreInvalidoException se o semestre já tiver terminado*/
    public Dia avancarDia(Semestre semestre) {
        if (semestre.terminou()) {
            throw new SemestreInvalidoException("semestre", "já atingiu o número máximo de dias");
        }

        Dia novoDia = new Dia();
        semestre.adicionarDia(novoDia);
        return novoDia;
    }

    /**lança SemestreInvalidoException se a disciplina já pertencer ao semestre*/
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

    /**lança SemestreInvalidoException  se o semestre for nulo
     lança PersistenciaException se ocorrer falha ao salvar após encerramento*/
    public void encerrarSemestre(Personagem personagem, Semestre semestre) throws PersistenciaException {
        if (semestre == null) {
            throw new SemestreInvalidoException("semestre", "não pode ser nulo");
        }

        if (!semestre.terminou()) {
            return;
        }

        personagem.adicionarSemestre(semestre);
        semestreRepo.adicionarSemestre(personagem.getPersonagemId(), semestre);
        semestreRepo.salvar();
    }

    /**lança SemestreInvalidoException se semestre ou disciplina forem nulos*/
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