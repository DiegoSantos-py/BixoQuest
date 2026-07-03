package service.batalha;

import model.Player.AcaoBatalha;
import model.Player.PlayerProva;

public class AcaoService {

    public boolean executarAcao(AcaoBatalha acao, PlayerProva player) {

        //aplica a ação com chance de acerto% de chance de funcionar(pega a chance de acerto, gera um num aleatorio, e so aplica se eesse numero for menor q a chance)
        if (Math.random() <= acao.getChanceAcerto()) {
            // Lógica de Dano e Conhecimento
            player.setDanoAtaque(player.getDanoAtaque() + acao.getBonusDano());
            player.setConhecimentoArea(player.getConhecimentoArea() + acao.getBonusConhecimento());
            
            // Lógica de Shield
            int novoShieldMax = Math.max(1, Math.round(0.2f * player.getConhecimentoArea() + 1f));
            if (novoShieldMax > player.getShieldMaximo()) {
                player.setShieldMaximo(novoShieldMax);
            }
            
            int novoShieldAtual = player.getShieldAtual() + acao.getBonusShield();
            if (novoShieldAtual > player.getShieldMaximo()) {
                novoShieldAtual = player.getShieldMaximo();
            }
            player.setShieldAtual(novoShieldAtual);
            
            // Lógica de Nota
            if (acao.getBonusNota() > 0) {
                float novaNota = player.getDesempenhoQuestaoAtual() + acao.getBonusNota();
                if (novaNota > 10f) {
                    novaNota = 10f;
                }
                player.setDesempenhoQuestaoAtual(novaNota);
            }
            
            return true;
        }
        //se der errado retorna falso
        return false;
    }

}
