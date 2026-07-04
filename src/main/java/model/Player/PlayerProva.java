package model.Player;

import java.util.ArrayList;

import model.util.Hitbox;
import model.util.Vector2D;
import model.Batalha.EntidadeBatalha;
import model.Projetil.Projetil;

public class PlayerProva extends EntidadeBatalha {
    
    public enum SoulMode { RED, BLUE, YELLOW }

    // --- Atributos de Física/Soul Mode ---
    private SoulMode soulMode = SoulMode.RED;
    private boolean isGrounded = false;
    private float yVelocity = 0f;
    private boolean isJumping = false;
    private float jumpTimer = 0f;
    private static final float MAX_JUMP_TIME = 0.25f; // Segundos permitidos para segurar o pulo
    private static final float GRAVITY = 800f;
    private static final float JUMP_FORCE = -400f;

    // --- Atributos do jogador ---
    private int shieldAtual;    
    private int shieldMaximo;
    private float desempenhoQuestaoAtual;
    private float danoAtaque;
    private float conhecimentoArea;
    private float TEMPO_IMUNIDADE = 0.75f; // 0.5 segundos de invulnerabilidade após receber dano
    private float VELOCIDADE = 225f;
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

    // --- Yellow Soul ---
    private model.Projetil.ProjetilFactory factoryAtual = null;
    private boolean atirando = false;
    private float cooldownTiro = 0f;
    private final model.Projetil.Comportamentos.ProjetilEliminaColisao ELIMINA_COLISAO =
            new model.Projetil.Comportamentos.ProjetilEliminaColisao();






    public PlayerProva(Hitbox hitbox, Vector2D velocidade, float conhecimentoArea) {
        super(hitbox, velocidade, "/assets/batalha/player/player.png");
        this.conhecimentoArea = conhecimentoArea;
        this.todosAcertosPerfeitos = true;

        this.shieldMaximo = Math.max(1, Math.round(0.2f * this.conhecimentoArea + 1f));
        //^^^ formula pra conhecimento 10 shield 3, 20 pra 5 shield 30->7
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
        if (danoShield <= 0 && danoNota <= 0f) {
            return; // projéteis inofensivos (como prévias) não contam como hit
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

    public boolean isInvulneravel() {
        return this.tempoImunidadeRestante > 0;
    }

    @Override
    public void atualizarPosicao(float deltaTime) {

        if (this.soulMode == SoulMode.RED) {
            float dx = 0;
            float dy = 0;
            if (movendoDireita) dx += 1;
            if (movendoEsquerda) dx += -1;
            if (movendoCima) dy += -1;
            if (movendoBaixo) dy += 1;

            float magnitude = (float) Math.sqrt(dx * dx + dy * dy);
            if (magnitude > 0) {
                dx /= magnitude;
                dy /= magnitude;
            }
            this.velocidade.set(dx * VELOCIDADE, dy * VELOCIDADE);
            
        } else if (this.soulMode == SoulMode.BLUE) {
            float dx = 0;
            if (movendoDireita) dx += 1;
            if (movendoEsquerda) dx += -1;
            
            // Pulo Inicial
            if (movendoCima && isGrounded) {
                isJumping = true;
                jumpTimer = 0f;
                isGrounded = false;
            }
            
            // Pulo Sustentado (Tempo Limite)
            if (isJumping && movendoCima && jumpTimer < MAX_JUMP_TIME) {
                yVelocity = JUMP_FORCE;
                jumpTimer += deltaTime;
            } else {
                if (isJumping && !movendoCima && yVelocity < 0) {
                    yVelocity *= 0.5f;
                }
                isJumping = false;
            }
            
            if (!isGrounded && !isJumping) {
                yVelocity += GRAVITY * deltaTime;
            } else if (isGrounded) {
                yVelocity = 0;
            }
            
            if (movendoBaixo && !isGrounded) {
                yVelocity += (GRAVITY * 1.5f) * deltaTime;
            }

            float maxFallSpeed = movendoBaixo ? (GRAVITY * 1.5f) : GRAVITY;
            if (yVelocity > maxFallSpeed) {
                yVelocity = maxFallSpeed;
            }
            
            this.velocidade.set(dx * VELOCIDADE, yVelocity);
        } else if (this.soulMode == SoulMode.YELLOW) {
            float dx = 0;
            float dy = 0;
            if (movendoDireita)  dx += 1;
            if (movendoEsquerda) dx -= 1;
            if (movendoCima)     dy -= 1;
            if (movendoBaixo)    dy += 1;

            float magnitude = (float) Math.sqrt(dx * dx + dy * dy);
            if (magnitude > 0) { dx /= magnitude; dy /= magnitude; }
            this.velocidade.set(dx * VELOCIDADE, dy * VELOCIDADE);

            cooldownTiro -= deltaTime;
            if (atirando && cooldownTiro <= 0f && factoryAtual != null) {
                    Projetil bala = factoryAtual.spawn(
                        getX(), getY(),
                        12, 13,
                        600f,
                        -(float) Math.PI / 2f,
                        0, 0,
                        0f, 2f,"tiroAmarelo.png"
                );
                if (bala != null) {
                    bala.addComportamento(ELIMINA_COLISAO);
                }
                cooldownTiro = 0.25f;
            }

        }

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

    public void setAtirando(boolean atirando) {
        this.atirando = atirando;
    }

    public void setFactoryAtual(model.Projetil.ProjetilFactory factory) {
        this.factoryAtual = factory;
    }


    public int getShieldMaximo() {
        return shieldMaximo;
    }

    public void setShieldMaximo(int shieldMaximo) {
        this.shieldMaximo = shieldMaximo;
    }

    public int getShieldAtual() {
        return shieldAtual;
    }

    public void setShieldAtual(int shieldAtual) {
        this.shieldAtual = shieldAtual;
    }

    public float getDanoAtaque() {
        return danoAtaque;
    }

    public void setDanoAtaque(float danoAtaque) {
        this.danoAtaque = danoAtaque;
    }

    public float getConhecimentoArea() {
        return conhecimentoArea;
    }

    public void setConhecimentoArea(float conhecimentoArea) {
        this.conhecimentoArea = conhecimentoArea;
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

    public void setGrounded(boolean grounded) {
        this.isGrounded = grounded;
    }

    public boolean isGrounded() {
        return isGrounded;
    }

    public SoulMode getSoulMode() {
        return soulMode;
    }

    public void setSoulMode(SoulMode mode) {
        this.soulMode = mode;
        if (mode == SoulMode.BLUE) {
            this.spriteDir = "/assets/batalha/player/playerAzul.png";
        } else if (mode == SoulMode.YELLOW) {
            this.spriteDir = "/assets/batalha/player/playerAmarelo.png";
        } else {
            this.spriteDir = "/assets/batalha/player/player.png";
        }
    }
}

