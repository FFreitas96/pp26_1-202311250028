package state;

import gerenciador.GerenciadorDebate;

/**
 * Utilitário estático com lógica compartilhada entre estados.
 * Elimina a duplicação do switch PRRT presente em
 * EstadoEmAndamento e EstadoAguardandoCiclo.
 *
 * O parâmetro drPendente distingue o comportamento do case TREPLICA:
 *   false → rodada encerrada normalmente (EstadoEmAndamento)
 *   true  → rodada encerrada com DR(s) pendente(s), aguarda gerente (EstadoAguardandoCiclo)
 */
final class EstadoDebateUtil {

    private EstadoDebateUtil() {}

    static void avancarCiclo(GerenciadorDebate gerente, boolean drPendente) {
        String faseAtual = gerente.getFaseAtual();
        if (faseAtual == null) {
            System.out.println("  Debate nao iniciado.");
            return;
        }
        switch (faseAtual) {
            case "PERGUNTA":
                gerente.setFaseAtual("RESPOSTA");
                gerente.iniciarFase(gerente.getTempos()[1]);
                break;
            case "RESPOSTA":
                gerente.setFaseAtual("REPLICA");
                gerente.iniciarFase(gerente.getTempos()[2]);
                break;
            case "REPLICA":
                gerente.setFaseAtual("TREPLICA");
                gerente.iniciarFase(gerente.getTempos()[3]);
                break;
            case "TREPLICA":
                if (gerente.getInquiridor() != null) gerente.getInquiridor().getMicrofone().desligar();
                if (gerente.getInquirido()  != null) gerente.getInquirido().getMicrofone().desligar();
                if (drPendente) {
                    gerente.registrarAcao("Rodada finalizada — DR(s) pendente(s)");
                    System.out.println("  [State] Ciclo encerrado. Ha "
                            + gerente.getFilaDRSize()
                            + " Direito(s) de Resposta pendente(s).");
                    System.out.println("  [State] Aguardando decisao do gerente: "
                            + "concederDR() ou negarDR().");
                } else {
                    gerente.registrarAcao("Rodada finalizada");
                    System.out.println("  [State] Rodada encerrada. Nenhum DR pendente.");
                }
                break;
            default:
                System.out.println("  Fase desconhecida: " + faseAtual);
        }
    }
}
