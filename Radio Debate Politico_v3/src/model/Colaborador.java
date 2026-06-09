package model;

import interfaces.Mediador;

public abstract class Colaborador {

    protected Mediador mediador;

    public void setMediador(Mediador mediador) {
        this.mediador = mediador;
    }

    public Mediador getMediador() {
        return mediador;
    }
}
