import javafx.application.Application;
import repository.EventoRepository;
import repository.PersonagemRepository;
import service.EventoService;
import service.PersonagemService;
import view.menu.*;

import controller.PersonagemController;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.Objects;

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

        Menu mainMenu = new Menu(personagemController);
        mainMenu.exibir(primaryStage);

        primaryStage.setTitle("BixoQuest");
        primaryStage.setFullScreen(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
