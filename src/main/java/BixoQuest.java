import controller.MapaController;
import controller.NpcController;
import javafx.application.Application;
import repository.EventoRepository;
import repository.LocalRepository;
import repository.NpcRepository;
import repository.PersonagemRepository;
import service.EventoService;
import service.MapaService;
import service.NpcService;
import service.PersonagemService;
import view.controleTelas.GerenciadorTelas;
import service.AudioService;

import controller.PersonagemController;
import controller.BatalhaController;
import javafx.stage.Stage;
import service.batalha.BatalhaService;
import repository.OponenteAnimalRepository;
import service.batalha.OponenteService;
import service.batalha.BatalhaLoopService;
import service.batalha.BatalhaFinalizacaoService;
import service.batalha.PlayerProvaService;

public class BixoQuest extends Application {

    @Override
    public void start(Stage primaryStage) {
        EventoRepository eventoRepository = new EventoRepository();
        EventoService eventoService = new EventoService(eventoRepository);

        PersonagemRepository personagemRepository = new PersonagemRepository();
        PersonagemService personagemService = new PersonagemService(personagemRepository,eventoService);
        PersonagemController personagemController= new PersonagemController(personagemService);

        LocalRepository mapaRepository = new LocalRepository();
        MapaService mapaService = new MapaService(mapaRepository);
        MapaController mapaController = new MapaController(mapaService);

        NpcRepository npcRepository = new NpcRepository();
        NpcService npcService = new NpcService(npcRepository);
        NpcController npcController = new NpcController(npcService);
        try {
            personagemRepository.carregar();
            eventoRepository.carregar();
        }
        catch (Exception PersistenciaException){
            System.out.println("Erro ao carregar arquivos, dados corrompidos?");
        }


        primaryStage.setTitle("BixoQuest");
        primaryStage.setFullScreen(true);

        OponenteAnimalRepository oponenteAnimalRepository = new OponenteAnimalRepository();
        OponenteService oponenteService = new OponenteService(oponenteAnimalRepository);
        BatalhaLoopService loopService = new BatalhaLoopService();
        BatalhaFinalizacaoService finalizacaoService = new BatalhaFinalizacaoService();
        PlayerProvaService playerProvaService = new PlayerProvaService();

        BatalhaService batalhaService = new BatalhaService(oponenteService, loopService, finalizacaoService, playerProvaService);
        AudioService audioService = new AudioService();
        BatalhaController batalhaController = new BatalhaController(batalhaService, npcRepository, audioService);

        GerenciadorTelas telas =
                new GerenciadorTelas(primaryStage,
                        personagemController,
                        mapaController,
                        npcController,
                        batalhaController);

        telas.mostrarInicio();

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
