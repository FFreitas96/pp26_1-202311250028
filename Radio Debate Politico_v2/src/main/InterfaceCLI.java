package main;

import fachada.Fachada;
import model.Candidato;
import model.Eleitor;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class InterfaceCLI {

    private final Fachada              fachada;
    private final Scanner              scanner;
    private final Map<Integer, Eleitor> eleitoresCadastrados;
    private       int                   proximoIdEleitor;
    private final String[]              fases = {"PERGUNTA", "RESPOSTA", "REPLICA", "TREPLICA"};
    private       int                   faseIndice;
    private final int[]                 tempos;
    private       boolean               candidatosCadastrados;
    private final List<Candidato>       listaCandidatos;

    public InterfaceCLI() {
        this.fachada              = Fachada.getInstance();
        this.scanner              = new Scanner(System.in);
        this.eleitoresCadastrados = new HashMap<>();
        this.proximoIdEleitor     = 100;
        this.faseIndice           = 0;
        this.tempos               = new int[]{30, 60, 30, 30};
        this.candidatosCadastrados = false;
        this.listaCandidatos      = new ArrayList<>();
    }

    public static void main(String[] args) {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        new InterfaceCLI().executar();
    }

    public void executar() {
        cabecalho();
        boolean rodando = true;
        while (rodando) {
            menu();
            String op = scanner.nextLine().trim();
            switch (op) {
                case "1":  cadastrarCandidatos();    break;
                case "2":  cadastrarEleitor();       break;
                case "3":  sortearInquiridor();      break;
                case "4":  definirInquirido();       break;
                case "5":  iniciarProximaFase();     break;
                case "6":  cancelarEleitor();        break;
                case "7":  clonarCandidato();        break;
                case "8":  clonarEleitor();          break;
                case "9":  finalizarDebate();        break;
                case "0":  rodando = false; System.out.println("Encerrando GDP."); break;
                default:   System.out.println("Opcao invalida.");
            }
        }
        scanner.close();
    }

    private void cabecalho() {
        System.out.println("==============================================================");
        System.out.println("  GDP - Gerenciador de Debate entre Politicos v2.0");
        System.out.println("  Builder | Prototype | Observer | Mediator | Facade");
        System.out.println("==============================================================\n");
    }

    private void menu() {
        System.out.println("\n==================== MENU ====================");
        System.out.println("1 - Cadastrar candidatos (Builder)");
        System.out.println("2 - Cadastrar eleitor    (Builder)");
        System.out.println("3 - Sortear inquiridor");
        System.out.println("4 - Definir inquirido");
        System.out.println("5 - Iniciar proxima fase [" + faseLabel() + "]");
        System.out.println("6 - Cancelar cadastro de eleitor");
        System.out.println("7 - Clonar candidato     (Prototype)");
        System.out.println("8 - Clonar eleitor       (Prototype)");
        System.out.println("9 - Finalizar debate e gerar relatorio");
        System.out.println("0 - Sair");
        System.out.println("==============================================");
        System.out.print("Escolha: ");
    }

    private String faseLabel() {
        return faseIndice >= fases.length ? "rodada encerrada" : fases[faseIndice];
    }

    private void cadastrarCandidatos() {
        if (candidatosCadastrados) {
            System.out.println("Candidatos ja cadastrados nesta sessao.");
            return;
        }
        System.out.println("\n[Builder] Cadastrando 3 candidatos via DebateDirector...");

        Candidato c1 = fachada.criarCandidato(1, "Ana Silva",   "PT",   11111);
        Candidato c2 = fachada.criarCandidato(2, "Carlos Melo", "PSDB", 22222);
        Candidato c3 = fachada.criarCandidato(3, "Renata Foz",  "PL",   33333);

        listaCandidatos.add(c1);
        listaCandidatos.add(c2);
        listaCandidatos.add(c3);

        fachada.configurarDebate(listaCandidatos, tempos);
        candidatosCadastrados = true;

        System.out.println("Candidatos cadastrados:");
        for (Candidato c : listaCandidatos) {
            System.out.println("  [" + c.getId() + "] " + c.getNome()
                    + " | " + c.getPartido()
                    + " | N." + c.getNumeroEleitoral());
        }
    }

    private void cadastrarEleitor() {
        if (!candidatosCadastrados) {
            System.out.println("Cadastre os candidatos primeiro (opcao 1).");
            return;
        }
        System.out.print("[Builder] Nome do eleitor: ");
        String nome = scanner.nextLine().trim();
        if (nome.isEmpty()) { System.out.println("Nome invalido."); return; }

        System.out.print("Zona eleitoral: ");
        String zona = scanner.nextLine().trim();

        System.out.print("Secao: ");
        int secao;
        try { secao = Integer.parseInt(scanner.nextLine().trim()); }
        catch (NumberFormatException e) { System.out.println("Secao invalida."); return; }

        System.out.println("Candidatos:");
        for (Candidato c : fachada.getGerenciador().getCandidatos()) {
            System.out.println("  [" + c.getId() + "] " + c.getNome()
                    + " (" + c.getPartido() + ")");
        }
        System.out.print("ID do candidato preferido: ");
        int idCand;
        try { idCand = Integer.parseInt(scanner.nextLine().trim()); }
        catch (NumberFormatException e) { System.out.println("ID invalido."); return; }

        Eleitor e = fachada.criarEleitor(proximoIdEleitor++, nome, zona, secao);
        fachada.cadastrarEleitor(e, idCand);
        eleitoresCadastrados.put(e.getId(), e);
        System.out.println("Eleitor criado e cadastrado (id=" + e.getId() + ")");
    }

    private void sortearInquiridor() {
        if (!candidatosCadastrados) { System.out.println("Cadastre candidatos primeiro."); return; }
        fachada.sortearInquiridor();
        faseIndice = 0;
    }

    private void definirInquirido() {
        if (fachada.getGerenciador().getInquiridor() == null) {
            System.out.println("Sorteie o inquiridor primeiro (opcao 3)."); return;
        }
        System.out.println("Candidatos:");
        for (Candidato c : fachada.getGerenciador().getCandidatos()) {
            String marca = (c == fachada.getGerenciador().getInquiridor()) ? " [inquiridor]" : "";
            System.out.println("  [" + c.getId() + "] " + c.getNome() + marca);
        }
        System.out.print("ID do inquirido: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            fachada.definirInquirido(id);
        } catch (NumberFormatException e) { System.out.println("ID invalido."); }
    }

    private void iniciarProximaFase() {
        if (fachada.getGerenciador().getInquiridor() == null
                || fachada.getGerenciador().getInquirido() == null) {
            System.out.println("Defina inquiridor e inquirido (opcoes 3 e 4)."); return;
        }
        if (faseIndice >= fases.length) {
            System.out.println("Todas as fases ja foram executadas nesta rodada."); return;
        }
        String fase = fases[faseIndice];
        fachada.getGerenciador().setFaseAtual(fase);
        fachada.getGerenciador().iniciarFase(tempos[faseIndice]);
        faseIndice++;
        if (faseIndice >= fases.length) System.out.println("\n>> Rodada concluida!");
    }

    private void cancelarEleitor() {
        if (eleitoresCadastrados.isEmpty()) { System.out.println("Nenhum eleitor cadastrado."); return; }
        System.out.println("Eleitores:");
        for (Eleitor e : eleitoresCadastrados.values()) {
            String pref = e.getCandidatoPreferido() != null
                    ? e.getCandidatoPreferido().getNome() : "(sem candidato)";
            System.out.println("  [" + e.getId() + "] " + e.getNome() + " -> " + pref);
        }
        System.out.print("ID do eleitor: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Eleitor el = eleitoresCadastrados.get(id);
            if (el == null) { System.out.println("Eleitor nao encontrado."); return; }
            el.cancelarCadastro();
        } catch (NumberFormatException e) { System.out.println("ID invalido."); }
    }

    private void clonarCandidato() {
        if (!candidatosCadastrados) { System.out.println("Cadastre candidatos primeiro."); return; }
        System.out.println("[Prototype] Candidatos disponiveis para clonar:");
        for (Candidato c : fachada.getGerenciador().getCandidatos()) {
            System.out.println("  [" + c.getId() + "] " + c.getNome());
        }
        System.out.print("ID do candidato a clonar: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            for (Candidato c : fachada.getGerenciador().getCandidatos()) {
                if (c.getId() == id) {
                    Candidato clone = fachada.clonarCandidato(c);
                    System.out.println("Clone criado: [" + clone.getId() + "] "
                            + clone.getNome() + " | " + clone.getPartido()
                            + " | Microfone id=" + clone.getMicrofone().getId());
                    return;
                }
            }
            System.out.println("Candidato nao encontrado.");
        } catch (NumberFormatException e) { System.out.println("ID invalido."); }
    }

    private void clonarEleitor() {
        if (eleitoresCadastrados.isEmpty()) { System.out.println("Nenhum eleitor cadastrado."); return; }
        System.out.println("[Prototype] Eleitores disponiveis para clonar:");
        for (Eleitor e : eleitoresCadastrados.values()) {
            System.out.println("  [" + e.getId() + "] " + e.getNome()
                    + " | Zona: " + e.getZonaEleitoral());
        }
        System.out.print("ID do eleitor a clonar: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Eleitor original = eleitoresCadastrados.get(id);
            if (original == null) { System.out.println("Eleitor nao encontrado."); return; }
            Eleitor clone = fachada.clonarEleitor(original);
            System.out.println("Clone criado: [" + clone.getId() + "] "
                    + clone.getNome() + " | Zona: " + clone.getZonaEleitoral()
                    + " | Candidato preferido: (nenhum - clone sem vinculo)");
        } catch (NumberFormatException e) { System.out.println("ID invalido."); }
    }

    private void finalizarDebate() {
        fachada.finalizarDebate();
    }
}
