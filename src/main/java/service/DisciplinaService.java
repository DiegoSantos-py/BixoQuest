package service;

import model.Disciplina.AreaConhecimento;
import model.Disciplina.Disciplina;
import repository.DisciplinaRepository;

import java.util.List;
import java.util.Map;

public class DisciplinaService {

    // Repositório responsável por armazenar e recuperar disciplinas
    private DisciplinaRepository disciplinaRepo;

    public DisciplinaService(DisciplinaRepository disciplinaRepo) {
        this.disciplinaRepo = disciplinaRepo;
    }

    public void criarDisciplinasPorNivel(String nome, int quantidadeNiveis, AreaConhecimento area) {

        if (nome == null || quantidadeNiveis <= 0 || area == null) {
            throw new IllegalArgumentException("Parâmetros inválidos");
        }

        // Cria várias disciplinas com níveis diferentes
        for (int i = 1; i <= quantidadeNiveis; i++) {

            Disciplina d = new Disciplina();

            d.setNome(nome);

            d.setCodigo(i);

            d.setArea(area);

            // Só adiciona se ainda não existir no repositório
            if (!disciplinaRepo.existe(d)) {
                disciplinaRepo.adicionar(d);
            }
        }
    }

    public List<Disciplina> buscarPorNome(String nome) {
        return disciplinaRepo.buscarPorNome(nome);
    }

    public Disciplina buscar(String nome, float codigo) {
        return disciplinaRepo.buscar(nome, codigo);
    }

    public Map<String, List<Disciplina>> carregarDisciplinas() {
        return disciplinaRepo.carregarDisciplinas();
    }
}