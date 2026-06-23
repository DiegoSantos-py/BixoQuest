package service;

import exception.Local.LocalDuplicadoException;
import exception.Local.LocalInvalidoException;
import exception.Local.LocalNaoEncontradoException;
import exception.PersistenciaException;
import model.Local.*;
import model.Npc.Npc;
import repository.LocalRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MapaService {

    private final LocalRepository localRepo;
    private final NpcService npcService;

    public MapaService(LocalRepository localRepo, NpcService npcService) {

        this.localRepo = localRepo;
        this.npcService = npcService;
    }

    // Inicialização
    /*lança PersistenciaException se ocorrer falha ao carregar o arquivo*/
    public void carregar() throws PersistenciaException {
        if (!localRepo.arquivoExiste()) {
            criarMapa();
        } else {
            localRepo.carregar();
        }
    }

    /*lança PersistenciaException se ocorrer falha ao salvar o arquivo*/
    public void salvar() throws PersistenciaException {
        localRepo.salvar();
    }

    // Leitura

    public Map<String, Local> carregarLocais() {
        return localRepo.carregarLocais();
    }

    /*lança LocalNaoEncontradoException se não existir local com o tipo informado*/
    public Local buscarPorTipo(TipoLocal tipo) {
        return localRepo.buscarPorTipo(tipo);
    }

    /*lança LocalNaoEncontradoException se não existir local com o nome informado*/
    public Local buscarPorNome(String nome) {
        return localRepo.buscarPorNome(nome);
    }

    public String buscarSpritePorNome(String nome) {return localRepo.buscarSpritePorNome(nome);}

    // Criação do mapa
    /*lança PersistenciaException se ocorrer falha ao salvar após criação*/
    //TODO: adicionar verificação para evitar que esse método seja chamado 2 vezes
    public Mapa criarMapa() throws PersistenciaException {
        Mapa mapa = new Mapa();

        Local[] pontos   = criarPontosDeOnibus();
        Local[] entradas = criarEstruturaModulo("Entrada módulo ", TipoLocal.ENTRADA, "/assets/background/background_entrada_modulo.png");
        Local[] cantinas = criarEstruturaModulo("Cantina módulo ", TipoLocal.CANTINA, "/assets/background/background_cantina.png");
        Local[] salas    = criarEstruturaModulo("Sala módulo ", TipoLocal.SALA, "/assets/background/background_sala.png");
        Local[] extras   = criarExtras("Laboratório", "Colegiado");

        conectarMapa(pontos, entradas, cantinas, salas, extras);

        mapa.setLocais(adicionarAoRepositorio(pontos, entradas, cantinas, salas, extras));

        return mapa;
    }

    // Lógica de negócio
    /*lança LocalInvalidoException se origem, destino ou direção forem nulos, ou origem == destino
     @throws LocalDuplicadoException se já existir vizinho na direção informada*/
    public void conectarLocais(Local origem, Local destino, Direcao direcao) {
        if (origem == null || destino == null) {
            throw new LocalInvalidoException("local", "origem e destino não podem ser nulos");
        }
        if (direcao == null) {
            throw new LocalInvalidoException("direcao", "não pode ser nula");
        }
        if (origem == destino) {
            throw new LocalInvalidoException("local", "um local não pode se conectar a si mesmo");
        }
        if (origem.getVizinho(direcao) != null) {
            throw new LocalDuplicadoException(origem.getNome() + " -> " + direcao.name());
        }

        origem.adicionarVizinho(direcao, destino);
        destino.adicionarVizinho(oposta(direcao), origem);
    }

    /*lança LocalInvalidoException se a zona, sua área ou o local forem inválidos, ou a zona não couber no local*/
    public void adicionarZona(ZonaInterativa zona, Local local) {
        if (zona == null) {
            throw new LocalInvalidoException("zona", "não pode ser nula");
        }
        if (zona.getArea() == null) {
            throw new LocalInvalidoException("zona.area", "não pode ser nula");
        }
        if (!local.getArea().contemArea(zona.getArea())) {
            throw new LocalInvalidoException("zona", "deve estar contida dentro da área do local");
        }
        if (local.getZonaInterativasDisponiveis().contains(zona)) {
            throw new LocalInvalidoException("zona", "já existe no local");
        }
        for (ZonaInterativa z : local.getZonaInterativasDisponiveis()) {
            if (z.getArea().intersecta(zona.getArea())) {
                throw new LocalInvalidoException("zona", "sobrepõe uma zona já existente");
            }
        }

        local.getZonaInterativasDisponiveis().add(zona);
    }

    public List<Npc> getNpcsDoLocal(String nomeLocal) {
        Local local = localRepo.buscarPorNome(nomeLocal);
        return local.getNomesNpcs().stream()
                .map(npcService::buscarPorNome)
                .collect(Collectors.toList());
    }

    // Helpers privados
    private Local[] criarPontosDeOnibus() {
        return new Local[]{
                new Local("Ponto de ônibus 1", criarAreaPadrao(), TipoLocal.PONTO_ONIBUS, "/assets/background/Background_ponto_onibus.png"),
                new Local("Ponto de ônibus 2", criarAreaPadrao(), TipoLocal.PONTO_ONIBUS, "/assets/background/Background_ponto_onibus.png"),
                new Local("Ponto de ônibus 3", criarAreaPadrao(), TipoLocal.PONTO_ONIBUS, "/assets/background/Background_ponto_onibus.png")
        };
    }

    private Local[] criarEstruturaModulo(String nomeBase, TipoLocal tipo, String spriteDir) {
        Local[] locais = new Local[7];
        for (int i = 0; i < 7; i++) {
            locais[i] = new Local(nomeBase + (i + 1), criarAreaPadrao(), tipo, spriteDir);
        }
        return locais;
    }

    private Local[] criarExtras(String n1, String n2) {
        return new Local[]{
                new Local(n1, criarAreaPadrao(), TipoLocal.LABORATORIO, "/assets/background/background_laboratorio.png"),
                new Local(n2, criarAreaPadrao(), TipoLocal.COLEGIADO, "/assets/background/background_cantina.png")
        };
    }

    private void conectarMapa(Local[] pontos, Local[] entradas,
                              Local[] cantinas, Local[] salas, Local[] extras) {
        verificarLocaisValidos(pontos);
        verificarLocaisValidos(entradas);
        verificarLocaisValidos(cantinas);
        verificarLocaisValidos(salas);
        verificarLocaisValidos(extras);

        conectarLocais(pontos[0], entradas[1], Direcao.CIMA);
        conectarLocais(pontos[1], entradas[3], Direcao.CIMA);
        conectarLocais(pontos[2], entradas[5], Direcao.CIMA);

        for (int i = 0; i < 7; i++) {
            conectarLocais(entradas[i], cantinas[i], Direcao.CIMA);
            conectarLocais(cantinas[i], salas[i],    Direcao.CIMA);

            if (i != 6) {
                conectarLocais(entradas[i], entradas[i + 1], Direcao.ESQUERDA);
                conectarLocais(cantinas[i], cantinas[i + 1], Direcao.ESQUERDA);
            }
        }

        conectarLocais(salas[2], extras[0], Direcao.CIMA);
        conectarLocais(salas[4], extras[1], Direcao.CIMA);
    }

    private Map<String, Local> adicionarAoRepositorio(Local[] pontos, Local[] entradas,
                                                      Local[] cantinas, Local[] salas,
                                                      Local[] extras) throws PersistenciaException {
        verificarLocaisValidos(pontos);
        verificarLocaisValidos(entradas);
        verificarLocaisValidos(cantinas);
        verificarLocaisValidos(salas);
        verificarLocaisValidos(extras);

        if (!localRepo.carregarLocais().isEmpty()) return localRepo.carregarLocais();

        for (Local p : pontos) localRepo.adicionarLocal(p);

        for (int i = 0; i < 7; i++) {
            localRepo.adicionarLocal(entradas[i]);
            localRepo.adicionarLocal(cantinas[i]);
            localRepo.adicionarLocal(salas[i]);
        }

        for (Local e : extras) localRepo.adicionarLocal(e);

        localRepo.salvar();

        return localRepo.carregarLocais();
    }

    private Area criarAreaPadrao() {
        return new Area(500, -500, 500, -500);
    }

    /*lança LocalInvalidoException se o tamanho for menor ou igual a zero*/
    private Area criarAreaCustomizada(int tamanho) {
        if (tamanho <= 0) {
            throw new LocalInvalidoException("tamanho", "deve ser maior que zero");
        }
        return new Area(tamanho, -tamanho, tamanho, -tamanho);
    }

    private ZonaInterativa criarZonaInterativa(String nome, int tamanho) {
        return new ZonaInterativa(criarAreaCustomizada(tamanho), nome);
    }

    private void verificarLocaisValidos(Local[] locais) {
        for (Local l : locais) {
            if (l == null) {
                throw new LocalInvalidoException("local", "nenhum local do array pode ser nulo");
            }
        }
    }

    private Direcao oposta(Direcao direcao) {
        return switch (direcao) {
            case CIMA     -> Direcao.BAIXO;
            case BAIXO    -> Direcao.CIMA;
            case ESQUERDA -> Direcao.DIREITA;
            case DIREITA  -> Direcao.ESQUERDA;
        };
    }
}