package mtd.command;

import mtd.command.produto.AtualizarProdutoCommand;
import mtd.command.produto.CadastrarProdutoCommand;
import mtd.command.produto.DeletarProdutoCommand;
import mtd.command.usuario.AdicionarUsuarioCommand;
import mtd.command.usuario.AtualizarUsuarioCommand;
import mtd.command.usuario.DeletarUsuarioCommand;
import mtd.exception.ValidationException;
import mtd.model.Produto;
import mtd.model.Usuario;
import mtd.infra.persistence.ram.ProdutoRepositoryRAM;
import mtd.infra.persistence.ram.UsuarioRepositoryRam;
import mtd.service.ProdutoService;
import mtd.service.UsuarioService;
import mtd.validator.ProdutoValidator;
import mtd.validator.UsuarioValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CommandInvoker e Commands concretos")
class CommandTest {

    private UsuarioService usuarioService;
    private ProdutoService produtoService;
    private CommandInvoker invoker;

    @BeforeEach
    void setUp() {
        usuarioService = new UsuarioService(new UsuarioRepositoryRam(), new UsuarioValidator());
        produtoService = new ProdutoService(new ProdutoRepositoryRAM(), new ProdutoValidator());
        invoker = new CommandInvoker();
    }

    private Usuario novoUsuario() {
        return new Usuario(1, "Lucas", "lucas@email.com", "Senha@123");
    }

    private Produto novoProduto(Long id, String titulo, String preco) {
        return new Produto(id, titulo, "Desc", new BigDecimal(preco), "M", 1L);
    }

    // =========================================================================
    // CommandInvoker
    // =========================================================================

    @Nested
    @DisplayName("CommandInvoker")
    class InvokerTest {

        @Test
        @DisplayName("deveRegistrarComandoNoHistoricoAposExecutar")
        void deveRegistrarComandoNoHistoricoAposExecutar() {
            invoker.executar(new AdicionarUsuarioCommand(usuarioService, novoUsuario()));
            assertEquals(1, invoker.tamanhoHistorico());
        }

        @Test
        @DisplayName("deveAcumularMultiplosComandosNoHistorico")
        void deveAcumularMultiplosComandosNoHistorico() {
            invoker.executar(new AdicionarUsuarioCommand(usuarioService, novoUsuario()));
            invoker.executar(new AtualizarUsuarioCommand(usuarioService,
                    new Usuario(1, "Lucaos", "novo@email.com", "Senha@123")));
            invoker.executar(new DeletarUsuarioCommand(usuarioService, 1));
            assertEquals(3, invoker.tamanhoHistorico());
        }

        @Test
        @DisplayName("deveRetornarUltimoComandoExecutado")
        void deveRetornarUltimoComandoExecutado() {
            Command cmd1 = new AdicionarUsuarioCommand(usuarioService, novoUsuario());
            Command cmd2 = new DeletarUsuarioCommand(usuarioService, 1);
            invoker.executar(cmd1);
            invoker.executar(cmd2);
            assertSame(cmd2, invoker.ultimoComando());
        }

        @Test
        @DisplayName("deveRetornarNullQuandoHistoricoVazio")
        void deveRetornarNullQuandoHistoricoVazio() {
            assertNull(invoker.ultimoComando());
        }
    }

    // =========================================================================
    // Commands de Usuario
    // =========================================================================

    @Nested
    @DisplayName("Commands de Usuario")
    class UsuarioCommands {

        @Test
        @DisplayName("AdicionarUsuarioCommand deve persistir usuario")
        void adicionarDevePersistir() {
            invoker.executar(new AdicionarUsuarioCommand(usuarioService, novoUsuario()));
            assertEquals(1, usuarioService.contarUsuarios());
        }

        @Test
        @DisplayName("AdicionarUsuarioCommand deve propagar ValidationException")
        void adicionarDevePropagarValidacao() {
            Usuario invalido = new Usuario(2, "Nome123", "x@x.com", "Senha@123");
            assertThrows(ValidationException.class,
                    () -> invoker.executar(new AdicionarUsuarioCommand(usuarioService, invalido)));
            assertEquals(0, usuarioService.contarUsuarios());
        }

        @Test
        @DisplayName("AtualizarUsuarioCommand deve atualizar dados")
        void atualizarDeveAlterarDados() {
            usuarioService.adicionarUsuario(novoUsuario());
            invoker.executar(new AtualizarUsuarioCommand(usuarioService,
                    new Usuario(1, "Lucaos", "novo@email.com", "Senha@123")));
            assertEquals("novo@email.com",
                    usuarioService.buscarPorId(1).get().getEmail());
        }

        @Test
        @DisplayName("DeletarUsuarioCommand deve remover usuario")
        void deletarDeveRemover() {
            usuarioService.adicionarUsuario(novoUsuario());
            invoker.executar(new DeletarUsuarioCommand(usuarioService, 1));
            assertEquals(0, usuarioService.contarUsuarios());
        }
    }

    // =========================================================================
    // Commands de Produto
    // =========================================================================

    @Nested
    @DisplayName("Commands de Produto")
    class ProdutoCommands {

        @Test
        @DisplayName("CadastrarProdutoCommand deve persistir e retornar com id")
        void cadastrarDevePersistirERetornarId() {
            CadastrarProdutoCommand cmd = new CadastrarProdutoCommand(
                    produtoService, novoProduto(null, "Jaqueta", "120.00"));
            invoker.executar(cmd);
            assertNotNull(cmd.getProdutoSalvo().getId());
            assertEquals(1, produtoService.contarProdutos());
        }

        @Test
        @DisplayName("CadastrarProdutoCommand deve propagar ValidationException")
        void cadastrarDevePropagarValidacao() {
            Produto invalido = new Produto(1L, "   ", "sem titulo", BigDecimal.ZERO, "M", 1L);
            assertThrows(ValidationException.class,
                    () -> invoker.executar(new CadastrarProdutoCommand(produtoService, invalido)));
        }

        @Test
        @DisplayName("AtualizarProdutoCommand deve alterar dados")
        void atualizarDeveAlterarDados() {
            produtoService.cadastrarProduto(novoProduto(1L, "Jaqueta", "120.00"));
            invoker.executar(new AtualizarProdutoCommand(produtoService,
                    novoProduto(1L, "Jaqueta 90s", "99.90")));
            assertEquals("Jaqueta 90s",
                    produtoService.buscarPorId(1L).get().getTitulo());
        }

        @Test
        @DisplayName("DeletarProdutoCommand deve remover produto")
        void deletarDeveRemover() {
            produtoService.cadastrarProduto(novoProduto(1L, "Jaqueta", "120.00"));
            invoker.executar(new DeletarProdutoCommand(produtoService, 1L));
            assertEquals(0, produtoService.contarProdutos());
        }
    }
}
