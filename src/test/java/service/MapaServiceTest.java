package service;

import exception.Local.LocalDuplicadoException;
import exception.Local.LocalInvalidoException;
import model.Local.Area;
import model.Local.Direcao;
import model.Local.Local;
import model.Local.Mapa;
import model.Local.TipoLocal;
import model.Local.ZonaInterativa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.LocalRepository;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MapaServiceTest {

    private MapaService mapaService;

    @BeforeEach
    void setUp() {
        mapaService = new MapaService(new LocalRepository());
    }

    // criarMapa

    @Test
    void deveCriarMapaComTodosOsLocais() throws Exception {
        Mapa mapa = mapaService.criarMapa();

        assertNotNull(mapa);
        assertNotNull(mapa.getLocais());
        assertEquals(26, mapa.getLocais().size());
    }

    @Test
    void deveCarregarLocaisAposCriarMapa() throws Exception {
        mapaService.criarMapa();

        Map<String, Local> locais = mapaService.carregarLocais();

        assertNotNull(locais);
        assertEquals(26, locais.size());
    }

    @Test
    void deveCriarLocaisEsperadosNoMapa() throws Exception {
        Mapa mapa = mapaService.criarMapa();

        assertTrue(mapa.getLocais().containsKey("Ponto de ônibus 1"));
        assertTrue(mapa.getLocais().containsKey("Entrada módulo 1"));
        assertTrue(mapa.getLocais().containsKey("Cantina módulo 1"));
        assertTrue(mapa.getLocais().containsKey("Sala módulo 1"));
        assertTrue(mapa.getLocais().containsKey("Laboratório"));
        assertTrue(mapa.getLocais().containsKey("Colegiado"));
    }

    @Test
    void criarMapaNaoDeveDuplicarLocaisAoSerChamadoDuasVezes() throws Exception {
        Mapa mapa1 = mapaService.criarMapa();
        Mapa mapa2 = mapaService.criarMapa();

        assertEquals(26, mapa1.getLocais().size());
        assertEquals(26, mapa2.getLocais().size());
        assertEquals(26, mapaService.carregarLocais().size());
    }

    @Test
    void deveConterConexoesImportantesDoMapa() throws Exception {
        Mapa mapa = mapaService.criarMapa();

        Local ponto1      = mapa.getLocais().get("Ponto de ônibus 1");
        Local entrada2    = mapa.getLocais().get("Entrada módulo 2");
        Local cantina2    = mapa.getLocais().get("Cantina módulo 2");
        Local sala3       = mapa.getLocais().get("Sala módulo 3");
        Local laboratorio = mapa.getLocais().get("Laboratório");

        assertEquals(entrada2,    ponto1.getVizinho(Direcao.CIMA));
        assertEquals(cantina2,    entrada2.getVizinho(Direcao.CIMA));
        assertEquals(laboratorio, sala3.getVizinho(Direcao.CIMA));
    }

    // conectarLocais

    @Test
    void deveConectarLocaisNosDoisSentidos() {
        Local origem  = new Local("Origem",  new Area(100, -100, 100, -100), TipoLocal.SALA);
        Local destino = new Local("Destino", new Area(100, -100, 100, -100), TipoLocal.CANTINA);

        mapaService.conectarLocais(origem, destino, Direcao.CIMA);

        assertEquals(destino, origem.getVizinho(Direcao.CIMA));
        assertEquals(origem,  destino.getVizinho(Direcao.BAIXO));
    }

    @Test
    void deveLancarExcecaoAoConectarComParametroNulo() {
        Local origem  = new Local("Origem",  new Area(100, -100, 100, -100), TipoLocal.SALA);
        Local destino = new Local("Destino", new Area(100, -100, 100, -100), TipoLocal.CANTINA);

        assertThrows(LocalInvalidoException.class, () ->
                mapaService.conectarLocais(null, destino, Direcao.CIMA));

        assertThrows(LocalInvalidoException.class, () ->
                mapaService.conectarLocais(origem, null, Direcao.CIMA));

        assertThrows(LocalInvalidoException.class, () ->
                mapaService.conectarLocais(origem, destino, null));
    }

    @Test
    void deveLancarExcecaoAoConectarLocalConsigoMesmo() {
        Local local = new Local("Local", new Area(100, -100, 100, -100), TipoLocal.SALA);

        assertThrows(LocalInvalidoException.class, () ->
                mapaService.conectarLocais(local, local, Direcao.CIMA));
    }

    @Test
    void deveLancarExcecaoQuandoJaExisteVizinhoNaDirecao() {
        Local origem   = new Local("Origem",   new Area(100, -100, 100, -100), TipoLocal.SALA);
        Local destino1 = new Local("Destino1", new Area(100, -100, 100, -100), TipoLocal.CANTINA);
        Local destino2 = new Local("Destino2", new Area(100, -100, 100, -100), TipoLocal.ENTRADA);

        mapaService.conectarLocais(origem, destino1, Direcao.CIMA);

        assertThrows(LocalDuplicadoException.class, () ->
                mapaService.conectarLocais(origem, destino2, Direcao.CIMA));
    }

    // adicionarZona

    @Test
    void deveAdicionarZonaAoLocal() {
        Local local = new Local("Biblioteca", new Area(100, -100, 100, -100), TipoLocal.SALA);
        ZonaInterativa zona = new ZonaInterativa(new Area(20, -20, 20, -20), "Mesa de estudos");

        mapaService.adicionarZona(zona, local);

        assertEquals(1, local.getZonaInterativasDisponiveis().size());
        assertTrue(local.getZonaInterativasDisponiveis().contains(zona));
    }

    @Test
    void deveLancarExcecaoAoAdicionarZonaSemArea() {
        Local local = new Local("Biblioteca", new Area(100, -100, 100, -100), TipoLocal.SALA);
        ZonaInterativa zona = new ZonaInterativa(null, "Mesa de estudos");

        assertThrows(LocalInvalidoException.class, () ->
                mapaService.adicionarZona(zona, local));
    }

    @Test
    void deveLancarExcecaoQuandoZonaNaoEstiverDentroDoLocal() {
        Local local = new Local("Biblioteca", new Area(50, -50, 50, -50), TipoLocal.SALA);
        ZonaInterativa zona = new ZonaInterativa(new Area(100, -100, 100, -100), "Zona grande");

        assertThrows(LocalInvalidoException.class, () ->
                mapaService.adicionarZona(zona, local));
    }

    @Test
    void deveLancarExcecaoAoAdicionarZonaDuplicada() {
        Local local = new Local("Biblioteca", new Area(100, -100, 100, -100), TipoLocal.SALA);
        ZonaInterativa zona = new ZonaInterativa(new Area(20, -20, 20, -20), "Mesa de estudos");

        mapaService.adicionarZona(zona, local);

        assertThrows(LocalInvalidoException.class, () ->
                mapaService.adicionarZona(zona, local));
    }

    @Test
    void deveLancarExcecaoQuandoZonaSobrepoeOutra() {
        Local local = new Local("Biblioteca", new Area(100, -100, 100, -100), TipoLocal.SALA);

        ZonaInterativa zona1 = new ZonaInterativa(new Area(20, -20, 20, -20), "Mesa 1");
        ZonaInterativa zona2 = new ZonaInterativa(new Area(15, -15, 15, -15), "Mesa 2");

        mapaService.adicionarZona(zona1, local);

        assertThrows(LocalInvalidoException.class, () ->
                mapaService.adicionarZona(zona2, local));
    }
}