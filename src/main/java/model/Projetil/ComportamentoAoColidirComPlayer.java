package model.Projetil;

import model.Player.PlayerProva;

public interface ComportamentoAoColidirComPlayer {
    void aoColidirComPlayer(Projetil projetil, PlayerProva target);
}
