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
        Hitbox hitboxPlayerProva = new Hitbox(new Vector2D(960, 800), new Vector2D(15, 15), 0.0f);  //é aqui onde VOCE nasce
        //o player inicia de forma estatica
        Vector2D velocidadeInicial = new Vector2D(0, 0);
        return new PlayerProva(hitboxPlayerProva, velocidadeInicial, conhecimento);
    }

    public void aplicarLimitesDeMovimento(PlayerProva player, Ataque ataque) {
        float RAIO = 22f;

        if (player.getCentro().getX() > ataque.getMaxX() - RAIO) {
            player.getCentro().setX(ataque.getMaxX() - RAIO);
            player.setMovendoDireita(false);
        }

        if (player.getCentro().getX() < ataque.getMinX() + RAIO) {
            player.getCentro().setX(ataque.getMinX() + RAIO);
            player.setMovendoEsquerda(false);
        }

        if (player.getCentro().getY() < ataque.getMinY() + RAIO) {
            player.getCentro().setY(ataque.getMinY() + RAIO);
            player.setMovendoBaixo(false);
        }

        if (player.getCentro().getY() >= ataque.getMaxY() - RAIO) {
            player.getCentro().setY(ataque.getMaxY() - RAIO);
            player.setMovendoCima(false);
            player.setGrounded(true);
        } else {
            player.setGrounded(false);
        }
    }

    public boolean executarAcao(PlayerProva player, AcaoBatalha acao) {
        boolean sucesso = acaoService.executarAcao(acao, player);
        player.addTurnosUsados();
        return sucesso;
    }
}
