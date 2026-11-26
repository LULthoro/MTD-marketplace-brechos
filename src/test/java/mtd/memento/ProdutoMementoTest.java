package mtd.memento;

import mtd.model.Produto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Memento de Produto (ProdutoMemento + ProdutoCaretaker + Produto Originador)")
class ProdutoMementoTest {

    private Produto produto;

    @BeforeEach
    void setUp() {
        produto = new Produto(1L, "Jaqueta Jeans Vintage",
                "Jaqueta anos 90 conservada", new BigDecimal("120.00"), "M", 1L);
    }

    // =========================================================================
    // ProdutoMemento — estado imutável
    // =========================================================================

    @Nested
    @DisplayName("ProdutoMemento")
    class MementoTest {

        @Test
        @DisplayName("deveCriarMementoComEstadoAtualDoProduto")
        void deveCriarMementoComEstadoAtual() {
            ProdutoMemento memento = produto.criarMemento();
            assertAll(
                    () -> assertEquals(produto.getId(),        memento.getId()),
                    () -> assertEquals(produto.getTitulo(),    memento.getTitulo()),
                    () -> assertEquals(produto.getDescricao(), memento.getDescricao()),
                    () -> assertEquals(produto.getPreco(),     memento.getPreco()),
                    () -> assertEquals(produto.getTamanho(),   memento.getTamanho()),
                    () -> assertEquals(produto.getUsuarioId(), memento.getUsuarioId())
            );
        }

        @Test
        @DisplayName("mementoNaoDeveReflitirAlteracoesPosterioresNoProduto")
        void mementoDeveSerImutavel() {
            ProdutoMemento memento = produto.criarMemento();
            String tituloOriginal = memento.getTitulo();

            // altera o produto original DEPOIS de criar o memento
            produto.setTitulo("Titulo Modificado");

            // o memento não muda — estado permanece congelado
            assertEquals(tituloOriginal, memento.getTitulo());
        }
    }

    // =========================================================================
    // Produto.fromMemento() — restauração
    // =========================================================================

    @Nested
    @DisplayName("Produto.fromMemento")
    class FromMementoTest {

        @Test
        @DisplayName("deveRestaurarProdutoComEstadoDoMemento")
        void deveRestaurarEstado() {
            ProdutoMemento memento = produto.criarMemento();

            // simula uma atualização
            produto.setTitulo("Titulo Novo");
            produto.setPreco(new BigDecimal("50.00"));

            // restaura a partir do memento
            Produto restaurado = Produto.fromMemento(memento);

            assertAll(
                    () -> assertEquals("Jaqueta Jeans Vintage", restaurado.getTitulo()),
                    () -> assertEquals(new BigDecimal("120.00"), restaurado.getPreco()),
                    () -> assertEquals(1L, restaurado.getId())
            );
        }
    }

    // =========================================================================
    // ProdutoCaretaker
    // =========================================================================

    @Nested
    @DisplayName("ProdutoCaretaker")
    class CaretakerTest {

        private ProdutoCaretaker caretaker;

        @BeforeEach
        void setUp() {
            caretaker = new ProdutoCaretaker();
        }

        @Test
        @DisplayName("semMementoSalvo_temMementoDeveSerFalso")
        void semMementoSalvoDeveRetornarFalso() {
            assertFalse(caretaker.temMemento());
            assertNull(caretaker.getUltimoMemento());
        }

        @Test
        @DisplayName("aposGravarMemento_temMementoDeveSerVerdadeiro")
        void aposGravarDeveRetornarVerdadeiro() {
            caretaker.salvarMemento(produto.criarMemento());
            assertTrue(caretaker.temMemento());
            assertNotNull(caretaker.getUltimoMemento());
        }

        @Test
        @DisplayName("deveSobrescreverMementoAnteriorAoGravarNovo")
        void deveSobrescreverMementoAnterior() {
            caretaker.salvarMemento(produto.criarMemento());

            produto.setTitulo("Segundo Estado");
            ProdutoMemento segundo = produto.criarMemento();
            caretaker.salvarMemento(segundo);

            assertEquals("Segundo Estado", caretaker.getUltimoMemento().getTitulo());
        }
    }

    // =========================================================================
    // Fluxo completo: salvar → alterar → restaurar
    // =========================================================================

    @Nested
    @DisplayName("Fluxo Completo Memento")
    class FluxoCompletoTest {

        @Test
        @DisplayName("deveSalvarAlterarERestaurarEstadoAnterior")
        void deveSalvarAlterarERestaurar() {
            ProdutoCaretaker caretaker = new ProdutoCaretaker();

            // 1. Salva estado atual antes de alterar
            caretaker.salvarMemento(produto.criarMemento());

            // 2. Altera o produto
            produto.setTitulo("Titulo Promocional");
            produto.setPreco(new BigDecimal("80.00"));

            // 3. Restaura
            Produto restaurado = Produto.fromMemento(caretaker.getUltimoMemento());

            assertEquals("Jaqueta Jeans Vintage", restaurado.getTitulo());
            assertEquals(new BigDecimal("120.00"), restaurado.getPreco());
        }
    }
}
