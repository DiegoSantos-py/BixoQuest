package model.Ataque.Ataques.Prova.Matematica;

import model.Ataque.Ataque;
import model.Batalha.EntidadeBatalha;
import model.Player.PlayerProva;
import model.Projetil.ComportamentoProjetil;
import model.Projetil.Projetil;
import model.Projetil.Comportamentos.ProjetilSenoidal;
import model.util.MathUtils;

public class AtaqueAmplitudeMaxima extends Ataque {

    private float timer = 0;
    private int projeteisLancados = 0;

    private final ProjetilSenoidal senoidalXUp   = new ProjetilSenoidal( 200f, (float) Math.PI * 1.5f, true);
    private final ProjetilSenoidal senoidalXDown  = new ProjetilSenoidal(-200f, (float) Math.PI * 1.5f, true);
    private final ProjetilSenoidal senoidalYUp    = new ProjetilSenoidal( 200f, (float) Math.PI * 1.5f, false);
    private final ProjetilSenoidal senoidalYDown  = new ProjetilSenoidal(-200f, (float) Math.PI * 1.5f, false);

    public AtaqueAmplitudeMaxima(PlayerProva target, EntidadeBatalha owner, float dificuldade) {

        super(target, owner,dificuldade, 200);
    }

    @Override
    protected void logicaAtaque(float dt) {



        timer += dt;
        float velocidade =  35f * (dificuldade/2 + 0.5f); // 1 pra ind 10 2 pra ind 30


        int limiteProjeteis = 12 + (int)(dificuldade/10);
        if (timer >= (10 / dificuldade) && !this.isFinalizado() && projeteisLancados < limiteProjeteis) {
            int canto = MathUtils.randomIntInRange(1,4); //1 = esquerda, 2 = cima, 3 = direita, 4 = baixo  //escolhe o canto
            float multiplicadorAleatorio = MathUtils.randomFloatInRange(1f,1.5f);
            float angulo = 0f;
            float origemX = 0f, origemY = 0f;
            ComportamentoProjetil comp = null;

            float centroX = (getMinX() + getMaxX()) / 2f;
            float centroY = (getMinY() + getMaxY()) / 2f;
            boolean vaiPraCima = MathUtils.randomFloatInRange(0, 1) > 0.5f;//escolhe se vai pra cima ou pra baixo

            switch (canto) {
                case 1: // Esquerda
                    origemX = getMinX();
                    origemY = centroY;
                    angulo = 0f; // Para a direita
                    comp = vaiPraCima ? senoidalXUp : senoidalXDown;
                    break;
                case 2: // Cima
                    origemX = centroX;
                    origemY = getMinY();
                    angulo = (float) (Math.PI / 2); // Para baixo
                    comp = vaiPraCima ? senoidalYUp : senoidalYDown;
                    break;
                case 3: // Direita
                    origemX = getMaxX();
                    origemY = centroY;
                    angulo = (float) Math.PI; // Para a esquerda
                    comp = vaiPraCima ? senoidalXUp : senoidalXDown;
                    break;
                case 4: // Baixo
                    origemX = centroX;
                    origemY = getMaxY();
                    angulo = (float) (-Math.PI / 2); // Para cima
                    comp = vaiPraCima ? senoidalYUp : senoidalYDown;
                    break;
            }

            // Spawna o projetil
           Projetil p1 = spawnProjetil(origemX, origemY, 40, 40, velocidade * multiplicadorAleatorio, angulo, angulo, 1, 0.5f, 2.5f, "latido.png");
            if (p1 != null) {
                p1.addComportamento(comp);
            }
            
            projeteisLancados += 1;
            timer = 0;
        }

        // lanca 5 + (tiros adicionais baseado na dificuldade) e
        if (projeteisLancados >=limiteProjeteis  ) {
            if(timer>=1f){
                this.encerrarAtaque();
            }
        }
    }



    @Override
    public void reiniciarAtaque() {
        super.reiniciarAtaque();
        this.timer = 0;
        this.projeteisLancados = 0;
    }


}
