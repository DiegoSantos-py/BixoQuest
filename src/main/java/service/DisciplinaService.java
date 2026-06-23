package service;

import exception.Disciplina.DisciplinaDuplicadaException;
import exception.Disciplina.DisciplinaInvalidaException;
import exception.Disciplina.DisciplinaNaoEncontradaException;
import exception.PersistenciaException;
import model.Disciplina.AreaConhecimento;
import model.Disciplina.Disciplina;
import repository.DisciplinaRepository;

import java.util.List;
import java.util.Map;

public class DisciplinaService {

    private final DisciplinaRepository disciplinaRepo;

    public DisciplinaService(DisciplinaRepository disciplinaRepo) {
        this.disciplinaRepo = disciplinaRepo;
    }

    // Inicialização
    /**lança PersistenciaException se ocorrer falha ao carregar o arquivo*/
    public void carregar() throws PersistenciaException {
        disciplinaRepo.carregar();
    }


        /**lança PersistenciaException se ocorrer falha ao salvar o arquivo*/
    public void salvar() throws PersistenciaException {
        disciplinaRepo.salvar();
    }

    // Escrita
    /**lança DisciplinaInvalidaException  se nome, quantidade ou área forem inválidos
        lança DisciplinaDuplicadaException se algum nível já existir no repositório
        lança PersistenciaException        se ocorrer falha ao salvar após criação*/
    public void criarDisciplinasPorNivel(String nome, int quantidadeNiveis, AreaConhecimento area)
            throws PersistenciaException {
        if (nome == null || nome.isBlank()) {
            throw new DisciplinaInvalidaException("nome", "não pode ser nulo ou vazio");
        }
        if (quantidadeNiveis <= 0) {
            throw new DisciplinaInvalidaException("quantidadeNiveis", "deve ser maior que zero");
        }
        if (area == null) {
            throw new DisciplinaInvalidaException("area", "não pode ser nula");
        }

        for (int i = 1; i <= quantidadeNiveis; i++) {
            Disciplina d = new Disciplina();
            d.setNome(nome);
            d.setCodigo(i);
            d.setArea(area);
            disciplinaRepo.adicionar(d);
        }

        disciplinaRepo.salvar();
    }

    public void criarTodasDisciplinas() throws PersistenciaException{
        criarDisciplinasPorNivel("Matematica", 4,AreaConhecimento.MAT);
        criarDisciplinasPorNivel("Natureza", 4, AreaConhecimento.NAT);
        criarDisciplinasPorNivel("Software", 4, AreaConhecimento.SOF);
        criarDisciplinasPorNivel("Hardware", 4, AreaConhecimento.HAR);
        criarDisciplinasPorNivel("Complementar",1,AreaConhecimento.COM);
        criarDisciplinasPorNivel("Humanistica",1,AreaConhecimento.HUM);
        criarDisciplinasPorNivel("Estagio",1,AreaConhecimento.EST);
        criarDisciplinasPorNivel("tfc", 1, AreaConhecimento.TFC);
        criarDisciplinasPorNivel("tcc", 1, AreaConhecimento.TCC);
    }
    // Leitura
    /**lança DisciplinaNaoEncontradaException se não houver disciplinas com o nome */
    public List<Disciplina> buscarPorNome(String nome) {
        return disciplinaRepo.buscarPorNome(nome);
    }

    public List<Disciplina> buscarPorArea(AreaConhecimento area) {
        return disciplinaRepo.buscarPorArea(area);
    }

    /**lança DisciplinaNaoEncontradaException se não encontrar a combinação nome + código
    */
    public Disciplina buscar(String nome, float codigo) {
        return disciplinaRepo.buscar(nome, codigo);
    }

    public boolean existe(Disciplina d) {
        return disciplinaRepo.existe(d);
    }

    /** lança DisciplinaNaoEncontradaException se não existir disciplina com código + 1*/
    public Disciplina proximaDisciplina(String nome, float codigoAtual) {
        return disciplinaRepo.proximaDisciplina(nome, codigoAtual);
    }

    public List<Disciplina> buscarDisciplinasIniciais() {
        return disciplinaRepo.buscarDisciplinasIniciais();
    }

    public Map<String, List<Disciplina>> carregarDisciplinas() {
        return disciplinaRepo.carregarDisciplinas();
    }
}