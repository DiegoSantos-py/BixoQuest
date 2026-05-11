package model.Evento;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import model.Disciplina.AreaConhecimento;
import model.Local.ZonaInterativa;

import java.util.HashMap;
import java.util.Map;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "tipo",
        defaultImpl = Evento.class  // usa Evento como padrão quando "tipo" não existe
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = EventoAleatorio.class, name = "aleatorio"),
        @JsonSubTypes.Type(value = Evento.class, name = "evento")
})
public class Evento {
    private boolean status;
    private String nome;
    private String descricao;

    // Efeitos no jogador
    @JsonIgnore
    private Map<AreaConhecimento, Double> efeitoConhecimento;
    private Map<String, Double> efeitoConhecimentoNomes = new HashMap<>();

    private double efeitoEnergia;
    private double efeitoMotivacao;
    private double efeitoSaude;
    private double efeitoDinheiro;
    private int efeitoTempo;

    // Requisitos para ativação
    private int tempoRequisito;
    @JsonIgnore
    private Evento eventoRequisito;
    private String eventoRequisitoNome;

    private double energiaMinima;
    private double custaDinheiro;
    private boolean repetivel;
    private ZonaInterativa zona;

    public Evento() {}

    public Evento(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }

    public Evento(double efeitoEnergia, Map<AreaConhecimento, Double> efeitoConhecimento, double efeitoMotivacao,
                  double efeitoSaude, double efeitoDinheiro, int efeitoTempo,
                  int tempoRequisito, Evento eventoRequisito, double energiaMinima) {
        this.efeitoEnergia = efeitoEnergia;
        this.efeitoMotivacao = efeitoMotivacao;
        this.efeitoSaude = efeitoSaude;
        this.efeitoDinheiro = efeitoDinheiro;
        this.efeitoTempo = efeitoTempo;
        this.tempoRequisito = tempoRequisito;
        this.energiaMinima = energiaMinima;
        setEfeitoConhecimento(efeitoConhecimento); // usa setter para sincronizar
        setEventoRequisito(eventoRequisito);        // usa setter para sincronizar
    }

    public Map<AreaConhecimento, Double> getEfeitosConhecimento() {
        return efeitoConhecimento;
    }

    public Map<String, Double> getEfeitoConhecimentoNomes() {
        return efeitoConhecimentoNomes;
    }

    public void setEfeitoConhecimentoNomes(Map<String, Double> efeitoConhecimentoNomes) {
        this.efeitoConhecimentoNomes = efeitoConhecimentoNomes;
        // reconstrói o mapa original ao carregar
        this.efeitoConhecimento = new HashMap<>();
        for (Map.Entry<String, Double> entry : efeitoConhecimentoNomes.entrySet()) {
            this.efeitoConhecimento.put(AreaConhecimento.valueOf(entry.getKey()), entry.getValue());
        }
    }

    public void setEfeitoConhecimento(Map<AreaConhecimento, Double> efeitoConhecimento) {
        this.efeitoConhecimento = efeitoConhecimento;
        this.efeitoConhecimentoNomes = new HashMap<>();
        if (efeitoConhecimento != null) {
            for (Map.Entry<AreaConhecimento, Double> entry : efeitoConhecimento.entrySet()) {
                this.efeitoConhecimentoNomes.put(entry.getKey().name(), entry.getValue());
            }
        }
    }

    public void setEfeitosConhecimento(Map<AreaConhecimento, Double> efeitosConhecimento) {
        setEfeitoConhecimento(efeitosConhecimento);
    }


    public Evento getEventoRequisito() {
        return eventoRequisito;
    }

    public void setEventoRequisito(Evento requisito) {
        this.eventoRequisito = requisito;
        this.eventoRequisitoNome = requisito != null ? requisito.getNome() : null;
    }

    public String getEventoRequisitoNome() { return eventoRequisitoNome; }
    public void setEventoRequisitoNome(String nome) { this.eventoRequisitoNome = nome; }



    public boolean isStatus() { return status; }
    public void setStatus(boolean isTrue) { this.status = isTrue; }
    public double getEfeitoEnergia() { return efeitoEnergia; }
    public void setEfeitoEnergia(double efeitoEnergia) { this.efeitoEnergia = efeitoEnergia; }
    public double getEfeitoMotivacao() { return efeitoMotivacao; }
    public void setEfeitoMotivacao(double efeitoMotivacao) { this.efeitoMotivacao = efeitoMotivacao; }
    public double getEfeitoSaude() { return efeitoSaude; }
    public void setEfeitoSaude(double efeitoSaude) { this.efeitoSaude = efeitoSaude; }
    public double getEfeitoDinheiro() { return efeitoDinheiro; }
    public void setEfeitoDinheiro(double efeitoDinheiro) { this.efeitoDinheiro = efeitoDinheiro; }
    public int getEfeitoTempo() { return efeitoTempo; }
    public void setEfeitoTempo(int i) { this.efeitoTempo = i; }
    public double getEnergiaMinima() { return energiaMinima; }
    public void setEnergiaMinima(int i) { this.energiaMinima = i; }
    public double getCustaDinheiro() { return custaDinheiro; }
    public void setCustaDinheiro(double custaDinheiro) { this.custaDinheiro = custaDinheiro; }
    public boolean isRepetivel() { return repetivel; }
    public void setRepetivel(boolean repetivel) { this.repetivel = repetivel; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public ZonaInterativa getZona() { return zona; }
    public void setZona(ZonaInterativa zona) { this.zona = zona; }
    public int getTempoRequisito() { return tempoRequisito; }
    public void setTempoRequisito(int tempoRequisito) { this.tempoRequisito = tempoRequisito; }

    @Override
    public String toString() {
        return this.nome + this.descricao;
    }
}