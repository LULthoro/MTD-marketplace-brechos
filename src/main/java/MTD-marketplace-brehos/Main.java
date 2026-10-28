package mtd;

import mtd.exception.PersistenciaException;
import mtd.exception.ValidacaoException;
import mtd.model.Usuario;
import mtd.repository.UsuarioRepository;
import mtd.repository.UsuarioRepositoryRam;
import mtd.repository.UsuarioRepositorySQLite;
import mtd.service.UsuarioService;
import mtd.validator.UsuarioValidator;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        String modo = (args.length > 0) ? args[0] : "ram";
        UsuarioRepository usuarioRepository = criarRepositorio(modo);
        UsuarioValidator usuarioValidator = new UsuarioValidator();

        UsuarioService usuarioService =
                new UsuarioService(usuarioRepository, usuarioValidator);

        System.out.println("=== TENTATIVAS DE CADASTRO ===\n");

        // Validos
        tentarAdicionarUsuario(usuarioService, new Usuario(1, "Lucas",  "lucas@email.com",  "Senha@123"));
        tentarAdicionarUsuario(usuarioService, new Usuario(2, "Maria",  "maria@email.com",  "Mar1a#Secret"));

        // Nome invalido
        tentarAdicionarUsuario(usuarioService, new Usuario(3, "aldo",          "vazio@email.com",   "Senha@123"));
        tentarAdicionarUsuario(usuarioService, new Usuario(4, "Lucas123",      "numeros@email.com", "Senha@123"));
        tentarAdicionarUsuario(usuarioService, new Usuario(5, "NomeMuitoLongo","longo@email.com",   "Senha@123"));

        // Senha invalida
        tentarAdicionarUsuario(usuarioService, new Usuario(6, "Pedro",  "pedro@email.com",  "Ab1@"));
        tentarAdicionarUsuario(usuarioService, new Usuario(7, "Ana",    "ana@email.com",     "somenteminusculas"));
        tentarAdicionarUsuario(usuarioService, new Usuario(8, "Carla",  "Ab1@cdef.gh",       "Ab1@cdef.gh"));

        System.out.println("\n=== USUARIOS CADASTRADOS COM SUCESSO ===\n");

        try {
            List<Usuario> usuarios = usuarioService.listarTodosUsuarios();
            for (Usuario usuario : usuarios) {
                System.out.println(usuario);
            }
        } catch (PersistenciaException e) {
            System.err.println("[ERRO] Falha ao listar os usuarios: " + e.getMessage());
        }
    }

    private static UsuarioRepository criarRepositorio(String modo) {
        if (modo.equalsIgnoreCase("bd")) {
            System.out.println("[INFO] Iniciando com persistencia em SQLite (BD)...");
            return new UsuarioRepositorySQLite();
        }
        System.out.println("[INFO] Iniciando com persistencia em RAM...");
        return new UsuarioRepositoryRam();
    }

    private static void tentarAdicionarUsuario(UsuarioService usuarioService, Usuario usuario) {
        try {
            usuarioService.adicionarUsuario(usuario);
            System.out.println("[OK]   Cadastrado: " + usuario.getNome());
        } catch (ValidacaoException e) {
            System.out.println("[ERRO] Rejeitado [" + usuario.getNome() + "] (Validacao): " + e.getMessage());
        } catch (PersistenciaException e) {
            System.err.println("[ERRO] Rejeitado [" + usuario.getNome() + "] (Banco de Dados): " + e.getMessage());
        }
    }
}