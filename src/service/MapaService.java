package service;
import model.Area;
import model.Mapa;
import model.Local;
import model.ZonaInterativa;

public class MapaService {

    public Mapa criarMapa(){
        Mapa mapa = new Mapa();

        Local[] pontos = criarPontosDeOnibus();
        Local[] entradas = criarEstruturaModulo("Entrada módulo");
        Local[] cantinas = criarEstruturaModulo("Cantina módulo");
        Local[] salas = criarEstruturaModulo("Sala módulo");
        Local[] extras = criarExtras("Laboratório", "Colegiado");

        conectarMapa(pontos, entradas, cantinas, salas, extras);
        adicionarAoMapa(mapa, pontos, entradas, cantinas, salas, extras);

        return mapa;
    }

    private Local[] criarPontosDeOnibus() {
        return new Local[] {
                new Local("Ponto de ônibus 1", criarAreaPadrao()),
                new Local("Ponto de ônibus 2", criarAreaPadrao()),
                new Local("Ponto de ônibus 3", criarAreaPadrao())
        };
    }

    private Local[] criarEstruturaModulo(String nome) {
        Local[] locais = new Local[7];
        for (int i = 0; i < 7; i++) {
            locais[i] = new Local(nome + (i + 1), criarAreaPadrao());
        }
        return locais;
    }

    private Local[] criarExtras(String n1 , String n2){
        Local[] extras = new Local[2];
        extras[0] = new Local(n1, criarAreaPadrao());
        extras[1] = new Local(n2, criarAreaPadrao());
        return extras;
    }
    private void conectarMapa(Local[] pontos, Local[] entradas,
                              Local[] cantinas, Local[] salas,
                              Local[] extras) {

        pontos[0].conectar(entradas[1]);
        pontos[1].conectar(entradas[3]);
        pontos[2].conectar(entradas[5]);

        for (int i = 0; i < 7; i++) {
            entradas[i].conectar(cantinas[i]);
            cantinas[i].conectar(salas[i]);

            if (i!=6){
                entradas[i].conectar(entradas[i+1]);
                cantinas[i].conectar(cantinas[i+1]);
            }
        }

        salas[2].conectar(extras[0]);
        salas[4].conectar(extras[1]);
    }

    private void adicionarAoMapa(Mapa mapa,
                                 Local[] pontos,
                                 Local[] entradas,
                                 Local[] cantinas,
                                 Local[] salas,
                                 Local[] extras) {

        for (Local p : pontos) mapa.adicionarLocal(p.getNome(), p);

        for (int i = 0; i < 7; i++) {
            mapa.adicionarLocal(entradas[i].getNome(), entradas[i]);
            mapa.adicionarLocal(cantinas[i].getNome(), cantinas[i]);
            mapa.adicionarLocal(salas[i].getNome(), salas[i]);
        }

        for (Local e : extras) mapa.adicionarLocal(e.getNome(), e);
    }

    private Area criarAreaPadrao(){
        Area area = new Area(500, -500, 500, -500);
        return area;
    }

    private Area criarAreaCustomizada(int tamanho){
        if (tamanho < 0){
            throw new IllegalArgumentException("Não é possível criar uma área com tamanho negativo.");
        }

        if (tamanho == 0){
            throw new IllegalArgumentException("Não é possível criar uma área com tamanho igual a zero");
        }

        return new Area(tamanho, -tamanho, tamanho, -tamanho);
    }

    private ZonaInterativa criarZonaInterativa(String nome, int tamanho){
        return new ZonaInterativa(criarAreaCustomizada(tamanho), nome);
    }

}
