package model.Projetil.Comportamentos;

import model.Projetil.ComportamentoProjetil;
import model.Projetil.Projetil;

import java.util.List;

/**
 * Comportamento stateless: elimina o primeiro projétil do pool (que não seja si mesmo)
 * com o qual colidir. Ao colidir, desativa ambos.
 * Projetado para projéteis do jogador no SoulMode.YELLOW.
 */
public class ProjetilEliminaColisao implements ComportamentoProjetil {

    @Override
    public void executar(Projetil projetil, float dt) {
        if (!projetil.isAtivo()) return;

        List<Projetil> ativos = projetil.getFactory().getAtivos();
        for (Projetil outro : ativos) {
            if (outro == projetil) continue; // pula a si mesmo
            if (projetil.getHitbox().checarColisao(outro.getHitbox()) && !outro.getSpriteUrl().endsWith("tiroAmarelo.png") && !outro.getSpriteUrl().endsWith("fogo.png"))  {
                outro.aoDespawnar();
                outro.desativar();
                
                projetil.aoDespawnar();
                projetil.desativar();
                return; // para imediatamente após eliminar o primeiro
            }
        }
    }
}
