package service;

import exception.Personagem.PersonagemDuplicadoException;
import exception.Personagem.PersonagemInvalidoException;
import exception.Personagem.PersonagemNaoEncontradoException;
import exception.PersistenciaException;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
            throw new PersonagemInvalidoException("nome", "não pode ser nulo ou vazio"); // ATUALIZADO
        }

        if (energia < 0) {
            throw new PersonagemInvalidoException("energia", "não pode ser negativa"); // ATUALIZADO
        }
        if (motivacao < 0) {
            throw new PersonagemInvalidoException("motivacao", "não pode ser negativa"); // ATUALIZADO
        }
        if (saude < 0) {
            throw new PersonagemInvalidoException("saude", "não pode ser negativa"); // ATUALIZADO
        }
        if (dinheiro < 0) {
            throw new PersonagemInvalidoException("dinheiro", "não pode ser negativo"); // ATUALIZADO
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

    public void atualizarPersonagem(Personagem p,
                                    Direcao direcao,
                                    Dia diaAtual,
                                    DiaService diaService){

        mover(p, direcao, diaAtual, diaService);


        Local local = p.getLocalAtual();
        for(ZonaInterativa z: local.getZonaInterativasDisponiveis()){
            for(Evento e: diaAtual.getEventosObrigatorios().values())
                if (z.contemCoordenada(p.getcX(), p.getcY())){
                    eventoService.executarEvento(e ,p, diaAtual, diaService);
                }
            for(Evento e: diaAtual.getEventosAleatorios().values())
                if (z.contemCoordenada(p.getcX(), p.getcY())){
                    eventoService.executarEvento(e ,p, diaAtual, diaService);
                }
        }
    }

    public void mover(Personagem p,
                      Direcao direcao,
                      Dia diaAtual,
                      DiaService diaService) {

        int novoX = p.getcX();
        int novoY = p.getcY();

        switch (direcao) {
            case CIMA: novoY--; break;
            case BAIXO: novoY++; break;
            case ESQUERDA: novoX--; break;
            case DIREITA: novoX++; break;
        }

        Local localAtual = p.getLocalAtual();

        if (localAtual.getArea().contemCoordenada(novoX, novoY)) {
            p.setcX(novoX);
            p.setcY(novoY);
        } else {
            Local vizinho = localAtual.getVizinho(direcao);

            if (vizinho != null) {
                p.setLocalAtual(vizinho);
                ajustarPosicaoAoEntrar(p, vizinho, direcao);
            }
        }

        diaService.verificarRetornoAoPonto(diaAtual, p);
    }

    private void ajustarPosicaoAoEntrar(Personagem p, Local novoLocal, Direcao direcao) {

        model.Local.Area area = novoLocal.getArea();

        switch (direcao) {
            case CIMA:
                p.setcY(area.getMinY());
                break;
            case BAIXO:
                p.setcY(area.getMaxY());
                break;
            case ESQUERDA:
                p.setcX(area.getMaxX());
                break;
            case DIREITA:
                p.setcX(area.getMinX());
                break;
        }
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
}