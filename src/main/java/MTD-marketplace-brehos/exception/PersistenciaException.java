package mtd.exception;

public class PersistenciaException extends MarketplaceException {

    public PersistenciaException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
