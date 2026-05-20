package observer;

/**
 * Interface Observavel (Subject) do padrão Observer.
 *
 * Define o contrato para entidades que podem ser observadas.
 * No contexto do debate, é implementada pela classe Candidato:
 * cada candidato mantém uma lista de eleitores (Observadores) que
 * desejam ser notificados quando ele receber direito de fala.
 */
public interface Observavel {

    /**
     * Cadastra um novo observador (Eleitor) para receber notificações.
     * @param o o observador a ser adicionado
     */
    void adicionarObservador(Observador o);

    /**
     * Remove um observador da lista de notificações.
     * @param o o observador a ser removido
     */
    void removerObservador(Observador o);

    /**
     * Notifica todos os observadores cadastrados.
     * Será chamado ANTES de o microfone ser ligado, garantindo que o
     * eleitor receba a mensagem "SEU CANDIDATO ESTÁ FALANDO" no momento
     * exato em que a fala vai começar.
     */
    void notificarObservadores();
}
