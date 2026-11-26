package mtd.repository;

import mtd.model.Produto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ProdutoRepositoryRAM")
class ProdutoRepositoryRAMTest {

    private ProdutoRepositoryRAM repository;

    @BeforeEach
    void setUp() {
        repository = new ProdutoRepositoryRAM();
    }

    private Produto novoProduto(Long id, String titulo) {
        return new Produto(id, titulo, "Descrição", new BigDecimal("99.90"), "M", 1L);
    }

    @Test
    @DisplayName("deveSalvarERecuperarPorId")
    void deveSalvarERecuperarPorId() {
        Produto salvo = repository.salvar(novoProduto(1L, "Jaqueta Jeans"));
        Optional<Produto> resultado = repository.buscarPorId(salvo.getId());
        assertTrue(resultado.isPresent());
        assertEquals("Jaqueta Jeans", resultado.get().getTitulo());
    }

    @Test
    @DisplayName("deveGerarIdAutomaticoQuandoIdNulo")
    void deveGerarIdAutomaticoQuandoIdNulo() {
        Produto semId = new Produto(null, "Blusa", "Sem ID",
                new BigDecimal("45.00"), "P", 1L);
        Produto salvo = repository.salvar(semId);
        assertNotNull(salvo.getId());
    }

    @Test
    @DisplayName("deveGerarIdsSequenciaisParaProdutosSemId")
    void deveGerarIdsSequenciaisParaProdutosSemId() {
        Produto p1 = repository.salvar(new Produto(null, "P1", "d", new BigDecimal("10"), "P", 1L));
        Produto p2 = repository.salvar(new Produto(null, "P2", "d", new BigDecimal("20"), "M", 1L));
        assertNotEquals(p1.getId(), p2.getId());
    }

    @Test
    @DisplayName("deveRetornarVazioParaIdNulo")
    void deveRetornarVazioParaIdNulo() {
        assertTrue(repository.buscarPorId(null).isEmpty());
    }

    @Test
    @DisplayName("deveRetornarVazioParaIdInexistente")
    void deveRetornarVazioParaIdInexistente() {
        assertTrue(repository.buscarPorId(999L).isEmpty());
    }

    @Test
    @DisplayName("deveListarTodosOsProdutos")
    void deveListarTodosOsProdutos() {
        repository.salvar(novoProduto(1L, "Jaqueta"));
        repository.salvar(novoProduto(2L, "Camisa"));
        List<Produto> lista = repository.listarTodos();
        assertEquals(2, lista.size());
    }

    @Test
    @DisplayName("deveRetornarListaVaziaQuandoVazio")
    void deveRetornarListaVaziaQuandoVazio() {
        assertTrue(repository.listarTodos().isEmpty());
    }

    @Test
    @DisplayName("deveAtualizarProdutoExistente")
    void deveAtualizarProdutoExistente() {
        repository.salvar(novoProduto(1L, "Jaqueta Jeans"));
        Produto atualizado = new Produto(1L, "Jaqueta Jeans 90s",
                "Versão nova", new BigDecimal("95.00"), "M", 1L);
        repository.atualizar(atualizado);
        Optional<Produto> resultado = repository.buscarPorId(1L);
        assertTrue(resultado.isPresent());
        assertEquals("Jaqueta Jeans 90s", resultado.get().getTitulo());
    }

    @Test
    @DisplayName("deveIgnorarAtualizacaoDeIdInexistente")
    void deveIgnorarAtualizacaoDeIdInexistente() {
        assertDoesNotThrow(() ->
                repository.atualizar(new Produto(99L, "Fantasma", "d",
                        new BigDecimal("10"), "P", 1L)));
        assertEquals(0, repository.contar());
    }

    @Test
    @DisplayName("deveDeletarProdutoExistente")
    void deveDeletarProdutoExistente() {
        repository.salvar(novoProduto(1L, "Jaqueta"));
        repository.deletar(1L);
        assertTrue(repository.buscarPorId(1L).isEmpty());
        assertEquals(0, repository.contar());
    }

    @Test
    @DisplayName("deveDeletarIdNuloSemErro")
    void deveDeletarIdNuloSemErro() {
        assertDoesNotThrow(() -> repository.deletar(null));
    }

    @Test
    @DisplayName("deveContarProdutosCorretamente")
    void deveContarProdutosCorretamente() {
        assertEquals(0, repository.contar());
        repository.salvar(novoProduto(1L, "Jaqueta"));
        repository.salvar(novoProduto(2L, "Camisa"));
        assertEquals(2, repository.contar());
    }
}

