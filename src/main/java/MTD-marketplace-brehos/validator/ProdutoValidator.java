package mtd.validator;

import mtd.exception.ValidationException;
import mtd.model.Produto;

import java.math.BigDecimal;

public class ProdutoValidator {

    public void validar(Produto produto) {
        if (produto == null) {
            throw new ValidationException("O produto nao pode ser nulo.");
        }
        validarTitulo(produto.getTitulo());
        validarPreco(produto.getPreco());
    }

    public void validarTitulo(String titulo) {
        if (titulo == null || titulo.isBlank()) {
            throw new ValidationException("O titulo do produto nao pode ser vazio.");
        }
    }

    public void validarPreco(BigDecimal preco) {
        if (preco == null || preco.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("O preco do produto deve ser maior que zero.");
        }
    }
}

