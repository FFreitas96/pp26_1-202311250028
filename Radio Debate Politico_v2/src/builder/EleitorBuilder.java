package builder;

import model.Eleitor;

public interface EleitorBuilder {

    EleitorBuilder comId(int id);
    EleitorBuilder comNome(String nome);
    EleitorBuilder comZonaEleitoral(String zonaEleitoral);
    EleitorBuilder comSecao(int secao);

    Eleitor build();
}
