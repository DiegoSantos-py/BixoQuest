package model.Player;

import java.util.ArrayList;
import java.util.List;
import model.util.Hitbox;
import model.util.Vector2D;
import model.EntidadeBatalha;

public class PlayerProva extends EntidadeBatalha {

    

    // --- Atributos do jogador ---
    private int shieldAtual;    
    private int shieldMaximo;
    private float danoAtaque;
    private float conhecimentoArea;
    private float TEMPO_IMUNIDADE = 0.5f; // 0.5 segundos de invulnerabilidade após receber dano
    private float tempoImunidadeRestante;
    // --- Estatística de desempenho ---
    private int turnosUsados;
    private int acertosPerfeitosConsecutivos;
    private int hitsRecebidos;
    private boolean perdeuNota;
    private boolean levouAlgumDano;
    private List<AcaoBatalha> acoesDisponiveis;




    public PlayerProva(Hitbox hitbox, Vector2D velocidade, float conhecimentoArea) {
        super(hitbox, velocidade);
        this.conhecimentoArea = conhecimentoArea; 
        
        this.shieldMaximo = (int) Math.round(0.22f * this.conhecimentoArea - 1.8f); 
        if (this.shieldMaximo < 0) this.shieldMaximo = 0;
        
        this.shieldAtual = shieldMaximo;
        this.danoAtaque = this.conhecimentoArea / 2f + 5f; 
        this.acoesDisponiveis = new ArrayList<AcaoBatalha>();
        this.turnosUsados = 0;
        this.acertosPerfeitosConsecutivos = 0;
        this.hitsRecebidos = 0;
        this.perdeuNota = false;
        this.levouAlgumDano = false;
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

    public void ReceberDano(float danoShield, float danoNota) {
        if (tempoImunidadeRestante > 0) {
            return; 
        }

        this.levouAlgumDano = true;
        this.hitsRecebidos++;
        if (shieldAtual <= 0) {
            this.perdeuNota = true;

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
        super.atualizarPosicao(deltaTime); 

        if (this.tempoImunidadeRestante > 0) {
            this.tempoImunidadeRestante -= deltaTime;
            
            if (this.tempoImunidadeRestante < 0) {
                this.tempoImunidadeRestante = 0;
            }
        }
    }

    public int getShieldAtual() {
        return shieldAtual;
    }

    public boolean isPerdeuNota() {
        return perdeuNota;
    }

    public float getDanoAtaque() {
        return danoAtaque;
    }
}
