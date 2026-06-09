package state;

import gerenciador.GerenciadorDebate;
import model.Candidato;

public class EstadoFinalizado implements EstadoDebate {

    @Override
    public void solicitarDR(Candidato candidato, GerenciadorDebate gerente) {
        System.out.println("  [DR] Debate encerrado. Solicitacao de DR ignorada.");
    }

    @Override
    public void concederDR(GerenciadorDebate gerente) {
        System.out.println("  [DR] Debate encerrado. Sem efeito.");
    }

    @Override
    public void negarDR(GerenciadorDebate gerente) {
        System.out.println("  [DR] Debate encerrado. Sem efeito.");
    }

    @Override
    public void proximaAcao(GerenciadorDebate gerente) {
        System.out.println("  [State] Debate encerrado. Nenhuma acao disponivel.");
    }
}
