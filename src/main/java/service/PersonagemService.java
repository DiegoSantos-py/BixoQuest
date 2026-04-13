package service;

import model.Disciplina.Disciplina;
import model.Evento.Evento;
import model.Local.Area;
import model.Local.Direcao;
import model.Local.Local;
import model.Local.ZonaInterativa;
import model.Personagem;
import model.Tempo.Semestre;
import model.Tempo.Dia;

import java.util.ArrayList;
import java.util.List;

public class PersonagemService {
    private EventoService eventoService;

    public PersonagemService(EventoService eventoService){
        this.eventoService = eventoService;
    }

    public Personagem criarPersonagem(String nome,
                                      double energia,
                                      double motivacao,
                                      double saude,
                                      double dinheiro,
                                      String spriteDir,
                                      Local localInicial,
                                      int posX,
                                      int posY) {

        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome inválido");
        }

        if (energia < 0 || motivacao < 0 || saude < 0 || dinheiro < 0) {
            throw new IllegalArgumentException("Atributos não podem ser negativos");
        }

        Personagem personagem = new Personagem(
                nome,
                energia,
                motivacao,
                saude,
                dinheiro,
                spriteDir
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

        Area area = novoLocal.getArea();

        switch (direcao) {
            case CIMA:
                p.setcY(area.getMinY()); // entra por baixo
                break;
            case BAIXO:
                p.setcY(area.getMaxY()); // entra por cima
                break;
            case ESQUERDA:
                p.setcX(area.getMaxX()); // entra pela direita
                break;
            case DIREITA:
                p.setcX(area.getMinX()); // entra pela esquerda
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
