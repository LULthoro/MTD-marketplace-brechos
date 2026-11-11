package mtd.controller;

import mtd.factory.RepositoryFactory;
import mtd.model.Produto;
import mtd.model.Usuario;
import mtd.repository.ProdutoRepository;
import mtd.repository.UsuarioRepository;
import mtd.service.ProdutoService;
import mtd.service.UsuarioService;
import mtd.validator.ProdutoValidator;
import mtd.validator.UsuarioValidator;

import java.util.List;
import java.util.Optional;

public class FacadeSingletonController {

    private static volatile FacadeSingletonController instance;

    private final UsuarioService usuarioService;
    private final ProdutoService produtoService;

    private FacadeSingletonController(UsuarioService usuarioService, ProdutoService produtoService) {
        this.usuarioService = usuarioService;
        this.produtoService = produtoService;
    }

    public static FacadeSingletonController getInstance(UsuarioService usuarioService, ProdutoService produtoService) {
        if (instance == null) {
            synchronized (FacadeSingletonController.class) {
                if (instance == null) {
                    instance = new FacadeSingletonController(usuarioService, produtoService);
                }
            }
        }
        return instance;
    }

    public static FacadeSingletonController getInstance(RepositoryFactory repositoryFactory) {
        if (instance == null) {
            synchronized (FacadeSingletonController.class) {
                if (instance == null) {
                    UsuarioRepository usuarioRepo = repositoryFactory.criarUsuarioRepository();
                    ProdutoRepository produtoRepo = repositoryFactory.criarProdutoRepository();

                    UsuarioService uService = new UsuarioService(usuarioRepo, new UsuarioValidator());
                    ProdutoService pService = new ProdutoService(produtoRepo, new ProdutoValidator());

                    instance = new FacadeSingletonController(uService, pService);
                }
            }
        }
        return instance;
    }

    public static FacadeSingletonController getInstance(String modo) {
        return getInstance(RepositoryFactory.selecionarFactory(modo));
    }

    public static FacadeSingletonController getInstance() {
        return getInstance("ram");
    }

    // -------------------------------------------------------------------------
    // Operacoes CRUD de Usuario (Facade)
    // -------------------------------------------------------------------------

    public void adicionarUsuario(Usuario usuario) {
        usuarioService.adicionarUsuario(usuario);
    }

    public Optional<Usuario> buscarUsuarioPorId(int id) {
        return usuarioService.buscarPorId(id);
    }

    public List<Usuario> listarTodosUsuarios() {
        return usuarioService.listarTodosUsuarios();
    }

    public void atualizarUsuario(Usuario usuario) {
        usuarioService.atualizarUsuario(usuario);
    }

    public void deletarUsuario(int id) {
        usuarioService.deletarUsuario(id);
    }

    // -------------------------------------------------------------------------
    // Operacoes CRUD de Produto (Facade)
    // -------------------------------------------------------------------------

    public Produto cadastrarProduto(Produto produto) {
        return produtoService.cadastrarProduto(produto);
    }

    public Optional<Produto> buscarProdutoPorId(Long id) {
        return produtoService.buscarPorId(id);
    }

    public List<Produto> listarTodosProdutos() {
        return produtoService.listarTodosProdutos();
    }

    public void atualizarProduto(Produto produto) {
        produtoService.atualizarProduto(produto);
    }

    public void deletarProduto(Long id) {
        produtoService.deletarProduto(id);
    }

    // -------------------------------------------------------------------------
    // Metricas consolidadas (Facade)
    // -------------------------------------------------------------------------

    public int getQuantidadeTotalEntidadesCadastradas() {
        return usuarioService.contarUsuarios() + produtoService.contarProdutos();
    }
}
