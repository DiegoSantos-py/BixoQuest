package service;
import model.Local;
import model.ZonaInterativa;
import repository.LocalRepository;



public class LocalService {
    private LocalRepository localRepo = new LocalRepository();



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