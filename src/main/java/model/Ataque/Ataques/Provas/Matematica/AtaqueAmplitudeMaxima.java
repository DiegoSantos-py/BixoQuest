package model.Ataque.Ataques.Provas.Matematica;

import model.Ataque.Ataque;
import model.Batalha.EntidadeBatalha;
import model.Player.PlayerProva;
import model.util.MathUtils;

public class AtaqueAmplitudeMaxima extends Ataque {

    private float timer = 0;
    private int projeteisSpawnados = 0;

    public AtaqueAmplitudeMaxima(PlayerProva target, EntidadeBatalha owner, float dificuldade) {

        super(target, owner,dificuldade, 200);
    }

    @Override
    protected void logicaAtaque(float dt) {

        float topLeftX = getMinX();
        float topLeftY = getMinY();

        float topRightX = getMaxX();
        float topRightY = getMinY();

        float bottomLeftX = getMinX();
        float bottomLeftY = getMaxY();

        float bottomRightX = getMaxX();
        float bottomRightY = getMaxY();


        timer += dt;
        float velocidade =  35f * (dificuldade/2 + 0.5f); // 1 pra ind 10 2 pra ind 30


        int limiteProjeteis = 12 + (int)(dificuldade/10);
        if (timer >= (10 / dificuldade) && !this.isFinalizado() && projeteisSpawnados < limiteProjeteis) {
            int canto = MathUtils.randomIntInRange(1,4); //1 = esquerda, 2 = cima, 3 = direita, 4 = baixo
            float MultipladorAleatorio = MathUtils.randomFloatInRange(1f,1.5f);
            float angulo = 0f;
            float spawn1X = 0f, spawn1Y = 0f;
            String comp = "";

            float midX = (getMinX() + getMaxX()) / 2f;
            float midY = (getMinY() + getMaxY()) / 2f;

            boolean goesUp = MathUtils.randomFloatInRange(0, 1) > 0.5f;

            switch (canto) {
                case 1: // Esquerda
                    spawn1X = getMinX(); spawn1Y = midY;
                    angulo = 0f; // Para a direita
                    comp = goesUp ? "SENOIDAL_X_UP" : "SENOIDAL_X_DOWN";
                    break;
                case 2: // Cima
                    spawn1X = midX; spawn1Y = getMinY();
                    angulo = (float) (Math.PI / 2); // Para baixo
                    comp = goesUp ? "SENOIDAL_Y_UP" : "SENOIDAL_Y_DOWN";
                    break;
                case 3: // Direita
                    spawn1X = getMaxX(); spawn1Y = midY;
                    angulo = (float) Math.PI; // Para a esquerda
                    comp = goesUp ? "SENOIDAL_X_UP" : "SENOIDAL_X_DOWN";
                    break;
                case 4: // Baixo
                    spawn1X = midX; spawn1Y = getMaxY();
                    angulo = (float) (-Math.PI / 2); // Para cima
                    comp = goesUp ? "SENOIDAL_Y_UP" : "SENOIDAL_Y_DOWN";
                    break;
            }

            // Spawna o projetil
            model.Projetil.Projetil p1 = spawnProjetil(spawn1X, spawn1Y, 40, 40, velocidade * MultipladorAleatorio, angulo, angulo, 1, 0.5f, 2.5f, "/assets/batalha/projeteis/latido.png");
            if (p1 != null) {
                p1.addComportamento(model.Projetil.Comportamentos.ComportamentoFactory.getAI(comp));
            }
            
            projeteisSpawnados += 1;
            timer = 0;
        }

        // lanca 5 + (tiros adicionais baseado na dificuldade) e
        if (projeteisSpawnados >=limiteProjeteis  ) {
            if(timer>=1f){
                this.encerrarAtaque();
            }
        }
    }



    @Override
    public void reiniciarAtaque() {
        super.reiniciarAtaque();
        this.timer = 0;
        this.projeteisSpawnados = 0;
    }

    @Override
    public String toString() {
        return "Ataque Amplitude Maxima";
    }
}
