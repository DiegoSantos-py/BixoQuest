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

import controller.PersonagemController;
import javafx.stage.Stage;

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

        GerenciadorTelas telas =
                new GerenciadorTelas(primaryStage,
                        personagemController,
                        mapaController,
                        npcController);

        telas.mostrarInicio();

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
