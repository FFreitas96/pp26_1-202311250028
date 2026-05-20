package gerenciador;

import interfaces.Mediador;
import model.Candidato;
import model.Cronometro;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Classe GerenciadorDebate (do diagrama original) - implementa Mediador.
 *
 * Orquestra todo o fluxo do debate: sorteio do inquiridor, definição
 * do inquirido, transição entre fases (PERGUNTA -> RESPOSTA ->
 * REPLICA -> TREPLICA) e finalização da rodada.
 *
 * Mudança da Questão 5: ao iniciar uma fase, em vez de ligar o
 * microfone do candidato diretamente, agora chama
 * candidato.receberFala(...), que primeiro NOTIFICA os eleitores
 * cadastrados e depois liga o microfone.
 */
public class GerenciadorDebate implements Mediador {

    private List<Candidato> candidatos;
    private Candidato inquiridor;
    private Candidato inquirido;
    private Cronometro cronometro;
    private Logger logger;
    private String faseAtual;
    private int[] tempos;

    public GerenciadorDebate() {
        this.candidatos = new ArrayList<>();
        this.cronometro = new Cronometro();
        this.cronometro.setMediador(this);
        this.logger = new Logger();
        this.faseAtual = null;
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

    /**
     * Sorteia um candidato disponível (que ainda não tenha sido inquiridor)
     * para fazer a próxima pergunta.
     */
    public void sortearInquiridor() {
        if (faseAtual == null) {
            faseAtual = "PERGUNTA";
        }

        List<Candidato> disponiveis = new ArrayList<>();
        for (Candidato c : candidatos) {
            if (!c.getJaPerguntou()) {
                disponiveis.add(c);
            }
        }

        if (disponiveis.isEmpty()) {
            System.out.println("Todos ja foram inquiridores");
            return;
        }

        int indice = new Random().nextInt(disponiveis.size());
        inquiridor = disponiveis.get(indice);
        inquiridor.marcarComoInquiridor();
        registrarAcao("Inquiridor sorteado: " + inquiridor.getNome());
    }

    /**
     * Define o candidato inquirido (que vai responder), dado o id.
     */
    public void definirInquirido(int id) {
        if (inquiridor == null) {
            System.out.println("Defina um inquiridor primeiro");
            return;
        }
        for (Candidato c : candidatos) {
            if (c.getId() == id) {
                if (c == inquiridor) {
                    System.out.println("O inquirido nao pode ser o proprio inquiridor");
                    return;
                }
                inquirido = c;
                registrarAcao("Inquirido definido: " + c.getNome());
                return;
            }
        }
        System.out.println("Candidato invalido (id=" + id + ")");
    }

    /**
     * Inicia a fase atual concedendo direito de fala ao candidato apropriado.
     *
     * PASSO CHAVE DA REFATORAÇÃO:
     *  - Antes da Questão 5, esta operação ligava o microfone diretamente.
     *  - Agora ela chama candidato.receberFala(...), que primeiro
     *    notifica os eleitores observadores e depois liga o microfone.
     */
    public void iniciarFase(int tempo) {
        if (inquiridor == null || inquirido == null) {
            System.out.println("Inquiridor e inquirido devem ser definidos antes de iniciar a fase");
            return;
        }
        if (faseAtual == null) {
            System.out.println("Fase atual nao definida");
            return;
        }

        if ("PERGUNTA".equals(faseAtual)) {
            inquiridor.receberFala("PERGUNTA");
            inquirido.getMicrofone().desligar();
        } else if ("RESPOSTA".equals(faseAtual)) {
            inquiridor.getMicrofone().desligar();
            inquirido.receberFala("RESPOSTA");
        } else if ("REPLICA".equals(faseAtual)) {
            inquiridor.receberFala("REPLICA");
            inquirido.getMicrofone().desligar();
        } else if ("TREPLICA".equals(faseAtual)) {
            inquiridor.getMicrofone().desligar();
            inquirido.receberFala("TREPLICA");
        }

        registrarAcao("Fase iniciada: " + faseAtual);
        cronometro.iniciar(tempo);
    }

    public void registrarAcao(String acao) {
        logger.registrar(acao);
    }

    @Override
    public void proximaAcao() {
        if (faseAtual == null) {
            System.out.println("Debate ainda nao iniciado");
            return;
        }
        if ("PERGUNTA".equals(faseAtual)) {
            faseAtual = "RESPOSTA";
            iniciarFase(tempos[1]);
        } else if ("RESPOSTA".equals(faseAtual)) {
            faseAtual = "REPLICA";
            iniciarFase(tempos[2]);
        } else if ("REPLICA".equals(faseAtual)) {
            faseAtual = "TREPLICA";
            iniciarFase(tempos[3]);
        } else if ("TREPLICA".equals(faseAtual)) {
            registrarAcao("Rodada finalizada");
            if (inquiridor != null) inquiridor.getMicrofone().desligar();
            if (inquirido != null)  inquirido.getMicrofone().desligar();
        }
    }

    // ---------------------------------------------------------------
    // Setters de controle para uso pelas interfaces (CLI/GUI/Demo).
    //
    // Substituem a "gambiarra" anterior baseada em reflection.
    // Permitem que a interface posicione o estado do debate em pontos
    // específicos (ex.: definir manualmente o inquiridor, ou pular para
    // uma fase específica) sem quebrar encapsulamento.
    // ---------------------------------------------------------------

    /**
     * Define manualmente a fase atual do debate. Aceita apenas valores
     * válidos: PERGUNTA, RESPOSTA, REPLICA, TREPLICA.
     */
    public void setFaseAtual(String fase) {
        if (fase == null) {
            this.faseAtual = null;
            return;
        }
        if (!"PERGUNTA".equals(fase) && !"RESPOSTA".equals(fase)
                && !"REPLICA".equals(fase) && !"TREPLICA".equals(fase)) {
            System.out.println("Fase invalida: " + fase);
            return;
        }
        this.faseAtual = fase;
    }

    /**
     * Define manualmente o inquiridor (usado em cenários roteirizados
     * de demonstração ou testes).
     */
    public void setInquiridor(Candidato c) {
        if (c == null) return;
        this.inquiridor = c;
        c.marcarComoInquiridor();
        registrarAcao("Inquiridor definido manualmente: " + c.getNome());
    }

    // ----------------- Getters -----------------

    public List<Candidato> getCandidatos() { return candidatos; }
    public Logger getLogger() { return logger; }
    public String getFaseAtual() { return faseAtual; }
    public Candidato getInquiridor() { return inquiridor; }
    public Candidato getInquirido() { return inquirido; }
    public Cronometro getCronometro() { return cronometro; }
}
