package mtd.memento;

/**
 * <<Controle / Caretaker>> Custodiante do último Memento de Produto.
 *
 * Guarda apenas o Memento da <strong>última</strong> atualização realizada,
 * conforme o requisito de desfazer somente a operação mais recente.
 * Não conhece nem manipula o conteúdo interno do Memento.
 */
public class ProdutoCaretaker {

    private ProdutoMemento ultimoMemento;

    /**
     * Armazena o Memento do estado atual do Produto, substituindo o anterior.
     * Deve ser chamado <em>antes</em> de executar {@code AtualizarProdutoCommand}.
     *
     * @param memento Instantâneo do Produto antes da atualização.
     */
    public void salvarMemento(ProdutoMemento memento) {
        this.ultimoMemento = memento;
    }

    /**
     * Retorna o último Memento salvo.
     *
     * @return Último {@link ProdutoMemento}, ou {@code null} se nenhuma
     *         atualização foi realizada ainda.
     */
    public ProdutoMemento getUltimoMemento() {
        return ultimoMemento;
    }

    /**
     * Indica se existe um Memento disponível para restauração.
     */
    public boolean temMemento() {
        return ultimoMemento != null;
    }
}
