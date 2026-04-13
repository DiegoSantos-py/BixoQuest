package model.util;

public class Hitbox {
    private Vector2D centro;
    private Vector2D tamanho;
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

    //o gemini mando botar fora do construtor por N razoes ent deixa ai ne
    public Hitbox(Vector2D centro, Vector2D tamanho, float anguloRad) {

    this.centro = centro;
    this.tamanho = tamanho;
    this.anguloRad = anguloRad;

    }

    public void atualizarPos(Vector2D deslocamento) {
        this.centro.add(deslocamento); 
    }

    public void rotacionar(float deltaAngulo) {
        this.anguloRad += deltaAngulo;
    }

    public Vector2D getCentro() { return centro; }
    public Vector2D getTamanho() { return tamanho; }

    public void setCentro(float x, float y) { this.centro.set(x, y); }
    public void setTamanho(float w, float h) { this.tamanho.set(w, h); }
    public void setAnguloRad(float anguloRad) { this.anguloRad = anguloRad; }




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

        // atualiza os vertices do cache
        vertices[0].set(cx + (-hw * cos - (-hh) * sin), cy + (-hw * sin + (-hh) * cos));
        vertices[1].set(cx + (hw * cos - (-hh) * sin), cy + (hw * sin + (-hh) * cos));
        vertices[2].set(cx + (hw * cos - hh * sin), cy + (hw * sin + hh * cos));
        vertices[3].set(cx + (-hw * cos - hh * sin), cy + (-hw * sin + hh * cos));
    }

    public boolean checarColisao(Hitbox outra) {

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