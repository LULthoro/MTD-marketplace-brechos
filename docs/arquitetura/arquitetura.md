# Arquitetura do Sistema — Marketplace de Brechós

## 1. Visão geral

O Marketplace de Brechós é desenvolvido em Java 21 gerenciado por Maven, seguindo a metodologia **Spec-Driven Development (SDD)**.

A arquitetura do sistema é organizada em camadas desacopladas, separando as responsabilidades de **Apresentação (`Main`)**, **Coordenação de Controle (`FacadeSingletonController`)**, **Regras de Negócio e Validação (`Service` e `Validator`)**, **Persistência Polimórfica (`Repository` RAM/SQLite)** e **Entidades de Domínio (`Model`)**.

---

# 2. Arquitetura em camadas (Até a Sprint 3)

O sistema encontra-se estruturado nas seguintes camadas:

```text
┌───────────────────────────────────────────┐
│               Apresentação                │
│                   Main                    │
└─────────────────────┬─────────────────────┘
                      │
                      ▼
┌───────────────────────────────────────────┐
│          Controller (Facade/Singleton)    │
│         FacadeSingletonController         │
└──────────┬─────────────────────┬──────────┘
           │                     │
           ▼                     ▼
┌─────────────────────┐ ┌───────────────────┐
│   UsuarioService    │ │  ProdutoService   │
│ + UsuarioValidator  │ │ + ProdutoValidator│
└──────────┬──────────┘ └────────┬──────────┘
           │                     │
           ▼                     ▼
┌─────────────────────┐ ┌───────────────────┐
│  UsuarioRepository  │ │ ProdutoRepository │
│   (RAM / SQLite)    │ │  (RAM / SQLite)   │
└──────────┬──────────┘ └────────┬──────────┘
           │                     │
           ▼                     ▼
┌─────────────────────┐ ┌───────────────────┐
│       Usuario       │ │      Produto      │
│   (Entidade Model)  │ │  (Entidade Model) │
└─────────────────────┘ └───────────────────┘
```

---

# 3. Diagrama de Classes de Análise (Fronteira, Controle e Entidade — Sprint 3)

O diagrama abaixo modela o sistema até a **Sprint 3** seguindo o padrão de análise **BCE (*Boundary, Control, Entity* — Fronteira, Controle e Entidade)**, contemplando:
- **CRUD completo das duas entidades (`Usuario` e `Produto`)**;
- Padrões de projeto **Singleton** (*thread-safe double-checked locking*) e **Facade** em `FacadeSingletonController`;
- Tratamento padronizado de exceções de domínio (`ValidationException` e `DatabaseOperationException`).

## 3.1 Responsabilidades por Estereótipo

- **`<<Fronteira>>` (*Boundary*):**
  - **Interface com o Usuário (CLI):** `Main` interpreta o modo de inicialização (`ram` ou `bd`), aciona a fachada e apresenta os resultados no console.
  - **Interface com Persistência Externa / SGBD:** `UsuarioRepository` e `ProdutoRepository` (com implementações `RAM` em `ConcurrentHashMap` e `SQLite` via JDBC) isolam a comunicação com o meio de armazenamento.
- **`<<Controle>>` (*Control*):**
  - **Coordenação Central (Facade + Singleton):** `FacadeSingletonController` unifica todas as operações CRUD de `UsuarioService` e `ProdutoService`, além de consolidar métricas (`getQuantidadeTotalEntidadesCadastradas()`).
  - **Regras de Negócio e Validação:** `UsuarioService`, `ProdutoService`, `UsuarioValidator` e `ProdutoValidator` coordenam os fluxos e lançam `ValidationException` quando regras de negócio são violadas.
- **`<<Entidade>>` (*Entity*):**
  - **Domínio:** `Usuario` e `Produto` representam os objetos persistentes do marketplace e seu relacionamento (`1 Usuario` possui `0..* Produto` via `usuarioId`).

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
        -tentarAdicionarUsuario(FacadeSingletonController, Usuario)$ void
        -tentarCadastrarProduto(FacadeSingletonController, Produto)$ void
    }

    %% ==========================================
    %% CAMADA DE CONTROLE (CONTROL)
    %% ==========================================
    class FacadeSingletonController {
        <<Controle / Facade & Singleton>>
        -instance : FacadeSingletonController$
        -usuarioService : UsuarioService
        -produtoService : ProdutoService
        -FacadeSingletonController(UsuarioService, ProdutoService)
        +getInstance(UsuarioService, ProdutoService)$ FacadeSingletonController
        +getInstance(String modo)$ FacadeSingletonController
        +getInstance()$ FacadeSingletonController
        +adicionarUsuario(Usuario usuario) void
        +buscarUsuarioPorId(int id) Optional~Usuario~
        +listarTodosUsuarios() List~Usuario~
        +atualizarUsuario(Usuario usuario) void
        +deletarUsuario(int id) void
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
        +buscarPorId(int id) Optional~Usuario~
        +listarTodosUsuarios() List~Usuario~
        +atualizarUsuario(Usuario usuario) void
        +deletarUsuario(int id) void
        +contarUsuarios() int
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
        +validar(Usuario usuario) void
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
        +buscarPorId(int id) Optional~Usuario~
        +listarTodos() List~Usuario~
        +atualizar(Usuario usuario) void
        +deletar(int id) void
        +contar() int
    }

    class UsuarioRepositoryRam {
        <<Fronteira>>
        -usuarios : Map~Integer, Usuario~
        +adicionar(Usuario usuario) void
        +buscarPorId(int id) Optional~Usuario~
        +listarTodos() List~Usuario~
        +atualizar(Usuario usuario) void
        +deletar(int id) void
        +contar() int
    }

    class UsuarioRepositorySQLite {
        <<Fronteira>>
        -URL : String$
        +adicionar(Usuario usuario) void
        +buscarPorId(int id) Optional~Usuario~
        +listarTodos() List~Usuario~
        +atualizar(Usuario usuario) void
        +deletar(int id) void
        +contar() int
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
        +setId(int id) void
        +getNome() String
        +setNome(String nome) void
        +getEmail() String
        +setEmail(String email) void
        +getSenha() String
        +setSenha(String senha) void
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
        +setId(Long id) void
        +getTitulo() String
        +setTitulo(String titulo) void
        +getDescricao() String
        +setDescricao(String descricao) void
        +getPreco() BigDecimal
        +setPreco(BigDecimal preco) void
        +getTamanho() String
        +setTamanho(String tamanho) void
        +getUsuarioId() Long
        +setUsuarioId(Long usuarioId) void
    }

    %% ==========================================
    %% RELACIONAMENTOS (FRONTEIRA -> CONTROLE -> ENTIDADE)
    %% ==========================================
    Main ..> FacadeSingletonController : aciona

    FacadeSingletonController *-- "1" UsuarioService : unifica
    FacadeSingletonController *-- "1" ProdutoService : unifica

    UsuarioService --> "1" UsuarioValidator : valida com
    UsuarioService --> "1" UsuarioRepository : persiste via
    UsuarioService ..> Usuario : manipula

    ProdutoService --> "1" ProdutoValidator : valida com
    ProdutoService --> "1" ProdutoRepository : persiste via
    ProdutoService ..> Produto : manipula

    UsuarioValidator ..> ValidationException : lanca
    ProdutoValidator ..> ValidationException : lanca
    UsuarioRepositorySQLite ..> DatabaseOperationException : lanca
    ProdutoRepositorySQLite ..> DatabaseOperationException : lanca

    UsuarioRepository <|.. UsuarioRepositoryRam : implements
    UsuarioRepository <|.. UsuarioRepositorySQLite : implements

    ProdutoRepository <|.. ProdutoRepositoryRAM : implements
    ProdutoRepository <|.. ProdutoRepositorySQLite : implements

    Usuario "1" <-- "0..*" Produto : pertence a (usuarioId)
```

---

# 4. Revisões de Código e Melhorias Aplicadas até a Sprint 3

1. **Desacoplamento de Exceções de Infraestrutura (`SQLException` → `DatabaseOperationException`):**
   - Anteriormente, `UsuarioRepository` e `UsuarioService` expunham `throws SQLException` em suas assinaturas, acoplando a camada de negócio à API JDBC mesmo quando executada em memória (`RAM`).
   - Na revisão da Sprint 3, toda `SQLException` lançada em `UsuarioRepositorySQLite` e `ProdutoRepositorySQLite` passou a ser capturada e relançada encapsulada em `DatabaseOperationException`.
2. **Padronização das Exceções de Validação (`ValidationException`):**
   - Tanto `UsuarioValidator` quanto `ProdutoValidator` agora lançam `ValidationException` em caso de violação de regras de negócio (nome vazio/com números/longo, senha fraca/igual ao nome ou e-mail, título vazio ou preço não positivo).
3. **Concorrência Segura em Memória (`ConcurrentHashMap`):**
   - Os repositórios em memória (`UsuarioRepositoryRam` e `ProdutoRepositoryRAM`) utilizam `ConcurrentHashMap`, evitando condições de corrida em acessos concorrentes.
4. **Simetria de Operações CRUD nas Duas Entidades (`Usuario` e `Produto`):**
   - Ambas as entidades contam com operações completas de criação, busca por ID (`Optional`), listagem completa, atualização, remoção e contagem (`contar()`), todas expostas de forma centralizada por `FacadeSingletonController`.
