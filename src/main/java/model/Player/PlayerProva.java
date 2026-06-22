package model.Player;

import java.util.ArrayList;

import model.util.Hitbox;
import model.util.Vector2D;
import model.Batalha.EntidadeBatalha;

public class PlayerProva extends EntidadeBatalha {

    

    // --- Atributos do jogador ---
    private int shieldAtual;    
    private int shieldMaximo;
    private float desempenhoQuestaoAtual;
    private float danoAtaque;
    private float conhecimentoArea;
    private float TEMPO_IMUNIDADE = 0.5f; // 0.5 segundos de invulnerabilidade após receber dano
    private float VELOCIDADE = 150f; // 80 unidades/s em todas as direcoes
    private float tempoImunidadeRestante;
    // --- Estatística de desempenho ---
    private ArrayList<Float> desempenhoQuestoes;
    private int turnosUsados;
    private boolean todosAcertosPerfeitos;
    private boolean perdeuNota;
    private boolean levouAlgumDano;
    private boolean movendoCima;
    private boolean movendoEsquerda;
    private boolean movendoBaixo;
    private boolean movendoDireita;






    public PlayerProva(Hitbox hitbox, Vector2D velocidade, float conhecimentoArea) {
        super(hitbox, velocidade, "/assets/batalha/player/player.png");
        this.conhecimentoArea = conhecimentoArea;
        this.todosAcertosPerfeitos = true;

        this.shieldMaximo = Math.round(0.22f * this.conhecimentoArea - 1.8f);
        //^^^ formula pra conhecimento 10 shield 1, 20 pra 3 shield 30->5
        if (this.shieldMaximo <= 0) this.shieldMaximo = 3; // Mínimo de 3 escudos para não morrer instantaneamente
        this.shieldAtual = shieldMaximo;
        this.danoAtaque = this.conhecimentoArea / 4f + 1f ;
        this.desempenhoQuestaoAtual = 10f; //desempenho começa em 10 independemtnete da prova e vai caindo com dano
        this.desempenhoQuestoes = new ArrayList<Float>();
        this.turnosUsados = 0;
        this.perdeuNota = false;
        this.levouAlgumDano = false;
        this.movendoCima = false;
        this.movendoEsquerda = false;
        this.movendoBaixo = false;
        this.movendoDireita = false;
    }

    public void aplicarBonusAcao(int bonusDano, int bonusShield, int bonusConhecimento) {
        this.danoAtaque += bonusDano;
        this.conhecimentoArea += bonusConhecimento;

        int novoShieldMax = (int) Math.round(0.22f * this.conhecimentoArea - 1.8f);
        if (novoShieldMax > this.shieldMaximo) {
            this.shieldMaximo = novoShieldMax;
        }

        this.shieldAtual += bonusShield;
        if (this.shieldAtual > this.shieldMaximo) {
            this.shieldAtual = this.shieldMaximo;
        }
    }

    public void adicionarDesempenhoQuestao(float desempenho){
        this.desempenhoQuestoes.add(desempenho);
    }

    public boolean getTodosAcertosPerfeitos(){
        return this.todosAcertosPerfeitos;
    }

    public void setTodosAcertosPerfeitos(boolean todosAcertosPerfeitos) {
        this.todosAcertosPerfeitos = todosAcertosPerfeitos;
    }
    public ArrayList<Float> getDesempenhoQuestoes(){
            return this.desempenhoQuestoes;
    }


    public float getDesempenhoQuestaoAtual(){
        return this.desempenhoQuestaoAtual;
    }

    public void setDesempenhoQuestaoAtual(float desempenhoQuestaoAtual) {
        this.desempenhoQuestaoAtual = desempenhoQuestaoAtual;
    }
    public void ReceberDano(int danoShield, float danoNota) {
        if (tempoImunidadeRestante > 0) {
            return; 
        }

        this.levouAlgumDano = true;
        if (shieldAtual <= 0) {
            this.perdeuNota = true;
            if(this.desempenhoQuestaoAtual - danoNota < 0){
                this.desempenhoQuestaoAtual = 0;
            }
            else {
                this.desempenhoQuestaoAtual -= danoNota;
            }
        }
        if (danoShield >= shieldAtual) {
            shieldAtual = 0;
        } else {
            shieldAtual -= danoShield;
        }
        tempoImunidadeRestante = TEMPO_IMUNIDADE; 
    }

    @Override
    public void atualizarPosicao(float deltaTime) {

        float dx = 0;
        float dy = 0;
        //dx e dy sao as direcoes
        //elas resetam a cada frame(pq se o jogador n tiver apertando nada ele n se mexe
        //se ta se movemndo pra direita, ele ganha 1 em dx
        //se esquerda,perde 1
        //isso faz com q sla se tiver apertando pra ir pra direita e apetar pra esquerda o player n se mova enquanto vier apertando pra direita
        //isso evita movimentos acidentais, oq é bom em jogos em q precisao é importante(como esse)
        if(movendoDireita){
            dx += 1;
        }
        if(movendoEsquerda){
            dx += -1;
        }
        if(movendoCima){
            dy += -1; // Na tela, para cima significa diminuir o Y
        }
        if(movendoBaixo){
            dy += 1;  // E para baixo significa aumentar o Y
        }

        float magnitude =  (float)Math.sqrt(dx * dx + dy * dy);


        if (magnitude > 0) {
            dx /= magnitude;
            dy /= magnitude;
        }


        this.velocidade.set(dx * VELOCIDADE, dy * VELOCIDADE);

        super.atualizarPosicao(deltaTime);
        if (this.tempoImunidadeRestante > 0) {
            this.tempoImunidadeRestante -= deltaTime;
            
            if (this.tempoImunidadeRestante < 0) {
                this.tempoImunidadeRestante = 0;
            }
        }
    }

    public boolean getMovendoCima() {
        return this.movendoCima;
    }
    public void setMovendoCima(boolean movendoCima) {
        this.movendoCima = movendoCima;
    }
    public boolean getMovendoBaixo() {
        return this.movendoBaixo;
    }

    public void setMovendoBaixo(boolean movendoBaixo) {
        this.movendoBaixo = movendoBaixo;
    }

    public boolean getMovendoEsquerda() {
        return this.movendoEsquerda;
    }

    public void setMovendoEsquerda(boolean movendoEsquerda) {
        this.movendoEsquerda = movendoEsquerda;
    }

    public boolean getMovendoDireita() {
        return this.movendoDireita;
    }

    public void setMovendoDireita(boolean movendoDireita) {
        this.movendoDireita = movendoDireita;
    }


    public int getShieldMaximo() {
        return shieldMaximo;
    }

    public int getShieldAtual() {
        return shieldAtual;
    }

    public float getDanoAtaque() {
        return danoAtaque;
    }


    public int getTurnosUsados() {
        return turnosUsados;
    }

    public void addTurnosUsados(){
        this.turnosUsados++;
    }


    public boolean getPerdeuNota() {
        return perdeuNota;
    }

    public boolean getLevouAlgumDano() {
        return levouAlgumDano;
    }
}

