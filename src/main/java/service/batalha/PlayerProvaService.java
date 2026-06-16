package service.batalha;

import model.Ataque.Ataque;
import model.Disciplina.AreaConhecimento;
import model.Personagem;
import model.Player.AcaoBatalha;
import model.Player.PlayerProva;
import model.util.Hitbox;
import model.util.Vector2D;

public class PlayerProvaService {

    private final AcaoService acaoService = new AcaoService();

    public PlayerProva gerarPlayerProva(Personagem personagem, AreaConhecimento areaDaBatalha) {
        float conhecimento = (float) personagem.getConhecimento(areaDaBatalha);
        // Spawna o player na parte inferior da tela (assumindo 1920x1080)
        Hitbox hitboxPlayerProva = new Hitbox(new Vector2D(960, 800), new Vector2D(5, 5), 0.0f);
        //o player inicia de forma estatica
        Vector2D velocidadeInicial = new Vector2D(0, 0);
        return new PlayerProva(hitboxPlayerProva, velocidadeInicial, conhecimento);
    }

    public void aplicarLimitesDeMovimento(PlayerProva player, Ataque ataque) {
        if (player.getCentro().getX() > ataque.getMaxX()) {
            player.getCentro().setX(ataque.getMaxX());
            player.setMovendoDireita(false);
        }

        if (player.getCentro().getX() < ataque.getMinX()) {
            player.getCentro().setX(ataque.getMinX());
            player.setMovendoEsquerda(false);
        }

        if (player.getCentro().getY() < ataque.getMinY()) {
            player.getCentro().setY(ataque.getMinY());
            player.setMovendoBaixo(false);
        }

        if (player.getCentro().getY() > ataque.getMaxY()) {
            player.getCentro().setY(ataque.getMaxY());
            player.setMovendoCima(false);
        }
    }

    public void executarAcao(PlayerProva player, AcaoBatalha acao) {
        acaoService.executarAcao(acao, player);
        player.addTurnosUsados();
    }
}
