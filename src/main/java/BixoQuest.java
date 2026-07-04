import controller.GameController;
import controller.MapaController;
import javafx.application.Application;
import repository.*;
import service.*;
import view.controleTelas.GerenciadorTelas;
import controller.PersonagemController;
import javafx.stage.Stage;

import controller.BatalhaController;
import service.batalha.BatalhaService;
import repository.OponenteAnimalRepository;
import service.batalha.OponenteService;
import service.batalha.BatalhaLoopService;
import service.batalha.BatalhaFinalizacaoService;
import service.batalha.PlayerProvaService;

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

        OponenteAnimalRepository oponenteAnimalRepository = new OponenteAnimalRepository();
        OponenteService oponenteService = new OponenteService(oponenteAnimalRepository);
        BatalhaLoopService loopService = new BatalhaLoopService();
        BatalhaFinalizacaoService finalizacaoService = new BatalhaFinalizacaoService();
        PlayerProvaService playerProvaService = new PlayerProvaService();

        BatalhaService batalhaService = new BatalhaService(oponenteService, loopService, finalizacaoService, playerProvaService);
        AudioService audioService = new AudioService();

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
                eventoService,
                mapaService,
                localRepository,
                semestreRepository,
                disciplinaRepository,
                eventoRepository,
                npcRepository,
                personagemRepository
        );

        PersonagemController personagemController = new PersonagemController(personagemService);
        BatalhaController batalhaController = new BatalhaController(batalhaService, npcRepository, audioService);
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
                gameController,
                batalhaController);


        telas.mostrarInicio();

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}