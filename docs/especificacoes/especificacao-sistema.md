# Especificação Técnica e Padrões de Projeto — MTD Marketplace de Brechós

## 1. Resumo Arquitetural

O projeto **MTD Marketplace de Brechós** implementa uma arquitetura em camadas desacoplada orientada por especificações (**Spec-Driven Development — SDD**), separando a camada de **Regras de Negócio (`business`)** da camada de **Infraestrutura (`infra`)** através dos seguintes padrões de projeto GoF e arquiteturais:

1. **Repository (`mtd.repository` + `mtd.infra.persistence.*`):**
   - Contratos de negócio: [`UsuarioRepository`](file:///home/lucas/MTD-marketplace-brechos/src/main/java/mtd/repository/UsuarioRepository.java) e [`ProdutoRepository`](file:///home/lucas/MTD-marketplace-brechos/src/main/java/mtd/repository/ProdutoRepository.java).
   - Implementações de infraestrutura em memória (`RAM`): [`UsuarioRepositoryRam`](file:///home/lucas/MTD-marketplace-brechos/src/main/java/mtd/infra/persistence/ram/UsuarioRepositoryRam.java) e [`ProdutoRepositoryRAM`](file:///home/lucas/MTD-marketplace-brechos/src/main/java/mtd/infra/persistence/ram/ProdutoRepositoryRAM.java).
   - Implementações de infraestrutura relacional (`SQLite`): [`UsuarioRepositorySQLite`](file:///home/lucas/MTD-marketplace-brechos/src/main/java/mtd/infra/persistence/sqlite/UsuarioRepositorySQLite.java) e [`ProdutoRepositorySQLite`](file:///home/lucas/MTD-marketplace-brechos/src/main/java/mtd/infra/persistence/sqlite/ProdutoRepositorySQLite.java).

2. **Abstract Factory e Factory Method (`mtd.factory` + `mtd.infra.factory`):**
   - [`RepositoryFactory`](file:///home/lucas/MTD-marketplace-brechos/src/main/java/mtd/factory/RepositoryFactory.java): define a fábrica abstrata da família de repositórios e o *Factory Method* seletor `selecionarFactory(String modo)`.
   - [`RamRepositoryFactory`](file:///home/lucas/MTD-marketplace-brechos/src/main/java/mtd/infra/factory/RamRepositoryFactory.java) e [`SQLiteRepositoryFactory`](file:///home/lucas/MTD-marketplace-brechos/src/main/java/mtd/infra/factory/SQLiteRepositoryFactory.java): fábricas concretas que instanciam os repositórios compatíveis com o motor escolhido.
   - [`EntityFactory`](file:///home/lucas/MTD-marketplace-brechos/src/main/java/mtd/factory/EntityFactory.java): centraliza a criação das entidades de domínio (`Usuario` e `Produto`).

3. **Singleton e Facade (`mtd.controller`):**
   - [`FacadeSingletonController`](file:///home/lucas/MTD-marketplace-brechos/src/main/java/mtd/controller/FacadeSingletonController.java): garante uma única instância *thread-safe* (*Double-Checked Locking*) e unifica o acesso a `UsuarioService`, `ProdutoService` e aos relatórios de estatísticas de acesso.

4. **Adapter (`mtd.service.log` + `mtd.infra.log`):**
   - [`AppLogger`](file:///home/lucas/MTD-marketplace-brechos/src/main/java/mtd/service/log/AppLogger.java) (*Target*): interface de log do domínio consumida por `UsuarioService` e `ProdutoService`.
   - [`JavaUtilLoggerAdapter`](file:///home/lucas/MTD-marketplace-brechos/src/main/java/mtd/infra/log/JavaUtilLoggerAdapter.java) (*Adapter*): adapta a biblioteca de log `java.util.logging.Logger` (*Adaptee*) para o contrato `AppLogger`.

5. **Template Method (`mtd.service.relatorio`):**
   - [`RelatorioEstatisticaAcessoTemplate`](file:///home/lucas/MTD-marketplace-brechos/src/main/java/mtd/service/relatorio/RelatorioEstatisticaAcessoTemplate.java) (*AbstractClass*): implementa o método template `final gerarRelatorio(...)` que consolida estatísticas de acesso dos usuários no sistema (total de usuários, total de acessos, média de acessos por usuário e usuário mais ativo).
   - [`RelatorioAcessoHTML`](file:///home/lucas/MTD-marketplace-brechos/src/main/java/mtd/service/relatorio/RelatorioAcessoHTML.java) e [`RelatorioAcessoPDF`](file:///home/lucas/MTD-marketplace-brechos/src/main/java/mtd/service/relatorio/RelatorioAcessoPDF.java) (*ConcreteClasses*): especializam os passos abstratos `montarCabecalho`, `montarCorpoEstatisticas` e `montarRodape` para produzir relatórios nos formatos HTML e PDF.

