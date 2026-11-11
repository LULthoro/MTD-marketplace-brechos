package mtd.infra.persistence.sqlite;

import mtd.exception.DatabaseOperationException;
import mtd.model.Usuario;
import mtd.repository.UsuarioRepository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UsuarioRepositorySQLite implements UsuarioRepository {

    private static final String URL = "jdbc:sqlite:marketplace.db";

    public UsuarioRepositorySQLite() {
        String sql = "CREATE TABLE IF NOT EXISTS usuarios (" +
                " id INTEGER PRIMARY KEY," +
                " nome TEXT NOT NULL," +
                " email TEXT NOT NULL," +
                " senha TEXT NOT NULL," +
                " quantidade_acessos INTEGER NOT NULL DEFAULT 0" +
                ");";

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new DatabaseOperationException("Falha ao inicializar a tabela de usuarios no SQLite.", e);
        }
    }

    @Override
    public void adicionar(Usuario usuario) {
        String sql = "INSERT OR REPLACE INTO usuarios(id, nome, email, senha, quantidade_acessos) VALUES(?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, usuario.getId());
            pstmt.setString(2, usuario.getNome());
            pstmt.setString(3, usuario.getEmail());
            pstmt.setString(4, usuario.getSenha());
            pstmt.setInt(5, usuario.getQuantidadeAcessos());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Erro ao adicionar usuario no banco de dados.", e);
        }
    }

    @Override
    public Optional<Usuario> buscarPorId(int id) {
        String sql = "SELECT id, nome, email, senha, quantidade_acessos FROM usuarios WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Usuario(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("email"),
                            rs.getString("senha"),
                            rs.getInt("quantidade_acessos")
                    ));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Erro ao buscar usuario por ID no banco de dados.", e);
        }
    }

    @Override
    public List<Usuario> listarTodos() {
        String sql = "SELECT id, nome, email, senha, quantidade_acessos FROM usuarios";
        List<Usuario> usuarios = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                usuarios.add(new Usuario(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("email"),
                        rs.getString("senha"),
                        rs.getInt("quantidade_acessos")
                ));
            }
            return usuarios;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Erro ao listar todos os usuarios no banco de dados.", e);
        }
    }

    @Override
    public void atualizar(Usuario usuario) {
        String sql = "UPDATE usuarios SET nome = ?, email = ?, senha = ?, quantidade_acessos = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, usuario.getNome());
            pstmt.setString(2, usuario.getEmail());
            pstmt.setString(3, usuario.getSenha());
            pstmt.setInt(4, usuario.getQuantidadeAcessos());
            pstmt.setInt(5, usuario.getId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Erro ao atualizar usuario no banco de dados.", e);
        }
    }

    @Override
    public void deletar(int id) {
        String sql = "DELETE FROM usuarios WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Erro ao deletar usuario no banco de dados.", e);
        }
    }

    @Override
    public int contar() {
        String sql = "SELECT COUNT(*) FROM usuarios";

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Erro ao contar usuarios no banco de dados.", e);
        }
    }
}
