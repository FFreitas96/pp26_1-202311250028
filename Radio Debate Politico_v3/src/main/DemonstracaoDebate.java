package main;

import fachada.Fachada;
import model.Candidato;
import model.Eleitor;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class DemonstracaoDebate {

    public static void main(String[] args) {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));

        Fachada fachada = Fachada.getInstance();
        int[] tempos = {30, 60, 30, 30};

        separador('=', 65);
        System.out.println("  GDP - Gerenciador de Debate entre Politicos v3.0");
        System.out.println("  Padroes: Builder | Prototype | Observer | Mediator | Facade | State");
        separador('=', 65);

        // ------------------------------------------------------------------
        titulo("ETAPA 1: Construindo candidatos via Builder (DebateDirector)");

        Candidato ana    = fachada.criarCandidato(1, "Ana Silva",   "PT",   11111);
        Candidato carlos = fachada.criarCandidato(2, "Carlos Melo", "PSDB", 22222);
        Candidato renata = fachada.criarCandidato(3, "Renata Foz",  "PL",   33333);

        System.out.println("  Candidatos criados via CandidatoBuilder:");
        imprimirCandidato(ana);
        imprimirCandidato(carlos);
        imprimirCandidato(renata);

        // ------------------------------------------------------------------
        titulo("ETAPA 2: Construindo eleitores via Builder (DebateDirector)");

        Eleitor joao  = fachada.criarEleitor(101, "Joao",  "Zona Norte", 5);
        Eleitor maria = fachada.criarEleitor(102, "Maria", "Zona Sul",   12);
        Eleitor pedro = fachada.criarEleitor(103, "Pedro", "Zona Leste", 7);
        Eleitor lucia = fachada.criarEleitor(104, "Lucia", "Zona Oeste", 3);

        System.out.println("  Eleitores criados via EleitorConcreteBuilder:");
        imprimirEleitor(joao);
        imprimirEleitor(maria);
        imprimirEleitor(pedro);
        imprimirEleitor(lucia);

        // ------------------------------------------------------------------
        titulo("ETAPA 3: Clonando objetos via Prototype");

        Candidato anaClone  = fachada.clonarCandidato(ana);
        Eleitor   joaoClone = fachada.clonarEleitor(joao);

        System.out.println("  Clone de Candidato:");
        imprimirCandidato(anaClone);
        System.out.println("  Clone de Eleitor:");
        imprimirEleitor(joaoClone);
        System.out.println("  >> Verificacao: ana != anaClone ? "
                + (ana != anaClone) + " (objetos distintos em memoria)");
        System.out.println("  >> Microfone ana.id=" + ana.getMicrofone().getId()
                + " | clone.id=" + anaClone.getMicrofone().getId()
                + " (microfones independentes - copia profunda)");

        // ------------------------------------------------------------------
        titulo("ETAPA 4: Configurando o debate");

        List<Candidato> candidatos = new ArrayList<>();
        candidatos.add(ana);
        candidatos.add(carlos);
        candidatos.add(renata);

        fachada.configurarDebate(candidatos, tempos);
        System.out.println("  Debate configurado com " + candidatos.size()
                + " candidatos | Tempos: P=" + tempos[0]
                + "s R=" + tempos[1] + "s Re=" + tempos[2]
                + "s Tr=" + tempos[3] + "s");

        // ------------------------------------------------------------------
        titulo("ETAPA 5: Cadastrando eleitores como Observadores (Observer)");

        fachada.cadastrarEleitor(joao,  1); // joao  -> Ana
        fachada.cadastrarEleitor(maria, 1); // maria -> Ana
        fachada.cadastrarEleitor(pedro, 2); // pedro -> Carlos
        fachada.cadastrarEleitor(lucia, 3); // lucia -> Renata

        System.out.println("\n  Maria muda de candidato preferido (Ana -> Carlos):");
        fachada.cadastrarEleitor(maria, 2);

        // ------------------------------------------------------------------
        titulo("ETAPA 6: RODADA 1 — Carlos pergunta para Ana");
        System.out.println("  [State] Estado inicial: "
                + fachada.getGerenciador().getEstadoAtual().getClass().getSimpleName());

        fachada.getGerenciador().setInquiridor(carlos);
        fachada.definirInquirido(1);

        executarFase(fachada, "PERGUNTA", tempos[0]);
        executarFase(fachada, "RESPOSTA", tempos[1]);

        // ------------------------------------------------------------------
        titulo("ETAPA 7: Tres candidatos solicitam DR durante o debate (State)");
        System.out.println("  [State] Simulando pressionamento do botao DR...\n");

        // Ana solicita DR (1a — muda estado para AguardandoCiclo)
        fachada.solicitarDR(1);
        System.out.println("  [State] Estado apos 1a solicitacao: "
                + fachada.getGerenciador().getEstadoAtual().getClass().getSimpleName());

        // Renata solicita DR (2a — acumula na fila)
        fachada.solicitarDR(3);
        System.out.println("  [State] Estado apos 2a solicitacao: "
                + fachada.getGerenciador().getEstadoAtual().getClass().getSimpleName());

        // Carlos solicita DR (3a — acumula na fila)
        fachada.solicitarDR(2);
        System.out.println("  [State] Candidatos na fila de DR: "
                + fachada.getGerenciador().getFilaDRSize());

        // Tentativa de Ana solicitar novamente — deve ser bloqueada pela flag
        System.out.println("\n  [State] Ana tenta solicitar DR novamente (deve ser bloqueado):");
        fachada.solicitarDR(1);
        System.out.println("  [State] Fila permanece com "
                + fachada.getGerenciador().getFilaDRSize() + " candidato(s)");

        // Continuacao das fases normais ate encerrar o ciclo
        executarFase(fachada, "REPLICA",  tempos[2]);
        executarFase(fachada, "TREPLICA", tempos[3]);

        System.out.println("\n  [State] Estado apos TREPLICA: "
                + fachada.getGerenciador().getEstadoAtual().getClass().getSimpleName());

        // ------------------------------------------------------------------
        titulo("ETAPA 8: Gerente concede os Direitos de Resposta (State)");

        fachada.concederDR();
        System.out.println("  [State] Estado apos concessao: "
                + fachada.getGerenciador().getEstadoAtual().getClass().getSimpleName());

        // ------------------------------------------------------------------
        titulo("ETAPA 9: Defesas em andamento — novo DR bloqueado (State)");

        // Tentativa de novo DR durante defesas — deve ser ignorada
        System.out.println("  [State] Tentativa de novo DR durante defesas:");
        fachada.solicitarDR(2);
        System.out.println("  [State] Estado permanece: "
                + fachada.getGerenciador().getEstadoAtual().getClass().getSimpleName());

        // Avanca a fila: cada chamada encerra a defesa atual e abre a proxima
        System.out.println("\n  [State] Encerrando defesa de Ana, abrindo Renata:");
        fachada.avancarEtapa();

        System.out.println("\n  [State] Encerrando defesa de Renata, abrindo Carlos:");
        fachada.avancarEtapa();

        System.out.println("\n  [State] Encerrando defesa de Carlos — fila vazia:");
        fachada.avancarEtapa();

        System.out.println("\n  [State] Estado apos fim das defesas: "
                + fachada.getGerenciador().getEstadoAtual().getClass().getSimpleName());

        // ------------------------------------------------------------------
        titulo("ETAPA 10: RODADA 2 — Renata pergunta para Carlos (fluxo normal retomado)");

        fachada.getGerenciador().setInquiridor(renata);
        fachada.definirInquirido(2);

        executarFase(fachada, "PERGUNTA", tempos[0]);
        executarFase(fachada, "RESPOSTA", tempos[1]);
        executarFase(fachada, "REPLICA",  tempos[2]);
        executarFase(fachada, "TREPLICA", tempos[3]);

        System.out.println("  [State] Estado apos TREPLICA sem DR: "
                + fachada.getGerenciador().getEstadoAtual().getClass().getSimpleName());

        // ------------------------------------------------------------------
        titulo("ETAPA 11: Demonstrando negarDR — Ana solicita e gerente nega");

        fachada.getGerenciador().setInquiridor(ana);
        fachada.definirInquirido(2);
        executarFase(fachada, "PERGUNTA", tempos[0]);

        fachada.solicitarDR(3); // Renata solicita DR
        executarFase(fachada, "RESPOSTA", tempos[1]);
        executarFase(fachada, "REPLICA",  tempos[2]);
        executarFase(fachada, "TREPLICA", tempos[3]);

        System.out.println("  [State] Gerente NEGA o DR:");
        fachada.negarDR();
        System.out.println("  [State] Estado apos negarDR: "
                + fachada.getGerenciador().getEstadoAtual().getClass().getSimpleName());

        // ------------------------------------------------------------------
        titulo("ETAPA 12: Joao cancela seu cadastro de observador");
        joao.cancelarCadastro();

        // ------------------------------------------------------------------
        titulo("ETAPA 13: Finalizando debate e gerando relatorio (Logger + State)");
        fachada.finalizarDebate();
        System.out.println("  [State] Estado final: "
                + fachada.getGerenciador().getEstadoAtual().getClass().getSimpleName());

        // Tentativa de acao apos finalizacao — deve ser ignorada
        System.out.println("\n  [State] Tentativa de solicitarDR apos finalizacao:");
        fachada.solicitarDR(1);

        separador('=', 65);
        System.out.println("  Demonstracao v3.0 concluida.");
        separador('=', 65);
    }

    private static void executarFase(Fachada fachada, String fase, int tempo) {
        System.out.println("\n  --- Iniciando fase: " + fase + " ---");
        fachada.getGerenciador().setFaseAtual(fase);
        fachada.getGerenciador().iniciarFase(tempo);
    }

    private static void imprimirCandidato(Candidato c) {
        System.out.println("    > [" + c.getId() + "] " + c.getNome()
                + " | Partido: " + c.getPartido()
                + " | N.Eleitoral: " + c.getNumeroEleitoral());
    }

    private static void imprimirEleitor(Eleitor e) {
        System.out.println("    > [" + e.getId() + "] " + e.getNome()
                + " | Zona: " + e.getZonaEleitoral()
                + " | Secao: " + e.getSecao());
    }

    private static void titulo(String texto) {
        System.out.println();
        separador('-', 65);
        System.out.println("  " + texto);
        separador('-', 65);
    }

    private static void separador(char ch, int n) {
        System.out.println(String.valueOf(ch).repeat(n));
    }
}
