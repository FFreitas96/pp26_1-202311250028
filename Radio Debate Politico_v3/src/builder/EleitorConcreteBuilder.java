package builder;

import model.Eleitor;

public class EleitorConcreteBuilder implements EleitorBuilder {

    private int    id;
    private String nome;
    private String zonaEleitoral;
    private int    secao;

    public EleitorConcreteBuilder() {
        reset();
    }

    @Override
    public EleitorBuilder comId(int id) {
        this.id = id;
        return this;
    }

    @Override
    public EleitorBuilder comNome(String nome) {
        this.nome = nome;
        return this;
    }

    @Override
    public EleitorBuilder comZonaEleitoral(String zonaEleitoral) {
        this.zonaEleitoral = zonaEleitoral;
        return this;
    }

    @Override
    public EleitorBuilder comSecao(int secao) {
        this.secao = secao;
        return this;
    }

    @Override
    public Eleitor build() {
        Eleitor e = new Eleitor(id, nome, zonaEleitoral, secao);
        reset();
        return e;
    }

    public EleitorConcreteBuilder reset() {
        this.id            = 0;
        this.nome          = "";
        this.zonaEleitoral = "";
        this.secao         = 0;
        return this;
    }
}
