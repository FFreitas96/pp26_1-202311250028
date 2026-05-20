package model;

import observer.Observavel;
import observer.Observador;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe Candidato REFATORADA para a Questão 5.
 *
 * Mudanças em relação ao diagrama original:
 *  - Agora implementa Observavel (Subject do padrão Observer).
 *  - Mantém uma lista de eleitores cadastrados (List&lt;Observador&gt;).
 *  - Ganhou os métodos adicionarObservador(), removerObservador()
 *    e notificarObservadores().
 *  - Ganhou o método receberFala(tipoFala) que NOTIFICA os eleitores
 *    ANTES de ligar o microfone, conforme exigido pelo enunciado:
 *    "antes do microfone ser aberto para o candidato, o eleitor deve
 *    receber a mensagem de notificação".
 */
public class Candidato extends Colaborador implements Observavel {

    private int id;
    private String nome;
    private boolean jaPerguntou;
    private Microfone microfone;
    private List<Observador> eleitores;

    public Candidato(int id, String nome, Microfone microfone) {
        this.id = id;
        this.nome = nome;
        this.jaPerguntou = false;
        this.microfone = microfone;
        this.eleitores = new ArrayList<>();
    }

    // ----------------- Métodos do padrão Observer -----------------

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
        String mensagem = "Candidato " + nome + " está falando";
        for (Observador e : eleitores) {
            e.atualizar(mensagem);
        }
    }

    // ----------------- Métodos de domínio -----------------

    /**
     * Marca este candidato como já tendo perguntado nesta rodada.
     */
    public void marcarComoInquiridor() {
        this.jaPerguntou = true;
    }

    public boolean getJaPerguntou() {
        return jaPerguntou;
    }

    /**
     * Concede direito de fala a este candidato.
     *
     * PASSO CRÍTICO DA QUESTÃO 5:
     *  1) Notifica TODOS os eleitores cadastrados (antes de tudo).
     *  2) Só DEPOIS liga o microfone.
     *
     * @param tipoFala "PERGUNTA", "RESPOSTA", "REPLICA" ou "TREPLICA"
     */
    public void receberFala(String tipoFala) {
        System.out.println("\n--- " + nome + " vai iniciar: " + tipoFala + " ---");
        notificarObservadores();   // (1) notificar ANTES
        microfone.ligar();         // (2) só depois liga o microfone
    }

    // ----------------- Getters/Setters -----------------

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Microfone getMicrofone() {
        return microfone;
    }

    public List<Observador> getEleitores() {
        return eleitores;
    }
}
