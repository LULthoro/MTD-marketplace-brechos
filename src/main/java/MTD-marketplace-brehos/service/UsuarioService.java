package mtd.service;

import mtd.model.Usuario;
import mtd.repository.UsuarioRepository;

import java.util.List;

public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public void adicionarUsuario(Usuario usuario) {
        usuarioRepository.adicionar(usuario);
    }

    public List<Usuario> listarTodosUsuarios() {
        return usuarioRepository.listarTodos();
    }
}