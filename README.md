# MTD-marketplace-brechos
Marketplace de brechós desenvolvido na disciplina de Métodos de Projeto de Software utilizando Spec-Driven Development (SDD), Java e diagramas UML.

## Como Executar o Projeto

O projeto utiliza o Maven para gerenciamento de dependências e execução. Ele suporta dois modos de armazenamento:

### 1. Usando Banco de Dados (SQLite - Recomendado)
Os dados serão persistidos no arquivo `marketplace.db`.
```bash
mvn compile exec:java "-Dexec.mainClass=mtd.Main" "-Dexec.args=bd"
```

### 2. Usando Memória (RAM)
Os dados são perdidos quando a execução termina.
```bash
mvn compile exec:java "-Dexec.mainClass=mtd.Main"
```
