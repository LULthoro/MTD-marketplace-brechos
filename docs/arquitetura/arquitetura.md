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