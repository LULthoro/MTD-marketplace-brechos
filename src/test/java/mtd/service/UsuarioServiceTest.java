package mtd.service;

import mtd.exception.ValidationException;
import mtd.model.Usuario;
import mtd.repository.UsuarioRepositoryRam;
import mtd.validator.UsuarioValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UsuarioService")
class UsuarioServiceTest {

    private UsuarioService service;

    @BeforeEach
    void setUp() {
        service = new UsuarioService(new UsuarioRepositoryRam(), new UsuarioValidator());
    }

    // =========================================================================
    // adicionarUsuario
    // =========================================================================

    @Test
    @DisplayName("deveCadastrarUsuarioComDadosValidos")
    void deveCadastrarUsuarioComDadosValidos() {
        Usuario u = new Usuario(1, "Lucas", "lucas@email.com", "Senha@123");
        assertDoesNotThrow(() -> service.adicionarUsuario(u));
        assertEquals(1, service.contarUsuarios());
    }

    @Test
    @DisplayName("deveCadastrarMultiplosUsuariosValidos")
    void deveCadastrarMultiplosUsuariosValidos() {
        service.adicionarUsuario(new Usuario(1, "Lucas", "lucas@email.com", "Senha@123"));
        service.adicionarUsuario(new Usuario(2, "Maria", "maria@email.com", "Mar1a#Secret"));
        assertEquals(2, service.contarUsuarios());
    }

    @Test
    @DisplayName("deveRejeitarNomeInvalidoENaoPersistir")
    void deveRejeitarNomeInvalidoENaoPersistir() {
        Usuario invalido = new Usuario(3, "Lucas123", "numeros@email.com", "Senha@123");
        assertThrows(ValidationException.class, () -> service.adicionarUsuario(invalido));
        assertEquals(0, service.contarUsuarios());
    }

    @Test
    @DisplayName("deveRejeitarSenhaInvalidaENaoPersistir")
    void deveRejeitarSenhaInvalidaENaoPersistir() {
        Usuario invalido = new Usuario(4, "Pedro", "pedro@email.com", "Ab1@");
        assertThrows(ValidationException.class, () -> service.adicionarUsuario(invalido));
        assertEquals(0, service.contarUsuarios());
    }

    // =========================================================================
    // buscarPorId
    // =========================================================================

    @Test
    @DisplayName("deveBuscarUsuarioCadastradoPorId")
    void deveBuscarUsuarioCadastradoPorId() {
        service.adicionarUsuario(new Usuario(10, "Ana", "ana@email.com", "Senha@123"));
        Optional<Usuario> resultado = service.buscarPorId(10);
        assertTrue(resultado.isPresent());
        assertEquals("Ana", resultado.get().getNome());
    }

    @Test
    @DisplayName("deveRetornarVazioParaIdInexistente")
    void deveRetornarVazioParaIdInexistente() {
        Optional<Usuario> resultado = service.buscarPorId(999);
        assertTrue(resultado.isEmpty());
    }

    // =========================================================================
    // listarTodosUsuarios
    // =========================================================================

    @Test
    @DisplayName("deveListarTodosUsuariosCadastrados")
    void deveListarTodosUsuariosCadastrados() {
        service.adicionarUsuario(new Usuario(1, "Lucas", "lucas@email.com", "Senha@123"));
        service.adicionarUsuario(new Usuario(2, "Maria", "maria@email.com", "Mar1a#Secret"));
        List<Usuario> lista = service.listarTodosUsuarios();
        assertEquals(2, lista.size());
    }

    @Test
    @DisplayName("deveRetornarListaVaziaQuandoNaoHaUsuarios")
    void deveRetornarListaVaziaQuandoNaoHaUsuarios() {
        assertTrue(service.listarTodosUsuarios().isEmpty());
    }

    // =========================================================================
    // atualizarUsuario
    // =========================================================================

    @Test
    @DisplayName("deveAtualizarUsuarioExistente")
    void deveAtualizarUsuarioExistente() {
        service.adicionarUsuario(new Usuario(1, "Lucas", "lucas@email.com", "Senha@123"));
        service.atualizarUsuario(new Usuario(1, "Lucaos", "novo@email.com", "Senha@123"));
        Optional<Usuario> atualizado = service.buscarPorId(1);
        assertTrue(atualizado.isPresent());
        assertEquals("novo@email.com", atualizado.get().getEmail());
    }

    @Test
    @DisplayName("deveRejeitarAtualizacaoComNomeInvalido")
    void deveRejeitarAtualizacaoComNomeInvalido() {
        service.adicionarUsuario(new Usuario(1, "Lucas", "lucas@email.com", "Senha@123"));
        assertThrows(ValidationException.class, () ->
                service.atualizarUsuario(new Usuario(1, "Nome123", "lucas@email.com", "Senha@123")));
    }

    // =========================================================================
    // deletarUsuario
    // =========================================================================

    @Test
    @DisplayName("deveDeletarUsuarioExistente")
    void deveDeletarUsuarioExistente() {
        service.adicionarUsuario(new Usuario(1, "Lucas", "lucas@email.com", "Senha@123"));
        service.deletarUsuario(1);
        assertEquals(0, service.contarUsuarios());
    }

    @Test
    @DisplayName("deveDeletarIdInexistenteSemErro")
    void deveDeletarIdInexistenteSemErro() {
        assertDoesNotThrow(() -> service.deletarUsuario(999));
    }

    // =========================================================================
    // contarUsuarios
    // =========================================================================

    @Test
    @DisplayName("deveContarCorretamenteAposOperacoes")
    void deveContarCorretamenteAposOperacoes() {
        assertEquals(0, service.contarUsuarios());
        service.adicionarUsuario(new Usuario(1, "Lucas", "lucas@email.com", "Senha@123"));
        assertEquals(1, service.contarUsuarios());
        service.deletarUsuario(1);
        assertEquals(0, service.contarUsuarios());
    }
}

