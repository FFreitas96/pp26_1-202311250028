package state;

import gerenciador.GerenciadorDebate;
import model.Candidato;
import java.util.List;

public class EstadoAguardandoCiclo implements EstadoDebate {

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
                + " adicionado a fila de DR. Total na fila: "
                + gerente.getFilaDRSize());
    }

    @Override
    public void concederDR(GerenciadorDebate gerente) {
        if (gerente.isFilaDRVazia()) {
            System.out.println("  [DR] Fila de DR vazia. Nada a conceder.");
            gerente.setEstado(new EstadoEmAndamento());
            return;
        }
        System.out.println("  [DR] Gerente CONCEDEU o Direito de Resposta. "
                + "Iniciando defesas (" + gerente.getFilaDRSize()
                + " candidato(s) na fila).");
        gerente.registrarAcao("Direito de Resposta concedido para "
                + gerente.getFilaDRSize() + " candidato(s)");
        gerente.setEstado(new EstadoEmDireitoDeResposta());
        gerente.processarProximoDR();
    }

    @Override
    public void negarDR(GerenciadorDebate gerente) {
        System.out.println("  [DR] Gerente NEGOU o Direito de Resposta. "
                + "Limpando fila (" + gerente.getFilaDRSize()
                + " candidato(s) dispensado(s)).");
        gerente.registrarAcao("Direito de Resposta negado");
        List<Candidato> removidos = gerente.limparFilaDR();
        for (Candidato c : removidos) {
            c.setSolicitouDR(false);
        }
        gerente.setEstado(new EstadoEmAndamento());
    }

    @Override
    public void proximaAcao(GerenciadorDebate gerente) {
        EstadoDebateUtil.avancarCiclo(gerente, true);
    }
}
