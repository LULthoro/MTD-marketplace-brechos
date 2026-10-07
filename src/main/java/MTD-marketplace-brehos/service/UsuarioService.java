package mtd.service;

import mtd.model.Usuario;
import mtd.repository.UsuarioRepository;
import mtd.validator.UsuarioValidator;

import java.sql.SQLException;
import java.util.List;

public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioValidator usuarioValidator;

    public UsuarioService(UsuarioRepository usuarioRepository, UsuarioValidator usuarioValidator) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioValidator = usuarioValidator;
    }

    public void adicionarUsuario(Usuario usuario) throws SQLException {
        usuarioValidator.validarNome(usuario.getNome());
        usuarioValidator.validarSenha(usuario.getSenha(), usuario.getNome(), usuario.getEmail());
        usuarioRepository.adicionar(usuario);
    }

    public List<Usuario> listarTodosUsuarios() throws SQLException {
        return usuarioRepository.listarTodos();
    }
}