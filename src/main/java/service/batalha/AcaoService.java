package service.batalha;

import model.Player.AcaoBatalha;
import model.Player.PlayerProva;

public class AcaoService {

    public boolean executarAcao(AcaoBatalha acao, PlayerProva player) {

        //aplica a ação com chance de acerto% de chance de funcionar(pega a chance de acerto, gera um num aleatorio, e so aplica se eesse numero for menor q a chance)
        if (Math.random() <= acao.getChanceAcerto()) {
            player.aplicarBonusAcao(acao.getBonusDano(), acao.getBonusShield(), acao.getBonusConhecimento());
            return true;
            //retorna true pra informar q deu certo(pode ser usado na view)
        }
        //se der errado retorna falso
        return false;
    }

}
