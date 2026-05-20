package observer;

/**
 * Interface Observador (Observer) do padrão Observer.
 *
 * Define o contrato para entidades que recebem notificações de um Observavel.
 * No contexto do debate, é implementada pela classe Eleitor: cada eleitor
 * tem um candidato preferido e é notificado quando esse candidato recebe
 * direito de fala (pergunta, resposta, réplica ou tréplica).
 */
public interface Observador {

    /**
     * Método chamado pelo Observavel para entregar uma notificação.
     * @param mensagem a mensagem a ser entregue ao observador
     */
    void atualizar(String mensagem);
}
