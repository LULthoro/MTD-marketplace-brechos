package mtd.command;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * <<Controle>> Invoker do padrão Command.
 *
 * Responsável por receber e executar objetos Command, mantendo
 * um histórico das operações realizadas (Deque de Commands executados).
 * Desacopla o chamador (FacadeSingletonController) da lógica
 * concreta de cada operação.
 */
public class CommandInvoker {

    private final Deque<Command> historico = new ArrayDeque<>();

    /**
     * Executa o comando recebido e o registra no histórico.
     *
     * @param command Comando a ser executado.
     */
    public void executar(Command command) {
        command.execute();
        historico.push(command);
    }

    /**
     * Retorna o último comando executado sem removê-lo do histórico.
     *
     * @return Último Command executado, ou {@code null} se o histórico estiver vazio.
     */
    public Command ultimoComando() {
        return historico.isEmpty() ? null : historico.peek();
    }

    /**
     * Retorna o tamanho do histórico de comandos executados.
     */
    public int tamanhoHistorico() {
        return historico.size();
    }
}
