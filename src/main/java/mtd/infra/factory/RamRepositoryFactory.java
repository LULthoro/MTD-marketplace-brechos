package mtd.infra.factory;

import mtd.factory.RepositoryFactory;
import mtd.infra.persistence.ram.ProdutoRepositoryRAM;
import mtd.infra.persistence.ram.UsuarioRepositoryRam;
import mtd.repository.ProdutoRepository;
import mtd.repository.UsuarioRepository;

public class RamRepositoryFactory implements RepositoryFactory {

    @Override
    public UsuarioRepository criarUsuarioRepository() {
        return new UsuarioRepositoryRam();
    }

    @Override
    public ProdutoRepository criarProdutoRepository() {
        return new ProdutoRepositoryRAM();
    }
}
