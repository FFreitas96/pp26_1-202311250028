package builder;

import model.Candidato;

public interface PoliticoBuilder {

    PoliticoBuilder comId(int id);
    PoliticoBuilder comNome(String nome);
    PoliticoBuilder comPartido(String partido);
    PoliticoBuilder comNumeroEleitoral(int numeroEleitoral);

    Candidato build();
}
