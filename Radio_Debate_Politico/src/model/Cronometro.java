package model;

/**
 * Classe Cronometro (do diagrama original).
 *
 * Controla o tempo de cada fase do debate (pergunta, resposta, etc.).
 * Quando o tempo se esgota, opcionalmente avisa o mediador para
 * avançar para a próxima ação.
 *
 * IMPORTANTE: para fins didáticos esta implementação é SIMULADA -
 * não bloqueia uma thread real. A contagem é apenas registrada e
 * a finalização é imediata. Em um sistema real haveria um Timer.
 */
public class Cronometro extends Colaborador {

    private int tempoAtual;
    private boolean autoAvanco;

    public Cronometro() {
        this.tempoAtual = 0;
        this.autoAvanco = false; // por padrão NÃO avança sozinho
    }

    /**
     * Define se ao finalizar o tempo o cronômetro deve chamar
     * automaticamente mediador.proximaAcao().
     */
    public void setAutoAvanco(boolean autoAvanco) {
        this.autoAvanco = autoAvanco;
    }

    /**
     * Inicia (simuladamente) a contagem regressiva por 'tempo' segundos.
     * Nesta versão didática não há espera real - a contagem é instantânea.
     */
    public void iniciar(int tempo) {
        this.tempoAtual = tempo;
        System.out.println("[Cronometro] Simulando contagem de " + tempo + " segundos");
        this.tempoAtual = 0;
        finalizarTempo();
    }

    /**
     * Disparado quando o tempo se esgota; opcionalmente avisa o mediador.
     */
    public void finalizarTempo() {
        System.out.println("[Cronometro] Tempo finalizado");
        if (autoAvanco && mediador != null) {
            mediador.proximaAcao();
        }
    }

    public int getTempoAtual() {
        return tempoAtual;
    }
}
