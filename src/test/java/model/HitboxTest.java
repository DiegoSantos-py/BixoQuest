package model;

import model.util.Hitbox;
import model.util.Vector2D;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HitboxTest {

    @Test
    void testColisaoOrtogonalPerfeita() {
        // Duas caixas 20x20 com centros a 10 pixels de distância — claramente se tocando
        Hitbox h1 = new Hitbox(new Vector2D(0, 0), new Vector2D(20, 20), 0.0f);
        Hitbox h2 = new Hitbox(new Vector2D(10, 10), new Vector2D(20, 20), 0.0f);

        assertTrue(h1.checarColisao(h2),
            "Duas caixas sobrepostas deveriam colidir, mas o algoritmo disse que não. Verifique os eixos OBB.");
        System.out.println("Colisão frontal entre duas caixas sobrepostas detectada com sucesso.");
    }

    @Test
    void testEspacoLimpoFalsoPositivo() {
        // Caixas com 40 pixels de separação — sem chance de toque
        Hitbox h1 = new Hitbox(new Vector2D(0, 0), new Vector2D(10, 10), 0.0f);
        Hitbox h2 = new Hitbox(new Vector2D(50, 50), new Vector2D(10, 10), 0.0f);

        assertFalse(h1.checarColisao(h2),
            "Caixas muito distantes foram marcadas como colidindo. Falso positivo detectado no algoritmo.");
        System.out.println("Caixas distantes corretamente ignoradas. Nenhum falso positivo de colisão.");
    }

    @Test
    void testColisaoExtremaComRotacao() {
        // Um coração parado em (20,20)
        Hitbox playerCaixa = new Hitbox(new Vector2D(20, 20), new Vector2D(10, 10), 0f);

        // Uma faca comprida girando 45 graus passando por cima do coração
        Hitbox projetilGirante = new Hitbox(new Vector2D(25, 25), new Vector2D(50, 2), (float)(Math.PI / 4));

        assertTrue(playerCaixa.checarColisao(projetilGirante),
            "A faca rotacionada a 45 graus deveria atingir o coração, mas o algoritmo OBB não detectou. Revise o cálculo de eixos rotacionados.");
        System.out.println("Colisão com projétil rotacionado a 45 graus detectada corretamente pelo algoritmo OBB.");
    }
}
