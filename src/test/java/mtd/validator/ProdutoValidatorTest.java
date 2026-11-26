package mtd.validator;

import mtd.exception.ValidationException;
import mtd.model.Produto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ProdutoValidator")
class ProdutoValidatorTest {

    private ProdutoValidator validator;

    @BeforeEach
    void setUp() {
        validator = new ProdutoValidator();
    }

    // =========================================================================
    // validarTitulo
    // =========================================================================

    @Nested
    @DisplayName("ValidarTitulo")
    class ValidarTitulo {

        @Test
        @DisplayName("deveAceitarTituloValido")
        void deveAceitarTituloValido() {
            assertDoesNotThrow(() -> validator.validarTitulo("Jaqueta Jeans Vintage"));
        }

        @Test
        @DisplayName("deveRejeitarTituloNulo")
        void deveRejeitarTituloNulo() {
            ValidationException ex = assertThrows(ValidationException.class,
                    () -> validator.validarTitulo(null));
            assertTrue(ex.getMessage().contains("vazio"));
        }

        @Test
        @DisplayName("deveRejeitarTituloVazio")
        void deveRejeitarTituloVazio() {
            ValidationException ex = assertThrows(ValidationException.class,
                    () -> validator.validarTitulo("   "));
            assertTrue(ex.getMessage().contains("vazio"));
        }

        @Test
        @DisplayName("deveRejeitarTituloStringVazia")
        void deveRejeitarTituloStringVazia() {
            ValidationException ex = assertThrows(ValidationException.class,
                    () -> validator.validarTitulo(""));
            assertTrue(ex.getMessage().contains("vazio"));
        }
    }

    // =========================================================================
    // validarPreco
    // =========================================================================

    @Nested
    @DisplayName("ValidarPreco")
    class ValidarPreco {

        @Test
        @DisplayName("deveAceitarPrecoPositivo")
        void deveAceitarPrecoPositivo() {
            assertDoesNotThrow(() -> validator.validarPreco(new BigDecimal("120.00")));
        }

        @Test
        @DisplayName("deveAceitarPrecoMinimo")
        void deveAceitarPrecoMinimo() {
            assertDoesNotThrow(() -> validator.validarPreco(new BigDecimal("0.01")));
        }

        @Test
        @DisplayName("deveRejeitarPrecoNulo")
        void deveRejeitarPrecoNulo() {
            ValidationException ex = assertThrows(ValidationException.class,
                    () -> validator.validarPreco(null));
            assertTrue(ex.getMessage().contains("maior que zero"));
        }

        @Test
        @DisplayName("deveRejeitarPrecoZero")
        void deveRejeitarPrecoZero() {
            ValidationException ex = assertThrows(ValidationException.class,
                    () -> validator.validarPreco(BigDecimal.ZERO));
            assertTrue(ex.getMessage().contains("maior que zero"));
        }

        @Test
        @DisplayName("deveRejeitarPrecoNegativo")
        void deveRejeitarPrecoNegativo() {
            ValidationException ex = assertThrows(ValidationException.class,
                    () -> validator.validarPreco(new BigDecimal("-10.00")));
            assertTrue(ex.getMessage().contains("maior que zero"));
        }
    }

    // =========================================================================
    // validar(Produto) — método raiz
    // =========================================================================

    @Nested
    @DisplayName("ValidarProduto")
    class ValidarProduto {

        @Test
        @DisplayName("deveRejeitarProdutoNulo")
        void deveRejeitarProdutoNulo() {
            ValidationException ex = assertThrows(ValidationException.class,
                    () -> validator.validar(null));
            assertTrue(ex.getMessage().contains("nulo"));
        }

        @Test
        @DisplayName("deveAceitarProdutoComDadosValidos")
        void deveAceitarProdutoComDadosValidos() {
            Produto p = new Produto(1L, "Vestido Floral", "Estampa exclusiva",
                    new BigDecimal("89.90"), "P", 2L);
            assertDoesNotThrow(() -> validator.validar(p));
        }
    }
}

