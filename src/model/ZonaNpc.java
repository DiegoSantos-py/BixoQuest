package model;

public class ZonaNpc extends ZonaInterativa{
    private Npc npc;

    public ZonaNpc(Area area, String nome){
        super(area, nome);
    }

    public ZonaNpc(Npc npc, Area area, String nome){
        super(area, nome);
        this.npc = npc;
    }

}
