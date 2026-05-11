package model.Disciplina;

import com.fasterxml.jackson.annotation.JsonIgnore;
import model.Evento.ProvaBatalha;

import java.util.Objects;

public class Disciplina {
    private String nome;
    private float codigo;
    private ProvaBatalha prova; //Classe relacionada ao evento de batalha
    private AreaConhecimento area;

    public AreaConhecimento getArea() {
        return area;
    }

    public void setArea(AreaConhecimento area) {
        this.area = area;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public float getCodigo() {
        return codigo;
    }

    public void setCodigo(float codigo) {
        this.codigo = codigo;
    }

    @JsonIgnore
    public ProvaBatalha getProva() {
        return prova;
    }

    public void setProva(ProvaBatalha prova) {
        this.prova = prova;
    }

    // Duas disciplina são iguais se tem mesmo nome e código
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Disciplina that = (Disciplina) o;
        return Float.compare(codigo, that.codigo) == 0 &&
                Objects.equals(nome, that.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome, codigo);
    }
}
