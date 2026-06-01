import javafx.application.Application;
import repository.EventoRepository;
import repository.PersonagemRepository;
import service.EventoService;
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
                new GerenciadorTelas(primaryStage, personagemController);

        telas.mostrarInicio();

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
