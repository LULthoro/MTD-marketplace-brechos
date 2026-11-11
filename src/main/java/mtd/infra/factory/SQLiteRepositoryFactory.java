package mtd.infra.factory;

import mtd.factory.RepositoryFactory;
import mtd.infra.persistence.sqlite.ProdutoRepositorySQLite;
import mtd.infra.persistence.sqlite.UsuarioRepositorySQLite;
import mtd.repository.ProdutoRepository;
import mtd.repository.UsuarioRepository;

public class SQLiteRepositoryFactory implements RepositoryFactory {

    @Override
    public UsuarioRepository criarUsuarioRepository() {
        return new UsuarioRepositorySQLite();
    }

    @Override
    public ProdutoRepository criarProdutoRepository() {
        return new ProdutoRepositorySQLite();
    }
}
