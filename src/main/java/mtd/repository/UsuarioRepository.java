package mtd.repository;

import mtd.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository {

    void adicionar(Usuario usuario);

    Optional<Usuario> buscarPorId(int id);

    List<Usuario> listarTodos();

    void atualizar(Usuario usuario);

    void deletar(int id);

    int contar();
}
