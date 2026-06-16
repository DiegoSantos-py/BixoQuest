package service.batalha;

import model.Ataque.Ataque;
import model.Batalha.EstadoBatalha;
import model.Batalha.Oponente;
import model.Batalha.Turno;
import model.Player.PlayerProva;

public class BatalhaLoopService {

    private final PlayerProvaService playerProvaService = new PlayerProvaService();

    public void atualizar(EstadoBatalha estado, float dt) {
        if (estado.isFinalizado()) {
            return;
        }

        PlayerProva player = estado.getPlayerProva();
        Oponente oponenteAtual = estado.getOponenteAtual();

        if (oponenteAtual != null && oponenteAtual.isDerrotado()) {
            iniciarNovaQuestao(estado);
        }

        if (oponenteAtual == null) {
            estado.setFinalizado(true);
            estado.setVitoria(true);
            return;
        }

        if (estado.isBatalhaAnimal()) {
            if (player.getShieldAtual() <= 0) {
                estado.setFinalizado(true);
                estado.setVitoria(false);
                return;
            }
            if (player.getTurnosUsados() >= 15) {
                estado.setFinalizado(true);
                estado.setVitoria(false);
                return;
            }
        }

        if(estado.getTurnoAtual() == Turno.TURNO_PLAYER){
            return; //n faz nada se for turno do player
        }
        if(estado.getTurnoAtual() == Turno.TURNO_INIMIGO) {
            Ataque ataque = oponenteAtual.getAtaqueAleatorio();
            if (ataque == null) {
                throw new exception.Ataque.NullAttackException();
            }
            estado.setAtaqueAtual(ataque);
            //escolhe ataque -> alterna estado pra Inimigo Atacando(executando ataque inimigo)
            estado.setTurnoAtual(Turno.EXECUTANDO_ATAQUE_INIMIGO);
        }

        if(estado.getTurnoAtual() == Turno.EXECUTANDO_ATAQUE_INIMIGO) {
            if (estado.getAtaqueAtual() == null) {
                throw new exception.Ataque.NullAttackException();
            }
            estado.getAtaqueAtual().atualizar(dt);
            estado.getPlayerProva().atualizarPosicao(dt);
            //garante q o plyayer n saia dos limites do ataque
            playerProvaService.aplicarLimitesDeMovimento(player, estado.getAtaqueAtual());

            if(estado.getAtaqueAtual().isFinalizado()){
                estado.getAtaqueAtual().reiniciarAtaque(); //reinica o ataque pra reutiçizar ele na prox rodada
                estado.setTurnoAtual(Turno.TURNO_PLAYER);
            }
        }
    }

    public void iniciarNovaQuestao(EstadoBatalha estado) {
        float desempenho = estado.getPlayerProva().getDesempenhoQuestaoAtual();
        estado.getPlayerProva().adicionarDesempenhoQuestao(desempenho);
        estado.getPlayerProva().setDesempenhoQuestaoAtual(10f);
        //salva a nota da questao anterior
        Oponente oponenteAtual = estado.getFilaOponentes().poll();
        estado.setOponenteAtual(oponenteAtual);
    }


    //o turno so aumenta qndo o PLAYER FAZ ALGO!!
    public void executarAcaoPlayer(EstadoBatalha estado, int acaoIndex) {
        if (estado.getTurnoAtual() != Turno.TURNO_PLAYER) return;
        
        model.Player.AcaoBatalha acao = estado.getOponenteAtual().getAcoesDisponiveis().get(acaoIndex);
        playerProvaService.executarAcao(estado.getPlayerProva(), acao);
        
        estado.setTurnoAtual(Turno.TURNO_INIMIGO);
    }

    public void atacarOponenteAtual(EstadoBatalha estado, float multiplicadorPrecisao) {
        Oponente oponenteAtual = estado.getOponenteAtual();
        if (oponenteAtual != null && !oponenteAtual.isDerrotado()) {
            if(multiplicadorPrecisao < 1){
                estado.getPlayerProva().setTodosAcertosPerfeitos(false);
            }
            float danoCausado = estado.getPlayerProva().getDanoAtaque() * multiplicadorPrecisao;
            oponenteAtual.receberDano(danoCausado);
        }

        estado.getPlayerProva().addTurnosUsados();
        estado.setTurnoAtual(Turno.TURNO_INIMIGO);
    }
}
