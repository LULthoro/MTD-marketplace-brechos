package mtd.repository;

import mtd.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UsuarioRepositoryRam")
class UsuarioRepositoryRamTest {

    private UsuarioRepositoryRam repository;

    @BeforeEach
    void setUp() {
        repository = new UsuarioRepositoryRam();
    }

    private Usuario novoUsuario(int id) {
        return new Usuario(id, "User" + id, "user" + id + "@email.com", "Senha@123");
    }

    @Test
    @DisplayName("deveAdicionarERecuperarPorId")
    void deveAdicionarERecuperarPorId() {
        repository.adicionar(novoUsuario(1));
        Optional<Usuario> resultado = repository.buscarPorId(1);
        assertTrue(resultado.isPresent());
        assertEquals(1, resultado.get().getId());
    }

    @Test
    @DisplayName("deveRetornarVazioParaIdInexistente")
    void deveRetornarVazioParaIdInexistente() {
        assertTrue(repository.buscarPorId(99).isEmpty());
    }

    @Test
    @DisplayName("deveListarTodosOsUsuarios")
    void deveListarTodosOsUsuarios() {
        repository.adicionar(novoUsuario(1));
        repository.adicionar(novoUsuario(2));
        List<Usuario> lista = repository.listarTodos();
        assertEquals(2, lista.size());
    }

    @Test
    @DisplayName("deveRetornarListaVaziaQuandoVazio")
    void deveRetornarListaVaziaQuandoVazio() {
        assertTrue(repository.listarTodos().isEmpty());
    }

    @Test
    @DisplayName("deveAtualizarUsuarioExistente")
    void deveAtualizarUsuarioExistente() {
        repository.adicionar(novoUsuario(1));
        Usuario atualizado = new Usuario(1, "Novo", "novo@email.com", "Senha@123");
        repository.atualizar(atualizado);
        Optional<Usuario> resultado = repository.buscarPorId(1);
        assertTrue(resultado.isPresent());
        assertEquals("Novo", resultado.get().getNome());
    }

    @Test
    @DisplayName("deveIgnorarAtualizacaoDeIdInexistente")
    void deveIgnorarAtualizacaoDeIdInexistente() {
        // não deve lançar exceção
        assertDoesNotThrow(() ->
                repository.atualizar(new Usuario(99, "Fantasma", "f@f.com", "Senha@123")));
        assertEquals(0, repository.contar());
    }

    @Test
    @DisplayName("deveDeletarUsuarioExistente")
    void deveDeletarUsuarioExistente() {
        repository.adicionar(novoUsuario(1));
        repository.deletar(1);
        assertTrue(repository.buscarPorId(1).isEmpty());
        assertEquals(0, repository.contar());
    }

    @Test
    @DisplayName("deveContarUsuariosCorretamente")
    void deveContarUsuariosCorretamente() {
        assertEquals(0, repository.contar());
        repository.adicionar(novoUsuario(1));
        repository.adicionar(novoUsuario(2));
        assertEquals(2, repository.contar());
    }
}

