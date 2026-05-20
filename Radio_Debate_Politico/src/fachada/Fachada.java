package fachada;

import gerenciador.GerenciadorDebate;
import model.Candidato;
import model.Eleitor;

import java.util.List;

/**
 * Classe Fachada (do diagrama original) - padrão Singleton + Facade.
 *
 * Fornece um ponto de entrada simplificado para as interfaces
 * (InterfaceGUI e InterfaceCLI) interagirem com o sistema do debate,
 * escondendo a complexidade do GerenciadorDebate.
 *
 * Acréscimo da Questão 5:
 *  - Novo método cadastrarEleitor(eleitor, idCandidato) que permite
 *    a uma interface externa registrar um eleitor para receber
 *    notificações de um candidato específico.
 */
public class Fachada {

    private static Fachada instance;
    private GerenciadorDebate gerenciador;

    private Fachada() {
        this.gerenciador = new GerenciadorDebate();
    }

    public static Fachada getInstance() {
        if (instance == null) {
            instance = new Fachada();
        }
        return instance;
    }

    /**
     * Configura o debate informando os candidatos participantes
     * e os tempos (em segundos) de cada fase: pergunta, resposta,
     * réplica e tréplica.
     */
    public void configurarDebate(List<Candidato> candidatos, int[] tempos) {
        if (candidatos == null || candidatos.isEmpty() || tempos == null || tempos.length < 4) {
            System.out.println("Erro: dados inválidos");
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

    /**
     * Inicia o debate. Para fins didáticos, já dispara o sorteio.
     */
    public void iniciarDebate() {
        if (gerenciador.getCandidatos().isEmpty()) {
            System.out.println("Erro: debate não configurado");
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

    /**
     * NOVO MÉTODO (Questão 5).
     *
     * Cadastra um eleitor como observador do candidato cujo id é fornecido.
     * Este é o ponto de entrada da nova funcionalidade na fachada.
     *
     * @param eleitor      o eleitor a ser cadastrado
     * @param idCandidato  o id do candidato preferido do eleitor
     */
    public void cadastrarEleitor(Eleitor eleitor, int idCandidato) {
        for (Candidato c : gerenciador.getCandidatos()) {
            if (c.getId() == idCandidato) {
                eleitor.seCadastrar(c);
                gerenciador.registrarAcao("Eleitor " + eleitor.getNome()
                        + " cadastrado em " + c.getNome());
                return;
            }
        }
        System.out.println("Candidato inválido");
    }

    public GerenciadorDebate getGerenciador() {
        return gerenciador;
    }
}
