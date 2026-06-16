package model.Evento.Prova;

import model.Disciplina.AreaConhecimento;
import model.Evento.Evento;
import model.Evento.Prova.Questao.Questao;
import model.Player.AcaoBatalha;
import repository.SemestreRepository;

import java.util.ArrayList;
import java.util.LinkedList;

public class ProvaBatalha extends Evento {

    private int nivelDisciplina;
    private AreaConhecimento areaConhecimento;
    private int quantidadeQuestoes;
    private LinkedList<Questao> questoes;
    private ArrayList<AcaoBatalha> acoesBatalha;
    private String spriteDirProva;

    public ProvaBatalha(String nome, String descricao,AreaConhecimento areaConhecimento,  int nivelDisciplina, int quantidadeQuestoes, ArrayList<AcaoBatalha> acoesBatalha, String spriteDirProva ) {
        super(nome, descricao);
        this.areaConhecimento = areaConhecimento;
        this.nivelDisciplina = nivelDisciplina;
        this.quantidadeQuestoes = quantidadeQuestoes;
        this.questoes = new LinkedList<>();
        this.acoesBatalha = acoesBatalha; // favor nao botar mais de 4 ações em prol da UI
        this.spriteDirProva = spriteDirProva;
    }




    public int getNivelDisciplina() {
        return nivelDisciplina;
    }

    public String getSpriteDirProva() {
        return spriteDirProva;
    }
    public void setSpriteDirProva(String spriteDirProva) {
        this.spriteDirProva = spriteDirProva;
    }
    public void setNivelDisciplina(int nivelDisciplina) {
        this.nivelDisciplina = nivelDisciplina;
    }

    public AreaConhecimento getAreaConhecimento() {
        return areaConhecimento;
    }

    public void setAreaConhecimento(AreaConhecimento areaConhecimento) {
        this.areaConhecimento = areaConhecimento;
    }

    public void addQuestao(Questao questao) {
        this.questoes.add(questao);
        this.quantidadeQuestoes++;
    }
    public int getQuantidadeQuestoes() {
        return quantidadeQuestoes;
    }

    public LinkedList<Questao> getQuestoes() {
        return questoes;
    }

    public void addAcaoBatalha(AcaoBatalha acaoBatalha) {
        this.acoesBatalha.add(acaoBatalha);
    }
    public ArrayList<AcaoBatalha> getAcoesBatalha() {
        return acoesBatalha;
    }


}
