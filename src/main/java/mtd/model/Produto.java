package mtd.model;

import mtd.memento.ProdutoMemento;

import java.math.BigDecimal;

/**
 * <<Entidade / Originador>> Entidade de domínio Produto.
 *
 * Atua como Originador no padrão Memento: pode criar um instantâneo
 * ({@link #criarMemento()}) do seu estado atual e restaurá-lo a partir
 * de um Memento salvo ({@link #restaurarMemento(ProdutoMemento)}).
 */
public class Produto {

    private Long id;
    private String titulo;
    private String descricao;
    private BigDecimal preco;
    private String tamanho;
    private Long usuarioId;

    public Produto() {
    }

    public Produto(Long id, String titulo, String descricao, BigDecimal preco, String tamanho, Long usuarioId) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.preco = preco;
        this.tamanho = tamanho;
        this.usuarioId = usuarioId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public String getTamanho() {
        return tamanho;
    }

    public void setTamanho(String tamanho) {
        this.tamanho = tamanho;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    // -------------------------------------------------------------------------
    // Memento — Originador
    // -------------------------------------------------------------------------

    /**
     * Cria um Memento com o estado atual deste Produto.
     * Deve ser chamado pelo Caretaker (via Facade) <em>antes</em> de qualquer
     * atualização cujo desfazer seja necessário.
     *
     * @return {@link mtd.memento.ProdutoMemento} com cópia imutável do estado.
     */
    public mtd.memento.ProdutoMemento criarMemento() {
        return new mtd.memento.ProdutoMemento(id, titulo, descricao, preco, tamanho, usuarioId);
    }

    /**
     * Cria um Produto a partir de um Memento previamente salvo.
     * Usado pelo Facade para restaurar o estado anterior após um desfazer.
     *
     * @param memento Instantâneo salvo pelo Caretaker.
     * @return Novo Produto com o estado do Memento.
     */
    public static Produto fromMemento(mtd.memento.ProdutoMemento memento) {
        return new Produto(
                memento.getId(),
                memento.getTitulo(),
                memento.getDescricao(),
                memento.getPreco(),
                memento.getTamanho(),
                memento.getUsuarioId()
        );
    }

    @Override
    public String toString() {
        return "Produto{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", descricao='" + descricao + '\'' +
                ", preco=" + preco +
                ", tamanho='" + tamanho + '\'' +
                ", usuarioId=" + usuarioId +
                '}';
    }
}
