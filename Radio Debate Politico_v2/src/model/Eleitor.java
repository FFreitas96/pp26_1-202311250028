package model;

import observer.Observador;
import prototype.Clonavel;

public class Eleitor implements Observador, Clonavel<Eleitor> {

    private final int    id;
    private final String nome;
    private final String zonaEleitoral;
    private final int    secao;
    private       Candidato candidatoPreferido;

    public Eleitor(int id, String nome, String zonaEleitoral, int secao) {
        this.id              = id;
        this.nome            = nome;
        this.zonaEleitoral   = zonaEleitoral;
        this.secao           = secao;
        this.candidatoPreferido = null;
    }

    @Override
    public void atualizar(String mensagem) {
        System.out.println("  [Eleitor " + nome
                + " | Zona " + zonaEleitoral + "-" + secao
                + "] >>> " + mensagem);
    }

    public void seCadastrar(Candidato c) {
        if (candidatoPreferido != null) {
            candidatoPreferido.removerObservador(this);
        }
        this.candidatoPreferido = c;
        c.adicionarObservador(this);
        System.out.println("  [Eleitor " + nome + "] cadastrado para notificacoes de "
                + c.getNome() + " (" + c.getPartido() + ")");
    }

    public void cancelarCadastro() {
        if (candidatoPreferido != null) {
            candidatoPreferido.removerObservador(this);
            System.out.println("  [Eleitor " + nome + "] cancelou cadastro em "
                    + candidatoPreferido.getNome());
            candidatoPreferido = null;
        }
    }

    @Override
    public Eleitor clonar() {
        return new Eleitor(
                this.id + 200,
                this.nome + " (clone)",
                this.zonaEleitoral,
                this.secao
        );
    }

    public int       getId()                 { return id; }
    public String    getNome()               { return nome; }
    public String    getZonaEleitoral()      { return zonaEleitoral; }
    public int       getSecao()              { return secao; }
    public Candidato getCandidatoPreferido() { return candidatoPreferido; }
}
