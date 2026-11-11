package mtd.infra.persistence.ram;

import mtd.model.Usuario;
import mtd.repository.UsuarioRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class UsuarioRepositoryRam implements UsuarioRepository {

    private final Map<Integer, Usuario> usuarios = new ConcurrentHashMap<>();

    @Override
    public void adicionar(Usuario usuario) {
        usuarios.put(usuario.getId(), usuario);
    }

    @Override
    public Optional<Usuario> buscarPorId(int id) {
        return Optional.ofNullable(usuarios.get(id));
    }

    @Override
    public List<Usuario> listarTodos() {
        return new ArrayList<>(usuarios.values());
    }

    @Override
    public void atualizar(Usuario usuario) {
        if (usuarios.containsKey(usuario.getId())) {
            usuarios.put(usuario.getId(), usuario);
        }
    }

    @Override
    public void deletar(int id) {
        usuarios.remove(id);
    }

    @Override
    public int contar() {
        return usuarios.size();
    }
}
