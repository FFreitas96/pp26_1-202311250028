package state;

import gerenciador.GerenciadorDebate;
import model.Candidato;

public class EstadoEmAndamento implements EstadoDebate {

    @Override
    public void solicitarDR(Candidato candidato, GerenciadorDebate gerente) {
        if (candidato.isSolicitouDR()) {
            System.out.println("  [DR] " + candidato.getNome()
                    + " ja solicitou Direito de Resposta neste ciclo.");
            return;
        }
        candidato.setSolicitouDR(true);
        gerente.addFilaDR(candidato);
        System.out.println("  [DR] " + candidato.getNome()
                + " solicitou Direito de Resposta. Posicao na fila: "
                + gerente.getFilaDRSize());
        gerente.setEstado(new EstadoAguardandoCiclo());
        System.out.println("  [State] Debate aguardando fim do ciclo atual "
                + "para processamento do DR.");
    }

    @Override
    public void concederDR(GerenciadorDebate gerente) {
        System.out.println("  [DR] Nenhum Direito de Resposta pendente para conceder.");
    }

    @Override
    public void negarDR(GerenciadorDebate gerente) {
        System.out.println("  [DR] Nenhum Direito de Resposta pendente para negar.");
    }

    @Override
    public void proximaAcao(GerenciadorDebate gerente) {
        EstadoDebateUtil.avancarCiclo(gerente, false);
    }
}
