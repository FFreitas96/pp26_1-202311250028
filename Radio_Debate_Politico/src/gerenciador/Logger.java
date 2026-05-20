package gerenciador;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe Logger (do diagrama original).
 *
 * Registra todas as ações relevantes ocorridas durante o debate
 * para posterior auditoria através do relatório.
 */
public class Logger {

    private List<String> logs;

    public Logger() {
        this.logs = new ArrayList<>();
    }

    /**
     * Registra uma ação no log.
     * @param acao a descrição da ação
     */
    public void registrar(String acao) {
        if (acao == null || acao.isEmpty()) {
            return;
        }
        logs.add(acao);
    }

    /**
     * Imprime o relatório com todas as ações registradas.
     */
    public void gerarRelatorio() {
        if (logs.isEmpty()) {
            System.out.println("Nenhuma ação registrada");
            return;
        }
        System.out.println("\n==================== RELATÓRIO DO DEBATE ====================");
        for (String log : logs) {
            System.out.println("- " + log);
        }
        System.out.println("=============================================================\n");
    }

    public List<String> getLogs() {
        return logs;
    }
}
