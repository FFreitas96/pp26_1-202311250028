package state;

import gerenciador.GerenciadorDebate;
import model.Candidato;

public interface EstadoDebate {
    void solicitarDR(Candidato candidato, GerenciadorDebate gerente);
    void concederDR(GerenciadorDebate gerente);
    void negarDR(GerenciadorDebate gerente);
    void proximaAcao(GerenciadorDebate gerente);
}
