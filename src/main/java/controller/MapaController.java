package controller;

import exception.Local.LocalDuplicadoException;
import exception.Local.LocalInvalidoException;
import exception.Local.LocalNaoEncontradoException;
import exception.PersistenciaException;
import model.Local.Direcao;
import model.Local.Local;
import model.Local.Mapa;
import model.Local.TipoLocal;
import model.Local.ZonaInterativa;
import service.MapaService;

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

    /** Retorna Optional vazio se não existir local com o tipo informado */
    public Optional<Local> buscarPorTipo(TipoLocal tipo) {
        try {
            return Optional.of(service.buscarPorTipo(tipo));
        } catch (LocalNaoEncontradoException e) {
            tratarNaoEncontrado(e);
            return Optional.empty();
        }
    }

    public Map<String, Local> carregarLocais() {
        return service.carregarLocais();
    }

    // Lógica de negócio
    /** Exibe erro se origem, destino ou direção forem inválidos ou conexão já existir */
    public void conectarLocais(Local origem, Local destino, Direcao direcao) {
        try {
            service.conectarLocais(origem, destino, direcao);
            exibirSucesso("Locais conectados com sucesso.");
        } catch (LocalInvalidoException e) {
            tratarInvalido(e);
        } catch (LocalDuplicadoException e) {
            tratarDuplicado(e);
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
