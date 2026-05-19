package service;

import model.EntidadeBatalha;
import model.Oponente;
import model.Player.AcaoBatalha;
import model.Player.PlayerProva;

public class AcaoService {

    public boolean executarAcao(AcaoBatalha acao, PlayerProva player) {


        if (Math.random() <= acao.getChanceAcerto()) {
            player.aplicarBonusAcao(acao.getBonusDano(), acao.getBonusShield(), acao.getBonusConhecimento());
            return true;
        }


        return false;
    }

}
