# Arquitetura do Sistema — Marketplace de Brechós

## 1. Visão Geral

O **Marketplace de Brechós** é desenvolvido em Java 21 gerenciado por Maven, seguindo a metodologia **Spec-Driven Development (SDD)**.

A arquitetura separa claramente as camadas de **Apresentação (`Main`)**, **Controle/Fachada (`controller`)**, **Negócio (`service`, `service.relatorio`, `service.log`, `validator`, `repository`, `factory`, `model`, `exception`)** e **Infraestrutura (`infra.persistence.ram`, `infra.persistence.sqlite`, `infra.factory`, `infra.log`)**.

---

# 2. Arquitetura em Camadas (`Business` vs `Infra`)

```text
┌───────────────────────────────────────────────────────────────────┐
│                         APRESENTAÇÃO (CLI)                        │
│                              mtd.Main                             │
└─────────────────────────────────┬─────────────────────────────────┘
                                  │
                                  ▼
┌───────────────────────────────────────────────────────────────────┐
│               CAMADA DE CONTROLE (Facade + Singleton)             │
│                  mtd.controller.FacadeSingletonController         │
└───────────┬─────────────────────┼─────────────────────┬───────────┘
            │                     │                     │
            ▼                     ▼                     ▼
┌─────────────────────┐ ┌───────────────────┐ ┌─────────────────────┐
│   CAMADA BUSINESS   │ │  CAMADA BUSINESS  │ │   CAMADA BUSINESS   │
│   UsuarioService    │ │  ProdutoService   │ │   Template Method   │
│ + UsuarioValidator  │ │ + ProdutoValidator│ │ RelatorioAcessoHTML │
│                     │ │                   │ │ RelatorioAcessoPDF  │
└───────────┬─────────┘ └─────────┬─────────┘ └─────────────────────┘
            │                     │
            ▼                     ▼
┌───────────────────────────────────────────┐ ┌─────────────────────┐
│        CONTRATOS DE NEGÓCIO (Interfaces)  │ │   ADAPTER TARGET    │
│    UsuarioRepository / ProdutoRepository  │ │      AppLogger      │
│      RepositoryFactory / EntityFactory    │ └──────────┬──────────┘
└─────────────────────┬─────────────────────┘            │
                      │                                  ▼
                      ▼                       ┌─────────────────────┐
┌───────────────────────────────────────────┐ │    CAMADA INFRA     │
│        CAMADA INFRA (Persistência)        │ │JavaUtilLoggerAdapter│
│  RamRepositoryFactory / SQLiteRepoFactory │ │  (Adaptee: Logger)  │
│  UsuarioRepositoryRam / SQLite            │ └─────────────────────┘
│  ProdutoRepositoryRAM / SQLite            │
└───────────────────────────────────────────┘
```

---

# 3. Catálogo de Padrões de Projeto e Estereótipos de Análise (BCE)

| # | Padrão de Projeto | Categoria | Estereótipo BCE | Classes Participantes no Projeto | Papel / Justificativa Arquitetural |
|---|---|---|---|---|---|
| **1** | **Singleton** | Criacional (GoF) | `<<Controle>>` | [`FacadeSingletonController`](file:///home/lucas/MTD-marketplace-brechos/src/main/java/mtd/controller/FacadeSingletonController.java) | Garante instância única e *thread-safe* (*Double-Checked Locking* com `volatile`) do controlador central. |
| **2** | **Facade** | Estrutural (GoF) | `<<Controle>>` | [`FacadeSingletonController`](file:///home/lucas/MTD-marketplace-brechos/src/main/java/mtd/controller/FacadeSingletonController.java) | Unifica o acesso a `UsuarioService`, `ProdutoService`, geração de relatórios (`RelatorioEstatisticaAcessoTemplate`) e métricas globais. |
| **3** | **Repository** | Arquitetural / Persistência | `<<Fronteira>>` | [`UsuarioRepository`](file:///home/lucas/MTD-marketplace-brechos/src/main/java/mtd/repository/UsuarioRepository.java), [`ProdutoRepository`](file:///home/lucas/MTD-marketplace-brechos/src/main/java/mtd/repository/ProdutoRepository.java) (Contratos `business`)<br>[`UsuarioRepositoryRam`](file:///home/lucas/MTD-marketplace-brechos/src/main/java/mtd/infra/persistence/ram/UsuarioRepositoryRam.java), [`ProdutoRepositoryRAM`](file:///home/lucas/MTD-marketplace-brechos/src/main/java/mtd/infra/persistence/ram/ProdutoRepositoryRAM.java), [`UsuarioRepositorySQLite`](file:///home/lucas/MTD-marketplace-brechos/src/main/java/mtd/infra/persistence/sqlite/UsuarioRepositorySQLite.java), [`ProdutoRepositorySQLite`](file:///home/lucas/MTD-marketplace-brechos/src/main/java/mtd/infra/persistence/sqlite/ProdutoRepositorySQLite.java) (`infra`) | Isola a camada `business` dos detalhes de armazenamento em memória (`ConcurrentHashMap`) ou banco relacional (`SQLite`). |
| **4** | **Abstract Factory & Factory Method** | Criacional (GoF) | `<<Controle>>` | [`RepositoryFactory`](file:///home/lucas/MTD-marketplace-brechos/src/main/java/mtd/factory/RepositoryFactory.java), [`RamRepositoryFactory`](file:///home/lucas/MTD-marketplace-brechos/src/main/java/mtd/infra/factory/RamRepositoryFactory.java), [`SQLiteRepositoryFactory`](file:///home/lucas/MTD-marketplace-brechos/src/main/java/mtd/infra/factory/SQLiteRepositoryFactory.java), [`EntityFactory`](file:///home/lucas/MTD-marketplace-brechos/src/main/java/mtd/factory/EntityFactory.java) | Seleciona e instancia famílias de repositórios (`RAM` ou `SQLite`) e cria entidades (`Usuario`, `Produto`) sem acoplar o cliente às classes de `infra`. |
| **5** | **Adapter** | Estrutural (GoF) | `<<Controle / Fronteira>>` | [`AppLogger`](file:///home/lucas/MTD-marketplace-brechos/src/main/java/mtd/service/log/AppLogger.java) (*Target*), [`JavaUtilLoggerAdapter`](file:///home/lucas/MTD-marketplace-brechos/src/main/java/mtd/infra/log/JavaUtilLoggerAdapter.java) (*Adapter*), `java.util.logging.Logger` (*Adaptee*) | Converte a interface da biblioteca de log (`java.util.logging.Logger`) para o contrato de domínio `AppLogger` consumido pelos `Services`. |
| **6** | **Template Method** | Comportamental (GoF) | `<<Controle>>` | [`RelatorioEstatisticaAcessoTemplate`](file:///home/lucas/MTD-marketplace-brechos/src/main/java/mtd/service/relatorio/RelatorioEstatisticaAcessoTemplate.java), [`RelatorioAcessoHTML`](file:///home/lucas/MTD-marketplace-brechos/src/main/java/mtd/service/relatorio/RelatorioAcessoHTML.java), [`RelatorioAcessoPDF`](file:///home/lucas/MTD-marketplace-brechos/src/main/java/mtd/service/relatorio/RelatorioAcessoPDF.java) | Define o esqueleto fixo (`final gerarRelatorio`) do cálculo de estatísticas de acesso dos usuários, delegando a formatação para as subclasses `HTML` e `PDF`. |

---

# 4. Diagrama de Classes de Análise e Projeto Atualizado (Mermaid)

O diagrama abaixo consolida tanto os estereótipos de **Análise BCE (`Fronteira`, `Controle`, `Entidade`)** quanto a identificação explícita de todos os **Padrões de Projeto (`Singleton`, `Facade`, `Repository`, `AbstractFactory`, `FactoryMethod`, `Adapter`, `TemplateMethod`)**:

```mermaid
classDiagram
    direction TB

    %% ==========================================
    %% CAMADA DE FRONTEIRA (CLI / APRESENTACAO)
    %% ==========================================
    class Main {
        <<Fronteira / Client>>
        +main(String[] args)$ void
        -tentarAdicionarUsuario(FacadeSingletonController, Usuario)$ void
        -tentarCadastrarProduto(FacadeSingletonController, Produto)$ void
    }

    %% ==========================================
    %% CONTROLE CENTRAL: FACADE + SINGLETON
    %% ==========================================
    class FacadeSingletonController {
        <<Controle / Facade & Singleton>>
        -instance : FacadeSingletonController$
        -usuarioService : UsuarioService
        -produtoService : ProdutoService
        -FacadeSingletonController(UsuarioService, ProdutoService)
        +getInstance(UsuarioService, ProdutoService)$ FacadeSingletonController
        +getInstance(RepositoryFactory)$ FacadeSingletonController
        +getInstance(String modo)$ FacadeSingletonController
        +getInstance()$ FacadeSingletonController
        +adicionarUsuario(Usuario usuario) void
        +buscarUsuarioPorId(int id) Optional~Usuario~
        +listarTodosUsuarios() List~Usuario~
        +atualizarUsuario(Usuario usuario) void
        +deletarUsuario(int id) void
        +registrarAcessoUsuario(int id) void
        +cadastrarProduto(Produto produto) Produto
        +buscarProdutoPorId(Long id) Optional~Produto~
        +listarTodosProdutos() List~Produto~
        +atualizarProduto(Produto produto) void
        +deletarProduto(Long id) void
        +getQuantidadeTotalEntidadesCadastradas() int
        +gerarRelatorioEstatisticaAcesso(RelatorioEstatisticaAcessoTemplate) String
        +gerarRelatorioAcessoHTML() String
        +gerarRelatorioAcessoPDF() String
    }

    %% ==========================================
    %% PADRAO ABSTRACT FACTORY & FACTORY METHOD
    %% ==========================================
    class RepositoryFactory {
        <<Controle / AbstractFactory & FactoryMethod>>
        +criarUsuarioRepository() UsuarioRepository
        +criarProdutoRepository() ProdutoRepository
        +selecionarFactory(String modo)$ RepositoryFactory
    }

    class RamRepositoryFactory {
        <<Controle / ConcreteFactory>>
        +criarUsuarioRepository() UsuarioRepository
        +criarProdutoRepository() ProdutoRepository
    }

    class SQLiteRepositoryFactory {
        <<Controle / ConcreteFactory>>
        +criarUsuarioRepository() UsuarioRepository
        +criarProdutoRepository() ProdutoRepository
    }

    class EntityFactory {
        <<Controle / FactoryMethod>>
        +criarUsuario(int id, String nome, String email, String senha)$ Usuario
        +criarProduto(Long id, String titulo, String descricao, BigDecimal preco, String tamanho, Long usuarioId)$ Produto
    }

    %% ==========================================
    %% CAMADA BUSINESS (SERVICES, VALIDATORS & EXCEPTIONS)
    %% ==========================================
    class UsuarioService {
        <<Controle / Business>>
        -usuarioRepository : UsuarioRepository
        -usuarioValidator : UsuarioValidator
        -logger : AppLogger
        +adicionarUsuario(Usuario usuario) void
        +buscarPorId(int id) Optional~Usuario~
        +listarTodosUsuarios() List~Usuario~
        +atualizarUsuario(Usuario usuario) void
        +deletarUsuario(int id) void
        +registrarAcesso(int usuarioId) void
        +contarUsuarios() int
    }

    class ProdutoService {
        <<Controle / Business>>
        -produtoRepository : ProdutoRepository
        -produtoValidator : ProdutoValidator
        -logger : AppLogger
        +cadastrarProduto(Produto produto) Produto
        +buscarPorId(Long id) Optional~Produto~
        +listarTodosProdutos() List~Produto~
        +atualizarProduto(Produto produto) void
        +deletarProduto(Long id) void
        +contarProdutos() int
    }

    class UsuarioValidator {
        <<Controle / Validator>>
        -NOME_MAXIMO_CARACTERES : int$
        -SENHA_MINIMO_CARACTERES : int$
        -SENHA_MAXIMO_CARACTERES : int$
        -SENHA_MINIMO_TIPOS_CARACTERES : int$
        +validar(Usuario usuario) void
        +validarNome(String nome) void
        +validarSenha(String senha, String nome, String email) void
    }

    class ProdutoValidator {
        <<Controle / Validator>>
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
    %% PADRAO TEMPLATE METHOD (RELATORIOS DE ACESSO)
    %% ==========================================
    class RelatorioEstatisticaAcessoTemplate {
        <<Controle / TemplateMethod AbstractClass>>
        +gerarRelatorio(List~Usuario~ usuarios, int totalProdutos) String
        -calcularTotalAcessos(List~Usuario~ usuarios) int
        -identificarUsuarioMaisAtivo(List~Usuario~ usuarios) String
        #montarCabecalho(String titulo, String dataHoraGeracao)* String
        #montarCorpoEstatisticas(int totalUsuarios, int totalAcessos, double mediaAcessos, String usuarioMaisAtivo, List~Usuario~ usuarios)* String
        #montarRodape(int totalEntidades)* String
    }

    class RelatorioAcessoHTML {
        <<Controle / TemplateMethod ConcreteClass>>
        #montarCabecalho(String titulo, String dataHoraGeracao) String
        #montarCorpoEstatisticas(int, int, double, String, List~Usuario~) String
        #montarRodape(int totalEntidades) String
    }

    class RelatorioAcessoPDF {
        <<Controle / TemplateMethod ConcreteClass>>
        #montarCabecalho(String titulo, String dataHoraGeracao) String
        #montarCorpoEstatisticas(int, int, double, String, List~Usuario~) String
        #montarRodape(int totalEntidades) String
    }

    %% ==========================================
    %% PADRAO ADAPTER (LOGGING)
    %% ==========================================
    class AppLogger {
        <<Controle / Adapter Target>>
        +info(String operacao, String mensagem) void
        +aviso(String operacao, String mensagem) void
        +erro(String operacao, String mensagem, Throwable causa) void
    }

    class JavaUtilLoggerAdapter {
        <<Fronteira Infra / Adapter>>
        -adaptee : Logger
        +info(String operacao, String mensagem) void
        +aviso(String operacao, String mensagem) void
        +erro(String operacao, String mensagem, Throwable causa) void
    }

    class JavaUtilLogger {
        <<Fronteira Externa / Adaptee : java.util.logging.Logger>>
        +log(Level level, String msg) void
    }

    %% ==========================================
    %% PADRAO REPOSITORY (CONTRATOS E INFRA)
    %% ==========================================
    class UsuarioRepository {
        <<Fronteira / Repository Interface>>
        +adicionar(Usuario usuario) void
        +buscarPorId(int id) Optional~Usuario~
        +listarTodos() List~Usuario~
        +atualizar(Usuario usuario) void
        +deletar(int id) void
        +contar() int
    }

    class UsuarioRepositoryRam {
        <<Fronteira Infra / Repository RAM>>
        -usuarios : Map~Integer, Usuario~
        +adicionar(Usuario usuario) void
        +buscarPorId(int id) Optional~Usuario~
        +listarTodos() List~Usuario~
        +atualizar(Usuario usuario) void
        +deletar(int id) void
        +contar() int
    }

    class UsuarioRepositorySQLite {
        <<Fronteira Infra / Repository SQLite>>
        -URL : String$
        +adicionar(Usuario usuario) void
        +buscarPorId(int id) Optional~Usuario~
        +listarTodos() List~Usuario~
        +atualizar(Usuario usuario) void
        +deletar(int id) void
        +contar() int
    }

    class ProdutoRepository {
        <<Fronteira / Repository Interface>>
        +salvar(Produto produto) Produto
        +buscarPorId(Long id) Optional~Produto~
        +listarTodos() List~Produto~
        +atualizar(Produto produto) void
        +deletar(Long id) void
        +contar() int
    }

    class ProdutoRepositoryRAM {
        <<Fronteira Infra / Repository RAM>>
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
        <<Fronteira Infra / Repository SQLite>>
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
        -quantidadeAcessos : int
        +getId() int
        +setId(int id) void
        +getNome() String
        +setNome(String nome) void
        +getEmail() String
        +setEmail(String email) void
        +getSenha() String
        +setSenha(String senha) void
        +getQuantidadeAcessos() int
        +setQuantidadeAcessos(int quantidadeAcessos) void
        +registrarAcesso() void
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
    %% RELACIONAMENTOS
    %% ==========================================
    Main ..> FacadeSingletonController : aciona
    Main ..> EntityFactory : cria entidades com

    FacadeSingletonController *-- "1" UsuarioService : unifica
    FacadeSingletonController *-- "1" ProdutoService : unifica
    FacadeSingletonController ..> RepositoryFactory : obtem repositorios via
    FacadeSingletonController ..> RelatorioEstatisticaAcessoTemplate : gera relatorios via

    RepositoryFactory <|.. RamRepositoryFactory : implements
    RepositoryFactory <|.. SQLiteRepositoryFactory : implements
    RamRepositoryFactory ..> UsuarioRepositoryRam : cria
    RamRepositoryFactory ..> ProdutoRepositoryRAM : cria
    SQLiteRepositoryFactory ..> UsuarioRepositorySQLite : cria
    SQLiteRepositoryFactory ..> ProdutoRepositorySQLite : cria

    EntityFactory ..> Usuario : instancia
    EntityFactory ..> Produto : instancia

    RelatorioEstatisticaAcessoTemplate <|-- RelatorioAcessoHTML : extends
    RelatorioEstatisticaAcessoTemplate <|-- RelatorioAcessoPDF : extends

    UsuarioService --> "1" UsuarioRepository : persiste via
    UsuarioService --> "1" UsuarioValidator : valida com
    UsuarioService --> "1" AppLogger : registra logs via
    UsuarioService ..> Usuario : manipula

    ProdutoService --> "1" ProdutoRepository : persiste via
    ProdutoService --> "1" ProdutoValidator : valida com
    ProdutoService --> "1" AppLogger : registra logs via
    ProdutoService ..> Produto : manipula

    UsuarioValidator ..> ValidationException : lanca
    ProdutoValidator ..> ValidationException : lanca
    UsuarioRepositorySQLite ..> DatabaseOperationException : lanca
    ProdutoRepositorySQLite ..> DatabaseOperationException : lanca

    AppLogger <|.. JavaUtilLoggerAdapter : implements
    JavaUtilLoggerAdapter --> "1" JavaUtilLogger : adapta (Adaptee)

    UsuarioRepository <|.. UsuarioRepositoryRam : implements
    UsuarioRepository <|.. UsuarioRepositorySQLite : implements
    ProdutoRepository <|.. ProdutoRepositoryRAM : implements
    ProdutoRepository <|.. ProdutoRepositorySQLite : implements

    Usuario "1" <-- "0..*" Produto : pertence a (usuarioId)
```
