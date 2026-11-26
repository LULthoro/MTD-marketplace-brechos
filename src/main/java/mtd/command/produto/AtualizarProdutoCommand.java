package mtd.command.produto;

import mtd.command.Command;
import mtd.model.Produto;
import mtd.service.ProdutoService;

/**
 * <<Controle / Command Concreto>> Comando para atualizar um Produto existente.
 *
 * Integrado ao padrão Memento: o estado anterior do Produto deve ser
 * salvo pelo Caretaker (ProdutoCaretaker) antes da execução deste comando,
 * viabilizando o desfazer via {@code FacadeSingletonController#desfazerUltimaAtualizacaoProduto()}.
 */
public class AtualizarProdutoCommand implements Command {

    private final ProdutoService produtoService;
    private final Produto produto;

    public AtualizarProdutoCommand(ProdutoService produtoService, Produto produto) {
        this.produtoService = produtoService;
        this.produto = produto;
    }

    @Override
    public void execute() {
        produtoService.atualizarProduto(produto);
    }
}
