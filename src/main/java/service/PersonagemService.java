package service;

import exception.Personagem.PersonagemDuplicadoException;
import exception.Personagem.PersonagemInvalidoException;
import exception.Personagem.PersonagemNaoEncontradoException;
import exception.PersistenciaException;
import model.Disciplina.AreaConhecimento;
import model.Disciplina.Disciplina;
import model.Evento.Evento;
import model.Local.Area;
import model.Local.Direcao;
import model.Local.Local;
import model.Local.ZonaInterativa;
import model.Personagem;
import model.Tempo.Dia;
import model.Tempo.Semestre;
import repository.PersonagemRepository;

import java.util.*;

public class PersonagemService {

    private final PersonagemRepository personagemRepo;
    private EventoService eventoService;

    public PersonagemService(PersonagemRepository personagemRepo, EventoService eventoService) {
        this.personagemRepo = personagemRepo;
        this.eventoService = eventoService;
    }

    // Inicialização
    /**lança PersistenciaException se ocorrer falha ao carregar o arquivo */
    public void carregar() throws PersistenciaException {
        personagemRepo.carregar();
    }

    /**lança PersistenciaException se ocorrer falha ao salvar o arquivo */
    public void salvar() throws PersistenciaException {
        personagemRepo.salvar();
    }

    // Escrita
    /**
     *lança PersonagemInvalidoException   se nome for nulo/vazio ou atributos negativos
     *lança PersonagemDuplicadoException  se já existir personagem com o mesmo id
     *lança PersistenciaException         se ocorrer falha ao salvar após criação
     */
    public Personagem criarESalvarPersonagem(String nome,
                                             double energia,
                                             double motivacao,
                                             double saude,
                                             double dinheiro,
                                             String spriteDir,
                                             Local localInicial,
                                             int posX,
                                             int posY,
                                             int personagemId) throws PersistenciaException {

        Personagem personagem = criarPersonagem(nome, energia, motivacao, saude, dinheiro,
                spriteDir, localInicial, posX, posY, personagemId);
        personagemRepo.adicionarPersonagem(personagem);
        personagemRepo.salvar();

        return personagem;
    }


    // Leitura
    /**lança PersonagemNaoEncontradoException se não existir personagem com o id informado */
    public Personagem buscarPorId(int id) {
        return personagemRepo.buscarPorId(id);
    }

    public String getSpriteBase(int id) {return buscarPorId(id).getSpriteDir();}

    public boolean existe(Personagem personagem) {
        return personagemRepo.existePersonagem(personagem);
    }

    public Map<Integer, Personagem> carregarPersonagens() {
        return personagemRepo.carregarPersonagens();
    }

    public Personagem criarPersonagem(String nome,
                                      double energia,
                                      double motivacao,
                                      double saude,
                                      double dinheiro,
                                      String spriteDir,
                                      Local localInicial,
                                      int posX,
                                      int posY,
                                      int personagemId) {

        if (nome == null || nome.isBlank()) {
            throw new PersonagemInvalidoException("nome", "não pode ser nulo ou vazio");
        }

        if (energia < 0) {
            throw new PersonagemInvalidoException("energia", "não pode ser negativa");
        }
        if (motivacao < 0) {
            throw new PersonagemInvalidoException("motivacao", "não pode ser negativa");
        }
        if (saude < 0) {
            throw new PersonagemInvalidoException("saude", "não pode ser negativa");
        }
        if (dinheiro < 0) {
            throw new PersonagemInvalidoException("dinheiro", "não pode ser negativo");
        }

        Personagem personagem = new Personagem(
                nome,
                energia,
                motivacao,
                saude,
                dinheiro,
                spriteDir,
                personagemId
        );

        // define posição inicial
        personagem.setcX(posX);
        personagem.setcY(posY);

        // define local inicial
        personagem.setLocalAtual(localInicial);

        return personagem;
    }

    public double calcularDesempenhoGeral(Personagem personagem) {

        int totalAprovadas = 0;
        int totalDisciplinas = 0;

        for (Semestre semestre : personagem.getSemestres()) {

            var disciplinas = semestre.getDisciplinas();

            totalDisciplinas += disciplinas.size();

            for (Disciplina d : disciplinas) {
                if (semestre.foiAprovado(d)) {
                    totalAprovadas++;
                }
            }
        }

        if (totalDisciplinas == 0) {
            return 0.0;
        }

        return (double) totalAprovadas / totalDisciplinas;
    }

    public Map<String, Double> getAtributos(int personagemId) {

        Personagem p = buscarPorId(personagemId);

        Map<String, Double> atributos = new LinkedHashMap<>();

        atributos.put("energia", p.getEnergia());
        atributos.put("motivação", p.getMotivacao());
        atributos.put("saúde", p.getSaude());
        atributos.put("dinheiro", p.getDinheiro());

        return atributos;
    }

    public Map<AreaConhecimento, Double> getConhecimentos(int personagemId){
        Personagem p = buscarPorId(personagemId);

        Map<AreaConhecimento, Double> conhecimentos = new HashMap<>();
        conhecimentos = p.getConhecimentos();

        return conhecimentos;
    }
}