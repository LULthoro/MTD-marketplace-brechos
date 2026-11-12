package mtd.validator;

import mtd.exception.ValidationException;
import mtd.model.Usuario;
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

    // =========================================================================
    // validarNome
    // =========================================================================

    @Nested
    @DisplayName("ValidarNome")
    class ValidarNome {

        @Test
        @DisplayName("deveAceitarNomeValido")
        void deveAceitarNomeValido() {
            assertDoesNotThrow(() -> validator.validarNome("Lucas"));
        }

        @Test
        @DisplayName("deveAceitarNomeNoLimite")
        void deveAceitarNomeNoLimite() {
            // exatamente 12 caracteres — deve ser aceito
            assertDoesNotThrow(() -> validator.validarNome("NomeNoLimite"));
        }

        @Test
        @DisplayName("deveRejeitarNomeNulo")
        void deveRejeitarNomeNulo() {
            ValidationException ex = assertThrows(ValidationException.class,
                    () -> validator.validarNome(null));
            assertTrue(ex.getMessage().contains("vazio"));
        }

        @Test
        @DisplayName("deveRejeitarNomeVazio")
        void deveRejeitarNomeVazio() {
            ValidationException ex = assertThrows(ValidationException.class,
                    () -> validator.validarNome("   "));
            assertTrue(ex.getMessage().contains("vazio"));
        }

        @Test
        @DisplayName("deveRejeitarNomeAcimaDoLimite")
        void deveRejeitarNomeAcimaDoLimite() {
            // 13 caracteres — deve ser rejeitado
            ValidationException ex = assertThrows(ValidationException.class,
                    () -> validator.validarNome("NomeMuitoLong"));
            assertTrue(ex.getMessage().contains("12"));
        }

        @Test
        @DisplayName("deveRejeitarNomeComNumeros")
        void deveRejeitarNomeComNumeros() {
            ValidationException ex = assertThrows(ValidationException.class,
                    () -> validator.validarNome("Lucas123"));
            assertTrue(ex.getMessage().contains("numeros"));
        }
    }

    // =========================================================================
    // validarSenha
    // =========================================================================

    @Nested
    @DisplayName("ValidarSenha")
    class ValidarSenha {

        private static final String NOME_PADRAO  = "Lucas";
        private static final String EMAIL_PADRAO = "lucas@email.com";

        @Test
        @DisplayName("deveAceitarSenhaValida")
        void deveAceitarSenhaValida() {
            assertDoesNotThrow(() ->
                    validator.validarSenha("Senha@123", NOME_PADRAO, EMAIL_PADRAO));
        }

        @Test
        @DisplayName("deveAceitarSenhaComTresTipos")
        void deveAceitarSenhaComTresTipos() {
            // maiúsculas + minúsculas + números (sem especial) = 3 tipos → válida
            assertDoesNotThrow(() ->
                    validator.validarSenha("Abcdef12", NOME_PADRAO, EMAIL_PADRAO));
        }

        @Test
        @DisplayName("deveRejeitarSenhaVazia")
        void deveRejeitarSenhaVazia() {
            ValidationException ex = assertThrows(ValidationException.class,
                    () -> validator.validarSenha("   ", NOME_PADRAO, EMAIL_PADRAO));
            assertTrue(ex.getMessage().contains("vazia"));
        }

        @Test
        @DisplayName("deveRejeitarSenhaCurta")
        void deveRejeitarSenhaCurta() {
            ValidationException ex = assertThrows(ValidationException.class,
                    () -> validator.validarSenha("Ab1@", NOME_PADRAO, EMAIL_PADRAO));
            assertTrue(ex.getMessage().contains("8"));
        }

        @Test
        @DisplayName("deveRejeitarSenhaLonga")
        void deveRejeitarSenhaLonga() {
            String senhaLonga = "A1@" + "a".repeat(127); // 130 chars
            ValidationException ex = assertThrows(ValidationException.class,
                    () -> validator.validarSenha(senhaLonga, NOME_PADRAO, EMAIL_PADRAO));
            assertTrue(ex.getMessage().contains("128"));
        }

        @Test
        @DisplayName("deveRejeitarSenhaComPoucosTipos")
        void deveRejeitarSenhaComPoucosTipos() {
            // apenas minúsculas = 1 tipo → rejeitada
            ValidationException ex = assertThrows(ValidationException.class,
                    () -> validator.validarSenha("somenteminusc", NOME_PADRAO, EMAIL_PADRAO));
            assertTrue(ex.getMessage().contains("3"));
        }

        @Test
        @DisplayName("deveRejeitarSenhaIgualAoNome")
        void deveRejeitarSenhaIgualAoNome() {
            // "Senha@123" tem 9 chars, 4 tipos (Maiusc+Minusc+Num+Especial), passa na forca
            // mas é igual ao nome informado → deve ser rejeitada
            ValidationException ex = assertThrows(ValidationException.class,
                    () -> validator.validarSenha("Senha@123", "Senha@123", EMAIL_PADRAO));
            assertTrue(ex.getMessage().contains("nome"));
        }

        @Test
        @DisplayName("deveRejeitarSenhaIgualAoEmail")
        void deveRejeitarSenhaIgualAoEmail() {
            ValidationException ex = assertThrows(ValidationException.class,
                    () -> validator.validarSenha("Ab1@cdef.gh", NOME_PADRAO, "Ab1@cdef.gh"));
            assertTrue(ex.getMessage().contains("email"));
        }
    }

    // =========================================================================
    // validar(Usuario) — método raiz
    // =========================================================================

    @Nested
    @DisplayName("ValidarUsuario")
    class ValidarUsuario {

        @Test
        @DisplayName("deveRejeitarUsuarioNulo")
        void deveRejeitarUsuarioNulo() {
            ValidationException ex = assertThrows(ValidationException.class,
                    () -> validator.validar(null));
            assertTrue(ex.getMessage().contains("nulo"));
        }

        @Test
        @DisplayName("deveAceitarUsuarioComDadosValidos")
        void deveAceitarUsuarioComDadosValidos() {
            Usuario u = new Usuario(1, "Maria", "maria@email.com", "Mar1a#Secret");
            assertDoesNotThrow(() -> validator.validar(u));
        }
    }
}
