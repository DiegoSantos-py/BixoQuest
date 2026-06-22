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

public class GameService {

    private final DiaService diaService;
    private final SemestreService semestreService;
    private final PersonagemService personagemService;
    private final InicializacaoService inicializacaoService;
    private final LocalRepository localRepo;
    private final SemestreRepository semestreRepo;
    private final DisciplinaRepository disciplinaRepo;
    private final EventoRepository eventoRepo;
    private final NpcRepository npcRepo;
    private final PersonagemRepository personagemRepo;

    private Semestre semestre;
    private Dia diaAtual;
    private int personagem;

    public GameService(DiaService diaService,
                       SemestreService semestreService,
                       PersonagemService personagemService,
                       InicializacaoService inicializacaoService,
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

        // 1. Carrega e reconstrói todos os objetos do jogo
        inicializacaoService.inicializar();

        // 2. Verifica se o personagem já tem semestres salvos
        List<Semestre> semestresExistentes;
        try {
            semestresExistentes = semestreRepo.getSemestresPorJogador(personagem);
        } catch (Exception e) {
            semestresExistentes = null;
        }

        if (semestresExistentes != null && !semestresExistentes.isEmpty()) {
            // Retoma sessão anterior — usa o último semestre
            semestre = semestresExistentes.get(semestresExistentes.size() - 1);
        } else {
            // Novo jogo — cria primeiro semestre
            semestre = semestreService.iniciarPrimeiroSemestre(personagem);
        }

        // 3. Avança para o próximo dia
        diaAtual = semestreService.avancarDia(semestre);

        // 5. Inicia o dia
        diaService.iniciarDia(diaAtual);
    }

    /**
     * Atualiza o estado do jogo a cada ciclo.
     * Ao fim de cada dia salva todos os repositórios exceto localRepo.
     * lança PersistenciaException se ocorrer falha ao salvar
     */
    public void atualizar() throws PersistenciaException {
        if (!diaService.isDiaEncerrado()) return;

        diaService.encerrarDia(diaAtual);

        // Salva o estado de todos os repositórios ao fim do dia
        salvarEstado();

        if (!semestreService.terminouSemestre(semestre)) {
            // Avança para o próximo dia
            diaAtual = semestreService.avancarDia(semestre);

            diaService.iniciarDia(diaAtual);
        } else {
            // Encerra o semestre e inicia o próximo
            semestre = semestreService.encerrarSemestre(personagemRepo.buscarPorId(personagem), semestre);
        }
    }

    /**
     * Verifica se o jogador concluiu o jogo aprovando em todas as disciplinas.
     * Percorre todos os semestres do jogador pelo SemestreRepository.
     */
    public boolean encerrarJogo() {
        List<Semestre> semestresJogador;
        try {
            semestresJogador = semestreRepo.getSemestresPorJogador(personagem);
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
}