package mtd.controller;

import mtd.command.CommandInvoker;
import mtd.command.produto.AtualizarProdutoCommand;
import mtd.command.produto.CadastrarProdutoCommand;
import mtd.command.produto.DeletarProdutoCommand;
import mtd.command.usuario.AdicionarUsuarioCommand;
import mtd.command.usuario.AtualizarUsuarioCommand;
import mtd.command.usuario.DeletarUsuarioCommand;
import mtd.memento.ProdutoCaretaker;
import mtd.memento.ProdutoMemento;
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

import java.util.List;
import java.util.Optional;

/**
 * <<Controle / Facade + Singleton + Invoker>>
 *
 * Ponto único de acesso à camada de negócio. Integra três padrões:
 *
 * <ul>
 *   <li><b>Singleton</b> (double-checked locking): garante instância única.</li>
 *   <li><b>Facade</b>: simplifica o acesso aos serviços e repositórios.</li>
 *   <li><b>Command</b>: todas as operações de mutação são encapsuladas em
 *       objetos Command e despachadas pelo {@link CommandInvoker}, desacoplando
 *       o chamador da lógica concreta de cada operação.</li>
 * </ul>
 *
 * O padrão <b>Memento</b> está integrado à operação {@link #atualizarProduto(Produto)}:
 * o estado anterior é salvo pelo {@link ProdutoCaretaker} antes da execução do
 * Command, permitindo desfazê-lo via {@link #desfazerUltimaAtualizacaoProduto()}.
 */
public class FacadeSingletonController {

    private static volatile FacadeSingletonController instance;

    private final UsuarioService usuarioService;
    private final ProdutoService produtoService;

    /** Invoker do padrão Command — executa e registra cada operação. */
    private final CommandInvoker invoker = new CommandInvoker();

    /** Caretaker do padrão Memento — custodia o último estado de Produto. */
    private final ProdutoCaretaker produtoCaretaker = new ProdutoCaretaker();

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
    // Operacoes CRUD de Usuario — via Command
    // -------------------------------------------------------------------------

    /** Encapsula a adição em {@link AdicionarUsuarioCommand} e despacha ao Invoker. */
    public void adicionarUsuario(Usuario usuario) {
        invoker.executar(new AdicionarUsuarioCommand(usuarioService, usuario));
    }

    public Optional<Usuario> buscarUsuarioPorId(int id) {
        return usuarioService.buscarPorId(id);
    }

    public List<Usuario> listarTodosUsuarios() {
        return usuarioService.listarTodosUsuarios();
    }

    /** Encapsula a atualização em {@link AtualizarUsuarioCommand} e despacha ao Invoker. */
    public void atualizarUsuario(Usuario usuario) {
        invoker.executar(new AtualizarUsuarioCommand(usuarioService, usuario));
    }

    /** Encapsula a remoção em {@link DeletarUsuarioCommand} e despacha ao Invoker. */
    public void deletarUsuario(int id) {
        invoker.executar(new DeletarUsuarioCommand(usuarioService, id));
    }

    // -------------------------------------------------------------------------
    // Operacoes CRUD de Produto — via Command + Memento na atualização
    // -------------------------------------------------------------------------

    /**
     * Encapsula o cadastro em {@link CadastrarProdutoCommand}, executa via
     * Invoker e retorna o Produto com id preenchido.
     */
    public Produto cadastrarProduto(Produto produto) {
        CadastrarProdutoCommand cmd = new CadastrarProdutoCommand(produtoService, produto);
        invoker.executar(cmd);
        return cmd.getProdutoSalvo();
    }

    public Optional<Produto> buscarProdutoPorId(Long id) {
        return produtoService.buscarPorId(id);
    }

    public List<Produto> listarTodosProdutos() {
        return produtoService.listarTodosProdutos();
    }

    /**
     * Padrão <b>Memento</b>: salva o estado atual do Produto no Caretaker
     * <em>antes</em> de executar o Command de atualização, viabilizando
     * o desfazer via {@link #desfazerUltimaAtualizacaoProduto()}.
     */
    public void atualizarProduto(Produto produto) {
        // Memento — captura estado anterior antes de alterar
        produtoService.buscarPorId(produto.getId())
                .ifPresent(atual -> produtoCaretaker.salvarMemento(atual.criarMemento()));

        invoker.executar(new AtualizarProdutoCommand(produtoService, produto));
    }

    /**
     * Padrão <b>Memento — Restaurar</b>: reconstrói o Produto a partir do último
     * Memento salvo pelo Caretaker e persiste o estado anterior no repositório.
     *
     * @throws IllegalStateException se não houver memento disponível para restauração.
     */
    public void desfazerUltimaAtualizacaoProduto() {
        if (!produtoCaretaker.temMemento()) {
            throw new IllegalStateException(
                    "Nao ha atualizacao de produto para desfazer.");
        }
        ProdutoMemento memento = produtoCaretaker.getUltimoMemento();
        Produto restaurado = Produto.fromMemento(memento);
        // Persiste diretamente via service para não sobrescrever o memento salvo
        produtoService.atualizarProduto(restaurado);
    }

    /** Encapsula a remoção em {@link DeletarProdutoCommand} e despacha ao Invoker. */
    public void deletarProduto(Long id) {
        invoker.executar(new DeletarProdutoCommand(produtoService, id));
    }

    // -------------------------------------------------------------------------
    // Metricas consolidadas (Facade)
    // -------------------------------------------------------------------------

    public int getQuantidadeTotalEntidadesCadastradas() {
        return usuarioService.contarUsuarios() + produtoService.contarProdutos();
    }

    // -------------------------------------------------------------------------
    // Acesso ao Invoker (para testes e auditoria)
    // -------------------------------------------------------------------------

    /** Retorna o Invoker para inspeção do histórico de Commands em testes. */
    public CommandInvoker getInvoker() {
        return invoker;
    }
}
