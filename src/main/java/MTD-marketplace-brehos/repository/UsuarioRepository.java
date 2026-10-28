package mtd.repository;

import mtd.exception.PersistenciaException;
import mtd.model.Usuario;

import java.util.List;

public interface UsuarioRepository {

    void adicionar(Usuario usuario) throws PersistenciaException;

    List<Usuario> listarTodos() throws PersistenciaException;
}
