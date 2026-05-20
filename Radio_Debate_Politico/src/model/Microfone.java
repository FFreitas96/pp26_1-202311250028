package model;

/**
 * Classe Microfone (do diagrama original).
 *
 * Representa o microfone usado por um candidato durante o debate.
 * Possui apenas dois estados: ligado ou desligado.
 *
 * IMPORTANTE: o microfone só deve ser ligado DEPOIS que os eleitores
 * forem notificados (ver Candidato.receberFala()).
 */
public class Microfone extends Colaborador {

    private int id;
    private boolean ligado;

    public Microfone(int id) {
        this.id = id;
        this.ligado = false;
    }

    /**
     * Liga o microfone (o candidato passa a falar).
     */
    public void ligar() {
        this.ligado = true;
        System.out.println("[Microfone " + id + "] LIGADO");
    }

    /**
     * Desliga o microfone (o candidato para de falar).
     */
    public void desligar() {
        this.ligado = false;
        System.out.println("[Microfone " + id + "] DESLIGADO");
    }

    public boolean isLigado() {
        return ligado;
    }

    public int getId() {
        return id;
    }
}
