package mtd.service;

import mtd.model.Usuario;
import mtd.repository.UsuarioRepository;
import mtd.validator.UsuarioValidator;

import java.util.List;
import java.util.Optional;

public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioValidator usuarioValidator;

    public UsuarioService(UsuarioRepository usuarioRepository, UsuarioValidator usuarioValidator) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioValidator = usuarioValidator;
    }

    public void adicionarUsuario(Usuario usuario) {
        usuarioValidator.validar(usuario);
        usuarioRepository.adicionar(usuario);
    }

    public Optional<Usuario> buscarPorId(int id) {
        return usuarioRepository.buscarPorId(id);
    }

    public List<Usuario> listarTodosUsuarios() {
        return usuarioRepository.listarTodos();
    }

    public void atualizarUsuario(Usuario usuario) {
        usuarioValidator.validar(usuario);
        usuarioRepository.atualizar(usuario);
    }

    public void deletarUsuario(int id) {
        usuarioRepository.deletar(id);
    }

    public int contarUsuarios() {
        return usuarioRepository.contar();
    }
}
