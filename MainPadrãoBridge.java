// =============================================
// INTERFACE IMPLEMENTADOR
// =============================================
interface Implementador {
    void getDados(String tipo);
}

// =============================================
// IMPLEMENTAÇÕES CONCRETAS
// =============================================
class PublicacaoImplBD implements Implementador {
    @Override
    public void getDados(String tipo) {
        System.out.println("  [PublicacaoImplBD] getDados chamado com tipo: " + tipo);
    }
}

class PublicacaoImplXML implements Implementador {
    @Override
    public void getDados(String tipo) {
        System.out.println("  [PublicacaoImplXML] getDados chamado com tipo: " + tipo);
        System.out.println("  [PublicacaoImplXML] tipo.setTitulo(\"...\") chamado");
        System.out.println("  [PublicacaoImplXML] tipo.setAutores(...) chamado");
        System.out.println("  [PublicacaoImplXML] tipo.setOutros(...) chamado");
    }
}

// =============================================
// ABSTRAÇÃO
// =============================================
abstract class Publicacao {
    protected Implementador imp;

    public Publicacao(Implementador imp) {
        this.imp = imp;
    }

    public void obterDados(String tipo) {
        System.out.println("  [Publicacao] obterDados chamado");
        imp.getDados(tipo);
    }

    public abstract void getTitulo();
    public abstract void getAutor(int id);
}

// =============================================
// ABSTRAÇÕES REFINADAS
// =============================================
class Livro extends Publicacao {
    public Livro(Implementador imp) {
        super(imp);
    }

    public void getISBN() {
        System.out.println("  [Livro] getISBN chamado");
    }

    @Override
    public void getTitulo() {
        System.out.println("  [Livro] getTitulo chamado");
    }

    @Override
    public void getAutor(int id) {
        System.out.println("  [Livro] getAutor chamado com id: " + id);
    }
}

class Revista extends Publicacao {
    public Revista(Implementador imp) {
        super(imp);
    }

    public void getArtigo() {
        System.out.println("  [Revista] getArtigo chamado");
    }

    @Override
    public void getTitulo() {
        System.out.println("  [Revista] getTitulo chamado");
    }

    @Override
    public void getAutor(int id) {
        System.out.println("  [Revista] getAutor chamado com id: " + id);
    }
}

// =============================================
// CLIENTE
// =============================================
public class Main {
    public static void main(String[] args) {

        Implementador banco = new PublicacaoImplBD();
        Implementador xml   = new PublicacaoImplXML();

        System.out.println("=== Cliente do Padrão Bridge ===\n");

        // CASE 1
        System.out.println("[CASE 1] Livro no Banco de Dados:");
        Livro livro1 = new Livro(banco);
        livro1.obterDados("Academico");
        livro1.getTitulo();
        livro1.getAutor(1);
        livro1.getISBN();
        System.out.println("----------------------------------------\n");

        // CASE 2
        System.out.println("[CASE 2] Livro no arquivo XML:");
        Livro livro2 = new Livro(xml);
        livro2.obterDados("Literatura");
        livro2.getTitulo();
        livro2.getAutor(2);
        livro2.getISBN();
        System.out.println("----------------------------------------\n");

        // CASE 3
        System.out.println("[CASE 3] Revista no Banco de Dados:");
        Revista revista1 = new Revista(banco);
        revista1.obterDados("Cientifico");
        revista1.getTitulo();
        revista1.getAutor(55);
        revista1.getArtigo();
        System.out.println("----------------------------------------\n");

        // CASE 4
        System.out.println("[CASE 4] Revista no arquivo XML:");
        Revista revista2 = new Revista(xml);
        revista2.obterDados("Informatica");
        revista2.getTitulo();
        revista2.getAutor(99);
        revista2.getArtigo();
        System.out.println("----------------------------------------\n");

        System.out.println("=== Fim da execução ===");
    }
}
