package mtd.repository;

import mtd.exception.PersistenciaException;
import mtd.model.Usuario;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UsuarioRepositorySQLite implements UsuarioRepository {

    private static final String URL = "jdbc:sqlite:marketplace.db";

    public UsuarioRepositorySQLite() {
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {

            String sql = "CREATE TABLE IF NOT EXISTS usuarios (" +
                    " id INTEGER PRIMARY KEY," +
                    " nome TEXT NOT NULL," +
                    " email TEXT NOT NULL," +
                    " senha TEXT NOT NULL" +
                    ");";
            stmt.execute(sql);

        } catch (SQLException e) {
            throw new PersistenciaException("Falha ao inicializar o banco de dados.", e);
        }
    }

    @Override
    public void adicionar(Usuario usuario) throws PersistenciaException {
        String sql = "INSERT INTO usuarios(id, nome, email, senha) VALUES(?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, usuario.getId());
            pstmt.setString(2, usuario.getNome());
            pstmt.setString(3, usuario.getEmail());
            pstmt.setString(4, usuario.getSenha());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenciaException(
                    "Erro ao adicionar usuario '" + usuario.getNome() + "' no banco de dados.", e
            );
        }
    }

    @Override
    public List<Usuario> listarTodos() throws PersistenciaException {
        String sql = "SELECT id, nome, email, senha FROM usuarios";
        List<Usuario> usuarios = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                usuarios.add(new Usuario(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("email"),
                        rs.getString("senha")
                ));
            }

        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao listar usuarios do banco de dados.", e);
        }

        return usuarios;
    }
}
