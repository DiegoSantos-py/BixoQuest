package model.Ataque.Ataques.Prova.Software;

import model.Ataque.Ataque;
import model.Batalha.EntidadeBatalha;
import model.Player.PlayerProva;
import model.Projetil.Projetil;
import model.util.MathUtils;

import java.util.ArrayList;

public class AtaqueAlgoritimo extends Ataque {

    // Temporizador principal que controla os delays do algoritmo
    private float timer = 0;
    // O ponteiro (Program Counter) que aponta para a instrução atual
    private int indiceInstrucao = 0;
    // A lista de comandos que formam o script do ataque (a "lógica" gerada)
    private final ArrayList<Instrucao> algoritmo = new ArrayList<>();
    
    // Matrizes 3x3 que representam a grid do ataque
    private Projetil[][] previas = new Projetil[3][3]; // Indicadores visuais inofensivos (onde as células estão)
    private Projetil[][] malha = new Projetil[3][3]; // Visuais de perigo ativo (quadrados iluminados)
    private boolean[][] ativos = new boolean[3][3]; // Mapa lógico de quais células estão ligadas no momento
    
    // Variáveis para calcular posições na tela (tamanho e ancoragem da grid)
    private float larguraCelula;

    private float alturaCelula;
    private float deslocamentoX;
    private float deslocamentoY;
    
    // O tamanho do sprite de cada célula
    private float tamanhoQuadrado = 65f;
    
    // Elementos visuais que mostram o script de comandos à direita
    private float instrucaoY = 0;
    private Projetil destaque; // O cursor amarelo (highlight) que indica qual linha do código está rodando

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

    /**
     * Gera aleatoriamente o script da batalha baseado na dificuldade.
     * Tenta alternar entre ativar e desativar casas para forçar o player a se mover.
     */
    private void gerarAlgoritmo() { 
        
        algoritmo.clear();
        // Inicia com um tempinho de folga pro player se localizar e ler as primeiras linhas
        algoritmo.add(new Instrucao(TipoInstrucao.ESPERAR, 1f, -1, -1, "esperarSegundos1.png"));
        
        int qtdInstrucoes = MathUtils.randomIntInRange(5 , 8 );
        boolean[][] estadoTemp = new boolean[3][3];
        
        for (int i = 0; i < qtdInstrucoes; i++) {

            ArrayList<int[]> inativos = new ArrayList<>();
            ArrayList<int[]> ativosTemp = new ArrayList<>();
            for (int linha = 0; linha < 3; linha++) {
                for (int coluna = 0; coluna < 3; coluna++) {
                    if (estadoTemp[linha][coluna]) ativosTemp.add(new int[]{linha, coluna});
                    else inativos.add(new int[]{linha, coluna});
                }
            }
            
            // Lógica para decidir se vamos ligar ou desligar um quadrado
            boolean temEspacoParaAtivar = !inativos.isEmpty();
            boolean gridTotalmenteApagada = ativosTemp.isEmpty();
            boolean ativado = Math.random() > 0.4; // 60% de chance de ativar

            boolean querAtivar = false;

            if (temEspacoParaAtivar) {
                if (gridTotalmenteApagada) {
                    // Se a grid não tem nenhum ativo, somos obrigados a ativar um
                    querAtivar = true;
                } else {
                    // Se já tem algum aceso, usamos a sorte (60% ativa, 40% desativa)
                    querAtivar = ativado;
                }
            }
            if (querAtivar) {
                // Seleciona um quadrado aleatório apagado para acender
                int[] escolhido = inativos.get(MathUtils.randomIntInRange(0, inativos.size() - 1));
                estadoTemp[escolhido[0]][escolhido[1]] = true;
                algoritmo.add(new Instrucao(TipoInstrucao.ATIVAR, 0, escolhido[0], escolhido[1], "ativarQuadrado" + escolhido[0] + escolhido[1] + ".png"));
            } else if (!ativosTemp.isEmpty()) {
                // Seleciona um quadrado aleatório aceso para apagar
                int[] escolhido = ativosTemp.get(MathUtils.randomIntInRange(0, ativosTemp.size() - 1));
                estadoTemp[escolhido[0]][escolhido[1]] = false;
                algoritmo.add(new Instrucao(TipoInstrucao.DESATIVAR, 0, escolhido[0], escolhido[1], "desativarQuadrado" + escolhido[0] + escolhido[1] + ".png"));
            }
            
            // Pausa (Delay) após cada comando, escala com a dificuldade
            algoritmo.add(new Instrucao(TipoInstrucao.ESPERAR, 1f, -1, -1, "esperarSegundos1.png"));
        }
        
        algoritmo.add(new Instrucao(TipoInstrucao.ESPERAR, 1f, -1, -1, "esperarSegundos1.png"));
        algoritmo.add(new Instrucao(TipoInstrucao.ENCERRAR, 0, -1, -1, "encerrar.png"));
    }

    @Override
    protected void logicaAtaque(float dt) {
        // Inicialização "lazy" executada apenas no primeiro frame do ataque
        if (larguraCelula == 0) {
            larguraCelula = (getMaxX() - getMinX()) / 3f;
            alturaCelula = (getMaxY() - getMinY()) / 3f;
            deslocamentoX = getMinX();
            deslocamentoY = getMinY();
            
            // Desenha o "fundo" da grid: projéteis inofensivos que mostram o mapa 3x3
            for (int linha = 0; linha < 3; linha++) {
                for (int coluna = 0; coluna < 3; coluna++) {
                    float px = deslocamentoX + coluna * larguraCelula + (larguraCelula / 2f);
                    float py = deslocamentoY + linha * alturaCelula + (alturaCelula / 2f);
                    // Projétil seguro (dano 0), apenas demarca a posição visualmente
                    previas[linha][coluna] = spawnProjetil(px, py, tamanhoQuadrado, tamanhoQuadrado, 0, 0, 0, 0, 0f, 99f, "quadradoPrevia.png");
                }
            }
            
            // Instancia o script na tela (à direita da arena) para o player ler!
            float instX = getMaxX() + 230;
            float instY = getMinY() + 20;
            for (int i = 0; i < algoritmo.size(); i++) {
                Instrucao inst = algoritmo.get(i);
                spawnProjetil(instX, instY + (i * 40), 250f, 25f, 0, 0, 0, 0, 0f, 99f, inst.spritePath);
            }
            
            // O cursor (highlight) que acompanha a leitura de linha em linha
            destaque = spawnProjetil(instX, instY, 260f,15f, 0, 0, 0, 0, 0f, 99f, "highlight.png");
        }
        
        // Desloca o cursor Y de acordo com a instrução atual
        if (destaque != null && indiceInstrucao < algoritmo.size()) {
            float targetY = getMinY() + 20 + (indiceInstrucao * 40);
            destaque.getHitbox().setCentro(getMaxX() + 200, targetY);
        }

        if (indiceInstrucao >= algoritmo.size()) return;
        
        // Engine de Interpretação: executa a lógica do array sequencialmente
        Instrucao inst = algoritmo.get(indiceInstrucao);
        
        // Comandos de delay travam o avanço até o timer estourar
        if (inst.tipo == TipoInstrucao.ESPERAR) {
            timer += dt;
            if (timer >= inst.tempo) {
                timer = 0;
                indiceInstrucao++;
            }
            return;
        }
        
        // Os demais comandos são processados imediatamente, avançando pra próxima linha de código
        if (inst.tipo == TipoInstrucao.ATIVAR) {
            ativos[inst.linha][inst.coluna] = true;
            spawnQuadrado(inst.linha, inst.coluna);
            indiceInstrucao++;
        } else if (inst.tipo == TipoInstrucao.DESATIVAR) {
            ativos[inst.linha][inst.coluna] = false;
            removerQuadrado(inst.linha, inst.coluna);
            indiceInstrucao++;
        } else if (inst.tipo == TipoInstrucao.ENCERRAR) {
            this.encerrarAtaque();
        }
    }
    
    private void spawnQuadrado(int linha, int coluna) {
        float px = deslocamentoX + coluna * larguraCelula + (larguraCelula / 2f);
        float py = deslocamentoY + linha * alturaCelula + (alturaCelula / 2f);
        
        Projetil quadrado = spawnProjetil(px, py, tamanhoQuadrado * 2f, tamanhoQuadrado * 2f, 0, 0, 0, 1, 1f, 99f, "quadrado.png");
        if (quadrado != null) {
            quadrado.setMultiplicadorSprite(1f);
        }
        malha[linha][coluna] = quadrado;
    }
    
    private void removerQuadrado(int linha, int coluna) {
        if (malha[linha][coluna] != null) {
            malha[linha][coluna].desativar();
            malha[linha][coluna] = null;
        }
    }
    
    @Override
    public void reiniciarAtaque() {
        super.reiniciarAtaque();
        timer = 0;
        indiceInstrucao = 0;
        instrucaoY = 0;
        larguraCelula = 0;
        gerarAlgoritmo();
        for (int linha = 0; linha < 3; linha++) {
            for (int coluna = 0; coluna < 3; coluna++) {
                ativos[linha][coluna] = false;
                if (malha[linha][coluna] != null) malha[linha][coluna].desativar();
                if (previas[linha][coluna] != null) previas[linha][coluna].desativar();
                malha[linha][coluna] = null;
                previas[linha][coluna] = null;
            }
        }
        if (destaque != null) {
            destaque.desativar();
            destaque = null;
        }
    }


}

//ataque da capa do jogo basicemnte
