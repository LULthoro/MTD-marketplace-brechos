package mtd;

import mtd.controller.FacadeSingletonController;
import mtd.exception.DatabaseOperationException;
import mtd.exception.ValidationException;
import mtd.factory.EntityFactory;
import mtd.model.Produto;
import mtd.model.Usuario;

import java.math.BigDecimal;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        String modo = (args.length > 0 && "bd".equalsIgnoreCase(args[0])) ? "bd" : "ram";
        System.out.println("[INFO] Iniciando sistema no modo: " + modo.toUpperCase());

        FacadeSingletonController facade = FacadeSingletonController.getInstance(modo);

        System.out.println("\n=== 1. CRUD E VALIDACAO DE USUARIOS ===\n");

        // Cadastros validos usando EntityFactory
        tentarAdicionarUsuario(facade, EntityFactory.criarUsuario(1, "Lucas", "lucas@email.com", "Senha@123"));
        tentarAdicionarUsuario(facade, EntityFactory.criarUsuario(2, "Maria", "maria@email.com", "Mar1a#Secret"));

        // Cadastros invalidos (regras de nome e senha)
        tentarAdicionarUsuario(facade, EntityFactory.criarUsuario(3, "", "vazio@email.com", "Senha@123"));
        tentarAdicionarUsuario(facade, EntityFactory.criarUsuario(4, "Lucas123", "numeros@email.com", "Senha@123"));
        tentarAdicionarUsuario(facade, EntityFactory.criarUsuario(5, "NomeMuitoLongo", "longo@email.com", "Senha@123"));
        tentarAdicionarUsuario(facade, EntityFactory.criarUsuario(6, "Pedro", "pedro@email.com", "Ab1@"));
        tentarAdicionarUsuario(facade, EntityFactory.criarUsuario(7, "Ana", "ana@email.com", "somenteminusculas"));
        tentarAdicionarUsuario(facade, EntityFactory.criarUsuario(8, "Carla", "Ab1@cdef.gh", "Ab1@cdef.gh"));

        // Atualizacao e busca por ID de Usuario
        facade.atualizarUsuario(EntityFactory.criarUsuario(2, "Mariana", "mariana@email.com", "Mar1a#Secret"));
        facade.buscarUsuarioPorId(2).ifPresent(u ->
                System.out.println("[UPDATE] Usuario ID 2 atualizado: " + u));

        // Simulacao de acessos dos usuarios ao sistema
        facade.registrarAcessoUsuario(1);
        facade.registrarAcessoUsuario(1);
        facade.registrarAcessoUsuario(1);
        facade.registrarAcessoUsuario(2);

        System.out.println("\n=== 2. CRUD E VALIDACAO DE PRODUTOS ===\n");

        // Cadastros validos de Produto
        tentarCadastrarProduto(facade, EntityFactory.criarProduto(1L, "Jaqueta Jeans Vintage", "Jaqueta anos 90 conservada", new BigDecimal("120.00"), "M", 1L));
        tentarCadastrarProduto(facade, EntityFactory.criarProduto(2L, "Camisa Flanelada Xadrez", "100% algodao seminova", new BigDecimal("65.50"), "G", 1L));
        tentarCadastrarProduto(facade, EntityFactory.criarProduto(3L, "Vestido Floral Retro", "Estampa exclusiva", new BigDecimal("89.90"), "P", 2L));

        // Cadastros invalidos de Produto (titulo vazio e preco negativo/zero)
        tentarCadastrarProduto(facade, EntityFactory.criarProduto(4L, "   ", "Produto sem titulo", new BigDecimal("50.00"), "M", 1L));
        tentarCadastrarProduto(facade, EntityFactory.criarProduto(5L, "Calca Cargo", "Preco invalido", new BigDecimal("-10.00"), "40", 2L));

        // Atualizacao, busca por ID e remocao de Produto
        facade.atualizarProduto(EntityFactory.criarProduto(1L, "Jaqueta Jeans Vintage 90s", "Jaqueta anos 90 na promocao", new BigDecimal("99.90"), "M", 1L));
        facade.buscarProdutoPorId(1L).ifPresent(p ->
                System.out.println("[UPDATE] Produto ID 1 atualizado: " + p));

        facade.deletarProduto(3L);
        System.out.println("[DELETE] Produto ID 3 removido do catalogo.");

        System.out.println("\n=== 3. RESUMO FINAL DO SISTEMA (FACADE) ===\n");

        List<Usuario> usuarios = facade.listarTodosUsuarios();
        System.out.println("Usuarios ativos (" + usuarios.size() + "):");
        for (Usuario u : usuarios) {
            System.out.println("  - " + u);
        }

        List<Produto> produtos = facade.listarTodosProdutos();
        System.out.println("\nProdutos ativos no catalogo (" + produtos.size() + "):");
        for (Produto p : produtos) {
            System.out.println("  - " + p);
        }

        System.out.println("\nTotal consolidado de entidades cadastradas (Usuarios + Produtos): "
                + facade.getQuantidadeTotalEntidadesCadastradas());

        System.out.println("\n=== 4. RELATORIO DE ACESSOS EM HTML (TEMPLATE METHOD) ===\n");
        System.out.println(facade.gerarRelatorioAcessoHTML());

        System.out.println("\n=== 5. RELATORIO DE ACESSOS EM PDF (TEMPLATE METHOD) ===\n");
        System.out.println(facade.gerarRelatorioAcessoPDF());
    }

    private static void tentarAdicionarUsuario(FacadeSingletonController facade, Usuario usuario) {
        try {
            facade.adicionarUsuario(usuario);
            System.out.println("[OK]   Usuario cadastrado: " + usuario.getNome());
        } catch (ValidationException e) {
            System.out.println("[ERRO] Usuario rejeitado [" + usuario.getNome() + "] (Validacao): " + e.getMessage());
        } catch (DatabaseOperationException e) {
            System.err.println("[ERRO] Usuario rejeitado [" + usuario.getNome() + "] (Banco de Dados): " + e.getMessage());
        }
    }

    private static void tentarCadastrarProduto(FacadeSingletonController facade, Produto produto) {
        try {
            Produto salvo = facade.cadastrarProduto(produto);
            System.out.println("[OK]   Produto cadastrado: " + salvo.getTitulo() + " (R$ " + salvo.getPreco() + ")");
        } catch (ValidationException e) {
            System.out.println("[ERRO] Produto rejeitado [" + produto.getTitulo() + "] (Validacao): " + e.getMessage());
        } catch (DatabaseOperationException e) {
            System.err.println("[ERRO] Produto rejeitado [" + produto.getTitulo() + "] (Banco de Dados): " + e.getMessage());
        }
    }
}
