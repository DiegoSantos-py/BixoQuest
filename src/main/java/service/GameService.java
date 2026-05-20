package service;

import model.Disciplina.Disciplina;
import model.Local.Local;
import model.Local.TipoLocal;
import model.Tempo.Dia;
import model.Tempo.Semestre;
import model.Personagem;
import repository.LocalRepository;

import java.util.List;

/*public class GameService {

    private DiaService diaService;
    private SemestreService semestreService;
    private DisciplinaService disciplinaService;
    private LocalRepository localRepo;

    private Semestre semestre;
    private Dia diaAtual;

    private Personagem personagem;

    public GameService(DiaService diaService, SemestreService semestreService,
                       DisciplinaService disciplinaService, LocalRepository localRepo,
                       Personagem personagem) {
        this.diaService = diaService;
        this.semestreService = semestreService;
        this.disciplinaService = disciplinaService;
        this.localRepo = localRepo;
        this.personagem = personagem;
    }

    public void iniciarJogo(Personagem personagem) {

        this.personagem = personagem;

        semestre = semestreService.criarSemestre();

        diaAtual = semestreService.avancarDia(semestre);

        personagem.setEnergia(40.0);
        Local pontoOnibus = localRepo.buscarPorTipo(TipoLocal.PONTO_ONIBUS);

        if (pontoOnibus == null) {
            throw new IllegalStateException("Nenhum ponto de ônibus encontrado");
        }

        personagem.setLocalAtual(pontoOnibus);
        personagem.setcX(0);
        personagem.setcY(0);

        diaService.iniciarDia(diaAtual);
    }

    public void atualizar(Personagem personagem) {

        if (diaService.isDiaEncerrado()) {

            diaService.encerrarDia(diaAtual);

            if (!semestreService.terminouSemestre(semestre)) {

                diaAtual = semestreService.avancarDia(semestre);

                // Define spawn
                Local pontoOnibus = localRepo.buscarPorTipo(TipoLocal.PONTO_ONIBUS);

                personagem.setLocalAtual(pontoOnibus);
                personagem.setcX(0);
                personagem.setcY(0);

                diaService.iniciarDia(diaAtual);

            } else {
                semestreService.encerrarSemestre(personagem, semestre);
            }
        }
    }

    public boolean encerrarJogo(Personagem personagem){

        // 1. Percorre todas as disciplinas do jogo
        for (List<Disciplina> lista : this.disciplinaService.carregarDisciplinas().values()) {
            for (Disciplina dSistema : lista) {

                boolean aprovado = false;

                // 2. Verifica se o jogador foi aprovado em algum semestre
                for (Semestre semestre : personagem.getSemestres()) {
                    if (semestre.foiAprovado(dSistema)) {
                        aprovado = true;
                        break;
                    }
                }

                // 3. Se falhou em qualquer disciplina → não concluiu o jogo
                if (!aprovado) {
                    return false;
                }
            }
        }

        return true;
    }

}*/