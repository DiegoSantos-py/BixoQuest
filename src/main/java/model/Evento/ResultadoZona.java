package model.Evento;

import model.Evento.Prova.ProvaBatalha;
import model.Evento.Prova.ProvaIDs;

public class ResultadoZona {

    public enum Status { SEM_EVENTO, EXECUTADO, REQUISITO_NAO_ATENDIDO, PROVA_BATALHA }

    private final Status status;
    private final Evento evento;
    private final String motivo;
    private final ProvaIDs provaId; // novo

    private ResultadoZona(Status status, Evento evento, String motivo, ProvaIDs provaId) {
        this.status = status;
        this.evento = evento;
        this.motivo = motivo;
        this.provaId = provaId;
    }

    public static ResultadoZona semEvento() {
        return new ResultadoZona(Status.SEM_EVENTO, null, null, null);
    }

    public static ResultadoZona executado(Evento evento) {
        return new ResultadoZona(Status.EXECUTADO, evento, null, null);
    }

    public static ResultadoZona requisitoNaoAtendido(String motivo) {
        return new ResultadoZona(Status.REQUISITO_NAO_ATENDIDO, null, motivo, null);
    }

    public static ResultadoZona provaBatalha(ProvaBatalha prova, ProvaIDs provaId) {
        return new ResultadoZona(Status.PROVA_BATALHA, prova, null, provaId);
    }

    public Status getStatus() { return status; }
    public Evento getEvento() { return evento; }
    public String getMotivo() { return motivo; }
    public ProvaIDs getProvaId() { return provaId; }
}