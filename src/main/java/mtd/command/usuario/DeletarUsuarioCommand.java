package mtd.command.usuario;

import mtd.command.Command;
import mtd.service.UsuarioService;

/**
 * <<Controle / Command Concreto>> Comando para deletar um Usuário pelo id.
 */
public class DeletarUsuarioCommand implements Command {

    private final UsuarioService usuarioService;
    private final int id;

    public DeletarUsuarioCommand(UsuarioService usuarioService, int id) {
        this.usuarioService = usuarioService;
        this.id = id;
    }

    @Override
    public void execute() {
        usuarioService.deletarUsuario(id);
    }
}
