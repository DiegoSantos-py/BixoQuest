package service;
import model.Local.Area;
import model.Local.Mapa;
import model.Local.Local;
import model.Local.ZonaInterativa;
import repository.LocalRepository;

import java.util.Map;

public class MapaService {
    private LocalRepository localRepo = new LocalRepository();

    public Map<String, Local> carregarLocais() {
        return localRepo.carregarLocal();
    }

    public Mapa criarMapa(){
        Mapa mapa = new Mapa();

        Local[] pontos = criarPontosDeOnibus();
        Local[] entradas = criarEstruturaModulo("Entrada módulo");
        Local[] cantinas = criarEstruturaModulo("Cantina módulo");
        Local[] salas = criarEstruturaModulo("Sala módulo");
        Local[] extras = criarExtras("Laboratório", "Colegiado");

        conectarMapa(pontos, entradas, cantinas, salas, extras);
        mapa.setLocais(adicionarAoRepositorio(pontos,entradas,cantinas,salas,extras));

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

        verificarLocalValido(pontos);
        verificarLocalValido(entradas);
        verificarLocalValido(cantinas);
        verificarLocalValido(salas);
        verificarLocalValido(extras);

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

    private Map<String, Local> adicionarAoRepositorio(Local[] pontos,
                                 Local[] entradas,
                                 Local[] cantinas,
                                 Local[] salas,
                                 Local[] extras) {

        verificarLocalValido(pontos);
        verificarLocalValido(entradas);
        verificarLocalValido(cantinas);
        verificarLocalValido(salas);
        verificarLocalValido(extras);

        if (localRepo.carregarLocal().size() != 0){ return localRepo.carregarLocal();}

        for (Local p : pontos) localRepo.adicionarLocal( p);

        for (int i = 0; i < 7; i++) {
            localRepo.adicionarLocal( entradas[i]);
            localRepo.adicionarLocal( cantinas[i]);
            localRepo.adicionarLocal( salas[i]);
        }

        for (Local e : extras) localRepo.adicionarLocal(e);

        return localRepo.carregarLocal();
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

    private void verificarLocalValido(Local[] locais){
        for (Local l: locais){
            if (l == null)
                throw new IllegalArgumentException("Objeto passado como parâmetro é inválido");
        }
    }
}
