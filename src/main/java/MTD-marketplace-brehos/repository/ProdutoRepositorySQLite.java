package mtd.repository;

import mtd.exception.DatabaseOperationException;
import mtd.model.Produto;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProdutoRepositorySQLite implements ProdutoRepository {

    private static final String URL = "jdbc:sqlite:marketplace.db";

    public ProdutoRepositorySQLite() {
        inicializarTabela();
    }

    private void inicializarTabela() {
        String sql = "CREATE TABLE IF NOT EXISTS produtos (" +
                " id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " titulo TEXT NOT NULL," +
                " descricao TEXT," +
                " preco NUMERIC NOT NULL," +
                " tamanho TEXT," +
                " usuario_id INTEGER NOT NULL" +
                ");";

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new DatabaseOperationException("Falha ao inicializar a tabela de produtos no SQLite.", e);
        }
    }

    @Override
    public Produto salvar(Produto produto) {
        boolean possuiId = produto.getId() != null;
        String sql = possuiId
                ? "INSERT INTO produtos (id, titulo, descricao, preco, tamanho, usuario_id) VALUES (?, ?, ?, ?, ?, ?)"
                : "INSERT INTO produtos (titulo, descricao, preco, tamanho, usuario_id) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            int idx = 1;
            if (possuiId) {
                pstmt.setLong(idx++, produto.getId());
            }
            pstmt.setString(idx++, produto.getTitulo());
            pstmt.setString(idx++, produto.getDescricao());
            pstmt.setBigDecimal(idx++, produto.getPreco());
            pstmt.setString(idx++, produto.getTamanho());
            pstmt.setLong(idx, produto.getUsuarioId() != null ? produto.getUsuarioId() : 0L);

            pstmt.executeUpdate();

            if (!possuiId) {
                try (ResultSet keys = pstmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        produto.setId(keys.getLong(1));
                    }
                }
            }
            return produto;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Erro ao salvar o produto no banco de dados.", e);
        }
    }

    @Override
    public Optional<Produto> buscarPorId(Long id) {
        String sql = "SELECT id, titulo, descricao, preco, tamanho, usuario_id FROM produtos WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearProduto(rs));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Erro ao buscar produto por ID no banco de dados.", e);
        }
    }

    @Override
    public List<Produto> listarTodos() {
        String sql = "SELECT id, titulo, descricao, preco, tamanho, usuario_id FROM produtos";
        List<Produto> produtos = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                produtos.add(mapearProduto(rs));
            }
            return produtos;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Erro ao listar todos os produtos do banco de dados.", e);
        }
    }

    @Override
    public void atualizar(Produto produto) {
        String sql = "UPDATE produtos SET titulo = ?, descricao = ?, preco = ?, tamanho = ?, usuario_id = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, produto.getTitulo());
            pstmt.setString(2, produto.getDescricao());
            pstmt.setBigDecimal(3, produto.getPreco());
            pstmt.setString(4, produto.getTamanho());
            pstmt.setLong(5, produto.getUsuarioId() != null ? produto.getUsuarioId() : 0L);
            pstmt.setLong(6, produto.getId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Erro ao atualizar o produto no banco de dados.", e);
        }
    }

    @Override
    public void deletar(Long id) {
        String sql = "DELETE FROM produtos WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Erro ao deletar o produto no banco de dados.", e);
        }
    }

    @Override
    public int contar() {
        String sql = "SELECT COUNT(*) FROM produtos";

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Erro ao contar os produtos no banco de dados.", e);
        }
    }

    private Produto mapearProduto(ResultSet rs) throws SQLException {
        return new Produto(
                rs.getLong("id"),
                rs.getString("titulo"),
                rs.getString("descricao"),
                rs.getBigDecimal("preco"),
                rs.getString("tamanho"),
                rs.getLong("usuario_id")
        );
    }
}

