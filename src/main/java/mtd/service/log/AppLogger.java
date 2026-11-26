package mtd.service.log;

public interface AppLogger {

    void info(String operacao, String mensagem);

    void aviso(String operacao, String mensagem);

    void erro(String operacao, String mensagem, Throwable causa);
}

