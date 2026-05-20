package model;

import interfaces.Mediador;

/**
 * Classe abstrata Colaborador (do diagrama original).
 *
 * Representa qualquer entidade que coopera com o Mediador
 * (GerenciadorDebate) durante o debate. Tanto Cronometro quanto
 * Microfone e Candidato herdam de Colaborador.
 */
public abstract class Colaborador {

    protected Mediador mediador;

    /**
     * Define o mediador que orquestra este colaborador.
     * @param mediador o mediador (geralmente GerenciadorDebate)
     */
    public void setMediador(Mediador mediador) {
        this.mediador = mediador;
    }

    /**
     * Acessa o mediador associado.
     */
    public Mediador getMediador() {
        return mediador;
    }
}
