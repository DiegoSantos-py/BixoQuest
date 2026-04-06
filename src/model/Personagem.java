package model;

import java.util.Map;
import java.util.Objects;

public class Personagem {
    private static int qntdPersonagem = 0;

    private int personagemId;
    private String nome;
    private double energia;
    private double sanidade;
    private double saude;
    private double dinheiro;
    private Map<String, Disciplina> disciplinas;

    private String spriteDir;

    private int SemestreAtual;
    private double desempenhoAcademico;

    private int cX = 0;
    private int cY = 0;

    public Personagem(){
        nome = "default";
        saude = 10.0;
        energia = 10.0;
        sanidade = 10.0;
        dinheiro = 10.0;
    }

    public Personagem(String nome, double energia, double sanidade, double saude, double dinheiro, String spriteDir){
        this.nome = nome;
        this.saude = saude;
        this.sanidade = sanidade;
        this.dinheiro = dinheiro;
        this.energia = energia;
        this.spriteDir = spriteDir;
        personagemId = qntdPersonagem++;
    }

    public int getPersonagemId() {
        return personagemId;
    }

    public void setPersonagemId(int personagemId) {
        this.personagemId = personagemId;
    }

    public int getcY() {
        return cY;
    }

    public void setcY(int cY) {
        this.cY = cY;
    }

    public int getcX() {
        return cX;
    }

    public void setcX(int cX) {
        this.cX = cX;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getEnergia() {
        return energia;
    }

    public void setEnergia(double energia) {
        this.energia = energia;
    }

    public double getDinheiro() {
        return dinheiro;
    }

    public void setDinheiro(double dinheiro) {
        this.dinheiro = dinheiro;
    }

    public double getSaude() {
        return saude;
    }

    public void setSaude(double saude) {
        this.saude = saude;
    }

    public double getSanidade() {
        return sanidade;
    }

    public void setSanidade(double sanidade) {
        this.sanidade = sanidade;
    }

    public Disciplina getDisciplina(String nome){
        return this.disciplinas.get(nome);
    }

    public void setDisciplinas(Map<String, Disciplina> disciplinas) {
        this.disciplinas = disciplinas;
    }

    public String getSpriteDir() {
        return spriteDir;
    }

    public int getSemestreAtual() {
        return SemestreAtual;
    }

    public void setSemestreAtual(int semestreAtual) {
        SemestreAtual = semestreAtual;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Personagem that = (Personagem) o;
        return Objects.equals(nome, that.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(nome);
    }

    @Override
    public String toString() {
        return "Personagem:" +
                "personagemId=" + personagemId +
                ", nome='" + nome + '\'' +
                ", energia=" + energia +
                ", sanidade=" + sanidade +
                ", saude=" + saude +
                ", dinheiro=" + dinheiro +
                ", spriteDir='" + spriteDir + '\'' +
                ", disciplinas=" + disciplinas +
                ", cX=" + cX +
                ", cY=" + cY;
    }
}
