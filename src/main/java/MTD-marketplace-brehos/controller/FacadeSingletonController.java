package mtd.controller;

import mtd.exception.DatabaseOperationException;
import mtd.model.Produto;
import mtd.model.Usuario;
import mtd.repository.ProdutoRepository;
import mtd.repository.ProdutoRepositoryRAM;
import mtd.repository.ProdutoRepositorySQLite;
import mtd.repository.UsuarioRepository;
import mtd.repository.UsuarioRepositoryRam;
import mtd.repository.UsuarioRepositorySQLite;
import mtd.service.ProdutoService;
import mtd.service.UsuarioService;
import mtd.validator.ProdutoValidator;
import mtd.validator.UsuarioValidator;

import java.sql.SQLException;
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

    public static FacadeSingletonController getInstance(String modo) {
        if (instance == null) {
            synchronized (FacadeSingletonController.class) {
                if (instance == null) {
                    boolean usarBanco = "bd".equalsIgnoreCase(modo);
                    UsuarioRepository usuarioRepo = usarBanco
                            ? new UsuarioRepositorySQLite()
                            : new UsuarioRepositoryRam();
                    ProdutoRepository produtoRepo = usarBanco
                            ? new ProdutoRepositorySQLite()
                            : new ProdutoRepositoryRAM();

                    UsuarioService uService = new UsuarioService(usuarioRepo, new UsuarioValidator());
                    ProdutoService pService = new ProdutoService(produtoRepo, new ProdutoValidator());

                    instance = new FacadeSingletonController(uService, pService);
                }
            }
        }
        return instance;
    }

    public static FacadeSingletonController getInstance() {
        return getInstance("ram");
    }

    // -------------------------------------------------------------------------
    // Operacoes de Usuario (Facade)
    // -------------------------------------------------------------------------

    public void adicionarUsuario(Usuario usuario) {
        try {
            usuarioService.adicionarUsuario(usuario);
        } catch (SQLException e) {
            throw new DatabaseOperationException("Erro ao adicionar usuario.", e);
        }
    }

    public List<Usuario> listarTodosUsuarios() {
        try {
            return usuarioService.listarTodosUsuarios();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Erro ao listar usuarios.", e);
        }
    }

    // -------------------------------------------------------------------------
    // Operacoes de Produto (Facade)
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
        return listarTodosUsuarios().size() + produtoService.contarProdutos();
    }
}

