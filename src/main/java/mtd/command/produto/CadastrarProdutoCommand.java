package mtd.command.produto;

import mtd.command.Command;
import mtd.model.Produto;
import mtd.service.ProdutoService;

/**
 * <<Controle / Command Concreto>> Comando para cadastrar um novo Produto.
 *
 * Armazena o produto retornado (com id gerado) após a execução,
 * permitindo que o chamador o recupere via {@link #getProdutoSalvo()}.
 */
public class CadastrarProdutoCommand implements Command {

    private final ProdutoService produtoService;
    private final Produto produto;
    private Produto produtoSalvo;

    public CadastrarProdutoCommand(ProdutoService produtoService, Produto produto) {
        this.produtoService = produtoService;
        this.produto = produto;
    }

    @Override
    public void execute() {
        this.produtoSalvo = produtoService.cadastrarProduto(produto);
    }

    /** Retorna o Produto com id preenchido após {@link #execute()}. */
    public Produto getProdutoSalvo() {
        return produtoSalvo;
    }
}
