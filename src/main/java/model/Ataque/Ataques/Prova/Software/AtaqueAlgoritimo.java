package model.Ataque.Ataques.Prova.Software;

import model.Ataque.Ataque;
import model.Batalha.EntidadeBatalha;
import model.Player.PlayerProva;
import java.util.ArrayList;

public class AtaqueAlgoritimo extends Ataque {

    private float timer = 0;
    private int instrucaoIndex = 0;
    private final ArrayList<Instrucao> algoritmo = new ArrayList<>();
    
    // Matrix of projectiles
    private model.Projetil.Projetil[][] previas = new model.Projetil.Projetil[3][3];
    private model.Projetil.Projetil[][] malha = new model.Projetil.Projetil[3][3];
    private model.Projetil.Projetil[][] malhaHitboxes = new model.Projetil.Projetil[3][3];
    private boolean[][] ativos = new boolean[3][3];
    
    private float cellWidth;
    private float cellHeight;
    private float offsetX;
    private float offsetY;
    
    private float tamanhoQuadrado = 65f;
    
    // To show instructions visually
    private float instrucaoY = 0;
    private model.Projetil.Projetil highlight;

    private enum TipoInstrucao {
        ESPERAR, ATIVAR, DESATIVAR, ENCERRAR
    }

    private static class Instrucao {
        TipoInstrucao tipo;
        float tempo;
        int linha;
        int coluna;
        String spritePath;

        Instrucao(TipoInstrucao tipo, float tempo, int linha, int coluna, String spritePath) {
            this.tipo = tipo;
            this.tempo = tempo;
            this.linha = linha;
            this.coluna = coluna;
            this.spritePath = spritePath;
        }
    }

    public AtaqueAlgoritimo(PlayerProva target, EntidadeBatalha owner, float dificuldade) {
        super(target, owner, dificuldade, 200);
        gerarAlgoritmo();
    }

    private void gerarAlgoritmo() {
        algoritmo.clear();
        algoritmo.add(new Instrucao(TipoInstrucao.ESPERAR, 1.0f, -1, -1, "esperarSegundos1.png"));
        
        int qtdInstrucoes = model.util.MathUtils.randomIntInRange(5, 8);
        boolean[][] estadoTemp = new boolean[3][3];
        
        for (int i = 0; i < qtdInstrucoes; i++) {
            // Find possible activations and deactivations
            ArrayList<int[]> inativos = new ArrayList<>();
            ArrayList<int[]> ativosTemp = new ArrayList<>();
            for (int l = 0; l < 3; l++) {
                for (int c = 0; c < 3; c++) {
                    if (estadoTemp[l][c]) ativosTemp.add(new int[]{l, c});
                    else inativos.add(new int[]{l, c});
                }
            }
            
            // Choose action
            boolean querAtivar = !inativos.isEmpty() && (ativosTemp.isEmpty() || Math.random() > 0.4);
            
            if (querAtivar) {
                int[] escolhido = inativos.get(model.util.MathUtils.randomIntInRange(0, inativos.size() - 1));
                estadoTemp[escolhido[0]][escolhido[1]] = true;
                algoritmo.add(new Instrucao(TipoInstrucao.ATIVAR, 0, escolhido[0], escolhido[1], "ativarQuadrado" + escolhido[0] + escolhido[1] + ".png"));
            } else if (!ativosTemp.isEmpty()) {
                int[] escolhido = ativosTemp.get(model.util.MathUtils.randomIntInRange(0, ativosTemp.size() - 1));
                estadoTemp[escolhido[0]][escolhido[1]] = false;
                algoritmo.add(new Instrucao(TipoInstrucao.DESATIVAR, 0, escolhido[0], escolhido[1], "desativarQuadrado" + escolhido[0] + escolhido[1] + ".png"));
            }
            
            // Always wait 1s between instructions to make it easier
            algoritmo.add(new Instrucao(TipoInstrucao.ESPERAR, 1.0f, -1, -1, "esperarSegundos1.png"));
        }
        
        algoritmo.add(new Instrucao(TipoInstrucao.ESPERAR, 1.0f, -1, -1, "esperarSegundos1.png"));
        algoritmo.add(new Instrucao(TipoInstrucao.ENCERRAR, 0, -1, -1, "encerrar.png"));
    }

    @Override
    protected void logicaAtaque(float dt) {
        if (cellWidth == 0) {
            cellWidth = (getMaxX() - getMinX()) / 3f;
            cellHeight = (getMaxY() - getMinY()) / 3f;
            offsetX = getMinX();
            offsetY = getMinY();
            
            // Spawns the matrix of safe "previa" squares
            for (int l = 0; l < 3; l++) {
                for (int c = 0; c < 3; c++) {
                    float px = offsetX + c * cellWidth + (cellWidth / 2f);
                    float py = offsetY + l * cellHeight + (cellHeight / 2f);
                    // Previa is a safe visual indicator (0 damage).
                    previas[l][c] = spawnProjetil(px, py, tamanhoQuadrado, tamanhoQuadrado, 0, 0, 0, 0, 0f, 99f, "quadradoPrevia.png");
                }
            }
            
            // Spawna todas as instrucoes SIMULTANEAMENTE e PARADAS na direita da tela
            float instX = getMaxX() + 200;
            float instY = getMinY() + 20;
            for (int i = 0; i < algoritmo.size(); i++) {
                Instrucao inst = algoritmo.get(i);
                spawnProjetil(instX, instY + (i * 35), 250f, 25f, 0, 0, 0, 0, 0f, 99f, inst.spritePath);
            }
            
            // Spawna o highlight (cursor visual) para as instrucoes
            highlight = spawnProjetil(instX, instY, 260f,15f, 0, 0, 0, 0, 0f, 99f, "highlight.png");
        }
        
        if (highlight != null && instrucaoIndex < algoritmo.size()) {
            float targetY = getMinY() + 20 + (instrucaoIndex * 35);
            highlight.getHitbox().setCentro(getMaxX() + 200, targetY);
        }

        if (instrucaoIndex >= algoritmo.size()) return;
        
        Instrucao inst = algoritmo.get(instrucaoIndex);
        
        // Se for uma instrucao de tempo, segura o processador
        if (inst.tipo == TipoInstrucao.ESPERAR) {
            timer += dt;
            if (timer >= inst.tempo) {
                timer = 0;
                instrucaoIndex++;
            }
            return;
        }
        
        // Senao, executa instantaneamente
        if (inst.tipo == TipoInstrucao.ATIVAR) {
            ativos[inst.linha][inst.coluna] = true;
            spawnQuadrado(inst.linha, inst.coluna);
            instrucaoIndex++;
        } else if (inst.tipo == TipoInstrucao.DESATIVAR) {
            ativos[inst.linha][inst.coluna] = false;
            removerQuadrado(inst.linha, inst.coluna);
            instrucaoIndex++;
        } else if (inst.tipo == TipoInstrucao.ENCERRAR) {
            this.encerrarAtaque();
        }
    }
    
    private void spawnQuadrado(int l, int c) {
        float px = offsetX + c * cellWidth + (cellWidth / 2f);
        float py = offsetY + l * cellHeight + (cellHeight / 2f);
        
        // Visual
        model.Projetil.Projetil visual = spawnProjetil(px, py, tamanhoQuadrado, tamanhoQuadrado, 0, 0, 0, 0, 0f, 99f, "quadrado.png");
        malha[l][c] = visual;
        
        // Hitbox invisível (dobro do tamanho do visual para refletir o sprite dobrado pela engine)
        model.Projetil.Projetil hitbox = spawnProjetil(px, py, tamanhoQuadrado * 2f, tamanhoQuadrado * 2f, 0, 0, 0, 1, 1f, 99f, "");
        malhaHitboxes[l][c] = hitbox;
    }
    
    private void removerQuadrado(int l, int c) {
        if (malha[l][c] != null) {
            malha[l][c].desativar();
            malha[l][c] = null;
        }
        if (malhaHitboxes[l][c] != null) {
            malhaHitboxes[l][c].desativar();
            malhaHitboxes[l][c] = null;
        }
    }
    
    @Override
    public void reiniciarAtaque() {
        super.reiniciarAtaque();
        timer = 0;
        instrucaoIndex = 0;
        instrucaoY = 0;
        cellWidth = 0;
        gerarAlgoritmo();
        for (int l = 0; l < 3; l++) {
            for (int c = 0; c < 3; c++) {
                ativos[l][c] = false;
                if (malha[l][c] != null) malha[l][c].desativar();
                if (malhaHitboxes[l][c] != null) malhaHitboxes[l][c].desativar();
                if (previas[l][c] != null) previas[l][c].desativar();
                malha[l][c] = null;
                malhaHitboxes[l][c] = null;
                previas[l][c] = null;
            }
        }
        if (highlight != null) {
            highlight.desativar();
            highlight = null;
        }
    }

    @Override
    public String toString() {
        return "Ataque Algoritimo";
    }
}
