package mtd.repository;

import mtd.model.Produto;

import java.util.List;
import java.util.Optional;

public interface ProdutoRepository {

    Produto salvar(Produto produto);

    Optional<Produto> buscarPorId(Long id);

    List<Produto> listarTodos();

    void atualizar(Produto produto);

    void deletar(Long id);

    int contar();
}

