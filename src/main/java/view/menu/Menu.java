package view.menu;

import controller.PersonagemController;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Objects;

public class Menu {

    private final PersonagemController personagemController;

    // Constructor Injection
    public Menu(PersonagemController personagemController) {
        this.personagemController = personagemController;
    }

    public void exibir(Stage primaryStage) {
        //a pilha da interface (as coisas q ficam na frente e atras)
        StackPane root = new StackPane();
        //faz com que se n tiver imagem o jogo crashe
        //todo: adicionar NullImageException
        Image bgImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/menuPrincipal/BackgroundMenu.png")));
        ImageView backgroundView = new ImageView(bgImage);//adiciona a imagem na view
        backgroundView.setFitWidth(1920);
        backgroundView.setFitHeight(1080);
        backgroundView.setPreserveRatio(false);//talvez ele tenha q esticar a imagem de fundo pra ela se enquadrar certinho em 1920x1080]
        //ela ja é 16x9 ent n muda nada
        //o container do menu em si
        VBox menuItems = new VBox(20); // espacaçemnto vertical de 20 pixels entre cada elemento
        menuItems.setAlignment(Pos.CENTER); // e poe tudo no centro
        menuItems.setTranslateY(-300); //move os itens 300pixels acima
        //756x287
        //a logo
        Image logoImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/menuPrincipal/BixoQuestLogo.png")));
        ImageView logoView = new ImageView(logoImage);
        logoView.setFitWidth(600);//max de 400 pixels
        logoView.setPreserveRatio(true); //mas vai preservar as dimensões da imagem

        // uma margin bottom pra distanciar a logo dos botoes
        VBox.setMargin(logoView, new javafx.geometry.Insets(0, 0, 50, 0));

        Button btnStart = new Button("Iniciar Jogo");
        Button btnConfig = new Button("Configurações");
        Button btnExit = new Button("Sair");


        btnStart.setPrefWidth(200);
        btnConfig.setPrefWidth(200);
        btnExit.setPrefWidth(200);

        // pelo menos o botao de sair funciona
        btnExit.setOnAction(event -> System.exit(0));

        btnStart.setOnAction(event -> {
            MenuPersonagens menuPersonagens = new MenuPersonagens(personagemController);
            menuPersonagens.exibir(primaryStage);
        });

        //poe tudo no container do menu Itens
        menuItems.getChildren().addAll(logoView, btnStart, btnConfig, btnExit);

        //poe a imagem do bg, e por cima do bg o container dos itens
        root.getChildren().addAll(backgroundView, menuItems);

        // cria a cena em si c os elementos e o tamanho
        Scene scene = new Scene(root, 1920, 1080);



        primaryStage.setTitle("BixoQuest");
        primaryStage.setScene(scene);

        //jogo em tela cheia pois claramente é um triple A
        primaryStage.setFullScreen(true);

        primaryStage.show();
    }

}