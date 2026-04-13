package service;

import model.Player.AcaoBatalha;
import model.Player.PlayerProva;

public class AcaoService {

    public boolean executarAcao(AcaoBatalha acao, PlayerProva player) {
        // A lógica de probabilidade que antes sujava o Model
        if (Math.random() <= acao.getChanceAcerto()) {
            player.aplicarBonusAcao(acao.getBonusDano(), acao.getBonusShield(), acao.getBonusConhecimento());
            return true;
        }
        return false;
    }
}
