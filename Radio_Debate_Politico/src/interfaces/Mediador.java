package interfaces;

/**
 * Interface Mediador do padrão Mediator (do diagrama original).
 *
 * O GerenciadorDebate implementa esta interface, atuando como mediador
 * entre os colaboradores (Cronometro, Microfone, Candidato).
 */
public interface Mediador {

    /**
     * Chamado por um Colaborador para sinalizar que precisa
     * que o mediador execute a próxima ação do fluxo do debate.
     */
    void proximaAcao();
}
