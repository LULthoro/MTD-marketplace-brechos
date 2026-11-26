package mtd.service;

import mtd.exception.ValidationException;
import mtd.model.Produto;
import mtd.repository.ProdutoRepositoryRAM;
import mtd.validator.ProdutoValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ProdutoService")
class ProdutoServiceTest {

    private ProdutoService service;

    @BeforeEach
    void setUp() {
        service = new ProdutoService(new ProdutoRepositoryRAM(), new ProdutoValidator());
    }

    private Produto novoProduto(Long id, String titulo, String preco) {
        return new Produto(id, titulo, "Descrição do item", new BigDecimal(preco), "M", 1L);
    }

    // =========================================================================
    // cadastrarProduto
    // =========================================================================

    @Test
    @DisplayName("deveCadastrarProdutoComDadosValidos")
    void deveCadastrarProdutoComDadosValidos() {
        Produto salvo = service.cadastrarProduto(novoProduto(1L, "Jaqueta Jeans", "120.00"));
        assertNotNull(salvo.getId());
        assertEquals("Jaqueta Jeans", salvo.getTitulo());
        assertEquals(1, service.contarProdutos());
    }

    @Test
    @DisplayName("deveCadastrarMultiplosProdutosValidos")
    void deveCadastrarMultiplosProdutosValidos() {
        service.cadastrarProduto(novoProduto(1L, "Jaqueta Jeans", "120.00"));
        service.cadastrarProduto(novoProduto(2L, "Camisa Flanelada", "65.50"));
        service.cadastrarProduto(novoProduto(3L, "Vestido Floral", "89.90"));
        assertEquals(3, service.contarProdutos());
    }

    @Test
    @DisplayName("deveRejeitarTituloVazioENaoPersistir")
    void deveRejeitarTituloVazioENaoPersistir() {
        Produto invalido = new Produto(4L, "   ", "Sem título", new BigDecimal("50.00"), "M", 1L);
        assertThrows(ValidationException.class, () -> service.cadastrarProduto(invalido));
        assertEquals(0, service.contarProdutos());
    }

    @Test
    @DisplayName("deveRejeitarPrecoNegativoENaoPersistir")
    void deveRejeitarPrecoNegativoENaoPersistir() {
        Produto invalido = new Produto(5L, "Calca Cargo", "Preço inválido",
                new BigDecimal("-10.00"), "40", 2L);
        assertThrows(ValidationException.class, () -> service.cadastrarProduto(invalido));
        assertEquals(0, service.contarProdutos());
    }

    @Test
    @DisplayName("deveRejeitarPrecoZeroENaoPersistir")
    void deveRejeitarPrecoZeroENaoPersistir() {
        Produto invalido = new Produto(6L, "Blusa", "Preço zero",
                BigDecimal.ZERO, "P", 1L);
        assertThrows(ValidationException.class, () -> service.cadastrarProduto(invalido));
        assertEquals(0, service.contarProdutos());
    }

    @Test
    @DisplayName("deveGerarIdAutomaticoQuandoIdNulo")
    void deveGerarIdAutomaticoQuandoIdNulo() {
        Produto semId = new Produto(null, "Blusa Boho", "Sem ID",
                new BigDecimal("45.00"), "P", 1L);
        Produto salvo = service.cadastrarProduto(semId);
        assertNotNull(salvo.getId());
    }

    // =========================================================================
    // buscarPorId
    // =========================================================================

    @Test
    @DisplayName("deveBuscarProdutoCadastradoPorId")
    void deveBuscarProdutoCadastradoPorId() {
        service.cadastrarProduto(novoProduto(10L, "Calça Cargo", "80.00"));
        Optional<Produto> resultado = service.buscarPorId(10L);
        assertTrue(resultado.isPresent());
        assertEquals("Calça Cargo", resultado.get().getTitulo());
    }

    @Test
    @DisplayName("deveRetornarVazioParaIdInexistente")
    void deveRetornarVazioParaIdInexistente() {
        assertTrue(service.buscarPorId(999L).isEmpty());
    }

    // =========================================================================
    // listarTodosProdutos
    // =========================================================================

    @Test
    @DisplayName("deveListarTodosProdutosCadastrados")
    void deveListarTodosProdutosCadastrados() {
        service.cadastrarProduto(novoProduto(1L, "Jaqueta", "120.00"));
        service.cadastrarProduto(novoProduto(2L, "Camisa", "65.50"));
        List<Produto> lista = service.listarTodosProdutos();
        assertEquals(2, lista.size());
    }

    @Test
    @DisplayName("deveRetornarListaVaziaQuandoNaoHaProdutos")
    void deveRetornarListaVaziaQuandoNaoHaProdutos() {
        assertTrue(service.listarTodosProdutos().isEmpty());
    }

    // =========================================================================
    // atualizarProduto
    // =========================================================================

    @Test
    @DisplayName("deveAtualizarProdutoExistente")
    void deveAtualizarProdutoExistente() {
        service.cadastrarProduto(novoProduto(1L, "Jaqueta Jeans", "120.00"));
        service.atualizarProduto(new Produto(1L, "Jaqueta Jeans 90s",
                "Versão atualizada", new BigDecimal("99.90"), "M", 1L));
        Optional<Produto> atualizado = service.buscarPorId(1L);
        assertTrue(atualizado.isPresent());
        assertEquals("Jaqueta Jeans 90s", atualizado.get().getTitulo());
        assertEquals(new BigDecimal("99.90"), atualizado.get().getPreco());
    }

    @Test
    @DisplayName("deveRejeitarAtualizacaoComPrecoInvalido")
    void deveRejeitarAtualizacaoComPrecoInvalido() {
        service.cadastrarProduto(novoProduto(1L, "Jaqueta Jeans", "120.00"));
        assertThrows(ValidationException.class, () ->
                service.atualizarProduto(new Produto(1L, "Jaqueta Jeans",
                        "Preço ruim", BigDecimal.ZERO, "M", 1L)));
    }

    // =========================================================================
    // deletarProduto
    // =========================================================================

    @Test
    @DisplayName("deveDeletarProdutoExistente")
    void deveDeletarProdutoExistente() {
        service.cadastrarProduto(novoProduto(1L, "Jaqueta", "120.00"));
        service.deletarProduto(1L);
        assertEquals(0, service.contarProdutos());
    }

    @Test
    @DisplayName("deveDeletarIdInexistenteSemErro")
    void deveDeletarIdInexistenteSemErro() {
        assertDoesNotThrow(() -> service.deletarProduto(999L));
    }

    // =========================================================================
    // contarProdutos
    // =========================================================================

    @Test
    @DisplayName("deveContarCorretamenteAposOperacoes")
    void deveContarCorretamenteAposOperacoes() {
        assertEquals(0, service.contarProdutos());
        service.cadastrarProduto(novoProduto(1L, "Jaqueta", "120.00"));
        assertEquals(1, service.contarProdutos());
        service.deletarProduto(1L);
        assertEquals(0, service.contarProdutos());
    }
}

