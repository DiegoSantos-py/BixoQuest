package service;

import model.Personagem;
import model.Tempo.Dia;
import model.Disciplina.Disciplina;
import model.Tempo.Semestre;
import repository.DisciplinaRepository;
import repository.SemestreRepository;

import java.util.ArrayList;
import java.util.List;

public class SemestreService {

    // Repositório responsável por salvar e recuperar semestres
    private SemestreRepository semestreRepo;

    private DisciplinaRepository disciplinaRepo;

    public SemestreService(SemestreRepository semestreRepo,
                           DisciplinaRepository disciplinaRepo) {
        this.semestreRepo = semestreRepo;
        this.disciplinaRepo = disciplinaRepo;
    }

    public Semestre criarSemestre() {
        return new Semestre();
    }

    public Semestre iniciarPrimeiroSemestre(int jogadorId) {

        Semestre semestre = criarSemestre();

        // Busca as disciplinas iniciais do jogo
        List<Disciplina> disciplinasIniciais =
                disciplinaRepo.buscarDisciplinasIniciais();

        semestre.setDisciplinas(disciplinasIniciais);

        return semestre;
    }

    public Dia avancarDia(Semestre semestre) {

        // Se o semestre já terminou, não cria novos dias
        if (semestre.terminou()) {
            return null;
        }

        Dia novoDia = new Dia();

        semestre.adicionarDia(novoDia);

        return novoDia;
    }

    public void adicionarDisciplina(Semestre semestre, Disciplina disciplina){

        // Não permite adicionar a mesma disciplina duas vezes
        if (semestre.getDisciplinas().contains(disciplina)){
            throw new IllegalArgumentException(
                    "Não é possível adicionar a mesma disciplina ao semestre duas vezes."
            );
        }

        semestre.adicionarDisciplinas(disciplina);
    }

    public boolean terminouSemestre(Semestre semestre){
        return semestre.terminou();
    }

    public Semestre encerrarSemestre(Personagem personagem, Semestre semestre) {

        if (semestre == null) {
            throw new IllegalArgumentException("Semestre inválido");
        }

        // Se ainda não terminou, retorna o mesmo semestre
        if (!semestre.terminou()) {
            return semestre;
        }

        // Salva o semestre no histórico do personagem
        personagem.adicionarSemestre(semestre);

        // Salva no repositório
        this.semestreRepo.adicionarSemestre(personagem.getPersonagemId(), semestre);

        Semestre novoSemestre = new Semestre();

        List<Disciplina> novasDisciplinas = new ArrayList<>();

        // Percorre todas as disciplinas do semestre atual
        for (Disciplina atual : semestre.getDisciplinas()) {

            // Se o aluno foi aprovado
            if (semestre.foiAprovado(atual)) {

                Disciplina proxima = disciplinaRepo.proximaDisciplina(
                        atual.getNome(),
                        atual.getCodigo()
                );

                // Se existir próxima disciplina, adiciona
                if (proxima != null) {
                    novasDisciplinas.add(proxima);
                }

            } else {
                // Se foi reprovado, repete a mesma disciplina
                novasDisciplinas.add(atual);
            }
        }

        novoSemestre.setDisciplinas(novasDisciplinas);

        this.semestreRepo.adicionarSemestre(personagem.getPersonagemId(), novoSemestre);

        return novoSemestre;
    }

    public void definirResultadoDisciplina(Semestre semestre, Disciplina disciplina, boolean aprovado) {

        if (semestre == null || disciplina == null) {
            throw new IllegalArgumentException("Parâmetros inválidos");
        }

        // Registra se o aluno foi aprovado ou não na disciplina
        semestre.registrarResultado(disciplina, aprovado);
    }
}