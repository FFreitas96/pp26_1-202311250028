package main;

import fachada.Fachada;

/**
 * Classe InterfaceGUI (do diagrama original).
 *
 * Representa uma interface gráfica que se comunica com o sistema
 * através da Fachada. Neste protótipo é apenas um stub.
 */
public class InterfaceGUI {

    public Fachada fachada;

    public InterfaceGUI() {
        this.fachada = Fachada.getInstance();
    }

    public static void main(String[] args) {
        System.out.println("InterfaceGUI inicializada. Execute DemonstracaoDebate para a demo completa.");
    }
}
