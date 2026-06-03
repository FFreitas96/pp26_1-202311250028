package builder;

import model.Candidato;
import model.Microfone;

public class CandidatoBuilder implements PoliticoBuilder {

    private int    id;
    private String nome;
    private String partido;
    private int    numeroEleitoral;

    public CandidatoBuilder() {
        reset();
    }

    @Override
    public PoliticoBuilder comId(int id) {
        this.id = id;
        return this;
    }

    @Override
    public PoliticoBuilder comNome(String nome) {
        this.nome = nome;
        return this;
    }

    @Override
    public PoliticoBuilder comPartido(String partido) {
        this.partido = partido;
        return this;
    }

    @Override
    public PoliticoBuilder comNumeroEleitoral(int numeroEleitoral) {
        this.numeroEleitoral = numeroEleitoral;
        return this;
    }

    @Override
    public Candidato build() {
        Candidato c = new Candidato(id, nome, partido, numeroEleitoral, new Microfone(id));
        reset();
        return c;
    }

    public CandidatoBuilder reset() {
        this.id              = 0;
        this.nome            = "";
        this.partido         = "";
        this.numeroEleitoral = 0;
        return this;
    }
}
