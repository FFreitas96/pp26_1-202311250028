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

    public Candidato(int id, String nome, String partido,
                     int numeroEleitoral, Microfone microfone) {
        this.id              = id;
        this.nome            = nome;
        this.partido         = partido;
        this.numeroEleitoral = numeroEleitoral;
        this.jaPerguntou     = false;
        this.microfone       = microfone;
        this.eleitores       = new ArrayList<>();
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
        String mensagem = "Candidato " + nome + " esta falando";
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
        notificarObservadores();
        microfone.ligar();
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

    public int              getId()              { return id; }
    public String           getNome()            { return nome; }
    public String           getPartido()         { return partido; }
    public int              getNumeroEleitoral() { return numeroEleitoral; }
    public Microfone        getMicrofone()       { return microfone; }
    public List<Observador> getEleitores()       { return eleitores; }
}
