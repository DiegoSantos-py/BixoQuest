package model.Disciplina;

import com.fasterxml.jackson.annotation.JsonIgnore;
import model.Evento.Prova.ProvaBatalha;
import model.Evento.Prova.ProvaFactory;
import model.Evento.Prova.ProvaIDs;

import java.util.Objects;

public class Disciplina {
    private String nome;
    private float codigo;
    private ProvaIDs provaId; //Classe relacionada ao evento de batalha
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

    public ProvaIDs getProvaId() { return provaId; }
    public void setProvaId(ProvaIDs provaId) { this.provaId = provaId; }

    @JsonIgnore
    public ProvaBatalha getProva() {
        return provaId != null ? ProvaFactory.criar(provaId) : null;
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

    @Override
    public String toString() {
        return this.nome;
    }
}
