package model.Player;

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


    private static PlayerProva instancia;

    public PlayerProva(Hitbox hitbox, Vector2D velocidade) {
        super(hitbox, velocidade);
        this.shieldMaximo = (int) Math.round(0.22f * conhecimentoArea - 1.8f); // funcao pra conhecimento 10 shield 0, 20 shield 3, 30 shield 5...
        this.shieldAtual = shieldMaximo;
        this.danoAtaque = 20.0f; 
        this.conhecimentoArea = 0.0f; 
        this.turnosUsados = 0;
        this.acertosPerfeitosConsecutivos = 0;
        this.hitsRecebidos = 0;
        this.perdeuNota = false;
        this.levouAlgumDano = false;
        PlayerProva.instancia = this; 
    }

    public static PlayerProva getInstancia() {
        return instancia;
    }

    public void ReceberDano(float danoShield, float danoNota) {
        if (tempoImunidadeRestante > 0) {
            return; // Não recebe danoShield negativo ou zero
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
}