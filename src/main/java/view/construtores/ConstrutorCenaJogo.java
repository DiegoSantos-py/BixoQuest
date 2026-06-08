package view.construtores;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import view.cena.CenaJogo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Builder concreto que implementa Construtor.
 * Responsável por carregar as imagens e acumular os dados visuais,
 * entregando uma CenaJogo via getResult().
 */
public class ConstrutorCenaJogo implements Construtor {

    private ImageView background;
    private final List<ImageView> elements = new ArrayList<>();
    private final List<ImageView> npcs     = new ArrayList<>();
    private final List<CenaJogo.ZoneEntry> zones = new ArrayList<>();
    private ImageView playerView;

    @Override
    public void setBackground(String imagePath, double largura, double altura) {
        background = carregarBackground(imagePath, largura, altura);
    }

    @Override
    public void addElement(String imagePath, double largura, double x, double y) {
        elements.add(carregarImagem(imagePath, largura, x, y));
    }

    @Override
    public void addNPC(String imagePath, double largura, double x, double y) {
        npcs.add(carregarImagem(imagePath, largura, x, y));
    }

    @Override
    public void addInteractiveZone(String id, String imagePath,
                                   double largura, double altura,
                                   double x, double y,
                                   Consumer<String> onEnter) {
        ImageView view = carregarImagem(imagePath, largura, x, y);
        view.setFitHeight(altura);
        view.setPreserveRatio(false);
        zones.add(new CenaJogo.ZoneEntry(id, view, onEnter));
    }

    @Override
    public void setPlayer(String imagePath, double largura, double x, double y) {
        playerView = carregarImagem(imagePath, largura, x, y);
    }

    @Override
    public CenaJogo getResult() {
        return new CenaJogo(background, elements, npcs, zones, playerView);
    }

    // ── Carregamento de imagens ───────────────────────────────────────────────

    private ImageView carregarBackground(String caminho, double largura, double altura) {
        var stream = getClass().getResourceAsStream(caminho);

        if (stream == null)
            throw new IllegalArgumentException("Imagem não encontrada: " + caminho);

        ImageView iv = new ImageView(new Image(stream));
        iv.setFitWidth(largura);
        iv.setFitHeight(altura);
        iv.setPreserveRatio(false);
        return iv;
    }

    private ImageView carregarImagem(String caminho, double largura, double x, double y) {
        var stream = getClass().getResourceAsStream(caminho);

        if (stream == null)
            throw new IllegalArgumentException("Imagem não encontrada: " + caminho);

        ImageView iv = new ImageView(new Image(stream));
        iv.setPreserveRatio(true);
        iv.setFitWidth(largura);
        iv.setLayoutX(x);
        iv.setLayoutY(y);
        return iv;
    }
}