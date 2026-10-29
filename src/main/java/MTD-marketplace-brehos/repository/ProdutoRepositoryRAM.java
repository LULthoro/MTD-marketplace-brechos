package mtd.repository;

import mtd.model.Produto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class ProdutoRepositoryRAM implements ProdutoRepository {

    private final Map<Long, Produto> produtos = new ConcurrentHashMap<>();
    private final AtomicLong geradorId = new AtomicLong(1);

    @Override
    public Produto salvar(Produto produto) {
        if (produto.getId() == null) {
            produto.setId(geradorId.getAndIncrement());
        } else {
            geradorId.updateAndGet(atual -> Math.max(atual, produto.getId() + 1));
        }
        produtos.put(produto.getId(), produto);
        return produto;
    }

    @Override
    public Optional<Produto> buscarPorId(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(produtos.get(id));
    }

    @Override
    public List<Produto> listarTodos() {
        return new ArrayList<>(produtos.values());
    }

    @Override
    public void atualizar(Produto produto) {
        if (produto.getId() != null && produtos.containsKey(produto.getId())) {
            produtos.put(produto.getId(), produto);
        }
    }

    @Override
    public void deletar(Long id) {
        if (id != null) {
            produtos.remove(id);
        }
    }

    @Override
    public int contar() {
        return produtos.size();
    }
}

