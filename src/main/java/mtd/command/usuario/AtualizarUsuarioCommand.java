package mtd.command.usuario;

import mtd.command.Command;
import mtd.model.Usuario;
import mtd.service.UsuarioService;

/**
 * <<Controle / Command Concreto>> Comando para atualizar dados de um Usuário.
 */
public class AtualizarUsuarioCommand implements Command {

    private final UsuarioService usuarioService;
    private final Usuario usuario;

    public AtualizarUsuarioCommand(UsuarioService usuarioService, Usuario usuario) {
        this.usuarioService = usuarioService;
        this.usuario = usuario;
    }

    @Override
    public void execute() {
        usuarioService.atualizarUsuario(usuario);
    }
}
