package model;

public class Microfone extends Colaborador {

    private int     id;
    private boolean ligado;

    public Microfone(int id) {
        this.id     = id;
        this.ligado = false;
    }

    public void ligar() {
        this.ligado = true;
        System.out.println("    [Microfone " + id + "] LIGADO");
    }

    public void desligar() {
        this.ligado = false;
        System.out.println("    [Microfone " + id + "] DESLIGADO");
    }

    public boolean isLigado() { return ligado; }
    public int     getId()    { return id; }
}
