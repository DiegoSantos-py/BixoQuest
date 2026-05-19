package service;

import model.Ataque.Ataque;
import model.Ataque.Ataques.AtaqueArranhao;
import model.Ataque.Ataques.AtaqueLatido;
import model.Ataque.Ataques.AtaqueMordida;
import model.EstadoBatalha;
import model.Evento.Prova.ProvaBatalha;
import model.Evento.Prova.Questao;
import model.Evento.Prova.ResultadoProva;
import model.Npc.Animal;
import model.Oponente;
import model.Personagem;
import model.Player.AcaoBatalha;
import model.Turno;
import model.Player.PlayerProva;
import model.Disciplina.AreaConhecimento;
import model.util.Hitbox;
import model.util.Vector2D;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class BatalhaService {

    //inicia a batalha animal
    public EstadoBatalha iniciarBatalha(Personagem personagem, Animal animal) {
        Oponente oponenteAnimal =  criarOponenteAnimal(animal);
        //seta o target de cada ataque dos animais como o o jogador
        PlayerProva playerProva = gerarPlayerProva(personagem, AreaConhecimento.ANI);
        for(Ataque ataque : oponenteAnimal.getAtaquesDisponiveis()){
            ataque.setTarget(playerProva);
        }

        Queue<Oponente> oponentes = new LinkedList<>();
        //adiciona todos os oponentes da batalha(No caso 1 unico(o animal em si))
        oponentes.add(oponenteAnimal);

        return new EstadoBatalha(playerProva, personagem ,oponentes, animal );

    }

    //mesmo fluxo pra prova
    public EstadoBatalha iniciarBatalha(Personagem personagem, ProvaBatalha provaBatalha) {
        Queue<Oponente> oponentes = new LinkedList<>();
        PlayerProva playerProva = gerarPlayerProva(personagem, provaBatalha.getAreaConhecimento());
        //mas cada questao é 1 oponente difernete com 1 ATAQUE(em média)(pode ter mais)
        for(Questao questao : provaBatalha.getQuestoes()){
            //cria os oponentes de cada questao
            Oponente oponenteQuestao = criarOponenteQuestao(questao);
            for(Ataque ataque : oponenteQuestao.getAtaquesDisponiveis()){
                //seta o targeet dos ataques como  o jogador
                ataque.setTarget(playerProva);
            }

            oponentes.add(oponenteQuestao);
        }
        return new EstadoBatalha(playerProva, personagem ,oponentes, provaBatalha );
    }


    public void atualizar(EstadoBatalha estado, float dt) {
        if (estado.isFinalizado()) {
            return;
        }

        PlayerProva player = estado.getPlayerProva();
        Oponente oponenteAtual = estado.getOponenteAtual();


        if (oponenteAtual != null && oponenteAtual.isDerrotado()) {
            float desempenho = estado.getPlayerProva().getDesempenhoQuestaoAtual();
            estado.getPlayerProva().adicionarDesempenhoQuestao(desempenho);
            estado.getPlayerProva().setDesempenhoQuestaoAtual(10f);
            //salva a nota da questao anterior
            oponenteAtual = estado.getFilaOponentes().poll();
            estado.setOponenteAtual(oponenteAtual);
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
        }

        if(estado.getTurnoAtual() == Turno.TURNO_PLAYER){
            return; //COISAS DO PLAYER FICAM NA FUNCAO ACAO PLAYER N DA PRA BOTAR AQ
        }
        if(estado.getTurnoAtual() == Turno.TURNO_INIMIGO) {
            Ataque ataque = oponenteAtual.getAtaqueAleatorio();
            estado.setAtaqueAtual(ataque);
            //escolhe ataque -> alterna estado pra Inimigo Atacando(executando ataque inimigo)
            estado.setTurnoAtual(Turno.EXECUTANDO_ATAQUE_INIMIGO);
        }

        if(estado.getTurnoAtual() == Turno.EXECUTANDO_ATAQUE_INIMIGO) {
            estado.getAtaqueAtual().atualizar(dt);
            estado.getPlayerProva().atualizarPosicao(dt);
            if(estado.getAtaqueAtual().isFinalizado()){
                estado.getAtaqueAtual().reiniciarAtaque(); //reinica o ataque pra reutiçizar ele na prox rodada
                estado.setTurnoAtual(Turno.TURNO_PLAYER);
            }
        }




    }


    //o turno so aumenta qndo o PLAYER FAZ ALGO!!
    public void executarAcaoPlayer(EstadoBatalha estado, int acaoIndex) {

        if (estado.getTurnoAtual() != Turno.TURNO_PLAYER) return;
        AcaoBatalha acao =
                estado.getPlayerProva()
                        .getAcoesDisponiveis()
                        .get(acaoIndex);

        AcaoService service = new AcaoService();
        service.executarAcao(acao, estado.getPlayerProva());
        estado.getPlayerProva().addTurnosUsados();
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

    private PlayerProva gerarPlayerProva(Personagem personagem, AreaConhecimento areaDaBatalha) {


        float conhecimento = (float) personagem.getConhecimento(areaDaBatalha);
        // Spawna o player na parte inferior da tela (assumindo 1920x1080)
        Hitbox hitboxPlayerProva = new Hitbox(new Vector2D(960, 800), new Vector2D(20, 20), 0.0f);

        Vector2D velocidadeInicial = new Vector2D(0, 0);

        return new PlayerProva(hitboxPlayerProva, velocidadeInicial, conhecimento);
    }

    private Oponente criarOponenteAnimal(Animal animal) {
        float hpCalculado = animal.getIndole();
        // Spawna o inimigo na parte superior da tela
        Hitbox hitboxAnimal = new Hitbox(new Vector2D(960, 300), new Vector2D(50, 50), 0.0f);
        Oponente animalOponente = new Oponente(hitboxAnimal, new Vector2D(0, 0), animal.getNome(), hpCalculado, AreaConhecimento.ANI);

        animalOponente.adicionarAtaque(new AtaqueMordida(null, animalOponente, animal.getIndole()));

        switch (animal.getEspecie()) {
            case GATO:
                animalOponente.adicionarAtaque(new AtaqueArranhao(null, animalOponente, animal.getIndole()));
                break;
            case CACHORRO:
                animalOponente.adicionarAtaque(new AtaqueLatido(null, animalOponente, animal.getIndole()));
                break;
        }

        return animalOponente;
    }



    private Oponente criarOponenteQuestao(Questao questao) {
        float hpCalculado = questao.getHp();
        // Spawna a questao na parte superior da tela
        Hitbox hitboxQuestao = new Hitbox(new Vector2D(960, 300), new Vector2D(50, 50), 0.0f);
        Oponente oponenteQuestao = new Oponente(hitboxQuestao, new Vector2D(0, 0), questao.getNome(), hpCalculado, questao.getAreaConhecimento());

        oponenteQuestao.adicionarAtaque(questao.getAtaque());

        return oponenteQuestao;
    }







    public ResultadoProva finalizarBatalha(EstadoBatalha estado , ProvaBatalha provaBatalha) {
        PlayerProva playerProva = estado.getPlayerProva();
        ArrayList<Float> desempenho = playerProva.getDesempenhoQuestoes();
        float notaFinal = calcularNotaFinal(desempenho, playerProva);
        ResultadoProva resultadoProva =  new ResultadoProva(
                estado.getPersonagem(),
                provaBatalha,
                notaFinal,
                playerProva.getTurnosUsados(),
                playerProva.getTodosAcertosPerfeitos(),
                playerProva.getLevouAlgumDano(),
                playerProva.getPerdeuNota()
        );
        return resultadoProva;
    }


    public void finalizarBatalha(EstadoBatalha estado , Animal animal) {
        animal.setDomado(true);;
        //todo: ver isso aq
    }


    private static float calcularNotaFinal(ArrayList<Float> desempenho, PlayerProva playerProva) {
        float notaFinal = 0;
        for(float nota : desempenho){
            notaFinal += nota;
        }
        //se n tiver elemtnos na lista(improvavel mas vai que) divide por 1 se nao pelo tamanho
        notaFinal /= (desempenho.isEmpty()) ? 1 : desempenho.size() ;
        //ganha PONTO SE
        //TERMINAR A PROVA RAPIDO, SE TERMINAR COM TODOS OS ACERTOS PERFEITOS(no meio do slider)
        //SEM PERDER NOTA(pode levar dano no shield)
        //SEM LEVAR DANO NO GERAL(por isso o maximo é 11,q nem numa prova normal questaoe xtra)
        notaFinal += (playerProva.getTurnosUsados() <= 20) ? 0.25f : 0;
        notaFinal += (playerProva.getTodosAcertosPerfeitos()) ? 0.25f : 0;
        notaFinal += (!playerProva.getPerdeuNota()) ? 0.25f : 0; 
        notaFinal += (!playerProva.getLevouAlgumDano()) ? 0.25f : 0;
        return notaFinal;
    }





}
