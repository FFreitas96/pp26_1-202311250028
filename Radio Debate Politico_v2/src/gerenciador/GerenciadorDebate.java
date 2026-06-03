package gerenciador;

import interfaces.Mediador;
import model.Candidato;
import model.Cronometro;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class GerenciadorDebate implements Mediador {

    private static final Set<String> FASES_VALIDAS =
            Set.of("PERGUNTA", "RESPOSTA", "REPLICA", "TREPLICA");

    private List<Candidato> candidatos;
    private Candidato       inquiridor;
    private Candidato       inquirido;
    private final Cronometro cronometro;
    private final Logger    logger;
    private String          faseAtual;
    private int[]           tempos;

    public GerenciadorDebate() {
        this.candidatos  = new ArrayList<>();
        this.cronometro  = new Cronometro();
        this.cronometro.setMediador(this);
        this.logger      = new Logger();
        this.faseAtual   = null;
    }

    public void setCandidatos(List<Candidato> candidatos) {
        this.candidatos = candidatos;
        for (Candidato c : candidatos) {
            c.setMediador(this);
            c.getMicrofone().setMediador(this);
        }
    }

    public void setTempos(int[] tempos) {
        this.tempos = tempos;
    }

    public void sortearInquiridor() {
        if (faseAtual == null) faseAtual = "PERGUNTA";

        List<Candidato> disponiveis = new ArrayList<>();
        for (Candidato c : candidatos) {
            if (!c.getJaPerguntou()) disponiveis.add(c);
        }

        if (disponiveis.isEmpty()) {
            System.out.println("  Todos os candidatos ja foram inquiridores.");
            return;
        }

        inquiridor = disponiveis.get(new Random().nextInt(disponiveis.size()));
        inquiridor.marcarComoInquiridor();
        registrarAcao("Inquiridor sorteado: " + inquiridor.getNome()
                + " (" + inquiridor.getPartido() + ")");
        System.out.println("  >> Inquiridor sorteado: " + inquiridor.getNome()
                + " | Partido: " + inquiridor.getPartido()
                + " | N. Eleitoral: " + inquiridor.getNumeroEleitoral());
    }

    public void definirInquirido(int id) {
        if (inquiridor == null) {
            System.out.println("  Defina um inquiridor primeiro.");
            return;
        }
        for (Candidato c : candidatos) {
            if (c.getId() == id) {
                if (c == inquiridor) {
                    System.out.println("  O inquirido nao pode ser o proprio inquiridor.");
                    return;
                }
                inquirido = c;
                registrarAcao("Inquirido definido: " + c.getNome()
                        + " (" + c.getPartido() + ")");
                System.out.println("  >> Inquirido definido: " + c.getNome()
                        + " | Partido: " + c.getPartido());
                return;
            }
        }
        System.out.println("  Candidato invalido (id=" + id + ")");
    }

    public void iniciarFase(int tempo) {
        if (inquiridor == null || inquirido == null) {
            System.out.println("  Defina inquiridor e inquirido antes.");
            return;
        }
        switch (faseAtual) {
            case "PERGUNTA":
                inquiridor.receberFala("PERGUNTA");
                inquirido.getMicrofone().desligar();
                break;
            case "RESPOSTA":
                inquiridor.getMicrofone().desligar();
                inquirido.receberFala("RESPOSTA");
                break;
            case "REPLICA":
                inquiridor.receberFala("REPLICA");
                inquirido.getMicrofone().desligar();
                break;
            case "TREPLICA":
                inquiridor.getMicrofone().desligar();
                inquirido.receberFala("TREPLICA");
                break;
        }
        registrarAcao("Fase iniciada: " + faseAtual
                + " | " + inquiridor.getNome() + " x " + inquirido.getNome());
        cronometro.iniciar(tempo);
    }

    public void registrarAcao(String acao) {
        logger.registrar(acao);
    }

    @Override
    public void proximaAcao() {
        if (faseAtual == null) { System.out.println("  Debate nao iniciado."); return; }
        switch (faseAtual) {
            case "PERGUNTA":  faseAtual = "RESPOSTA";  iniciarFase(tempos[1]); break;
            case "RESPOSTA":  faseAtual = "REPLICA";   iniciarFase(tempos[2]); break;
            case "REPLICA":   faseAtual = "TREPLICA";  iniciarFase(tempos[3]); break;
            case "TREPLICA":
                registrarAcao("Rodada finalizada");
                if (inquiridor != null) inquiridor.getMicrofone().desligar();
                if (inquirido  != null) inquirido.getMicrofone().desligar();
                break;
        }
    }

    public void setFaseAtual(String fase) {
        if (fase == null) { this.faseAtual = null; return; }
        if (!FASES_VALIDAS.contains(fase)) {
            System.out.println("  Fase invalida: " + fase);
            return;
        }
        this.faseAtual = fase;
    }

    public void setInquiridor(Candidato c) {
        if (c == null) return;
        this.inquiridor = c;
        c.marcarComoInquiridor();
        registrarAcao("Inquiridor definido: " + c.getNome());
    }

    public List<Candidato> getCandidatos()  { return candidatos; }
    public Logger          getLogger()      { return logger; }
    public String          getFaseAtual()   { return faseAtual; }
    public Candidato       getInquiridor()  { return inquiridor; }
    public Candidato       getInquirido()   { return inquirido; }
    public Cronometro      getCronometro()  { return cronometro; }
}
