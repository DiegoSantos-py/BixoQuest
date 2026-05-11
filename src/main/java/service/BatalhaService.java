package service;

public class BatalhaService {
    //TODO: MUITOS METODOS AQUI N EXISTEM. EX: isDerrotado, isAnimal
    /*
    public EstadoBatalha iniciarBatalha(Personagem personagem, Oponente origem) {
        Queue<Oponente> fila = processarFactoryOponentes(origem);
        final boolean isAnimal = origem.isAnimal();
        return new EstadoBatalha(personagem, conhecimentoArea, fila, isAnimal);
    }
    //todo : isso
    public void atualizar(EstadoBatalha estado, float dt) {
        if (estado.isFinalizado()) {
            return;
        }

        PlayerProva player = estado.getPlayer();
        Oponente oponenteAtual = estado.getOponenteAtual();

        if (estado.isBatalhaAnimal()) {
            if (player.getShieldAtual() <= 0) {
                estado.setFinalizado(true);
                estado.setVitoria(false);
                return;
            }
        }

        if (oponenteAtual != null && oponenteAtual.isDerrotado()) {
            oponenteAtual = estado.getFilaOponentes().poll();
            estado.setOponenteAtual(oponenteAtual);
            
            if (oponenteAtual == null) {
                estado.setFinalizado(true);
                estado.setVitoria(true); 
                return;
            }
        }
        
        if (oponenteAtual != null) {
            oponenteAtual.atualizarPosicao(dt);
        }
        player.atualizarPosicao(dt);
    }

    public void atacarOponenteAtual(EstadoBatalha estado, float multiplicadorPrecisao) {
        Oponente oponenteAtual = estado.getOponenteAtual();
        if (oponenteAtual != null && !oponenteAtual.isDerrotado()) {
            float danoCausado = estado.getPlayer().getDanoAtaque() * multiplicadorPrecisao;
            oponenteAtual.receberDano(danoCausado);
        }
    }
    private Queue<Oponente> processarFactoryOponentes(Oponente origem) {
        Queue<Oponente> fila = new LinkedList<>();

        if (origem.getAreaConhecimento() == AreaConhecimento.ANI) {



            Oponente animalOponente = new Oponente(
                    new Hitbox(new Vector2D(0, 0), new Vector2D(50, 50), 0.0f),
                    new Vector2D(0, 0),
                    animal.getNome(),
                    hpBase
            ) {
                @Override
                public Ataque criarAtaque(PlayerProva alvo, EntidadeBatalha owner) {
                    return new AtaqueMordida(alvo, owner);
                }
            };
     
            fila.add(animalOponente);
        }

        if (origem instanceof ProvaBatalha) {

            ProvaBatalha prova = (ProvaBatalha) origem;

            int numQuestoes = prova.getQuantidadeQuestoes();
            float hpBaseQuestao = prova.getNivelDisciplina() * 15.0f;

            for (int i = 1; i <= numQuestoes; i++) {

                String nomeQuestao = "Questão " + i + " de " + prova.getNome();

                Oponente questao = new Oponente(
                        new Hitbox(new Vector2D(0, 0), new Vector2D(50, 50), 0.0f),
                        new Vector2D(0, 0),
                        nomeQuestao,
                        hpBaseQuestao
                ) {
                    @Override
                    public Ataque criarAtaque(PlayerProva alvo, EntidadeBatalha owner) {
                        return new AtaqueMordida(alvo, owner);
                    }
                };

                fila.add(questao);
            }
        }

        return fila;
    }*/

}
