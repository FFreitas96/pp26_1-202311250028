package gerenciador;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Logger {

    private final List<String> logs;

    public Logger() {
        this.logs = new ArrayList<>(32);
    }

    public void registrar(String acao) {
        if (acao == null || acao.isEmpty()) return;
        logs.add(acao);
    }

    public void gerarRelatorio() {
        if (logs.isEmpty()) {
            System.out.println("Nenhuma acao registrada.");
            return;
        }
        System.out.println("\n==================== RELATORIO DO DEBATE ====================");
        for (String log : logs) {
            System.out.println("  - " + log);
        }
        System.out.println("=============================================================\n");
    }

    public List<String> getLogs() {
        return Collections.unmodifiableList(logs);
    }
}
