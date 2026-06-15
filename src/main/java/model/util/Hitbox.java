package model.util;

import exception.Utils.HitboxInvalidaException;

public final class Hitbox {
    private Vector2D centro;
    private Vector2D tamanho;
    private boolean ativa;
    private float anguloRad;

    // esses arrays e vetores são criados UMA VEZ por hitbox, pra n atiçar o garbage collector 
    //os 4 cantos da hitbox
    private final Vector2D[] vertices = new Vector2D[] {
        new Vector2D(0,0), new Vector2D(0,0), new Vector2D(0,0), new Vector2D(0,0)
    };

    //os 2 eixos de projeção (normais dos lados)
    private final Vector2D[] eixosCache = new Vector2D[] {
        new Vector2D(0,0), new Vector2D(0,0)
    };

    public Hitbox(Vector2D centro, Vector2D tamanho, float anguloRad) {
        if(centro == null){

            throw new HitboxInvalidaException("centro","o centro não pode ser nulo.");
        }
        this.centro = centro;
        if(tamanho == null || tamanho.getY() < 0 || tamanho.getX() < 0) {
            throw new HitboxInvalidaException("tamanho","o tamho da hitbox precisa ser positovo tnato em x quanto em y.");
        }
        this.tamanho = tamanho;
        this.anguloRad = anguloRad;
        this.ativa = true;

    }

    public void atualizarPos(Vector2D deslocamento) {
        this.centro.set(this.centro.getX() + deslocamento.getX(), this.centro.getY() + deslocamento.getY()); 
    }

    public float getAnguloRotacao(){
        return this.anguloRad;
    }

    public void rotacionar(float deltaAngulo) {
        this.anguloRad += deltaAngulo;
    }

    public Vector2D getCentro() { return centro; }
    public Vector2D getTamanho() { return tamanho; }

    public void setCentro(float x, float y) { this.centro.set(x, y); }
    public void setTamanho(float w, float h) { this.tamanho.set(w, h); }
    public void setAnguloRad(float anguloRad) { this.anguloRad = anguloRad; }
    public void ativar() { this.ativa = true; }
    public void desativar() { this.ativa = false; }
    
    public Vector2D[] getVertices() {
        atualizarCache(); 
        return vertices;
    }



    //calc de sobreposicao maluco
    private boolean temSobreposicao(Vector2D eixo, Vector2D[] verticesA, Vector2D[] verticesB) {
        
        float minA = Float.MAX_VALUE;
        float maxA = -Float.MAX_VALUE;
        // projeta os vertices de A no eixo e encontra o intervalo [minA, maxA]
        for (Vector2D v : verticesA) {
            float proj = v.produtoEscalar(eixo); 
            if (proj < minA) minA = proj;
            if (proj > maxA) maxA = proj;
        }

        float minB = Float.MAX_VALUE;
        float maxB = -Float.MAX_VALUE;
        // projeta os vertices de B no eixo e encontra o intervalo [minB, maxB]
        for (Vector2D v : verticesB) {
            float proj = v.produtoEscalar(eixo);
            if (proj < minB) minB = proj;
            if (proj > maxB) maxB = proj;
        }
        // se os intervalos [minA, maxA] e [minB, maxB] não se sobrepõem, não há colisão
        return maxA >= minB && maxB >= minA;
}

    private void atualizarCache() {

        //matematica magica nao questione
        float cos = (float) Math.cos(anguloRad);
        float sin = (float) Math.sin(anguloRad);
        float hw = tamanho.getX() / 2f;
        float hh = tamanho.getY() / 2f;
        float cx = centro.getX();
        float cy = centro.getY();

        eixosCache[0].set(cos, sin);    
        eixosCache[1].set(-sin, cos);

        // atualiza os vertices do cache usando a função de matriz de rotação centralizada
        MathUtils.rotacionarPonto(-hw, -hh, cos, sin, cx, cy, vertices[0]);
        MathUtils.rotacionarPonto(hw, -hh, cos, sin, cx, cy, vertices[1]);
        MathUtils.rotacionarPonto(hw, hh, cos, sin, cx, cy, vertices[2]);
        MathUtils.rotacionarPonto(-hw, hh, cos, sin, cx, cy, vertices[3]);
    }

    public boolean checarColisao(Hitbox outra) {

        if(!this.ativa || !outra.ativa ) {
            return false;
        }

        this.atualizarCache();
        outra.atualizarCache();

        for (Vector2D eixo : this.eixosCache) {
            if (!temSobreposicao(eixo, this.vertices, outra.vertices)) return false;
        }
        for (Vector2D eixo : outra.eixosCache) {
            if (!temSobreposicao(eixo, this.vertices, outra.vertices)) return false;
        }

        return true;
    }
}