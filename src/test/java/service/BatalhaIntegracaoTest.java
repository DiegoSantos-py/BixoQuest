package service;

import model.Disciplina.AreaConhecimento;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

import model.EstadoBatalha;
import model.Personagem;
import model.Turno;
import model.Npc.Animal;
import model.Npc.Especie;
import model.Oponente;
import model.Player.PlayerProva;
import model.Projetil.Projetil;

public class BatalhaIntegracaoTest {

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

        // A BATALHA OCORRE ATÉ O PLAYER OU INIMIGO MORRER
        while (!estado.isFinalizado()) {

            if (estado.getTurnoAtual() == Turno.TURNO_PLAYER) {
                System.out.println("\n[ >>> TURNO DO JOGADOR - ATACANDO! <<< ]");
                service.atacarOponenteAtual(estado, 1.0f);
            }
            else {
                int frameCounter = 0;
                while (estado.getTurnoAtual() != Turno.TURNO_PLAYER && !estado.isFinalizado()) {
                    service.atualizar(estado, 0.0166f); // 60 fps (1/60)s por update
                    frameCounter++;

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
}
