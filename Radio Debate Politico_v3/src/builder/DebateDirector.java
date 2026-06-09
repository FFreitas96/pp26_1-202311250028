package builder;

import model.Candidato;
import model.Eleitor;

public class DebateDirector {

    private PoliticoBuilder candidatoBuilder;
    private EleitorBuilder  eleitorBuilder;

    public DebateDirector(PoliticoBuilder candidatoBuilder,
                          EleitorBuilder  eleitorBuilder) {
        this.candidatoBuilder = candidatoBuilder;
        this.eleitorBuilder   = eleitorBuilder;
    }

    public void setCandidatoBuilder(PoliticoBuilder candidatoBuilder) {
        this.candidatoBuilder = candidatoBuilder;
    }

    public void setEleitorBuilder(EleitorBuilder eleitorBuilder) {
        this.eleitorBuilder = eleitorBuilder;
    }

    public Candidato construirCandidato(int id, String nome,
                                        String partido, int numeroEleitoral) {
        return candidatoBuilder
                .comId(id)
                .comNome(nome)
                .comPartido(partido)
                .comNumeroEleitoral(numeroEleitoral)
                .build();
    }

    public Eleitor construirEleitor(int id, String nome,
                                    String zona, int secao) {
        return eleitorBuilder
                .comId(id)
                .comNome(nome)
                .comZonaEleitoral(zona)
                .comSecao(secao)
                .build();
    }
}
