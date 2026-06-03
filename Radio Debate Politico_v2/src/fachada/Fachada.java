package fachada;

import builder.CandidatoBuilder;
import builder.DebateDirector;
import builder.EleitorConcreteBuilder;
import gerenciador.GerenciadorDebate;
import model.Candidato;
import model.Eleitor;

import java.util.List;

public class Fachada {

    private static Fachada           instance;
    private final  GerenciadorDebate gerenciador;
    private final  DebateDirector    director;

    private Fachada() {
        this.gerenciador = new GerenciadorDebate();
        this.director    = new DebateDirector(
                new CandidatoBuilder(),
                new EleitorConcreteBuilder()
        );
    }

    public static Fachada getInstance() {
        if (instance == null) {
            instance = new Fachada();
        }
        return instance;
    }

    public void configurarDebate(List<Candidato> candidatos, int[] tempos) {
        if (candidatos == null || candidatos.isEmpty()
                || tempos == null || tempos.length < 4) {
            System.out.println("Erro: dados invalidos para configurar debate.");
            return;
        }
        gerenciador.setCandidatos(candidatos);
        gerenciador.setTempos(tempos);
    }

    public void sortearInquiridor() {
        gerenciador.sortearInquiridor();
    }

    public void definirInquirido(int idCandidato) {
        gerenciador.definirInquirido(idCandidato);
    }

    public void iniciarDebate() {
        if (gerenciador.getCandidatos().isEmpty()) {
            System.out.println("Erro: debate nao configurado.");
            return;
        }
        gerenciador.registrarAcao("Debate iniciado");
        gerenciador.sortearInquiridor();
    }

    public void avancarEtapa() {
        gerenciador.proximaAcao();
    }

    public void finalizarDebate() {
        gerenciador.registrarAcao("Debate finalizado");
        gerenciador.getLogger().gerarRelatorio();
    }

    public void cadastrarEleitor(Eleitor eleitor, int idCandidato) {
        for (Candidato c : gerenciador.getCandidatos()) {
            if (c.getId() == idCandidato) {
                eleitor.seCadastrar(c);
                gerenciador.registrarAcao("Eleitor " + eleitor.getNome()
                        + " [Zona " + eleitor.getZonaEleitoral()
                        + "-" + eleitor.getSecao() + "] cadastrado em "
                        + c.getNome());
                return;
            }
        }
        System.out.println("Candidato invalido (id=" + idCandidato + ")");
    }

    public Candidato criarCandidato(int id, String nome,
                                    String partido, int numeroEleitoral) {
        return director.construirCandidato(id, nome, partido, numeroEleitoral);
    }

    public Eleitor criarEleitor(int id, String nome, String zona, int secao) {
        return director.construirEleitor(id, nome, zona, secao);
    }

    public Candidato clonarCandidato(Candidato original) {
        return original.clonar();
    }

    public Eleitor clonarEleitor(Eleitor original) {
        return original.clonar();
    }

    public GerenciadorDebate getGerenciador() {
        return gerenciador;
    }
}
