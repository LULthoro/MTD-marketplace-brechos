package mtd.validator;

import mtd.exception.ValidacaoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UsuarioValidator")
class UsuarioValidatorTest {

    private UsuarioValidator validator;

    @BeforeEach
    void setUp() {
        validator = new UsuarioValidator();
    }

    // -------------------------------------------------------------------------
    // Validacao de nome
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("validarNome")
    class ValidarNome {

        @Test
        @DisplayName("deve aceitar nome valido")
        void deveAceitarNomeValido() {
            assertDoesNotThrow(() -> validator.validarNome("Lucas"));
        }

        @Test
        @DisplayName("deve rejeitar nome vazio")
        void deveRejeitarNomeVazio() {
            ValidacaoException ex = assertThrows(ValidacaoException.class,
                    () -> validator.validarNome(""));
            assertEquals("O nome nao pode ser vazio.", ex.getMessage());
        }

        @Test
        @DisplayName("deve rejeitar nome nulo")
        void deveRejeitarNomeNulo() {
            assertThrows(ValidacaoException.class,
                    () -> validator.validarNome(null));
        }

        @Test
        @DisplayName("deve rejeitar nome com mais de 12 caracteres")
        void deveRejeitarNomeAcimaDoLimite() {
            ValidacaoException ex = assertThrows(ValidacaoException.class,
                    () -> validator.validarNome("NomeMuitoLongo"));
            assertTrue(ex.getMessage().contains("12 caracteres"));
        }

        @Test
        @DisplayName("deve rejeitar nome com numeros")
        void deveRejeitarNomeComNumeros() {
            ValidacaoException ex = assertThrows(ValidacaoException.class,
                    () -> validator.validarNome("Lucas123"));
            assertEquals("O nome nao pode conter numeros.", ex.getMessage());
        }

        @Test
        @DisplayName("deve aceitar nome com exatamente 12 caracteres")
        void deveAceitarNomeNoLimite() {
            assertDoesNotThrow(() -> validator.validarNome("DozeCaracter"));
        }
    }

    // -------------------------------------------------------------------------
    // Validacao de senha
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("validarSenha")
    class ValidarSenha {

        @Test
        @DisplayName("deve aceitar senha valida")
        void deveAceitarSenhaValida() {
            assertDoesNotThrow(() -> validator.validarSenha("Senha@123", "Lucas", "lucas@email.com"));
        }

        @Test
        @DisplayName("deve rejeitar senha vazia")
        void deveRejeitarSenhaVazia() {
            assertThrows(ValidacaoException.class,
                    () -> validator.validarSenha("", "Lucas", "lucas@email.com"));
        }

        @Test
        @DisplayName("deve rejeitar senha com menos de 8 caracteres")
        void deveRejeitarSenhaCurta() {
            ValidacaoException ex = assertThrows(ValidacaoException.class,
                    () -> validator.validarSenha("Ab1@", "Lucas", "lucas@email.com"));
            assertTrue(ex.getMessage().contains("8 caracteres"));
        }

        @Test
        @DisplayName("deve rejeitar senha com mais de 128 caracteres")
        void deveRejeitarSenhaLonga() {
            String senhaLonga = "Aa1@" + "a".repeat(130);
            assertThrows(ValidacaoException.class,
                    () -> validator.validarSenha(senhaLonga, "Lucas", "lucas@email.com"));
        }

        @Test
        @DisplayName("deve rejeitar senha com menos de 3 tipos de caracteres")
        void deveRejeitarSenhaComPoucosTipos() {
            ValidacaoException ex = assertThrows(ValidacaoException.class,
                    () -> validator.validarSenha("somenteminusculas", "Lucas", "lucas@email.com"));
            assertTrue(ex.getMessage().contains("3"));
        }

        @Test
        @DisplayName("deve rejeitar senha identica ao nome")
        void deveRejeitarSenhaIgualAoNome() {
            // Senha que seria valida em outros aspectos mas igual ao nome
            assertThrows(ValidacaoException.class,
                    () -> validator.validarSenha("Lucas", "Lucas", "lucas@email.com"));
        }

        @Test
        @DisplayName("deve rejeitar senha identica ao email")
        void deveRejeitarSenhaIgualAoEmail() {
            assertThrows(ValidacaoException.class,
                    () -> validator.validarSenha("Ab1@cdef.gh", "Carla", "Ab1@cdef.gh"));
        }

        @Test
        @DisplayName("deve aceitar senha com maiusculas, minusculas e numeros")
        void deveAceitarSenhaComTresTipos() {
            assertDoesNotThrow(() -> validator.validarSenha("Senha123", "Lucas", "lucas@email.com"));
        }
    }
}
