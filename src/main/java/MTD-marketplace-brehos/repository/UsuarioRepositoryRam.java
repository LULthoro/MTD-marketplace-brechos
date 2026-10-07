package mtd.repository;

import mtd.model.Usuario;

import java.util.ArrayList;
import java.util.List;

public class UsuarioRepositoryRam implements UsuarioRepository {

    private final List<Usuario> usuarios;

    public UsuarioRepositoryRam() {
        this.usuarios = new ArrayList<>();
    }

    @Override
    public void adicionar(Usuario usuario) {
        usuarios.add(usuario);
    }

    @Override
    public List<Usuario> listarTodos() {
        return new ArrayList<>(usuarios);
    }
}
