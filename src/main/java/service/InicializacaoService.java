package service;

import exception.PersistenciaException;
import model.Disciplina.Disciplina;
import model.Evento.Evento;
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

    public InicializacaoService(LocalRepository localRepo,
                                DisciplinaRepository disciplinaRepo,
                                EventoRepository eventoRepo,
                                NpcRepository npcRepo,
                                SemestreRepository semestreRepo,
                                PersonagemRepository personagemRepo) {
        this.localRepo = localRepo;
        this.disciplinaRepo = disciplinaRepo;
        this.eventoRepo = eventoRepo;
        this.npcRepo = npcRepo;
        this.semestreRepo = semestreRepo;
        this.personagemRepo = personagemRepo;
    }

    /**
     * Carrega todos os repositórios e reconstrói as referências entre objetos.
     * @throws PersistenciaException se ocorrer falha ao carregar qualquer arquivo
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
    }
}
