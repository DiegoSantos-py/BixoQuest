package model.Projetil.Comportamentos;

import model.Projetil.ComportamentoProjetil;
import model.Projetil.Projetil;
import model.util.Vector2D;

public class ProjetilOrbita implements ComportamentoProjetil {
    
    private Vector2D centro;
    private float velocidadeAngular;
    private float raioForcado;
    private float anguloInicial;
    private boolean usaAnguloFixo;

    /**
     * Construtor completo com raio e ângulo forçados.
     */
    public ProjetilOrbita(Vector2D centro, float velocidadeAngular, float raioForcado, float anguloInicial) {
        this.centro = centro;
        this.velocidadeAngular = velocidadeAngular;
        this.raioForcado = raioForcado;
        this.anguloInicial = anguloInicial;
        this.usaAnguloFixo = true;
    }

    /**
     * @param centro O ponto ao redor do qual o projétil vai orbitar.
     * @param velocidadeAngular A velocidade de rotação em radianos por segundo (positivo = horário, negativo = anti-horário).
     * @param raioForcado O raio da órbita. Se for menor ou igual a 0, ele usará a distância atual do projétil dinamicamente.
     */
    public ProjetilOrbita(Vector2D centro, float velocidadeAngular, float raioForcado) {
        this.centro = centro;
        this.velocidadeAngular = velocidadeAngular;
        this.raioForcado = raioForcado;
        this.usaAnguloFixo = false;
    }

    /**
     * Construtor sem raio forçado. O projétil vai orbitar mantendo a distância exata de onde ele foi spawnado.
     */
    public ProjetilOrbita(Vector2D centro, float velocidadeAngular) {
        this(centro, velocidadeAngular, -1f);
    }

    @Override
    public void executar(Projetil p, float dt) {
        if (!p.isAtivo() || centro == null) return;
        
        float centroX = centro.getX();
        float centroY = centro.getY();
        
        float dx = p.getX() - centroX;
        float dy = p.getY() - centroY;
        
        // Se um raio foi forçado (> 0), usa ele. Se não, mantem a distância natural.
        float raio = (raioForcado > 0) ? raioForcado : (float) Math.hypot(dx, dy);
        
        float novoAngulo;
        if (usaAnguloFixo) {
            // Se o ângulo inicial foi fornecido, a órbita é 100% determinística baseada no tempo de vida
            novoAngulo = anguloInicial + (velocidadeAngular * p.getTempoDeVida());
        } else {
            // Se não, calcula com base na posição do frame anterior
            float anguloAtual = (float) Math.atan2(dy, dx);
            novoAngulo = anguloAtual + (velocidadeAngular * dt);
        }
        
        float novoX = centroX + (float) Math.cos(novoAngulo) * raio;
        float novoY = centroY + (float) Math.sin(novoAngulo) * raio;
        
        // Converte o deslocamento desejado em velocidade para manter a integridade da física
        p.getVelocidade().set(
            (novoX - p.getX()) / dt,
            (novoY - p.getY()) / dt
        );
    }
}
