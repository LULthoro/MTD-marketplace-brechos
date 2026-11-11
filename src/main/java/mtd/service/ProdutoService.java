package mtd.service;

import mtd.infra.log.JavaUtilLoggerAdapter;
import mtd.model.Produto;
import mtd.repository.ProdutoRepository;
import mtd.service.log.AppLogger;
import mtd.validator.ProdutoValidator;

import java.util.List;
import java.util.Optional;

public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final ProdutoValidator produtoValidator;
    private final AppLogger logger;

    public ProdutoService(ProdutoRepository produtoRepository, ProdutoValidator produtoValidator, AppLogger logger) {
        this.produtoRepository = produtoRepository;
        this.produtoValidator = produtoValidator;
        this.logger = logger;
    }

    public ProdutoService(ProdutoRepository produtoRepository, ProdutoValidator produtoValidator) {
        this(produtoRepository, produtoValidator, new JavaUtilLoggerAdapter("ProdutoService"));
    }

    public Produto cadastrarProduto(Produto produto) {
        try {
            produtoValidator.validar(produto);
            Produto salvo = produtoRepository.salvar(produto);
            logger.info("CADASTRO_PRODUTO", "Produto cadastrado: " + salvo.getTitulo() + " (ID=" + salvo.getId() + ")");
            return salvo;
        } catch (RuntimeException e) {
            logger.aviso("VALIDACAO_PRODUTO", "Falha ao cadastrar produto: " + e.getMessage());
            throw e;
        }
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
        logger.info("ATUALIZAR_PRODUTO", "Produto ID " + produto.getId() + " atualizado.");
    }

    public void deletarProduto(Long id) {
        produtoRepository.deletar(id);
        logger.info("DELETAR_PRODUTO", "Produto ID " + id + " removido do catalogo.");
    }

    public int contarProdutos() {
        return produtoRepository.contar();
    }
}
