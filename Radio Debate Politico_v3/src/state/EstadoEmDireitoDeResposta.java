package state;

import gerenciador.GerenciadorDebate;
import model.Candidato;

public class EstadoEmDireitoDeResposta implements EstadoDebate {

    @Override
    public void solicitarDR(Candidato candidato, GerenciadorDebate gerente) {
        System.out.println("  [DR] Solicitacao de DR de " + candidato.getNome()
                + " IGNORADA — defesas em andamento (previne ciclos infinitos).");
    }

    @Override
    public void concederDR(GerenciadorDebate gerente) {
        System.out.println("  [DR] Ja em execucao de Direitos de Resposta. Sem efeito.");
    }

    @Override
    public void negarDR(GerenciadorDebate gerente) {
        System.out.println("  [DR] Ja em execucao de Direitos de Resposta. Sem efeito.");
    }

    @Override
    public void proximaAcao(GerenciadorDebate gerente) {
        gerente.processarProximoDR();
    }
}
