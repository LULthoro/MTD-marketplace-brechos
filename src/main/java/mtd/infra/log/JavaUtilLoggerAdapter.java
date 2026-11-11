package mtd.infra.log;

import mtd.service.log.AppLogger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class JavaUtilLoggerAdapter implements AppLogger {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final Logger adaptee;

    public JavaUtilLoggerAdapter(String nomeContexto) {
        this.adaptee = Logger.getLogger(nomeContexto);
        configurarAdaptee();
    }

    public JavaUtilLoggerAdapter() {
        this("MTD-Marketplace-Logger");
    }

    private void configurarAdaptee() {
        this.adaptee.setUseParentHandlers(false);
        if (this.adaptee.getHandlers().length == 0) {
            ConsoleHandler handler = new ConsoleHandler();
            handler.setLevel(Level.ALL);
            handler.setFormatter(new Formatter() {
                @Override
                public String format(LogRecord record) {
                    String dataHora = LocalDateTime.now().format(FORMATTER);
                    return String.format("[LOG-%s] %s | %s%n",
                            record.getLevel().getName(),
                            dataHora,
                            record.getMessage());
                }
            });
            this.adaptee.addHandler(handler);
        }
        this.adaptee.setLevel(Level.ALL);
    }

    @Override
    public void info(String operacao, String mensagem) {
        adaptee.log(Level.INFO, String.format("[%s] %s", operacao, mensagem));
    }

    @Override
    public void aviso(String operacao, String mensagem) {
        adaptee.log(Level.WARNING, String.format("[%s] %s", operacao, mensagem));
    }

    @Override
    public void erro(String operacao, String mensagem, Throwable causa) {
        String detalheCausa = (causa != null) ? " (" + causa.getMessage() + ")" : "";
        adaptee.log(Level.SEVERE, String.format("[%s] %s%s", operacao, mensagem, detalheCausa));
    }
}

