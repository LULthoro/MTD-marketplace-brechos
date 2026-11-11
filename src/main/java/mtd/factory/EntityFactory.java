package mtd.factory;

import mtd.model.Produto;
import mtd.model.Usuario;

import java.math.BigDecimal;

public class EntityFactory {

    private EntityFactory() {
    }

    public static Usuario criarUsuario(int id, String nome, String email, String senha) {
        return new Usuario(id, nome, email, senha);
    }

    public static Produto criarProduto(Long id, String titulo, String descricao, BigDecimal preco, String tamanho, Long usuarioId) {
        return new Produto(id, titulo, descricao, preco, tamanho, usuarioId);
    }
}
