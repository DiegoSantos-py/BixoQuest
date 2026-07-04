package service;

import exception.PersistenciaException;
import model.Disciplina.AreaConhecimento;
import model.Disciplina.Disciplina;
import model.Evento.Evento;
import model.Evento.EventoAleatorio;
import model.Evento.Prova.ProvaBatalha;
import model.Evento.Prova.ProvaFactory;
import model.Evento.Prova.ProvaIDs;
import model.Evento.ResultadoZona;
import model.Local.ZonaInterativa;
import model.Personagem;
import model.Tempo.Dia;
import model.Tempo.Semestre;
import repository.*;

import java.util.*;

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
    private final MapaService mapaService;

    private boolean diaTransicionado;
    private Semestre semestre;
    private Dia diaAtual;
    private int personagem;

    public GameService(DiaService diaService,
                       SemestreService semestreService,
                       PersonagemService personagemService,
                       InicializacaoService inicializacaoService,
                       EventoService eventoService,
                       MapaService mapaService,
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
        this.mapaService = mapaService;

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
        Personagem p = personagemRepo.buscarPorId(personagem);

        this.semestre = semestreService.iniciarSemestreComEscolha(personagem, disciplinasEscolhidas, p);

        for (Disciplina d : disciplinasEscolhidas) {
            p.getConhecimentos().put(d.getArea(), 0.0);
        }
        personagemRepo.salvar();

        semestreRepo.adicionarSemestre(personagem, semestre);
        semestreRepo.salvar();

        diaAtual = semestreService.avancarDia(semestre);
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
        List<Evento> obrigatorios = montarEventosObrigatoriosDoDia();
        List<EventoAleatorio> aleatorios = List.of(); // eventos aleatórios ainda não modelados nesse fluxo

        diaService.gerarEventosDoDia(diaAtual, obrigatorios, aleatorios);
        diaService.iniciarDia(diaAtual);
    }

    public void consumirTempoProva(int minutos) {
        if (diaAtual == null) return;
        long restante = diaService.getTempoRestante(diaAtual) / 60;
        diaService.consumirTempoEvento(diaAtual, (int) Math.min(minutos, restante));
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

    public ResultadoZona processarZona(String nomeZona) {
        if (diaAtual == null) return ResultadoZona.semEvento();

        Evento evento = diaAtual.getEventosObrigatorios().get(nomeZona);
        if (evento == null) {
            evento = diaAtual.getEventosAleatorios().get(nomeZona);
        }

        if (evento == null) {
            if (ZONA_PARA_AREA.containsKey(nomeZona)) {
                return ResultadoZona.requisitoNaoAtendido("Você não está cursando essa disciplina");
            }
            return ResultadoZona.semEvento();
        }

        Personagem personagemObj = personagemRepo.buscarPorId(personagem);

        if (evento instanceof ProvaBatalha prova) {
            ResultadoZona bloqueio = eventoService.podeExecutar(evento, personagemObj, semestre, diaAtual, diaService);
            if (bloqueio != null) return bloqueio;

            ProvaIDs provaId = resolverProvaId(prova);
            if (provaId == null) return ResultadoZona.requisitoNaoAtendido("Prova não configurada corretamente");

            return ResultadoZona.provaBatalha(prova, provaId);
        }

        ResultadoZona bloqueio = eventoService.podeExecutar(evento, personagemObj, semestre, diaAtual, diaService);
        if (bloqueio != null) return bloqueio;

        eventoService.executarEvento(evento, personagemObj, diaAtual, diaService);
        return ResultadoZona.executado(evento);
    }

    private ProvaIDs resolverProvaId(ProvaBatalha prova) {
        if (semestre == null) return null;

        return semestre.getDisciplinas().stream()
                .filter(d -> d.getArea() == prova.getAreaConhecimento()
                        && d.getCodigo() == prova.getNivelDisciplina())
                .map(Disciplina::getProvaId)
                .findFirst()
                .orElse(null);
    }

    // GameService
    public void confirmarResultadoProva(ProvaIDs provaId, boolean aprovado) throws PersistenciaException {
        Disciplina disciplina = semestre.getDisciplinas().stream()
                .filter(d -> d.getProvaId() == provaId)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Disciplina não encontrada para prova: " + provaId));

        semestreService.definirResultadoDisciplina(semestre, disciplina, aprovado);
        semestreRepo.salvar();
    }

    private static final Map<String, AreaConhecimento> ZONA_PARA_AREA = Map.of(
            "Sala matemática", AreaConhecimento.MAT,
            "Sala software", AreaConhecimento.SOF,
            "Sala naturezas", AreaConhecimento.NAT,
            "Sala hardware", AreaConhecimento.HAR,
            "Sala tcc", AreaConhecimento.TCC,
            "Sala estágio", AreaConhecimento.EST
    );

    private static final Map<String, String> ZONA_PARA_NOME_EVENTO_ESTUDO = Map.of(
            "Sala matemática", "Estudar Matemática",
            "Sala software", "Estudar Software",
            "Sala naturezas", "Estudar Naturezas",
            "Sala hardware", "Estudar Hardware",
            "Sala tcc", "Estudar TCC",
            "Sala estágio", "Estudar Estágio"
    );

    private static final Set<String> ZONAS_RESERVADAS_DISCIPLINA = Set.of(
            "Sala matemática", "Sala software", "Sala naturezas",
            "Sala hardware", "Sala tcc", "Sala estágio"
    );

    private List<Evento> montarEventosObrigatoriosDoDia() {
        List<Evento> eventos = new ArrayList<>();

        if (semestre == null) return eventos;

        boolean periodoDeProvas = semestre.estaEmPeriodoDeProvas();

        for (Map.Entry<String, AreaConhecimento> entry : ZONA_PARA_AREA.entrySet()) {
            String nomeZona = entry.getKey();
            AreaConhecimento area = entry.getValue();

            semestre.getDisciplinas().stream()
                    .filter(d -> d.getArea() == area)
                    .findFirst()
                    .ifPresent(disciplina -> {
                        if (periodoDeProvas) {
                            if (disciplina.getProvaId() == null) return;
                            ProvaBatalha prova = ProvaFactory.criar(disciplina.getProvaId());
                            prova.setDisciplinaRequisitoNome(disciplina.getNome());
                            prova.setDisciplinaRequisitoCodigo(disciplina.getCodigo());
                            eventos.add(prova);
                        } else {
                            String nomeEventoEstudo = ZONA_PARA_NOME_EVENTO_ESTUDO.get(nomeZona);
                            Evento eventoEstudo = eventoService.buscarPorNome(nomeEventoEstudo);
                            eventos.add(eventoEstudo);
                        }
                    });
        }

        return eventos;
    }

    public void debugForcarFimDeSemestre() throws PersistenciaException {
        if (semestre == null || diaAtual == null) return;

        while (!semestreService.terminouSemestre(semestre)) {
            diaService.encerrarDia(diaAtual);
            diaAtual = semestreService.avancarDia(semestre);
        }

        // dispara o mesmo caminho que atualizar() usaria ao detectar fim de semestre
        semestreService.encerrarSemestre(personagemRepo.buscarPorId(personagem), semestre);
        this.semestre = null;
        diaTransicionado = true; // reaproveita o flag existente, se CenaJogo estiver checando isso
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