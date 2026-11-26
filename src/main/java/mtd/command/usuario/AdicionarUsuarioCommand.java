package mtd.command.usuario;

import mtd.command.Command;
import mtd.model.Usuario;
import mtd.service.UsuarioService;

/**
 * <<Controle / Command Concreto>> Comando para adicionar um novo Usuário.
 */
public class AdicionarUsuarioCommand implements Command {

    private final UsuarioService usuarioService;
    private final Usuario usuario;

    public AdicionarUsuarioCommand(UsuarioService usuarioService, Usuario usuario) {
        this.usuarioService = usuarioService;
        this.usuario = usuario;
    }

    @Override
    public void execute() {
        usuarioService.adicionarUsuario(usuario);
    }
}
