package model.Ataque.Ataques.Prova.Hardware;

import model.Ataque.Ataque;
import model.Batalha.EntidadeBatalha;
import model.Player.PlayerProva;
import model.Projetil.Comportamentos.ProjetilAceleracao;
import model.Projetil.Projetil;
import model.util.MathUtils;

public class AtaqueBarramento extends Ataque {

    private float timer = 0;
    private boolean viasCriadas = false;
    private int numVias = 8;
    private float[] posViasY;
    
    private boolean vaiPraEsquerda = true;
    private ProjetilAceleracao acelerador;

    public AtaqueBarramento(PlayerProva target, EntidadeBatalha owner, float dificuldade) {
        super(target, owner, dificuldade, 100);
        this.acelerador = new ProjetilAceleracao(160f + (dificuldade * 5f));
    }

    @Override
    protected void logicaAtaque(float dt) {
        float duracaoAtaque = 20f;

        if (!viasCriadas) {
            float largura = (maxX - minX);
            float altura = (maxY - minY);
            //definine o espacamento entre cada linha baseado na altura
            float espacamentoY = altura / (numVias + 1f);
            float centroCaixaX = (minX + maxX) / 2;
            //guarda as posicoes de cada linha num array pra usar pra sapawnar os projetis
            posViasY = new float[numVias];

            for (int i = 1; i <= numVias; i++) {
                float posY = minY + (espacamentoY * i);
                posViasY[i - 1] = posY;
                
                Projetil linhaH = spawnProjetil(
                        centroCaixaX, posY,
                        3, largura / 2,
                        0, 0, (float) (Math.PI / 2),
                        0, 0f,
                        duracaoAtaque,
                        "arranhaoPrevia.png");
                if (linhaH != null) {
                    linhaH.setPersistente(true);
                }
            }
            viasCriadas = true;
            timer = 30f;
        }

        timer += dt;
        float tempoEspera = Math.max(1.3f, 2.8f - (dificuldade / 20f));

        float velocidade = 50f + (dificuldade * 2f);
        
        if (tempoDecorrido >= 1.5f && timer >= tempoEspera && tempoDecorrido < duracaoAtaque - 2.0f) {
            
            int viaSegura1 = MathUtils.randomIntInRange(0, numVias - 1);
            int viaSegura2 = MathUtils.randomIntInRange(0, numVias - 1);
            if (MathUtils.randomFloatInRange(0, 1) > 0.4f) {
                viaSegura2 = viaSegura1; //60% de chance de so ter 1 via disponivel
            }

            for (int i = 0; i < numVias; i++) {
                boolean ehZero = (i == viaSegura1 || i == viaSegura2);
                
                float inicioX = vaiPraEsquerda ? minX - 20 : maxX + 20;
                float anguloMovimento = vaiPraEsquerda ? 0 : (float) Math.PI;
                
                String sprite = ehZero ? "zero.png" : "um.png";
                float larg = ehZero ? 24f : 23f;
                float alt = ehZero ? 23f : 25f;
                
                int danoShield = ehZero ? 0 : 1;
                float danoNota = ehZero ? 0f : 1f;
                
                float tempoDespawn = 8.0f;
                float rotacaoSprite = anguloMovimento + (float) (Math.PI / 2f);
                
                Projetil p = spawnProjetil(
                    inicioX, posViasY[i],
                    larg, alt,
                    velocidade,
                    anguloMovimento, rotacaoSprite,
                    danoShield, danoNota,
                    tempoDespawn,
                    sprite
                );
                
                if (p != null) {
                    p.addComportamento(acelerador);
                }
            }
            
            vaiPraEsquerda = !vaiPraEsquerda;
            timer = 0;
        }

        if (tempoDecorrido >= duracaoAtaque) {
            encerrarAtaque();
        }
    }

    @Override
    public void reiniciarAtaque() {
        super.reiniciarAtaque();
        timer = 0;
        viasCriadas = false;
        vaiPraEsquerda = true;
    }
}
