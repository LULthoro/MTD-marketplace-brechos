package mtd.repository;

import mtd.model.Usuario;

import java.sql.SQLException;
import java.util.List;

public interface UsuarioRepository {

    void adicionar(Usuario usuario) throws SQLException;

    List<Usuario> listarTodos() throws SQLException;
}
