package mtd.factory;

import mtd.infra.factory.RamRepositoryFactory;
import mtd.infra.factory.SQLiteRepositoryFactory;
import mtd.repository.ProdutoRepository;
import mtd.repository.UsuarioRepository;

public interface RepositoryFactory {

    UsuarioRepository criarUsuarioRepository();

    ProdutoRepository criarProdutoRepository();

    static RepositoryFactory selecionarFactory(String modo) {
        if ("bd".equalsIgnoreCase(modo)) {
            return new SQLiteRepositoryFactory();
        }
        return new RamRepositoryFactory();
    }
}

