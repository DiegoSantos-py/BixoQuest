package repository;

import model.Evento.Prova.ResultadoProva;


import java.util.ArrayList;
import java.util.List;

public class ResultadoProvaRepository {
    
    private List<ResultadoProva> bancoResultados;

    public ResultadoProvaRepository() {
        this.bancoResultados = new ArrayList<>();
    }

    public void salvar(ResultadoProva resultado) {
        if (resultado != null) {
            bancoResultados.add(resultado);
        }
    }


    
    public List<ResultadoProva> buscarTodos() {
        return new ArrayList<>(bancoResultados);
    }
}
