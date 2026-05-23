package service;

import exception.PersistenciaException;
import model.Disciplina.Disciplina;
import model.Local.TipoLocal;
import model.Local.Local;
import model.Personagem;
import model.Tempo.Dia;
import model.Tempo.Semestre;
import repository.*;

import java.util.List;
import java.util.Map;

public class GameService {

    private final DiaService diaService;
    private final SemestreService semestreService;
    private final PersonagemService personagemService;
    private final InicializacaoService inicializacaoService;
    private final LocalRepository localRepo;
    private final SemestreRepository semestreRepo;
    private final DisciplinaRepository disciplinaRepo;

    private Semestre semestre;
    private Dia diaAtual;
    private Personagem personagem;

    public GameService(DiaService diaService,
                       SemestreService semestreService,
                       PersonagemService personagemService,
                       InicializacaoService inicializacaoService,
                       LocalRepository localRepo,
                       SemestreRepository semestreRepo,
                       DisciplinaRepository disciplinaRepo) {
        this.diaService = diaService;
        this.semestreService = semestreService;
        this.personagemService = personagemService;
        this.inicializacaoService = inicializacaoService;
        this.localRepo = localRepo;
        this.semestreRepo = semestreRepo;
        this.disciplinaRepo = disciplinaRepo;
    }

    /**
     * Inicializa o jogo — carrega todos os repositórios e reconstrói referências.
     * Se o personagem já existir, retoma a sessão anterior.
     * Se não existir, inicia um novo jogo.
     * @throws PersistenciaException se ocorrer falha ao carregar qualquer arquivo
     */
    public void iniciarJogo(Personagem personagem) throws PersistenciaException {
        this.personagem = personagem;

        // 1. Carrega e reconstrói todos os objetos do jogo
        inicializacaoService.inicializar();

        // 2. Verifica se o personagem já tem semestres salvos
        List<Semestre> semestresExistentes;
        try {
            semestresExistentes = semestreRepo.getSemestresPorJogador(personagem.getPersonagemId());
        } catch (Exception e) {
            semestresExistentes = null;
        }

        if (semestresExistentes != null && !semestresExistentes.isEmpty()) {
            // Retoma sessão anterior — usa o último semestre
            semestre = semestresExistentes.get(semestresExistentes.size() - 1);
        } else {
            // Novo jogo — cria primeiro semestre
            semestre = semestreService.iniciarPrimeiroSemestre(personagem.getPersonagemId());
        }

        // 3. Avança para o próximo dia
        diaAtual = semestreService.avancarDia(semestre);

        // 4. Define posição inicial no ponto de ônibus
        Local pontoOnibus = localRepo.buscarPorTipo(TipoLocal.PONTO_ONIBUS);
        personagem.setLocalAtual(pontoOnibus);
        personagem.setcX(0);
        personagem.setcY(0);
        personagem.setEnergia(40.0);

        // 5. Inicia o dia
        diaService.iniciarDia(diaAtual);
    }

    /**
     * Atualiza o estado do jogo a cada ciclo.
     * Verifica se o dia encerrou e avança ou encerra o semestre conforme necessário.
     * @throws PersistenciaException se ocorrer falha ao salvar após encerramento de semestre
     */
    public void atualizar() throws PersistenciaException {
        if (!diaService.isDiaEncerrado()) return;

        diaService.encerrarDia(diaAtual);

        if (!semestreService.terminouSemestre(semestre)) {
            // Avança para o próximo dia
            diaAtual = semestreService.avancarDia(semestre);

            Local pontoOnibus = localRepo.buscarPorTipo(TipoLocal.PONTO_ONIBUS);
            personagem.setLocalAtual(pontoOnibus);
            personagem.setcX(0);
            personagem.setcY(0);

            diaService.iniciarDia(diaAtual);
        } else {
            // Encerra o semestre e inicia o próximo
            semestre = semestreService.encerrarSemestre(personagem, semestre);
        }
    }

    /**
     * Verifica se o jogador concluiu o jogo aprovando em todas as disciplinas.
     * Percorre todos os semestres do jogador pelo SemestreRepository.
     */
    public boolean encerrarJogo() {
        List<Semestre> semestresJogador;
        try {
            semestresJogador = semestreRepo.getSemestresPorJogador(personagem.getPersonagemId());
        } catch (Exception e) {
            return false;
        }

        // Percorre todas as disciplinas do jogo
        for (List<Disciplina> lista : disciplinaRepo.carregarDisciplinas().values()) {
            for (Disciplina dSistema : lista) {
                boolean aprovado = false;

                // Verifica se o jogador foi aprovado em algum semestre
                for (Semestre s : semestresJogador) {
                    if (s.foiAprovado(dSistema)) {
                        aprovado = true;
                        break;
                    }
                }

                // Se falhou em qualquer disciplina — não concluiu o jogo
                if (!aprovado) return false;
            }
        }

        return true;
    }

    public Semestre getSemestre() { return semestre; }
    public Dia getDiaAtual() { return diaAtual; }
    public Personagem getPersonagem() { return personagem; }
    public void setPersonagem(Personagem personagem) {
        this.personagem = personagem;
    }
}
