package service;

import model.Ataque.Ataque;
import model.Ataque.Ataques.AtaqueProjetilHoming;
import model.Disciplina.AreaConhecimento;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

import model.Batalha.EstadoBatalha;
import model.Personagem;
import model.Batalha.Turno;
import model.Npc.Animal;
import model.Npc.Especie;
import model.Batalha.Oponente;
import model.Player.PlayerProva;
import model.Projetil.Projetil;

public class BatalhaIntegracaoTest {
    private float dt = 0.0166f;
    @Test
    public void testBatalhaAnimalCompleta() {
        //cria o service de batalha o personagem e o animal etc bla bla(adicioan 5 de conhecimento pro player ter algum shield(importante)
        BatalhaService service = new BatalhaService();

        Personagem personagem = new Personagem("Veterano", 100, 100, 100, 100, "sprite.png");
        personagem.atualizarConhecimento(AreaConhecimento.ANI, 5);

        java.util.ArrayList<String> falas = new java.util.ArrayList<>();
        falas.add("Au au");
        Animal cachorro = new Animal("Cachorro", "spr/espirro.png", 40, 40, falas, Especie.CACHORRO, 40);

        EstadoBatalha estado = service.iniciarBatalha(personagem, cachorro);
        Ataque homing  =  new AtaqueProjetilHoming(estado.getPlayerProva(),estado.getOponenteAtual(),estado.getAnimal().getIndole());
        estado.getOponenteAtual().adicionarAtaque(homing);

        // Abre a janela do visualizador
        javax.swing.JFrame frame = new javax.swing.JFrame("Visualizador Batalha BixoQuest");
        frame.setSize(1920, 1080);
        frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        VisualizadorPanel panel = new VisualizadorPanel(estado);
        frame.add(panel);
        frame.setVisible(true);

        // CONTROLES WASD / SETAS
        frame.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                if (estado.getPlayerProva() == null) return;
                switch (e.getKeyCode()) {
                    case java.awt.event.KeyEvent.VK_W:
                    case java.awt.event.KeyEvent.VK_UP:
                        // No Java Graphics, Y negativo é pra cima, mas sua física usa Y positivo pra cima.
                        // Se os controles ficarem invertidos, basta trocar Cima/Baixo aqui.
                        estado.getPlayerProva().setMovendoBaixo(true); break;
                    case java.awt.event.KeyEvent.VK_S:
                    case java.awt.event.KeyEvent.VK_DOWN:
                        estado.getPlayerProva().setMovendoCima(true); break;
                    case java.awt.event.KeyEvent.VK_A:
                    case java.awt.event.KeyEvent.VK_LEFT:
                        estado.getPlayerProva().setMovendoEsquerda(true); break;
                    case java.awt.event.KeyEvent.VK_D:
                    case java.awt.event.KeyEvent.VK_RIGHT:
                        estado.getPlayerProva().setMovendoDireita(true); break;
                }
            }

            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                if (estado.getPlayerProva() == null) return;
                switch (e.getKeyCode()) {
                    case java.awt.event.KeyEvent.VK_W:
                    case java.awt.event.KeyEvent.VK_UP:
                        estado.getPlayerProva().setMovendoBaixo(false); break;
                    case java.awt.event.KeyEvent.VK_S:
                    case java.awt.event.KeyEvent.VK_DOWN:
                        estado.getPlayerProva().setMovendoCima(false); break;
                    case java.awt.event.KeyEvent.VK_A:
                    case java.awt.event.KeyEvent.VK_LEFT:
                        estado.getPlayerProva().setMovendoEsquerda(false); break;
                    case java.awt.event.KeyEvent.VK_D:
                    case java.awt.event.KeyEvent.VK_RIGHT:
                        estado.getPlayerProva().setMovendoDireita(false); break;
                }
            }
        });

        // A BATALHA OCORRE ATÉ O PLAYER OU INIMIGO MORRER
        while (!estado.isFinalizado()) {

            if (estado.getTurnoAtual() == Turno.TURNO_PLAYER) {
                System.out.println("\n[ >>> TURNO DO JOGADOR - ATACANDO! <<< ]");
                service.atacarOponenteAtual(estado, 1.0f);
            }
            else {
                int frameCounter = 0;
                while (estado.getTurnoAtual() != Turno.TURNO_PLAYER && !estado.isFinalizado()) {
                    service.atualizar(estado, 0.016f); // 60 fps (1/60)s por update
                    frameCounter++;
                    
                    // Atualiza a tela e pausa por 16 milissegundos para simular 60 FPS
                    panel.repaint();
                    try { Thread.sleep(16); } catch (Exception e) {}

                    // DADOS DO PLAYER E OPONENTE
                    PlayerProva p = estado.getPlayerProva();
                    Oponente o = estado.getOponenteAtual();
                    System.out.println("\n--- INIMIGO ATACANDO!!! ---");

                    System.out.println("\n--- FRAME: " + frameCounter + "---");
                    System.out.println("[PLAYER] Shield: " + p.getShieldAtual() + " | Dano Base: " + p.getDanoAtaque() +
                            " | PosX: " + p.getHitbox().getCentro().getX() + " | PosY: " + p.getHitbox().getCentro().getY());
                    if (o != null) {
                        System.out.println("[INIMIGO] " + o.getNome() + " | HP: " + o.getHpAtual() + " / " + o.getHpMaximo());
                    }

                    //DADOS DE ATAQUE E PROJETIS
                    if (estado.getAtaqueAtual() != null) {
                        System.out.println("Fase: " + estado.getTurnoAtual() +

                                " | Ataques: " + estado.getOponenteAtual().getAtaquesDisponiveis() +
                                " | Ataque: " + estado.getAtaqueAtual().getClass().getSimpleName() +
                                " | Projéteis na tela: " + estado.getAtaqueAtual().getProjeteis().size());

                        // Imprime o status de cada projétil ativo
                        List<Projetil> projeteis = estado.getAtaqueAtual().getProjeteis();
                        for (int i = 0; i < projeteis.size(); i++) {
                            Projetil proj = projeteis.get(i);
                            System.out.println("  [" + i + "] " + proj.getClass().getSimpleName() +
                                    " | Pos: (" + proj.getHitbox().getCentro().getX() + ", " + proj.getHitbox().getCentro().getY() + ")" +
                                    " | Dano: " + proj.getDanoShield());
                        }
                    }
                }
            }
        }

        System.out.println("\n===========================");
        System.out.println("BATALHA FINALIZADA!");
        System.out.println("player ganhou: " + estado.isVitoria());
        System.out.println("===========================\n");

        //verifica se a batlaha finilzou no geral
        assertTrue(estado.isFinalizado());
    }


    //TODO: MEXER NISSO AQUI
    // CLASSE INTERNA PARA DESENHAR O JOGO (Usando Vertices)
    class VisualizadorPanel extends javax.swing.JPanel {
        private EstadoBatalha estado;

        public VisualizadorPanel(EstadoBatalha estado) {
            this.estado = estado;
        }

        private void desenharHitbox(java.awt.Graphics2D g2d, model.util.Hitbox hb) {
            model.util.Vector2D[] vertices = hb.getVertices();
            int[] xPoints = new int[4];
            int[] yPoints = new int[4];
            for (int i = 0; i < 4; i++) {
                xPoints[i] = (int) vertices[i].getX();
                yPoints[i] = (int) vertices[i].getY();
            }
            g2d.fillPolygon(xPoints, yPoints, 4);
        }


        @Override
        protected void paintComponent(java.awt.Graphics g) {
            super.paintComponent(g);
            java.awt.Graphics2D g2d = (java.awt.Graphics2D) g;

            // Fundo Preto
            g2d.setColor(java.awt.Color.BLACK);
            g2d.fillRect(0, 0, getWidth(), getHeight());

            if (estado == null) return;

            // Desenha Inimigo (Vermelho)
            Oponente o = estado.getOponenteAtual();
            if (o != null) {
                g2d.setColor(java.awt.Color.RED);
                desenharHitbox(g2d, o.getHitbox());
            }

            // Desenha Player (Azul)
            model.Player.PlayerProva p = estado.getPlayerProva();
            if (p != null) {
                g2d.setColor(java.awt.Color.BLUE);
                desenharHitbox(g2d, p.getHitbox());
            }

            // Desenha Projéteis (Brancos)
            if (estado.getAtaqueAtual() != null) {
                g2d.setColor(java.awt.Color.WHITE);
                for (model.Projetil.Projetil proj : estado.getAtaqueAtual().getProjeteis()) {
                    desenharHitbox(g2d, proj.getHitbox());
                }
            }

            // Desenha HUD (Textos do Terminal na Tela)
            g2d.setColor(java.awt.Color.GREEN);
            g2d.setFont(new java.awt.Font("Monospaced", java.awt.Font.BOLD, 24));
            
            if (p != null) {
                g2d.drawString("[PLAYER] Shield: " + p.getShieldAtual() + " | Dano Base: " + p.getDanoAtaque(), 20, 40);
            }
            if (o != null) {
                g2d.drawString("[INIMIGO] " + o.getNome() + " | HP: " + String.format("%.1f", o.getHpAtual()) + " / " + o.getHpMaximo(), 20, 80);
            }
            if (estado.getAtaqueAtual() != null) {
                g2d.drawString("Fase: " + estado.getTurnoAtual(), 20, 120);
                g2d.drawString("Ataque: " + estado.getAtaqueAtual().getClass().getSimpleName(), 20, 160);
                g2d.drawString("Projéteis na tela: " + estado.getAtaqueAtual().getProjeteis().size(), 20, 200);
            }
        }
    }






}
