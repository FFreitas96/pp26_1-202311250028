package main;

import fachada.Fachada;
import model.Candidato;
import model.Eleitor;
import model.Microfone;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe de demonstração da Questão 5.
 *
 * Mostra na prática o funcionamento do padrão Observer:
 *  1) Cria candidatos.
 *  2) Cria eleitores e os cadastra nos candidatos preferidos.
 *  3) Inicia o debate e percorre as fases.
 *  4) A cada início de fase, ANTES do microfone ser ligado, os eleitores
 *     do candidato falante recebem a notificação "Candidato xxxx esta
 *     falando".
 *
 * Cenário simulado:
 *  - 3 candidatos: Ana, Alberto e Felipe.
 *  - 5 eleitores distribuídos entre os candidatos.
 *  - Rodada 1: Ana (inquiridor) -> Alberto (inquirido).
 *  - Rodada 2: Felipe (inquiridor) -> Alberto (inquirido).
 */
public class DemonstracaoDebate {

    public static void main(String[] args) {
        // Garante saida em UTF-8 (acentos nao ficam como "?")
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));

        System.out.println("==========================================");
        System.out.println(" DEMONSTRACAO - Radio Debate Politico");
        System.out.println(" Questao 5 - Padrao Observer");
        System.out.println("==========================================\n");

        // --- 1) Criar candidatos ---
        Candidato ana     = new Candidato(1, "Ana",     new Microfone(1));
        Candidato alberto = new Candidato(2, "Alberto", new Microfone(2));
        Candidato felipe  = new Candidato(3, "Felipe",  new Microfone(3));

        List<Candidato> candidatos = new ArrayList<>();
        candidatos.add(ana);
        candidatos.add(alberto);
        candidatos.add(felipe);

        // --- 2) Configurar a fachada ---
        int[] tempos = { 30, 60, 30, 30 };
        Fachada fachada = Fachada.getInstance();
        fachada.configurarDebate(candidatos, tempos);

        // --- 3) Cadastrar eleitores ---
        System.out.println(">>> ETAPA 1: Cadastro de eleitores\n");
        Eleitor carla   = new Eleitor(101, "Carla");
        Eleitor roberto = new Eleitor(102, "Roberto");
        Eleitor patricia = new Eleitor(103, "Patricia");
        Eleitor diego   = new Eleitor(104, "Diego");
        Eleitor renata  = new Eleitor(105, "Renata");

        // Carla e Renata preferem Ana
        fachada.cadastrarEleitor(carla, 1);
        fachada.cadastrarEleitor(renata, 1);
        // Roberto prefere Alberto
        fachada.cadastrarEleitor(roberto, 2);
        // Patricia e Diego preferem Felipe
        fachada.cadastrarEleitor(patricia, 3);
        fachada.cadastrarEleitor(diego, 3);

        // --- 4) Demonstracao: Renata muda de candidato preferido ---
        System.out.println("\n>>> ETAPA 2: Renata muda de candidato preferido (de Ana para Felipe)\n");
        fachada.cadastrarEleitor(renata, 3);

        // --- 5) Simular RODADA 1: Ana pergunta para Alberto ---
        System.out.println("\n>>> ETAPA 3: RODADA 1 - Ana pergunta a Alberto\n");
        fachada.getGerenciador().setInquiridor(ana);
        fachada.definirInquirido(2);

        executarFase(fachada, "PERGUNTA",  tempos[0]);
        executarFase(fachada, "RESPOSTA",  tempos[1]);
        executarFase(fachada, "REPLICA",   tempos[2]);
        executarFase(fachada, "TREPLICA",  tempos[3]);

        // --- 6) Simular RODADA 2: Felipe pergunta para Alberto ---
        System.out.println("\n>>> ETAPA 4: RODADA 2 - Felipe pergunta a Alberto\n");
        fachada.getGerenciador().setInquiridor(felipe);
        fachada.definirInquirido(2);

        executarFase(fachada, "PERGUNTA",  tempos[0]);
        executarFase(fachada, "RESPOSTA",  tempos[1]);
        executarFase(fachada, "REPLICA",   tempos[2]);
        executarFase(fachada, "TREPLICA",  tempos[3]);

        // --- 7) Cancelamento de cadastro ---
        System.out.println("\n>>> ETAPA 5: Diego cancela seu cadastro\n");
        diego.cancelarCadastro();

        // --- 8) Finalizar ---
        System.out.println("\n>>> ETAPA 6: Finalizando debate\n");
        fachada.finalizarDebate();
    }

    /**
     * Helper: posiciona a fase atual no gerenciador (via setter publico)
     * e chama iniciarFase().
     */
    private static void executarFase(Fachada fachada, String fase, int tempo) {
        fachada.getGerenciador().setFaseAtual(fase);
        fachada.getGerenciador().iniciarFase(tempo);
    }
}
