package mtd.command.produto;

import mtd.command.Command;
import mtd.service.ProdutoService;

/**
 * <<Controle / Command Concreto>> Comando para deletar um Produto pelo id.
 */
public class DeletarProdutoCommand implements Command {

    private final ProdutoService produtoService;
    private final Long id;

    public DeletarProdutoCommand(ProdutoService produtoService, Long id) {
        this.produtoService = produtoService;
        this.id = id;
    }

    @Override
    public void execute() {
        produtoService.deletarProduto(id);
    }
}
