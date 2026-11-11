package mtd.service;

import mtd.model.Produto;
import mtd.repository.ProdutoRepository;
import mtd.validator.ProdutoValidator;

import java.util.List;
import java.util.Optional;

public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final ProdutoValidator produtoValidator;

    public ProdutoService(ProdutoRepository produtoRepository, ProdutoValidator produtoValidator) {
        this.produtoRepository = produtoRepository;
        this.produtoValidator = produtoValidator;
    }

    public Produto cadastrarProduto(Produto produto) {
        produtoValidator.validar(produto);
        return produtoRepository.salvar(produto);
    }

    public Optional<Produto> buscarPorId(Long id) {
        return produtoRepository.buscarPorId(id);
    }

    public List<Produto> listarTodosProdutos() {
        return produtoRepository.listarTodos();
    }

    public void atualizarProduto(Produto produto) {
        produtoValidator.validar(produto);
        produtoRepository.atualizar(produto);
    }

    public void deletarProduto(Long id) {
        produtoRepository.deletar(id);
    }

    public int contarProdutos() {
        return produtoRepository.contar();
    }
}

