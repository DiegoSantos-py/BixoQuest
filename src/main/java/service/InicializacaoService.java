package service;

import exception.PersistenciaException;
import model.Disciplina.AreaConhecimento;
import model.Disciplina.Disciplina;
import model.Evento.Evento;
import model.Evento.Prova.ProvaIDs;
import model.Local.Local;
import model.Personagem;
import model.Tempo.Dia;
import model.Tempo.Semestre;
import repository.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public class InicializacaoService {

    private final LocalRepository localRepo;
    private final DisciplinaRepository disciplinaRepo;
    private final EventoRepository eventoRepo;
    private final NpcRepository npcRepo;
    private final SemestreRepository semestreRepo;
    private final PersonagemRepository personagemRepo;
    private final DisciplinaService disciplinaService;

    public InicializacaoService(LocalRepository localRepo,
                                DisciplinaRepository disciplinaRepo,
                                EventoRepository eventoRepo,
                                NpcRepository npcRepo,
                                SemestreRepository semestreRepo,
                                PersonagemRepository personagemRepo,
                                DisciplinaService disciplinaService) {
        this.localRepo = localRepo;
        this.disciplinaRepo = disciplinaRepo;
        this.eventoRepo = eventoRepo;
        this.npcRepo = npcRepo;
        this.semestreRepo = semestreRepo;
        this.personagemRepo = personagemRepo;
        this.disciplinaService = disciplinaService;
    }

    /**
      Carrega todos os repositórios e reconstrói as referências entre objetos.
      lança PersistenciaException se ocorrer falha ao carregar qualquer arquivo
     */
    public void inicializar() throws PersistenciaException {

        // 1. Carregar repositórios na ordem correta
        localRepo.carregar();
        disciplinaRepo.carregar();
        eventoRepo.carregar();
        npcRepo.carregar();
        semestreRepo.carregar();
        personagemRepo.carregar();

        // 2. Reconstruir localAtual de cada personagem
        for (Personagem p : personagemRepo.carregarPersonagens().values()) {
            String nomeLocal = p.getLocalAtualNome();
            if (nomeLocal != null) {
                try {
                    p.setLocalAtual(localRepo.buscarPorNome(nomeLocal));
                } catch (Exception e) {
                    // local pode não existir mais — mantém null
                }
            }
        }

        // 3. Reconstruir resultados de cada semestre
        for (List<Semestre> lista : semestreRepo.carregarSemestres().values()) {
            for (Semestre semestre : lista) {
                for (Map.Entry<String, Boolean> entry : semestre.getResultadosNomes().entrySet()) {
                    String[] partes = entry.getKey().split(":");
                    String nome = partes[0];
                    float codigo = Float.parseFloat(partes[1]);
                    try {
                        Disciplina disciplina = disciplinaRepo.buscar(nome, codigo);
                        semestre.getResultados().put(disciplina, entry.getValue());
                    } catch (Exception e) {
                        // disciplina pode não existir mais — ignora
                    }
                }
            }
        }

        // 4. Reconstruir eventos de cada dia e redefinir início
        for (List<Semestre> lista : semestreRepo.carregarSemestres().values()) {
            for (Semestre semestre : lista) {
                for (Dia dia : semestre.getDias()) {

                    // Reconstrói eventos obrigatórios
                    for (String nome : dia.getEventosObrigatoriosNomes()) {
                        try {
                            Evento evento = eventoRepo.buscarPorNome(nome);
                            dia.getEventosObrigatorios().put(nome, evento);
                        } catch (Exception e) {
                            // evento pode não existir mais — ignora
                        }
                    }

                    // Reconstrói eventos aleatórios
                    for (String nome : dia.getEventosAleatoriosNomes()) {
                        try {
                            Evento evento = eventoRepo.buscarPorNome(nome);
                            dia.getEventosAleatorios().put(nome, evento);
                        } catch (Exception e) {
                            // evento pode não existir mais — ignora
                        }
                    }

                    // Redefine início do dia
                    dia.setInicio(Instant.now());
                }
            }
        }
        inicializarCatalogoDisciplinas(disciplinaService);
    }

    public void inicializarCatalogoDisciplinas(DisciplinaService disciplinaService) throws PersistenciaException {
        disciplinaService.carregar();

        if (!disciplinaService.carregarDisciplinas().isEmpty()) {
            return;
        }

        disciplinaService.criarDisciplinasPorNivel("Matemática", 3, AreaConhecimento.MAT);
        disciplinaService.criarDisciplinasPorNivel("Naturezas", 3, AreaConhecimento.NAT);
        disciplinaService.criarDisciplinasPorNivel("Software", 3, AreaConhecimento.SOF);
        disciplinaService.criarDisciplinasPorNivel("Hardware", 3, AreaConhecimento.HAR);
        disciplinaService.criarDisciplinasPorNivel("TCC", 1, AreaConhecimento.TCC);
        disciplinaService.criarDisciplinasPorNivel("Estágio", 1, AreaConhecimento.EST);

        associarProvaIds(disciplinaService);
    }

    private void associarProvaIds(DisciplinaService disciplinaService) throws PersistenciaException {
        associar(disciplinaService, "Matemática", 1, ProvaIDs.MAT_01);
        associar(disciplinaService, "Matemática", 2, ProvaIDs.MAT_02);
        associar(disciplinaService, "Matemática", 3, ProvaIDs.MAT_03);

        associar(disciplinaService, "Naturezas", 1, ProvaIDs.NAT_01);
        associar(disciplinaService, "Naturezas", 2, ProvaIDs.NAT_02);
        associar(disciplinaService, "Naturezas", 3, ProvaIDs.NAT_03);

        associar(disciplinaService, "Software", 1, ProvaIDs.SOFT_01);
        associar(disciplinaService, "Software", 2, ProvaIDs.SOFT_02);
        associar(disciplinaService, "Software", 3, ProvaIDs.SOFT_03);

        associar(disciplinaService, "Hardware", 1, ProvaIDs.HARD_01);
        associar(disciplinaService, "Hardware", 2, ProvaIDs.HARD_02);
        associar(disciplinaService, "Hardware", 3, ProvaIDs.HARD_03);

        associar(disciplinaService, "TCC", 1, ProvaIDs.TCC);
        associar(disciplinaService, "Estágio", 1, ProvaIDs.ESTAGIO);

        disciplinaService.salvar();
    }

    private void associar(DisciplinaService disciplinaService, String nome, float codigo, ProvaIDs provaId) {
        Disciplina d = disciplinaService.buscar(nome, codigo);
        d.setProvaId(provaId);
    }
}
