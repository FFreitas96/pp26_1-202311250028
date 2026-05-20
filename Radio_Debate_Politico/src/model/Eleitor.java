package model;

import observer.Observador;

/**
 * Classe Eleitor (NOVA - adicionada na Questão 5).
 *
 * Representa um eleitor que se cadastrou para receber notificações
 * em tempo real do candidato de sua preferência.
 *
 * Regras do enunciado:
 *  - Cada eleitor tem UM ÚNICO candidato de preferência.
 *  - Cada candidato tem um grupo de eleitores (0..*) que o observam.
 *  - Quando o candidato preferido começa a falar, o eleitor recebe
 *    a mensagem: "Candidato xxxx está falando".
 *
 * É o "Observer" do padrão Observer.
 */
public class Eleitor implements Observador {

    private int id;
    private String nome;
    private Candidato candidatoPreferido;

    public Eleitor(int id, String nome) {
        this.id = id;
        this.nome = nome;
        this.candidatoPreferido = null;
    }

    /**
     * Método chamado pelo Candidato quando ele vai começar a falar.
     * Este é o ponto de entrega da notificação ao eleitor.
     */
    @Override
    public void atualizar(String mensagem) {
        System.out.println("[Eleitor " + nome + "] >>> " + mensagem);
    }

    /**
     * Cadastra este eleitor como observador do candidato 'c'.
     * Se o eleitor já estava cadastrado em outro candidato,
     * cancela esse cadastro antes (regra: 1 candidato por eleitor).
     *
     * @param c o candidato preferido do eleitor
     */
    public void seCadastrar(Candidato c) {
        if (candidatoPreferido != null) {
            candidatoPreferido.removerObservador(this);
        }
        this.candidatoPreferido = c;
        c.adicionarObservador(this);
        System.out.println("[Eleitor " + nome + "] cadastrado para receber notificações de "
                + c.getNome());
    }

    /**
     * Cancela o cadastro do eleitor no candidato preferido (se houver).
     */
    public void cancelarCadastro() {
        if (candidatoPreferido != null) {
            candidatoPreferido.removerObservador(this);
            System.out.println("[Eleitor " + nome + "] cancelou cadastro em "
                    + candidatoPreferido.getNome());
            candidatoPreferido = null;
        }
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Candidato getCandidatoPreferido() {
        return candidatoPreferido;
    }
}
