package mtd.exception;

public class MarketplaceException extends RuntimeException {

    public MarketplaceException(String mensagem) {
        super(mensagem);
    }

    public MarketplaceException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
