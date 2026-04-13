package service;
import model.Local.*;
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
        Local[] entradas = criarEstruturaModulo("Entrada módulo ", TipoLocal.ENTRADA);
        Local[] cantinas = criarEstruturaModulo("Cantina módulo ", TipoLocal.CANTINA);
        Local[] salas = criarEstruturaModulo("Sala módulo ", TipoLocal.SALA);
        Local[] extras = criarExtras("Laboratório", "Colegiado");

        conectarMapa(pontos, entradas, cantinas, salas, extras);

        mapa.setLocais(
                adicionarAoRepositorio(pontos, entradas, cantinas, salas, extras)
        );

        return mapa;
    }

    private Local[] criarPontosDeOnibus() {
        return new Local[] {
                new Local("Ponto de ônibus 1", criarAreaPadrao(), TipoLocal.PONTO_ONIBUS),
                new Local("Ponto de ônibus 2", criarAreaPadrao(), TipoLocal.PONTO_ONIBUS),
                new Local("Ponto de ônibus 3", criarAreaPadrao(), TipoLocal.PONTO_ONIBUS)
        };
    }

    private Local[] criarEstruturaModulo(String nomeBase, TipoLocal tipo) {
        Local[] locais = new Local[7];

        for (int i = 0; i < 7; i++) {
            locais[i] = new Local(nomeBase + (i + 1), criarAreaPadrao(), tipo);
        }

        return locais;
    }

    private Local[] criarExtras(String n1 , String n2){
        Local[] extras = new Local[2];

        extras[0] = new Local(n1, criarAreaPadrao(), TipoLocal.LABORATORIO);
        extras[1] = new Local(n2, criarAreaPadrao(), TipoLocal.COLEGIADO);

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

        conectarLocais(pontos[0], entradas[1], Direcao.CIMA); // conecta à entrada do módulo 2
        conectarLocais(pontos[1], entradas[3], Direcao.CIMA); // conecta à entrada do módulo 4
        conectarLocais(pontos[2], entradas[5], Direcao.CIMA); // conecta à entrada do módulo 6

        for (int i = 0; i < 7; i++) {
            conectarLocais(entradas[i], cantinas[i], Direcao.CIMA);
            conectarLocais(cantinas[i], salas[i], Direcao.CIMA);

            if (i!=6){
                conectarLocais(entradas[i], entradas[i+1], Direcao.ESQUERDA);
                conectarLocais(cantinas[i], cantinas[i+1], Direcao.ESQUERDA);
            }
        }

        conectarLocais(salas[2], extras[0], Direcao.CIMA); // conecta sala do módulo 3 ao laboratório
        conectarLocais(salas[4], extras[1], Direcao.CIMA); // conecta sala do módulo 5 ao colegiado
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
            localRepo.adicionarLocal(entradas[i]);
            localRepo.adicionarLocal(cantinas[i]);
            localRepo.adicionarLocal(salas[i]);
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

    public void conectarLocais(Local origem, Local destino, Direcao direcao) {

        if (origem == null || destino == null || direcao == null) {
            throw new IllegalArgumentException("Parâmetros inválidos");
        }

        if (origem == destino) {
            throw new IllegalArgumentException("Um local não pode se conectar a si mesmo.");
        }

        if (origem.getVizinho(direcao) != null) {
            throw new IllegalStateException("Já existe um vizinho nessa direção");
        }
        // conexão ida
        origem.adicionarVizinho(direcao, destino);

        // conexão volta
        destino.adicionarVizinho(oposta(direcao), origem);
    }

    private Direcao oposta(Direcao direcao) {
        switch (direcao) {
            case CIMA: return Direcao.BAIXO;
            case BAIXO: return Direcao.CIMA;
            case ESQUERDA: return Direcao.DIREITA;
            case DIREITA: return Direcao.ESQUERDA;
            default: throw new IllegalArgumentException("Direção inválida");
        }
    }

    public void adicionarZona(ZonaInterativa zona, Local local) {

        if (zona.getArea() == null) {
            throw new IllegalArgumentException("Não é possível adicionar uma zona sem área definida.");
        }

        //Verificar se a zona está dentro do local
        if (!local.getArea().contemArea(zona.getArea())) {
            throw new IllegalArgumentException("A zona deve estar contida dentro da área do local.");
        }

        //Verificar duplicidade
        if (local.getZonaInterativasDisponiveis().contains(zona)) {
            throw new IllegalArgumentException("Não é possível adicionar duas zonas iguais ao mesmo local.");
        }

        //Verificar sobreposição com outras zonas
        for (ZonaInterativa z : local.getZonaInterativasDisponiveis()) {
            if (z.getArea().intersecta(zona.getArea())) {
                throw new IllegalArgumentException("A zona sobrepõe uma zona já existente.");
            }
        }

        //Adicionar zona
        local.getZonaInterativasDisponiveis().add(zona);
    }

}
