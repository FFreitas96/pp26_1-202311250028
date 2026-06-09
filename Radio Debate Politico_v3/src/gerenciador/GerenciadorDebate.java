package gerenciador;

import interfaces.Mediador;
import model.Candidato;
import model.Cronometro;
import state.EstadoDebate;
import state.EstadoEmAndamento;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

public class GerenciadorDebate implements Mediador {

    private static final Set<String> FASES_VALIDAS =
            Set.of("PERGUNTA", "RESPOSTA", "REPLICA", "TREPLICA");

    private List<Candidato>      candidatos;
    private Candidato            inquiridor;
    private Candidato            inquirido;
    private final Cronometro     cronometro;
    private final Logger         logger;
    private String               faseAtual;
    private int[]                tempos;

    // State
    private EstadoDebate         estadoAtual;
    private final Queue<Candidato> filaDR;

    // Candidato cuja defesa de DR está em andamento
    private Candidato candidatoDRAtivo;

    public GerenciadorDebate() {
        this.candidatos        = new ArrayList<>();
        this.cronometro        = new Cronometro();
        this.cronometro.setMediador(this);
        this.logger            = new Logger();
        this.faseAtual         = null;
        this.estadoAtual       = new EstadoEmAndamento();
        this.filaDR            = new ArrayDeque<>();
        this.candidatoDRAtivo  = null;
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

    /**
     * Delega ao estado atual.
     * EstadoEmAndamento / EstadoAguardandoCiclo: avanca o ciclo PRRT.
     * EstadoEmDireitoDeResposta: processa o proximo DR da fila.
     * EstadoFinalizado: sem efeito.
     */
    @Override
    public void proximaAcao() {
        estadoAtual.proximaAcao(this);
    }

    // ==================== State: métodos de DR ====================

    /**
     * Chamado via Mediador por Candidato.solicitarDireitoDeResposta().
     * Delega ao estado atual — aceita ou ignora conforme o estado.
     */
    @Override
    public void solicitarDR(Candidato candidato) {
        estadoAtual.solicitarDR(candidato, this);
    }

    /**
     * Concede os DRs pendentes. Só tem efeito em EstadoAguardandoCiclo.
     */
    public void concederDR() {
        estadoAtual.concederDR(this);
    }

    /**
     * Nega os DRs pendentes. Só tem efeito em EstadoAguardandoCiclo.
     */
    public void negarDR() {
        estadoAtual.negarDR(this);
    }

    /**
     * Abre o microfone do proximo candidato da filaDR para sua defesa (1 min).
     * Desliga o microfone do candidato anterior antes de iniciar o proximo.
     * Quando a fila esgota, retorna ao EstadoEmAndamento.
     *
     * Correção E1: microfone do candidato anterior é desligado aqui,
     * garantindo que nunca haja dois microfones de DR ligados ao mesmo tempo.
     */
    public void processarProximoDR() {
        // Desligar microfone do candidato que acabou de falar (se houver)
        if (candidatoDRAtivo != null) {
            candidatoDRAtivo.getMicrofone().desligar();
            candidatoDRAtivo.getMicrofone().resetarBotaoDR();
            candidatoDRAtivo = null;
        }

        if (filaDR.isEmpty()) {
            System.out.println("  [DR] Todas as defesas concluidas. "
                    + "Retornando ao fluxo normal do debate.");
            registrarAcao("Direitos de Resposta concluidos");
            setEstado(new EstadoEmAndamento());
            return;
        }

        Candidato proximo = filaDR.poll();
        System.out.println("\n  [DR] Vez de " + proximo.getNome()
                + " (" + proximo.getPartido() + ") — 1 minuto para defesa.");
        registrarAcao("Direito de Resposta: " + proximo.getNome()
                + " (" + proximo.getPartido() + ")");

        proximo.receberFala("DR");
        proximo.setSolicitouDR(false);
        candidatoDRAtivo = proximo;

        System.out.println("  [DR] Restam " + filaDR.size()
                + " candidato(s) na fila de DR.");
        cronometro.iniciar(60);
    }

    public void setEstado(EstadoDebate novoEstado) {
        this.estadoAtual = novoEstado;
        System.out.println("  [State] Novo estado: "
                + novoEstado.getClass().getSimpleName());
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

    // ── Acessores para os estados (W4: fila exposta só ao pacote state) ──

    /** Retorna visão não-modificável da fila para consulta de tamanho. */
    public int          getFilaDRSize()    { return filaDR.size(); }

    /** Adiciona candidato à fila de DR. Usado pelos estados. */
    public void         addFilaDR(Candidato c) { filaDR.add(c); }

    /** Remove e retorna o proximo da fila. Usado por processarProximoDR. */
    Candidato           pollFilaDR()       { return filaDR.poll(); }

    /** Verifica se a fila está vazia. */
    public boolean      isFilaDRVazia()    { return filaDR.isEmpty(); }

    /**
     * Limpa a fila. Usado por negarDR em EstadoAguardandoCiclo.
     * Retorna os candidatos removidos para reset de flags.
     */
    public List<Candidato> limparFilaDR() {
        List<Candidato> removidos = new ArrayList<>(filaDR);
        filaDR.clear();
        return removidos;
    }

    public List<Candidato>  getCandidatos()  { return Collections.unmodifiableList(candidatos); }
    public Logger           getLogger()      { return logger; }
    public String           getFaseAtual()   { return faseAtual; }
    public Candidato        getInquiridor()  { return inquiridor; }
    public Candidato        getInquirido()   { return inquirido; }
    public Cronometro       getCronometro()  { return cronometro; }
    public EstadoDebate     getEstadoAtual() { return estadoAtual; }
    public int[]            getTempos()      { return tempos; }
}
