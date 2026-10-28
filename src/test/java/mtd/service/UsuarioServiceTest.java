package mtd.service;

import mtd.exception.ValidacaoException;
import mtd.model.Usuario;
import mtd.repository.UsuarioRepositoryRam;
import mtd.validator.UsuarioValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UsuarioService")
class UsuarioServiceTest {

    private UsuarioService usuarioService;

    @BeforeEach
    void setUp() {
        usuarioService = new UsuarioService(
                new UsuarioRepositoryRam(),
                new UsuarioValidator()
        );
    }

    @Test
    @DisplayName("deve cadastrar usuario com dados validos")
    void deveCadastrarUsuarioComDadosValidos() throws Exception {
        Usuario usuario = new Usuario(1, "Lucas", "lucas@email.com", "Senha@123");

        usuarioService.adicionarUsuario(usuario);

        List<Usuario> usuarios = usuarioService.listarTodosUsuarios();
        assertEquals(1, usuarios.size());
        assertEquals("Lucas", usuarios.get(0).getNome());
    }

    @Test
    @DisplayName("deve rejeitar usuario com nome invalido e nao persistir")
    void deveRejeitarNomeInvalidoENaoPersistir() {
        Usuario usuario = new Usuario(1, "Lucas123", "lucas@email.com", "Senha@123");

        assertThrows(ValidacaoException.class,
                () -> usuarioService.adicionarUsuario(usuario));

        assertDoesNotThrow(() -> {
            List<Usuario> usuarios = usuarioService.listarTodosUsuarios();
            assertTrue(usuarios.isEmpty(), "Nenhum usuario deveria ter sido salvo.");
        });
    }

    @Test
    @DisplayName("deve rejeitar usuario com senha invalida e nao persistir")
    void deveRejeitarSenhaInvalidaENaoPersistir() {
        Usuario usuario = new Usuario(1, "Lucas", "lucas@email.com", "fraca");

        assertThrows(ValidacaoException.class,
                () -> usuarioService.adicionarUsuario(usuario));

        assertDoesNotThrow(() -> {
            List<Usuario> usuarios = usuarioService.listarTodosUsuarios();
            assertTrue(usuarios.isEmpty(), "Nenhum usuario deveria ter sido salvo.");
        });
    }

    @Test
    @DisplayName("deve cadastrar multiplos usuarios validos")
    void deveCadastrarMultiplosUsuariosValidos() throws Exception {
        usuarioService.adicionarUsuario(new Usuario(1, "Lucas", "lucas@email.com", "Senha@123"));
        usuarioService.adicionarUsuario(new Usuario(2, "Maria", "maria@email.com", "Mar1a#Secret"));

        List<Usuario> usuarios = usuarioService.listarTodosUsuarios();
        assertEquals(2, usuarios.size());
    }
}
