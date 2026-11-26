package mtd.service;

import mtd.infra.log.JavaUtilLoggerAdapter;
import mtd.model.Usuario;
import mtd.repository.UsuarioRepository;
import mtd.service.log.AppLogger;
import mtd.validator.UsuarioValidator;

import java.util.List;
import java.util.Optional;

public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioValidator usuarioValidator;
    private final AppLogger logger;

    public UsuarioService(UsuarioRepository usuarioRepository, UsuarioValidator usuarioValidator, AppLogger logger) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioValidator = usuarioValidator;
        this.logger = logger;
    }

    public UsuarioService(UsuarioRepository usuarioRepository, UsuarioValidator usuarioValidator) {
        this(usuarioRepository, usuarioValidator, new JavaUtilLoggerAdapter("UsuarioService"));
    }

    public void adicionarUsuario(Usuario usuario) {
        try {
            usuarioValidator.validar(usuario);
            usuarioRepository.adicionar(usuario);
            logger.info("CADASTRO_USUARIO", "Usuario cadastrado com sucesso: " + usuario.getNome());
        } catch (RuntimeException e) {
            logger.aviso("VALIDACAO_USUARIO", "Falha ao cadastrar usuario '" + (usuario != null ? usuario.getNome() : "null") + "': " + e.getMessage());
            throw e;
        }
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
        logger.info("ATUALIZAR_USUARIO", "Usuario ID " + usuario.getId() + " atualizado para: " + usuario.getNome());
    }

    public void deletarUsuario(int id) {
        usuarioRepository.deletar(id);
        logger.info("DELETAR_USUARIO", "Usuario ID " + id + " removido.");
    }

    public void registrarAcesso(int usuarioId) {
        usuarioRepository.buscarPorId(usuarioId).ifPresent(usuario -> {
            usuario.registrarAcesso();
            usuarioRepository.atualizar(usuario);
            logger.info("ACESSO_USUARIO", "Acesso contabilizado para " + usuario.getNome() + " (total=" + usuario.getQuantidadeAcessos() + ")");
        });
    }

    public int contarUsuarios() {
        return usuarioRepository.contar();
    }
}
