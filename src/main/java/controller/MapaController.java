package controller;

import exception.Local.LocalDuplicadoException;
import exception.Local.LocalInvalidoException;
import exception.Local.LocalNaoEncontradoException;
import exception.PersistenciaException;
import model.Local.*;
import model.Npc.Npc;
import service.MapaService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MapaController extends BaseController {

    private final MapaService service;

    public MapaController(MapaService service) {
        this.service = service;
    }

    // Inicialização
    /** Exibe erro se ocorrer falha ao carregar o arquivo */
    public void carregar() {
        try {
            service.carregar();
        } catch (PersistenciaException e) {
            tratarErroPersistencia(e);
        }
    }

    /** Retorna Optional vazio se ocorrer falha ao criar o mapa */
    public Optional<Mapa> criarMapa() {
        try {
            Mapa mapa = service.criarMapa();
            exibirSucesso("Mapa criado com sucesso.");
            service.salvar();
            return Optional.of(mapa);
        } catch (PersistenciaException e) {
            tratarErroPersistencia(e);
            return Optional.empty();
        }
    }

    // Leitura
    /** Retorna Optional vazio se local não for encontrado */
    public Optional<Local> buscarPorNome(String nome) {
        try {
            return Optional.of(service.buscarPorNome(nome));
        } catch (LocalNaoEncontradoException e) {
            tratarNaoEncontrado(e);
            return Optional.empty();
        }
    }

    public String buscarSpritePorNome(String nome) {
        try {
            return service.buscarSpritePorNome(nome);
        } catch (LocalNaoEncontradoException e) {
            tratarNaoEncontrado(e);
            return "/assets/Errors/ImageNotFound.png";
        }
    }

    /** Retorna Optional vazio se não existir local com o tipo informado */
    public Optional<Local> buscarPorTipo(TipoLocal tipo) {
        try {
            return Optional.of(service.buscarPorTipo(tipo));
        } catch (LocalNaoEncontradoException e) {
            tratarNaoEncontrado(e);
            return Optional.empty();
        }
    }

    public List<Npc> getNpcsDoLocal(String nomeLocal) {
        try {
            return service.getNpcsDoLocal(nomeLocal);
        } catch (LocalNaoEncontradoException e) {
            tratarNaoEncontrado(e);
            return List.of();
        }
    }

    public Map<String, Local> carregarLocais() {
        return service.carregarLocais();
    }

    public List<ElementoLocal> getElementos(String nomeLocal) {
        try {
            return service.buscarPorNome(nomeLocal).getElementos();
        } catch (LocalNaoEncontradoException e) {
            tratarNaoEncontrado(e);
            return List.of();
        }
    }

    /** Exibe erro se zona, área ou local forem inválidos */
    public void adicionarZona(ZonaInterativa zona, Local local) {
        try {
            service.adicionarZona(zona, local);
            exibirSucesso("Zona adicionada com sucesso.");
        } catch (LocalInvalidoException e) {
            tratarInvalido(e);
        }
    }

    public List<ZonaInterativa> getZonasDoLocal(String nomeLocal) {
        try {
            return service.buscarPorNome(nomeLocal).getZonaInterativasDisponiveis();
        } catch (LocalNaoEncontradoException e) {
            tratarNaoEncontrado(e);
            return List.of();
        }
    }

    public Optional<String> getVizinho(String nomeLocal, String direcao) {
        try {
            Local local = service.buscarPorNome(nomeLocal);
            Direcao d = Direcao.valueOf(direcao);
            Local vizinho = local.getVizinho(d);
            return Optional.ofNullable(vizinho).map(Local::getNome);
        } catch (LocalNaoEncontradoException e) {
            tratarNaoEncontrado(e);
            return Optional.empty();
        }
    }


    // Exibição
    @Override
    protected void exibirErro(String mensagem) {
        System.err.println("[ERRO] " + mensagem);
    }

    @Override
    protected void exibirSucesso(String mensagem) {
        System.out.println("[OK] " + mensagem);
    }

    // Tratamento local de exceções
    private void tratarInvalido(LocalInvalidoException e) {
        exibirErro("Dado inválido no campo '" + e.getCampoCausador() + "': " + e.getMessage());
    }

    private void tratarDuplicado(LocalDuplicadoException e) {
        exibirErro("Já cadastrado: " + e.getMessage());
    }

    private void tratarNaoEncontrado(LocalNaoEncontradoException e) {
        exibirErro("Não encontrado: " + e.getMessage());
    }
}
