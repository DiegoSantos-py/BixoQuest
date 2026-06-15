package controller;

import exception.Personagem.PersonagemDuplicadoException;
import exception.Personagem.PersonagemInvalidoException;
import exception.Personagem.PersonagemNaoEncontradoException;
import exception.PersistenciaException;
import model.Local.Local;
import model.Personagem;
import service.PersonagemService;

import java.util.Map;
import java.util.Optional;

public class PersonagemController extends BaseController {

    private final PersonagemService service;

    public PersonagemController(PersonagemService service) {
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

    // Escrita
    /** Retorna Optional vazio se personagem for inválido, duplicado ou falhar ao salvar */
    public Optional<Personagem> criarESalvarPersonagem(String nome,
                                                       double energia,
                                                       double motivacao,
                                                       double saude,
                                                       double dinheiro,
                                                       String spriteDir,
                                                       Local localInicial,
                                                       int posX,
                                                       int posY,
                                                       int personagemId) {
        try {
            Personagem p = service.criarESalvarPersonagem(nome, energia, motivacao,
                    saude, dinheiro, spriteDir, localInicial, posX, posY, personagemId);
            exibirSucesso("Personagem criado com sucesso.");

            return Optional.of(p);
        } catch (PersonagemInvalidoException e) {
            tratarInvalido(e);
            return Optional.empty();
        } catch (PersonagemDuplicadoException e) {
            tratarDuplicado(e);
            return Optional.empty();
        } catch (PersistenciaException e) {
            tratarErroPersistencia(e);
            return Optional.empty();
        }
    }


    // Leitura
    /** Retorna Optional vazio se personagem não for encontrado */
    public Optional<Personagem> buscarPorId(int id) {
        try {
            return Optional.of(service.buscarPorId(id));
        } catch (PersonagemNaoEncontradoException e) {
            tratarNaoEncontrado(e);
            return Optional.empty();
        }
    }

    public boolean existe(Personagem personagem) {
        return service.existe(personagem);
    }

    public Map<Integer, Personagem> carregarPersonagens() {
        return service.carregarPersonagens();
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
    private void tratarInvalido(PersonagemInvalidoException e) {
        exibirErro("Dado inválido no campo '" + e.getCampoCausador() + "': " + e.getMessage());
    }

    private void tratarDuplicado(PersonagemDuplicadoException e) {
        exibirErro("Já cadastrado: " + e.getMessage());
    }

    private void tratarNaoEncontrado(PersonagemNaoEncontradoException e) {
        exibirErro("Não encontrado: " + e.getMessage());
    }
}
