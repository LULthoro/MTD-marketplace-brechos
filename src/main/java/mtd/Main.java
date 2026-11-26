package mtd;

import mtd.controller.FacadeSingletonController;
import mtd.exception.DatabaseOperationException;
import mtd.exception.ValidationException;
import mtd.model.Produto;
import mtd.model.Usuario;

import java.math.BigDecimal;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        String modo = (args.length > 0 && "bd".equalsIgnoreCase(args[0])) ? "bd" : "ram";
        System.out.println("[INFO] Iniciando sistema no modo: " + modo.toUpperCase());

        FacadeSingletonController facade = FacadeSingletonController.getInstance(modo);

        System.out.println("\n=== 1. CRUD E VALIDACAO DE USUARIOS (via Command) ===\n");

        // Cadastros validos
        tentarAdicionarUsuario(facade, new Usuario(1, "Lucas", "lucas@email.com", "Senha@123"));
        tentarAdicionarUsuario(facade, new Usuario(2, "Maria", "maria@email.com", "Mar1a#Secret"));

        // Cadastros invalidos (regras de nome e senha)
        tentarAdicionarUsuario(facade, new Usuario(3, "", "vazio@email.com", "Senha@123"));
        tentarAdicionarUsuario(facade, new Usuario(4, "Lucas123", "numeros@email.com", "Senha@123"));
        tentarAdicionarUsuario(facade, new Usuario(5, "NomeMuitoLongo", "longo@email.com", "Senha@123"));
        tentarAdicionarUsuario(facade, new Usuario(6, "Pedro", "pedro@email.com", "Ab1@"));
        tentarAdicionarUsuario(facade, new Usuario(7, "Ana", "ana@email.com", "somenteminusculas"));
        tentarAdicionarUsuario(facade, new Usuario(8, "Carla", "Ab1@cdef.gh", "Ab1@cdef.gh"));

        // Atualizacao e busca por ID de Usuario (Command AtualizarUsuarioCommand)
        facade.atualizarUsuario(new Usuario(2, "Mariana", "mariana@email.com", "Mar1a#Secret"));
        facade.buscarUsuarioPorId(2).ifPresent(u ->
                System.out.println("[UPDATE] Usuario ID 2 atualizado: " + u));

        System.out.println("\n=== 2. CRUD E VALIDACAO DE PRODUTOS (via Command) ===\n");

        // Cadastros validos de Produto (Command CadastrarProdutoCommand)
        tentarCadastrarProduto(facade, new Produto(1L, "Jaqueta Jeans Vintage", "Jaqueta anos 90 conservada", new BigDecimal("120.00"), "M", 1L));
        tentarCadastrarProduto(facade, new Produto(2L, "Camisa Flanelada Xadrez", "100% algodao seminova", new BigDecimal("65.50"), "G", 1L));
        tentarCadastrarProduto(facade, new Produto(3L, "Vestido Floral Retro", "Estampa exclusiva", new BigDecimal("89.90"), "P", 2L));

        // Cadastros invalidos de Produto
        tentarCadastrarProduto(facade, new Produto(4L, "   ", "Produto sem titulo", new BigDecimal("50.00"), "M", 1L));
        tentarCadastrarProduto(facade, new Produto(5L, "Calca Cargo", "Preco invalido", new BigDecimal("-10.00"), "40", 2L));

        System.out.println("\n=== 3. MEMENTO — DESFAZER ULTIMA ATUALIZACAO DE PRODUTO ===\n");

        // Estado ANTES da atualizacao
        facade.buscarProdutoPorId(1L).ifPresent(p ->
                System.out.println("[ANTES]  Produto ID 1: " + p));

        // Atualiza produto (Command AtualizarProdutoCommand + Memento salvo automaticamente)
        facade.atualizarProduto(new Produto(1L, "Jaqueta Jeans Vintage 90s", "Preco promocional", new BigDecimal("99.90"), "M", 1L));
        facade.buscarProdutoPorId(1L).ifPresent(p ->
                System.out.println("[DEPOIS] Produto ID 1: " + p));

        // Desfaz a ultima atualizacao (Memento restaura estado anterior)
        facade.desfazerUltimaAtualizacaoProduto();
        facade.buscarProdutoPorId(1L).ifPresent(p ->
                System.out.println("[UNDO]   Produto ID 1: " + p));

        // Remocao e historico de Commands
        facade.deletarProduto(3L);
        System.out.println("[DELETE] Produto ID 3 removido do catalogo.");

        System.out.println("\n=== 4. RESUMO FINAL DO SISTEMA (FACADE) ===\n");

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

        System.out.println("\nHistorico de Commands executados: "
                + facade.getInvoker().tamanhoHistorico() + " operacoes registradas.");
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
