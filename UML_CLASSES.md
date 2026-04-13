# Diagrama UML de Classes — BixoQuest

```mermaid
classDiagram
    direction TB

    %% ══════════════════════════════════
    %% INTERFACES
    %% ══════════════════════════════════

    class Batalhavel {
        <<interface>>
        +getNomeBatalha() String
    }

    %% ══════════════════════════════════
    %% ENUMS
    %% ══════════════════════════════════

    class ProjetilID {
        <<enumeration>>
        BASICO
        HOMING
        EXPLOSIVE
    }

    class AreaConhecimento {
        <<enumeration>>
        MAT
        ...
    }

    class TipoLocal {
        <<enumeration>>
        PONTO_ONIBUS
        SALA
        CANTINA
        ENTRADA
        LABORATORIO
        COLEGIADO
    }

    class Direcao {
        <<enumeration>>
        CIMA
        BAIXO
        ESQUERDA
        DIREITA
    }

    %% ══════════════════════════════════
    %% UTIL (Física 2D)
    %% ══════════════════════════════════

    class Vector2D {
        +x : float
        +y : float
        +add(Vector2D) void
        +subtrair(Vector2D) Vector2D
        +normalize() Vector2D
        +magnitude() float
        +mult(float) Vector2D
        +produtoEscalar(Vector2D) float
        +set(float, float) void
    }

    class Hitbox {
        -centro : Vector2D
        -tamanho : Vector2D
        -anguloRad : float
        +checarColisao(Hitbox) boolean
        +atualizarPos(Vector2D) void
        +rotacionar(float) void
        +setCentro(float, float) void
        +setTamanho(float, float) void
        +setAnguloRad(float) void
    }

    %% ══════════════════════════════════
    %% ENTIDADE BASE (Abstrata)
    %% ══════════════════════════════════

    class EntidadeBatalha {
        <<abstract>>
        #hitbox : Hitbox
        #velocidade : Vector2D
        #velocidadeAngular : float
        #ativo : boolean
        +atualizarPosicao(float) void
        +isAtivo() boolean
        +desativar() void
    }

    %% ══════════════════════════════════
    %% PLAYER (Entidade de Batalha)
    %% ══════════════════════════════════

    class PlayerProva {
        -shieldAtual : int
        -shieldMaximo : int
        -danoAtaque : float
        -conhecimentoArea : float
        -tempoImunidadeRestante : float
        -turnosUsados : int
        -acertosPerfeitosConsecutivos : int
        -hitsRecebidos : int
        -perdeuNota : boolean
        -levouAlgumDano : boolean
        -acoesDisponiveis : List~AcaoBatalha~
        +ReceberDano(float, float) void
        +aplicarBonusAcao(int, int, int) void
        +atualizarPosicao(float) void
        +getShieldAtual() int
        +getDanoAtaque() float
        +isPerdeuNota() boolean
    }

    class AcaoBatalha {
        -nome : String
        -descricao : String
        -chanceAcerto : float
        -bonusDano : int
        -bonusShield : int
        -bonusConhecimento : int
    }

    %% ══════════════════════════════════
    %% OPONENTE (Entidade de Batalha)
    %% ══════════════════════════════════

    class Oponente {
        <<abstract>>
        #nome : String
        #hpAtual : float
        #hpMaximo : float
        +receberDano(float) void
        +isDerrotado() boolean
        +criarAtaque(PlayerProva, EntidadeBatalha) Ataque
    }

    %% ══════════════════════════════════
    %% PROJÉTEIS
    %% ══════════════════════════════════

    class Projetil {
        <<abstract>>
        #danoShield : int
        #danoNota : float
        #tempoDeVida : float
        #duracaoMaxima : float
        #owner : EntidadeBatalha
        #target : PlayerProva
        #factory : ProjetilFactory
        +reviver(float, float, float, float, float, float, float, int, float, float) void
        +atualizar(float) void
        +executarAI(float) void
        +aoColidirComPlayer() void
        +aoDespawnar() void
        +getDanoShield() int
        +getDanoNota() float
    }

    class ProjetilBasico {
        +executarAI(float) void
        +aoColidirComPlayer() void
    }

    class ProjetilQueSegue {
        +executarAI(float) void
        +aoColidirComPlayer() void
    }

    class ProjetilExplosivo {
        +executarAI(float) void
        +aoColidirComPlayer() void
    }

    class ProjetilFactory {
        -poolBasico : ProjetilBasico[]
        -poolHoming : ProjetilQueSegue[]
        -poolExplosivo : ProjetilExplosivo[]
        -indexBasico : int
        -indexHoming : int
        -indexExplosivo : int
        +spawn(float, float, float, float, float, float, float, ProjetilID, int, float, float) Projetil
        +atualizar(float) void
        +getAtivos() List~Projetil~
    }

    %% ══════════════════════════════════
    %% ATAQUE
    %% ══════════════════════════════════

    class Ataque {
        <<abstract>>
        #tempoDecorrido : float
        #finalizado : boolean
        #factory : ProjetilFactory
        #owner : EntidadeBatalha
        #target : PlayerProva
        +atualizar(float) void
        +encerrarAtaque() void
        +isFinalizado() boolean
        +getProjeteis() List~Projetil~
        #spawnProjetil(float, float, float, float, float, float, float, ProjetilID, int, float, float) void
        #logicaAtaque(float) void
    }

    class AtaqueMordida {
        #logicaAtaque(float) void
    }

    %% ══════════════════════════════════
    %% ESTADO DA BATALHA
    %% ══════════════════════════════════

    class EstadoBatalha {
        -personagemOriginal : Personagem
        -player : PlayerProva
        -filaOponentes : Queue~Oponente~
        -oponenteAtual : Oponente
        -finalizado : boolean
        -vitoria : boolean
        -isBatalhaAnimal : boolean
        +getPersonagemOriginal() Personagem
        +isFinalizado() boolean
        +isVitoria() boolean
        +isBatalhaAnimal() boolean
    }

    %% ══════════════════════════════════
    %% PERSONAGEM (Mundo / RPG)
    %% ══════════════════════════════════

    class Personagem {
        -personagemId : int
        -nome : String
        -energia : double
        -motivacao : double
        -saude : double
        -dinheiro : double
        -spriteDir : String
        -cX : int
        -cY : int
        -localAtual : Local
        -conhecimentos : Map~AreaConhecimento, Double~
        -semestres : List~Semestre~
        -desempenhoAcademico : double
        +adicionarConhecimento(AreaConhecimento, double) void
        +getConhecimento(AreaConhecimento) double
        +getConhecimentoPorDisciplina(Disciplina) double
        +adicionarSemestre(Semestre) void
    }

    %% ══════════════════════════════════
    %% NPC
    %% ══════════════════════════════════

    class Animal {
        -nome : String
        -indole : float
        +getNomeBatalha() String
    }

    %% ══════════════════════════════════
    %% EVENTO
    %% ══════════════════════════════════

    class Evento {
        -efeitoEnergia : double
        -efeitosConhecimento : Map~AreaConhecimento, Double~
        -efeitoMotivacao : double
        -efeitoSaude : double
        -efeitoDinheiro : double
        -efeitoTempo : int
        -tempoRequisito : int
        -eventoRequisito : Evento
        -energiaMinima : double
        -custaDinheiro : double
        -repetivel : boolean
        -status : boolean
        -zona : ZonaInterativa
        +isStatus() boolean
        +isRepetivel() boolean
        +getEfeitosConhecimento() Map~AreaConhecimento, Double~
    }

    class EventoAleatorio {
        -probabilidade : double
        +deveAtivar() boolean
    }

    class ProvaBatalha {
        -nome : String
        -descricao : String
        -nivelDisciplina : int
        -quantidadeQuestoes : int
        +getNomeBatalha() String
        +getNivelDisciplina() int
        +getQuantidadeQuestoes() int
    }

    class ResultadoProva {
        -personagem : Personagem
        -prova : ProvaBatalha
        -notaFinal : float
        -turnosUsados : int
        -acertosPerfeitosConsecutivos : int
        -hitsRecebidos : int
        -levouAlgumDano : boolean
        -perdeuNota : boolean
    }

    %% ══════════════════════════════════
    %% LOCAL / MAPA
    %% ══════════════════════════════════

    class Local {
        -nome : String
        -area : Area
        -tipo : TipoLocal
        -vizinhos : Map~Direcao, Local~
        -zonaInterativasDisponiveis : List~ZonaInterativa~
        +adicionarVizinho(Direcao, Local) void
        +getVizinho(Direcao) Local
    }

    class Area {
        -maxX : int
        -minX : int
        -maxY : int
        -minY : int
        +contemArea(Area) boolean
        +intersecta(Area) boolean
        +contemCoordenada(int, int) boolean
    }

    class ZonaInterativa {
        -area : Area
        -nome : String
        +contemCoordenada(int, int) boolean
    }

    class Mapa {
        -locais : Map~String, Local~
    }

    %% ══════════════════════════════════
    %% TEMPO
    %% ══════════════════════════════════

    class Semestre {
        -dias : List~Dia~
        -disciplinas : List~Disciplina~
        -resultados : Map~Disciplina, Boolean~
        +adicionarDia(Dia) void
        +adicionarDisciplinas(Disciplina) void
        +registrarResultado(Disciplina, boolean) void
        +foiAprovado(Disciplina) boolean
        +terminou() boolean
    }

    class Dia {
        -inicio : Instant
        -duracao : Duration
        -eventosObrigatorios : Map~String, Evento~
        -eventosAleatorios : Map~String, EventoAleatorio~
        -saiuDoPonto : boolean
    }

    %% ══════════════════════════════════
    %% DISCIPLINA
    %% ══════════════════════════════════

    class Disciplina {
        -nome : String
        -codigo : int
        -area : AreaConhecimento
    }

    %% ══════════════════════════════════
    %% REPOSITÓRIOS
    %% ══════════════════════════════════

    class ResultadoProvaRepository {
        -resultados : List~ResultadoProva~
        +salvar(ResultadoProva) void
        +buscarTodos() List~ResultadoProva~
    }

    class DisciplinaRepository {
        +adicionar(Disciplina) void
        +existe(Disciplina) boolean
        +buscarPorNome(String) List~Disciplina~
        +buscar(String, float) Disciplina
        +buscarDisciplinasIniciais() List~Disciplina~
        +carregarDisciplinas() Map~String, List~Disciplina~~
        +proximaDisciplina(String, int) Disciplina
    }

    class LocalRepository {
        +adicionarLocal(Local) void
        +carregarLocal() Map~String, Local~
        +buscarPorTipo(TipoLocal) Local
    }

    class SemestreRepository {
        +adicionarSemestre(int, Semestre) void
    }

    %% ══════════════════════════════════
    %% SERVICES
    %% ══════════════════════════════════

    class BatalhaService {
        +iniciarBatalha(Personagem, Batalhavel) EstadoBatalha
        +atualizar(EstadoBatalha, float) void
        +atacarOponenteAtual(EstadoBatalha, float) void
        -processarFactoryOponentes(Batalhavel) Queue~Oponente~
    }

    class AcaoService {
        +processarAcao(AcaoBatalha, PlayerProva) boolean
    }

    class DiaService {
        -diaEncerrado : boolean
        +iniciarDia(Dia) void
        +pararTempo() void
        +encerrarDia(Dia) void
        +avancarTempo(Dia, long) void
        +pularTempo(Dia, long) void
        +consumirTempoEvento(Dia, int) void
        +verificarRetornoAoPonto(Dia, Personagem) void
        +isDiaEncerrado() boolean
        +getTempoRestante(Dia) long
        +gerarEventosDoDia(Dia, List, List) void
    }

    class DisciplinaService {
        -disciplinaRepo : DisciplinaRepository
        +criarDisciplinasPorNivel(String, int, AreaConhecimento) void
        +buscarPorNome(String) List~Disciplina~
        +buscar(String, float) Disciplina
        +carregarDisciplinas() Map~String, List~Disciplina~~
    }

    class EventoService {
        +criarEvento(String, String, double, Map, double, double, double, int, int, Evento, double, double, boolean, ZonaInterativa) Evento
        +podeExecutar(Evento, Personagem, Dia, DiaService) boolean
        +executarEvento(Evento, Personagem, Dia, DiaService) void
    }

    class GameService {
        -diaService : DiaService
        -semestreService : SemestreService
        -disciplinaService : DisciplinaService
        -localRepo : LocalRepository
        -semestre : Semestre
        -diaAtual : Dia
        -personagem : Personagem
        +iniciarJogo(Personagem) void
        +atualizar(Personagem) void
        +encerrarJogo(Personagem) boolean
    }

    class MapaService {
        -localRepo : LocalRepository
        +carregarLocais() Map~String, Local~
        +criarMapa() Mapa
        +conectarLocais(Local, Local, Direcao) void
        +adicionarZona(ZonaInterativa, Local) void
    }

    class PersonagemService {
        -eventoService : EventoService
        +criarPersonagem(String, double, double, double, double, String, Local, int, int) Personagem
        +atualizarPersonagem(Personagem, Direcao, Dia, DiaService) void
        +mover(Personagem, Direcao, Dia, DiaService) void
        +calcularDesempenhoGeral(Personagem) double
    }

    class SemestreService {
        -semestreRepo : SemestreRepository
        -disciplinaRepo : DisciplinaRepository
        +criarSemestre() Semestre
        +iniciarPrimeiroSemestre(int) Semestre
        +avancarDia(Semestre) Dia
        +adicionarDisciplina(Semestre, Disciplina) void
        +terminouSemestre(Semestre) boolean
        +encerrarSemestre(Personagem, Semestre) Semestre
        +definirResultadoDisciplina(Semestre, Disciplina, boolean) void
    }


    %% ══════════════════════════════════════════════════════════════
    %% HERANÇA  --|>
    %% ══════════════════════════════════════════════════════════════

    EntidadeBatalha <|-- PlayerProva
    EntidadeBatalha <|-- Oponente
    EntidadeBatalha <|-- Projetil
    Projetil        <|-- ProjetilBasico
    Projetil        <|-- ProjetilQueSegue
    Projetil        <|-- ProjetilExplosivo
    Ataque          <|-- AtaqueMordida
    Evento          <|-- EventoAleatorio

    %% ══════════════════════════════════════════════════════════════
    %% IMPLEMENTAÇÃO DE INTERFACE  ..|>
    %% ══════════════════════════════════════════════════════════════

    Batalhavel <|.. Animal
    Batalhavel <|.. ProvaBatalha

    %% ══════════════════════════════════════════════════════════════
    %% COMPOSIÇÃO  *--   (parte não existe sem o todo)
    %% ══════════════════════════════════════════════════════════════

    Hitbox          *-- "2"    Vector2D         : centro / tamanho
    EntidadeBatalha *-- "1"    Hitbox           : hitbox
    EntidadeBatalha *-- "1"    Vector2D         : velocidade
    EstadoBatalha   *-- "1"    PlayerProva      : player
    Personagem      *-- "0..*" Semestre         : historico
    Semestre        *-- "0..*" Dia              : dias
    Local           *-- "1"    Area             : area
    ZonaInterativa  *-- "1"    Area             : area
    Ataque          *-- "1"    ProjetilFactory  : factory

    %% ══════════════════════════════════════════════════════════════
    %% AGREGAÇÃO  o--   (parte pode existir sem o todo)
    %% ══════════════════════════════════════════════════════════════

    ProjetilFactory o-- "0..N" ProjetilBasico   : poolBasico
    ProjetilFactory o-- "0..N" ProjetilQueSegue : poolHoming
    ProjetilFactory o-- "0..N" ProjetilExplosivo: poolExplosivo
    Local           o-- "0..*" ZonaInterativa   : zonas
    Semestre        o-- "1..*" Disciplina        : disciplinas
    PlayerProva     o-- "0..*" AcaoBatalha       : acoesDisponiveis
    Mapa            o-- "1..*" Local             : locais

    %% ══════════════════════════════════════════════════════════════
    %% ASSOCIAÇÃO  -->   (referência direcional persistente)
    %% ══════════════════════════════════════════════════════════════

    EstadoBatalha --> "1"    Personagem      : personagemOriginal
    EstadoBatalha --> "0..1" Oponente        : oponenteAtual
    EstadoBatalha --> "0..*" Oponente        : filaOponentes
    Personagem    --> "0..1" Local           : localAtual
    Projetil      --> "1"    PlayerProva     : target
    Projetil      --> "1"    EntidadeBatalha : owner
    Ataque        --> "1"    PlayerProva     : target
    Ataque        --> "1"    EntidadeBatalha : owner
    ResultadoProva --> "1"   Personagem      : personagem
    ResultadoProva --> "1"   ProvaBatalha    : prova
    Evento        --> "0..1" Evento          : eventoRequisito
    Evento        --> "0..1" ZonaInterativa  : zona
    Dia           --> "0..*" Evento          : eventosObrigatorios
    Dia           --> "0..*" EventoAleatorio : eventosAleatorios
    Local         --> "0..4" Local           : vizinhos
    Disciplina    --> "1"    AreaConhecimento: area

    %% ══════════════════════════════════════════════════════════════
    %% DEPENDÊNCIA  ..>   (usa pontualmente, não armazena referência)
    %% ══════════════════════════════════════════════════════════════

    BatalhaService     ..> EstadoBatalha          : cria
    BatalhaService     ..> Batalhavel             : recebe
    BatalhaService     ..> Oponente               : instancia (factory anônima)
    BatalhaService     ..> Personagem             : lê atributos
    AcaoService        ..> AcaoBatalha            : processa
    AcaoService        ..> PlayerProva            : modifica
    DiaService         ..> Dia                    : controla tempo
    DiaService         ..> Personagem             : verifica posição
    DisciplinaService  ..> DisciplinaRepository   : delega
    EventoService      ..> Evento                 : valida e executa
    EventoService      ..> Personagem             : aplica efeitos
    EventoService      ..> DiaService             : consome tempo
    GameService        ..> DiaService             : orquestra dia
    GameService        ..> SemestreService        : orquestra semestre
    GameService        ..> DisciplinaService      : consulta disciplinas
    GameService        ..> LocalRepository        : busca locais
    MapaService        ..> LocalRepository        : persiste locais
    MapaService        ..> Mapa                   : monta
    MapaService        ..> Direcao               : conecta vizinhos
    PersonagemService  ..> EventoService          : delega eventos
    PersonagemService  ..> DiaService             : verifica retorno
    PersonagemService  ..> Personagem             : cria e movimenta
    SemestreService    ..> SemestreRepository     : persiste
    SemestreService    ..> DisciplinaRepository   : busca progressão
    SemestreService    ..> Semestre               : gerencia
    ResultadoProvaRepository ..> ResultadoProva   : armazena
    Oponente           ..> Ataque                 : instancia (abstract factory)
    LocalRepository    ..> TipoLocal              : filtra por tipo
```
