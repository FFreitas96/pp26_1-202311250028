package model;

public class Microfone extends Colaborador {

    private int     id;
    private boolean ligado;
    private boolean botaoDRPressionado;

    public Microfone(int id) {
        this.id                  = id;
        this.ligado              = false;
        this.botaoDRPressionado  = false;
    }

    public void ligar() {
        this.ligado = true;
        System.out.println("    [Microfone " + id + "] LIGADO");
    }

    public void desligar() {
        this.ligado = false;
        System.out.println("    [Microfone " + id + "] DESLIGADO");
    }

    /**
     * Aciona o botão DR do microfone.
     * Registra o pressionamento e sinaliza visualmente.
     * A coordenação com o GerenciadorDebate é responsabilidade
     * do Candidato dono deste microfone, via solicitarDireitoDeResposta().
     */
    public void pressionarBotaoDR() {
        this.botaoDRPressionado = true;
        System.out.println("    [Microfone " + id + "] Botao DR PRESSIONADO");
    }

    public void resetarBotaoDR() {
        this.botaoDRPressionado = false;
    }

    public boolean isBotaoDRPressionado() { return botaoDRPressionado; }
    public boolean isLigado()             { return ligado; }
    public int     getId()                { return id; }
}
