# Arquitetura do Sistema — Marketplace de Brechós

## 1. Visão geral

O Marketplace de Brechós será desenvolvido inicialmente utilizando Java.

A arquitetura do sistema será organizada em camadas, buscando separar as responsabilidades relacionadas à apresentação, regras de negócio, acesso aos dados e entidades do domínio.

A estrutura inicial será preparada para permitir que o armazenamento em memória utilizado durante o desenvolvimento possa posteriormente ser substituído por um banco de dados.

---

# 2. Arquitetura em camadas

O sistema será organizado nas seguintes camadas:

```text
┌───────────────────────────────┐
│          Apresentação         │
│              Main             │
└───────────────┬───────────────┘
                │
                ▼
┌───────────────────────────────┐
│            Service            │
│        Regras de negócio      │
└───────────────┬───────────────┘
                │
                ▼
┌───────────────────────────────┐
│          Repository           │
│       Acesso aos dados        │
└───────────────┬───────────────┘
                │
                ▼
┌───────────────────────────────┐
│             Model             │
│       Entidades do domínio    │
└───────────────────────────────┘
```

---

# 3. Diagrama de Classes de Análise (Fronteira, Controle e Entidade)

O diagrama abaixo modela o sistema seguindo o padrão **BCE (*Boundary, Control, Entity* — Fronteira, Controle e Entidade)**, contemplando os padrões **Singleton** e **Facade** aplicados em `FacadeSingletonController`, a separação de validações (`UsuarioValidator` e `ProdutoValidator`), o tratamento de exceções customizadas (`ValidationException` e `DatabaseOperationException`) e a persistência polimórfica em memória (`RAM`) e banco de dados (`SQLite`).

## 3.1 Responsabilidades por Estereótipo

- **`<<Fronteira>>` (*Boundary*):**
  - **Interface com o Usuário (CLI):** `Main` recebe os parâmetros de execução e exibe os resultados no console.
  - **Interface com Persistência Externa / SGBD:** `UsuarioRepository` e `ProdutoRepository` (e suas implementações `RAM` e `SQLite`) isolam a comunicação com o meio de armazenamento.
- **`<<Controle>>` (*Control*):**
  - **Coordenação Central (Facade + Singleton):** `FacadeSingletonController` unifica o acesso aos serviços de usuário e produto com instanciação *thread-safe* (*double-checked locking*).
  - **Regras de Negócio e Validação:** `UsuarioService`, `ProdutoService`, `UsuarioValidator` e `ProdutoValidator` coordenam os fluxos de negócio e garantem a integridade dos dados através de `ValidationException` e `DatabaseOperationException`.
- **`<<Entidade>>` (*Entity*):**
  - **Domínio:** `Usuario` e `Produto` representam os dados persistentes e o relacionamento entre vendedor/usuário e os itens anunciados no marketplace.

## 3.2 Diagrama UML (Mermaid)

```mermaid
classDiagram
    direction TB

    %% ==========================================
    %% CAMADA DE FRONTEIRA (BOUNDARY) - INTERACAO
    %% ==========================================
    class Main {
        <<Fronteira>>
        +main(String[] args)$ void
        -criarRepositorio(String modo)$ UsuarioRepository
        -tentarAdicionarUsuario(UsuarioService, Usuario)$ void
    }

    %% ==========================================
    %% CAMADA DE CONTROLE (CONTROL)
    %% ==========================================
    class FacadeSingletonController {
        <<Controle>>
        -instance : FacadeSingletonController$
        -usuarioService : UsuarioService
        -produtoService : ProdutoService
        -FacadeSingletonController(UsuarioService, ProdutoService)
        +getInstance(UsuarioService, ProdutoService)$ FacadeSingletonController
        +getInstance(String modo)$ FacadeSingletonController
        +getInstance()$ FacadeSingletonController
        +adicionarUsuario(Usuario usuario) void
        +listarTodosUsuarios() List~Usuario~
        +cadastrarProduto(Produto produto) Produto
        +buscarProdutoPorId(Long id) Optional~Produto~
        +listarTodosProdutos() List~Produto~
        +atualizarProduto(Produto produto) void
        +deletarProduto(Long id) void
        +getQuantidadeTotalEntidadesCadastradas() int
    }

    class UsuarioService {
        <<Controle>>
        -usuarioRepository : UsuarioRepository
        -usuarioValidator : UsuarioValidator
        +adicionarUsuario(Usuario usuario) void
        +listarTodosUsuarios() List~Usuario~
    }

    class ProdutoService {
        <<Controle>>
        -produtoRepository : ProdutoRepository
        -produtoValidator : ProdutoValidator
        +cadastrarProduto(Produto produto) Produto
        +buscarPorId(Long id) Optional~Produto~
        +listarTodosProdutos() List~Produto~
        +atualizarProduto(Produto produto) void
        +deletarProduto(Long id) void
        +contarProdutos() int
    }

    class UsuarioValidator {
        <<Controle>>
        -NOME_MAXIMO_CARACTERES : int$
        -SENHA_MINIMO_CARACTERES : int$
        -SENHA_MAXIMO_CARACTERES : int$
        -SENHA_MINIMO_TIPOS_CARACTERES : int$
        +validarNome(String nome) void
        +validarSenha(String senha, String nome, String email) void
    }

    class ProdutoValidator {
        <<Controle>>
        +validar(Produto produto) void
        +validarTitulo(String titulo) void
        +validarPreco(BigDecimal preco) void
    }

    class ValidationException {
        <<Exception>>
        +ValidationException(String message)
        +ValidationException(String message, Throwable cause)
    }

    class DatabaseOperationException {
        <<Exception>>
        +DatabaseOperationException(String message)
        +DatabaseOperationException(String message, Throwable cause)
    }

    %% ==========================================
    %% CAMADA DE FRONTEIRA DE DADOS (REPOSITORIES)
    %% ==========================================
    class UsuarioRepository {
        <<Interface / Fronteira>>
        +adicionar(Usuario usuario) void
        +listarTodos() List~Usuario~
    }

    class UsuarioRepositoryRam {
        <<Fronteira>>
        -usuarios : List~Usuario~
        +adicionar(Usuario usuario) void
        +listarTodos() List~Usuario~
    }

    class UsuarioRepositorySQLite {
        <<Fronteira>>
        -URL : String$
        +adicionar(Usuario usuario) void
        +listarTodos() List~Usuario~
    }

    class ProdutoRepository {
        <<Interface / Fronteira>>
        +salvar(Produto produto) Produto
        +buscarPorId(Long id) Optional~Produto~
        +listarTodos() List~Produto~
        +atualizar(Produto produto) void
        +deletar(Long id) void
        +contar() int
    }

    class ProdutoRepositoryRAM {
        <<Fronteira>>
        -produtos : Map~Long, Produto~
        -geradorId : AtomicLong
        +salvar(Produto produto) Produto
        +buscarPorId(Long id) Optional~Produto~
        +listarTodos() List~Produto~
        +atualizar(Produto produto) void
        +deletar(Long id) void
        +contar() int
    }

    class ProdutoRepositorySQLite {
        <<Fronteira>>
        -URL : String$
        +salvar(Produto produto) Produto
        +buscarPorId(Long id) Optional~Produto~
        +listarTodos() List~Produto~
        +atualizar(Produto produto) void
        +deletar(Long id) void
        +contar() int
    }

    %% ==========================================
    %% CAMADA DE ENTIDADE (ENTITY)
    %% ==========================================
    class Usuario {
        <<Entidade>>
        -id : int
        -nome : String
        -email : String
        -senha : String
        +getId() int
        +getNome() String
        +getEmail() String
        +getSenha() String
    }

    class Produto {
        <<Entidade>>
        -id : Long
        -titulo : String
        -descricao : String
        -preco : BigDecimal
        -tamanho : String
        -usuarioId : Long
        +getId() Long
        +getTitulo() String
        +getDescricao() String
        +getPreco() BigDecimal
        +getTamanho() String
        +getUsuarioId() Long
    }

    %% ==========================================
    %% RELACIONAMENTOS (FRONTEIRA -> CONTROLE -> ENTIDADE)
    %% ==========================================
    Main ..> FacadeSingletonController : aciona
    Main ..> UsuarioService : aciona

    FacadeSingletonController *-- "1" UsuarioService : unifica
    FacadeSingletonController *-- "1" ProdutoService : unifica
    FacadeSingletonController ..> DatabaseOperationException : lanca

    UsuarioService --> "1" UsuarioValidator : valida com
    UsuarioService --> "1" UsuarioRepository : persiste via
    UsuarioService ..> Usuario : manipula

    ProdutoService --> "1" ProdutoValidator : valida com
    ProdutoService --> "1" ProdutoRepository : persiste via
    ProdutoService ..> Produto : manipula

    ProdutoValidator ..> ValidationException : lanca
    ProdutoRepositorySQLite ..> DatabaseOperationException : lanca

    UsuarioRepository <|.. UsuarioRepositoryRam : implements
    UsuarioRepository <|.. UsuarioRepositorySQLite : implements

    ProdutoRepository <|.. ProdutoRepositoryRAM : implements
    ProdutoRepository <|.. ProdutoRepositorySQLite : implements

    Usuario "1" <-- "0..*" Produto : pertence a (usuarioId)
```
