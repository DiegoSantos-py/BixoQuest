package service;

import exception.PersistenciaException;
import model.Disciplina.Disciplina;
import model.Evento.Evento;
import model.Personagem;
import model.Tempo.Dia;
import model.Tempo.Semestre;
import repository.*;

import java.util.List;
import java.util.Optional;

public class GameService {

    private final DiaService diaService;
    private final SemestreService semestreService;
    private final PersonagemService personagemService;
    private final InicializacaoService inicializacaoService;
    private final EventoService eventoService;
    private final LocalRepository localRepo;
    private final SemestreRepository semestreRepo;
    private final DisciplinaRepository disciplinaRepo;
    private final EventoRepository eventoRepo;
    private final NpcRepository npcRepo;
    private final PersonagemRepository personagemRepo;

    private boolean diaTransicionado;
    private Semestre semestre;
    private Dia diaAtual;
    private int personagem;

    public GameService(DiaService diaService,
                       SemestreService semestreService,
                       PersonagemService personagemService,
                       InicializacaoService inicializacaoService,
                       EventoService eventoService,
                       LocalRepository localRepo,
                       SemestreRepository semestreRepo,
                       DisciplinaRepository disciplinaRepo,
                       EventoRepository eventoRepo,
                       NpcRepository npcRepo,
                       PersonagemRepository personagemRepo) {

        this.diaService = diaService;
        this.semestreService = semestreService;
        this.personagemService = personagemService;
        this.inicializacaoService = inicializacaoService;
        this.eventoService = eventoService;

        this.localRepo = localRepo;
        this.semestreRepo = semestreRepo;
        this.disciplinaRepo = disciplinaRepo;
        this.eventoRepo = eventoRepo;
        this.npcRepo = npcRepo;
        this.personagemRepo = personagemRepo;
    }

    /**
     * Inicializa o jogo — carrega todos os repositórios e reconstrói referências.
     * Se o personagem já existir, retoma a sessão anterior.
     * Se não existir, inicia um novo jogo.
     * lança PersistenciaException se ocorrer falha ao carregar qualquer arquivo
     */
    public void iniciarJogo(int personagem) throws PersistenciaException {
        this.personagem = personagem;
        inicializacaoService.inicializar();

        List<Semestre> semestresExistentes = semestreRepo.getSemestresPorJogador(personagem);

        if (!semestresExistentes.isEmpty()) {
            Semestre ultimo = semestresExistentes.get(semestresExistentes.size() - 1);

            if (!semestreService.terminouSemestre(ultimo)) {
                this.semestre = ultimo;
                diaAtual = semestreService.avancarDia(semestre);
                return;
            }
        }

        this.semestre = null;
    }

    /** Indica se o jogador precisa escolher disciplinas antes de continuar */
    public boolean precisaEscolherDisciplinas() {
        return semestre == null;
    }

    /** Disciplinas disponíveis pro jogador escolher no próximo semestre */
    public List<Disciplina> obterDisciplinasDisponiveis() {
        return semestreService.getDisciplinasDisponiveis(personagem);
    }

    /** Chamado pelo Controller depois que o jogador escolheu as disciplinas na View */
    public void confirmarEscolhaDisciplinas(List<Disciplina> disciplinasEscolhidas) throws PersistenciaException {
        this.semestre = semestreService.iniciarSemestreComEscolha(personagem, disciplinasEscolhidas);

        semestreRepo.adicionarSemestre(personagem, semestre);
        semestreRepo.salvar();

        resetarConhecimentosParaNovoSemestre(disciplinasEscolhidas);

        diaAtual = semestreService.avancarDia(semestre);
        iniciarProximoDia();
    }

    private void resetarConhecimentosParaNovoSemestre(List<Disciplina> disciplinasEscolhidas) throws PersistenciaException {
        Personagem p = personagemRepo.buscarPorId(personagem);
        for (Disciplina d : disciplinasEscolhidas) {
            p.getConhecimentos().put(d.getArea(), 10.0);
        }
        personagemRepo.salvar();
    }

    public boolean encerrarJogo() {
        List<Semestre> semestresJogador = semestreRepo.getSemestresPorJogador(personagem);
        // try/catch removido — lista vazia já resolve naturalmente pra false no loop abaixo

        for (List<Disciplina> lista : disciplinaRepo.carregarDisciplinas().values()) {
            for (Disciplina dSistema : lista) {
                boolean aprovado = false;
                for (Semestre s : semestresJogador) {
                    if (s.foiAprovado(dSistema)) {
                        aprovado = true;
                        break;
                    }
                }
                if (!aprovado) return false;
            }
        }

        return true;
    }


    /**
     * Atualiza o estado do jogo a cada ciclo.
     * Ao fim de cada dia salva todos os repositórios exceto localRepo.
     * lança PersistenciaException se ocorrer falha ao salvar
     */
    public void atualizar() throws PersistenciaException {
        diaTransicionado = false;

        if (semestre == null) return;
        if (!diaService.isDiaEncerrado()) return;

        diaService.encerrarDia(diaAtual);
        salvarEstado();

        if (!semestreService.terminouSemestre(semestre)) {
            diaAtual = semestreService.avancarDia(semestre); // só cria/avança o Dia, não inicia o timer
            diaTransicionado = true;
        } else {
            semestreService.encerrarSemestre(personagemRepo.buscarPorId(personagem), semestre);
            this.semestre = null;
            diaTransicionado = true;
        }
    }

    /** Chamado pelo runnable da cena "fim do dia", ao voltar pro jogo */
    public void iniciarProximoDia() {
        diaService.iniciarDia(diaAtual);
    }

    public boolean houveTransicaoDeDia() {
        return diaTransicionado;
    }

    public void pausarDia() {
        diaService.pausar();
    }

    public void retomarDia() {
        if (diaAtual != null) {
            diaService.retomar(diaAtual);
        }
    }

    public Optional<Evento> processarZona(String nomeZona) {
        if (diaAtual == null) return Optional.empty();

        Evento evento = diaAtual.getEventosObrigatorios().get(nomeZona);
        if (evento == null) {
            evento = diaAtual.getEventosAleatorios().get(nomeZona);
        }
        if (evento == null) return Optional.empty(); // não há evento nessa zona — cai no fallback de navegação

        Personagem personagemObj = personagemRepo.buscarPorId(personagem);

        if (!eventoService.podeExecutar(evento, personagemObj, semestre, diaAtual, diaService)) {
            return Optional.empty(); // existe evento, mas requisitos não atendidos
        }

        eventoService.executarEvento(evento, personagemObj, diaAtual, diaService);
        return Optional.of(evento); // evento executado — View decide o que exibir
    }

    /**
     * Salva o estado de todos os repositórios exceto localRepo.
     * Chamado ao fim de cada dia.
     * lança PersistenciaException se ocorrer falha ao salvar qualquer repositório
     */
    private void salvarEstado() throws PersistenciaException {
        personagemRepo.salvar();
        semestreRepo.salvar();
        disciplinaRepo.salvar();
        eventoRepo.salvar();
        npcRepo.salvar();
    }

    public Semestre getSemestre() { return semestre; }
    public int getDiaAtual() { return semestre.getDiaAtual(); }
    public Personagem getPersonagem() { return personagemRepo.buscarPorId(personagem); }
    public void setPersonagem(int personagem) { this.personagem = personagem; }

    public long getTempoRestanteSegundos() {
        if (diaAtual == null) return 0;
        return diaService.getTempoRestante(diaAtual);
    }
}