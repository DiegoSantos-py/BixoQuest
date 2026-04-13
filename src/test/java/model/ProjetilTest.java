package model;

import model.Player.PlayerProva;
import model.Projetil.Projeteis.ProjetilQueSegue;
import model.util.Hitbox;
import model.util.Vector2D;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProjetilTest {

    @Test
    void testInteligenciaArtificialDeProjetilQueSegue() {
        // Player parado na direita, bem longe do míssil
        PlayerProva alvo = new PlayerProva(
            new Hitbox(new Vector2D(100, 0), new Vector2D(10, 10), 0f),
            new Vector2D(0, 0),
            50f  // conhecimento alto o suficiente pra ter shield
        );

        // Míssil nasce no ponto (0,0) olhando APENAS pra cima (velocidade Y), sem noção do player
        ProjetilQueSegue missil = new ProjetilQueSegue(
            new Hitbox(new Vector2D(0, 0), new Vector2D(5, 5), 0),
            new Vector2D(0, 10),
            5, 0f, 1000f
        );
        missil.reviver(0, 0, 5, 5, 0, 10, 0, 5, 0f, 1000f); //reespawna o missl em 0,0 , velocidade 10 em y , sem rotacao na hitbox e dando 5 de dano
                                                            // shield, 0 dano nota e durando 1 segundo
        missil.setTarget(alvo);

        // Roda 1 frame de IA (60fps)
        missil.executarAI(0.16f);

        // Após a IA processar, o míssil deve ter dobrado pro eixo X em direção ao player
        assertTrue(missil.getVelocidade().getX() > 0,
            "O míssil ignorou o player em X=100 e continuou reto pra cima. A IA de rastreamento não está funcionando!");
        System.out.println("Míssil rastreador passou no teste! Ele detectou o player a distância e corrigiu sua rota em direção a ele.");
    }

    @Test
    void testDesengatilhoAposDano() {
        // Player com conhecimento 50 garante shield > 0 (0.22*50 - 1.8 = 9.2 → shield = 9)
        PlayerProva alvo = new PlayerProva(
            new Hitbox(new Vector2D(10, 10), new Vector2D(10, 10), 0f),
            new Vector2D(0, 0),
            50f
        );
        int shieldAntes = alvo.getShieldAtual();

        // Projétil nasce exatamente em cima do player (colisão garantida no primeiro frame)
        ProjetilQueSegue tiro = new ProjetilQueSegue(
            new Hitbox(new Vector2D(10, 10), new Vector2D(10, 10), 0),
            new Vector2D(0, 0),
            5, 0f, 1000f
        );
        tiro.reviver(10, 10, 10, 10, 0, 0, 0, 5, 0f, 1000f);
        tiro.setTarget(alvo);

        // Roda 1 frame — colisão deve acontecer, dano ser aplicado e projétil morrer
        tiro.atualizar(0.16f);

        assertFalse(tiro.isAtivo(),
            "O projétil ainda está vivo depois de colidir com o player! Ele deveria ter se destruído.");
        assertTrue(alvo.getShieldAtual() < shieldAntes,
            "O projétil colidiu mas o shield do player não foi reduzido. O dano não está sendo aplicado na colisão!");
        System.out.println("Projétil colidiu, aplicou " + (shieldAntes - alvo.getShieldAtual()) + " de dano no shield e se destruiu corretamente.");
    }
}
