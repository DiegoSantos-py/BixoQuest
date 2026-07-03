import controller.GameController;
import controller.MapaController;
import controller.NpcController;
import javafx.application.Application;
import repository.*;
import service.*;
import view.controleTelas.GerenciadorTelas;
import controller.PersonagemController;
import javafx.stage.Stage;

public class BixoQuest extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Repositórios
        LocalRepository localRepository = new LocalRepository();
        SemestreRepository semestreRepository = new SemestreRepository();
        DisciplinaRepository disciplinaRepository = new DisciplinaRepository();
        EventoRepository eventoRepository = new EventoRepository();
        NpcRepository npcRepository = new NpcRepository();
        PersonagemRepository personagemRepository = new PersonagemRepository();

        // Serviços
        EventoService eventoService = new EventoService(eventoRepository);
        PersonagemService personagemService = new PersonagemService(personagemRepository, eventoService);
        DiaService diaService = new DiaService();
        SemestreService semestreService = new SemestreService( semestreRepository, disciplinaRepository);
        NpcService npcService = new NpcService(npcRepository);
        MapaService mapaService = new MapaService(localRepository, npcService);
        DisciplinaService disciplinaService = new DisciplinaService(disciplinaRepository);
        InicializacaoService inicializacaoService = new InicializacaoService(
                localRepository,
                disciplinaRepository,
                eventoRepository,
                npcRepository,
                semestreRepository,
                personagemRepository,
                disciplinaService
        );

        GameService gameService = new GameService(
                diaService,
                semestreService,
                personagemService,
                inicializacaoService,
                localRepository,
                semestreRepository,
                disciplinaRepository,
                eventoRepository,
                npcRepository,
                personagemRepository
        );

        PersonagemController personagemController = new PersonagemController(personagemService);
        MapaController mapaController = new MapaController(mapaService);
        GameController gameController = new GameController(gameService);


        try {
            eventoRepository.carregar();
            personagemRepository.carregar();
        } catch (Exception e) {
            System.out.println("Erro ao carregar arquivos, dados corrompidos?");
        }

        primaryStage.setTitle("BixoQuest");
        primaryStage.setFullScreen(true);

        GerenciadorTelas telas = new GerenciadorTelas(
                primaryStage,
                personagemController,
                mapaController,
                gameController);


        telas.mostrarInicio();

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}