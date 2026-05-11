package model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import model.Disciplina.AreaConhecimento;
import model.Disciplina.Disciplina;
import model.Local.Local;
import model.Tempo.Semestre;

import java.util.*;

public class Personagem {
    private static int qntdPersonagem = 0;

    private int personagemId;
    private String nome;
    private double energia;
    private double motivacao;
    private double saude;
    private double dinheiro;
    @JsonIgnore
    private Local localAtual;

    private String localAtualNome;

    private Map<AreaConhecimento, Double> conhecimentos;

    private String spriteDir; // preparação para próxima fase

    @JsonIgnore
    private List<Semestre> semestres;
    private double desempenhoAcademico;

    private int cX = 0;
    private int cY = 0;

    public Personagem(){
        this.nome = "Um estudante aleatório";
        this.saude = 40.0;
        this.energia = 40.0;
        this.motivacao = 40.0;
        this.dinheiro = 40.0;

        this.conhecimentos = new HashMap<>();
        this.semestres = new ArrayList<>();

        for (AreaConhecimento area : AreaConhecimento.values()) {
            this.conhecimentos.put(area, 10.0);
        }
    }

    public Personagem(String nome, double energia, double motivacao, double saude, double dinheiro, String spriteDir){
        this.nome = nome;
        this.saude = saude;
        this.motivacao = motivacao;
        this.dinheiro = dinheiro;
        this.energia = energia;
        this.spriteDir = spriteDir;
        this.personagemId = qntdPersonagem++;
        this.semestres = new ArrayList<>();

        this.conhecimentos = new HashMap<>();

        for (AreaConhecimento area : AreaConhecimento.values()) {
            this.conhecimentos.put(area, 10.0);
        }
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
    public double addEnergia(int valor) {
        this.energia += valor;
        return this.energia;
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

    public double getMotivacao() {
        return motivacao;
    }

    public void setMotivacao(double motivacao) {
        this.motivacao = motivacao;
    }

    public void addMotivacao(int valor) {
        this.motivacao += valor;
    }

    public String getSpriteDir() {
        return spriteDir;
    }

    public Local getLocalAtual() {
        return localAtual;
    }

    public void setLocalAtual(Local local) {
        this.localAtual = local;
        this.localAtualNome = local != null ? local.getNome() : null;
    }

    public double getConhecimentoPorDisciplina(Disciplina disciplina) {
        return conhecimentos.get(disciplina.getArea());
    }

    public double getConhecimento(AreaConhecimento area) {
        return conhecimentos.get(area);
    }

    public void adicionarConhecimento(AreaConhecimento area, double valor) {
        conhecimentos.put(area, getConhecimento(area) + valor);
    }

    public List<Semestre> getSemestres() {
        return semestres;
    }

    public void adicionarSemestre(Semestre semestre) {
        this.semestres.add(semestre);
    }

    public double getDesempenhoAcademico() {
        return desempenhoAcademico;
    }

    public String getLocalAtualNome() { return localAtualNome; }
    public void setLocalAtualNome(String nome) { this.localAtualNome = nome; }

    // é igual se nome e id são iguais
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Personagem that = (Personagem) o;
        return personagemId == that.personagemId && Objects.equals(nome, that.nome);
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
                ", motivacao=" + motivacao +
                ", saude=" + saude +
                ", dinheiro=" + dinheiro +
                ", spriteDir='" + spriteDir + '\'' +
                ", cX=" + cX +
                ", cY=" + cY;
    }


}
