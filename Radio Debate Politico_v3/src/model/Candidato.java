package model;

import observer.Observavel;
import observer.Observador;
import prototype.Clonavel;

import java.util.ArrayList;
import java.util.List;

public class Candidato extends Colaborador implements Observavel, Clonavel<Candidato> {

    private final int              id;
    private final String           nome;
    private final String           partido;
    private final int              numeroEleitoral;
    private       boolean          jaPerguntou;
    private final Microfone        microfone;
    private final List<Observador> eleitores;
    private       boolean          solicitouDR;

    public Candidato(int id, String nome, String partido,
                     int numeroEleitoral, Microfone microfone) {
        this.id              = id;
        this.nome            = nome;
        this.partido         = partido;
        this.numeroEleitoral = numeroEleitoral;
        this.jaPerguntou     = false;
        this.microfone       = microfone;
        this.eleitores       = new ArrayList<>();
        this.solicitouDR     = false;
    }

    @Override
    public void adicionarObservador(Observador o) {
        if (!eleitores.contains(o)) {
            eleitores.add(o);
        }
    }

    @Override
    public void removerObservador(Observador o) {
        eleitores.remove(o);
    }

    @Override
    public void notificarObservadores() {
        notificarObservadores("Candidato " + nome + " esta falando");
    }

    public void notificarObservadores(String mensagem) {
        for (Observador e : eleitores) {
            e.atualizar(mensagem);
        }
    }

    public void marcarComoInquiridor() {
        this.jaPerguntou = true;
    }

    public boolean getJaPerguntou() {
        return jaPerguntou;
    }

    public void receberFala(String tipoFala) {
        System.out.println("\n  [" + nome + " - " + partido + "] inicia: " + tipoFala);
        if ("DR".equals(tipoFala)) {
            notificarObservadores("[DIREITO DE RESPOSTA] " + nome
                    + " (" + partido + ") esta realizando sua defesa");
        } else {
            notificarObservadores();
        }
        microfone.ligar();
    }

    /**
     * Candidato aciona o botão DR do microfone e solicita ao GerenciadorDebate.
     * O Candidato é o coordenador: conhece tanto o microfone quanto o mediador.
     */
    public void solicitarDireitoDeResposta() {
        if (solicitouDR) {
            System.out.println("  [DR] " + nome
                    + " ja solicitou Direito de Resposta neste ciclo.");
            return;
        }
        microfone.pressionarBotaoDR();
        if (mediador != null) {
            mediador.solicitarDR(this);
        }
    }

    @Override
    public Candidato clonar() {
        return new Candidato(
                this.id + 100,
                this.nome + " (clone)",
                this.partido,
                this.numeroEleitoral,
                new Microfone(this.id + 100)
        );
    }

    public void    setSolicitouDR(boolean v)    { this.solicitouDR = v; }
    public boolean isSolicitouDR()              { return solicitouDR; }

    public int              getId()              { return id; }
    public String           getNome()            { return nome; }
    public String           getPartido()         { return partido; }
    public int              getNumeroEleitoral() { return numeroEleitoral; }
    public Microfone        getMicrofone()       { return microfone; }
    public List<Observador> getEleitores()       { return eleitores; }
}
