package mtd;

import mtd.model.Usuario;
import mtd.repository.UsuarioRepository;
import mtd.service.UsuarioService;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        UsuarioRepository usuarioRepository = new UsuarioRepository();

        UsuarioService usuarioService =
                new UsuarioService(usuarioRepository);

        Usuario usuario1 = new Usuario(
                1,
                "Lucas",
                "lucas@email.com"
        );

        Usuario usuario2 = new Usuario(
                2,
                "Maria",
                "maria@email.com"
        );

        usuarioService.adicionarUsuario(usuario1);
        usuarioService.adicionarUsuario(usuario2);

        List<Usuario> usuarios =
                usuarioService.listarTodosUsuarios();

        System.out.println("=== USUÁRIOS CADASTRADOS ===");

        for (Usuario usuario : usuarios) {
            System.out.println(usuario);
        }
    }
}