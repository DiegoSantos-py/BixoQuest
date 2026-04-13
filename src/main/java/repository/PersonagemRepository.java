package repository;

import model.Personagem;

import java.util.HashMap;
import java.util.Map;

public class PersonagemRepository {
    private Map<Integer, Personagem> personagens = new HashMap<>();

    // guarda todos personagens existentes
    public void adicionarPersonagem(Personagem personagem){
        if (this.personagens.containsKey(personagem.getPersonagemId()) || personagem == null){
            return;
    }
        this. personagens.put(personagem.getPersonagemId(), personagem);}

    public boolean existePersonagem(Personagem personagem){
        if (personagem == null || !this.personagens.containsKey(personagem.getPersonagemId())) {return false;}
        return true;
    }

    public Map<Integer, Personagem> carregarPersonagens(){
        return this.personagens;
    }
}
