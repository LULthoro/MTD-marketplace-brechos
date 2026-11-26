package mtd.memento;

import java.math.BigDecimal;

/**
 * <<Entidade / Memento>> Instantâneo imutável do estado de um Produto.
 *
 * Criado pelo Originador ({@code Produto#criarMemento()}) e custodiado
 * pelo Caretaker ({@code ProdutoCaretaker}) para permitir a restauração
 * do estado anterior após uma atualização.
 *
 * <p>Todos os campos são imutáveis (sem setters), garantindo que o estado
 * salvo não seja alterado após a criação do Memento.</p>
 */
public class ProdutoMemento {

    private final Long id;
    private final String titulo;
    private final String descricao;
    private final BigDecimal preco;
    private final String tamanho;
    private final Long usuarioId;

    public ProdutoMemento(Long id, String titulo, String descricao,
                          BigDecimal preco, String tamanho, Long usuarioId) {
        this.id        = id;
        this.titulo    = titulo;
        this.descricao = descricao;
        this.preco     = preco;
        this.tamanho   = tamanho;
        this.usuarioId = usuarioId;
    }

    public Long getId()           { return id; }
    public String getTitulo()     { return titulo; }
    public String getDescricao()  { return descricao; }
    public BigDecimal getPreco()  { return preco; }
    public String getTamanho()    { return tamanho; }
    public Long getUsuarioId()    { return usuarioId; }

    @Override
    public String toString() {
        return "ProdutoMemento{id=" + id + ", titulo='" + titulo + "', preco=" + preco + "}";
    }
}
