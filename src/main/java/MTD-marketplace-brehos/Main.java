package mtd;

import mtd.model.Usuario;
import mtd.repository.UsuarioRepository;
import mtd.service.UsuarioService;
import mtd.validator.UsuarioValidator;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        UsuarioRepository usuarioRepository = new UsuarioRepository();
        UsuarioValidator usuarioValidator = new UsuarioValidator();

        UsuarioService usuarioService =
                new UsuarioService(usuarioRepository, usuarioValidator);

        System.out.println("=== TENTATIVAS DE CADASTRO ===\n");

        // Validos
        tentarAdicionarUsuario(usuarioService, new Usuario(1, "Lucas",  "lucas@email.com",  "Senha@123"));
        tentarAdicionarUsuario(usuarioService, new Usuario(2, "Maria",  "maria@email.com",  "Mar1a#Secret"));

        // Nome invalido
        tentarAdicionarUsuario(usuarioService, new Usuario(3, "",             "vazio@email.com",   "Senha@123"));
        tentarAdicionarUsuario(usuarioService, new Usuario(4, "Lucas123",     "numeros@email.com", "Senha@123"));
        tentarAdicionarUsuario(usuarioService, new Usuario(5, "NomeMuitoLongo","longo@email.com",  "Senha@123"));

        // Senha muito curta
        tentarAdicionarUsuario(usuarioService, new Usuario(6, "Pedro",  "pedro@email.com",  "Ab1@"));

        // Senha com poucos tipos de caracteres (so minusculas)
        tentarAdicionarUsuario(usuarioService, new Usuario(7, "Ana",    "ana@email.com",     "somenteminusculas"));

        // Senha identica ao email
        tentarAdicionarUsuario(usuarioService, new Usuario(8, "Carla",  "Ab1@cdef.gh",       "Ab1@cdef.gh"));

        System.out.println("\n=== USUARIOS CADASTRADOS COM SUCESSO ===\n");

        List<Usuario> usuarios = usuarioService.listarTodosUsuarios();

        for (Usuario usuario : usuarios) {
            System.out.println(usuario);
        }
    }

    private static void tentarAdicionarUsuario(UsuarioService usuarioService, Usuario usuario) {
        try {
            usuarioService.adicionarUsuario(usuario);
            System.out.println("[OK]   Cadastrado: " + usuario.getNome());
        } catch (IllegalArgumentException e) {
            System.out.println("[ERRO] Rejeitado [" + usuario.getNome() + "]: " + e.getMessage());
        }
    }
}