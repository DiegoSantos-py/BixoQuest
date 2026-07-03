package repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import exception.Batalha.OponenteAnimalNaoEncontradoException;
import exception.OperacaoPersistencia;
import exception.PersistenciaException;
import model.Batalha.Oponente;
import model.Batalha.OponenteDados;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class OponenteAnimalRepository {

    //esse repository serve pra pegar o animal e extrair os dados como diretorio do sprite, o textozinho q fica embaixo do nome e a o texto da caixa de batalha
    private Map<String, OponenteDados> oponentes;
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final File ARQUIVO = new File("gameFiles/oponenteDados.json");

    public OponenteAnimalRepository() {
        this.oponentes = new HashMap<>();
        try {
            carregar();
        } catch (Exception e) {
            System.err.println("Erro ao carregar oponenteDados.json: " + e.getMessage());
        }
    }

    /**
     * Carrega o dicionário de oponentes do arquivo JSON para a memória.
     */
    public void carregar() throws PersistenciaException {
        if (!ARQUIVO.exists()) return;

        try {
            this.oponentes = mapper.readValue(
                    ARQUIVO,
                    mapper.getTypeFactory().constructMapType(
                            HashMap.class,
                            String.class,
                            OponenteDados.class
                    )
            );
        } catch (Exception e) {
            throw new PersistenciaException(OperacaoPersistencia.CARREGAR, e);
        }
    }

    /**
     * Busca os dados do oponente diretamente pelo nome.
     */
    public OponenteDados buscarPorNome(String nome) {
        OponenteDados oponente = oponentes.get(nome);
        
        if (oponente == null && nome != null) {
            oponente = oponentes.get(nome.toLowerCase());
        }

        if (oponente == null) {
            throw new OponenteAnimalNaoEncontradoException(nome);
        }

        return oponente;
    }
}