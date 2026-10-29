# Casos de Uso — Marketplace de Brechós

## 1. Visão geral

O Marketplace de Brechós é um sistema destinado à comercialização de peças de vestuário de segunda mão, conectando compradores, vendedores/brechós e administradores.

O sistema possui três atores principais:

- Comprador
- Vendedor
- Administrador

Os casos de uso representam as principais interações desses atores com o sistema.

---

## 2. Atores

### 2.1 Comprador

Usuário que utiliza o marketplace para pesquisar, comprar e avaliar produtos disponibilizados pelos brechós.

Principais ações:

- Fazer cadastro
- Fazer login
- Buscar peças
- Filtrar peças
- Comprar peças
- Finalizar compra
- Aplicar cupom
- Avaliar brechó

---

### 2.2 Vendedor

Responsável pelo brechó e pelos produtos disponibilizados no marketplace.

Principais ações:

- Fazer login
- Cadastrar brechó
- Gerir produtos
- Consultar vendas

---

### 2.3 Administrador

Responsável pela administração e moderação da plataforma.

Principais ações:

- Aprovar brechós
- Gerir usuários

---

# 3. Casos de uso

## UC01 — Fazer cadastro

**Ator principal:** Comprador

**Descrição:**  
Permite que um novo usuário realize seu cadastro no marketplace.

**Pré-condições:**
- O usuário ainda não possui cadastro no sistema.

**Fluxo principal:**
1. O usuário acessa a opção de cadastro.
2. O sistema solicita os dados necessários.
3. O usuário informa seus dados.
4. O sistema valida os dados informados.
5. O sistema cria o usuário.
6. O sistema confirma o cadastro.

**Pós-condições:**
- O usuário está cadastrado no sistema.

---

## UC02 — Fazer login

**Atores:** Comprador, Vendedor

**Descrição:**  
Permite que um usuário cadastrado acesse o sistema.

**Pré-condições:**
- O usuário possui cadastro.

**Fluxo principal:**
1. O usuário informa suas credenciais.
2. O sistema verifica os dados.
3. O sistema autentica o usuário.
4. O sistema disponibiliza as funcionalidades correspondentes ao seu perfil.

**Pós-condições:**
- O usuário está autenticado.

---

## UC03 — Buscar peças

**Ator principal:** Comprador

**Descrição:**  
Permite que o comprador procure peças disponíveis no marketplace.

**Fluxo principal:**
1. O comprador informa um termo de busca.
2. O sistema pesquisa os produtos cadastrados.
3. O sistema apresenta os resultados encontrados.

---

## UC04 — Filtrar peças

**Ator principal:** Comprador

**Descrição:**  
Permite restringir os resultados da busca de acordo com critérios definidos pelo sistema.

**Fluxo principal:**
1. O comprador realiza uma busca.
2. O comprador seleciona um ou mais filtros.
3. O sistema aplica os filtros.
4. O sistema apresenta os produtos correspondentes.

**Relacionamento:**
- Estende o caso de uso "Buscar peças".

---

## UC05 — Comprar peças

**Ator principal:** Comprador

**Descrição:**  
Permite que o comprador selecione produtos disponíveis para realizar uma compra.

**Fluxo principal:**
1. O comprador seleciona uma peça.
2. O sistema apresenta os dados do produto.
3. O comprador seleciona a opção de compra.
4. O sistema inicia o processo de finalização da compra.

---

## UC06 — Finalizar compra

**Ator principal:** Comprador

**Descrição:**  
Permite que o comprador conclua uma compra.

**Fluxo principal:**
1. O comprador revisa os produtos selecionados.
2. O sistema apresenta o valor da compra.
3. O comprador informa os dados necessários para a compra.
4. O sistema valida os dados.
5. O sistema realiza a validação do pagamento.
6. O sistema registra a compra.
7. O sistema apresenta a confirmação.

**Pós-condições:**
- A compra é registrada no sistema.

---

## UC07 — Aplicar cupom

**Ator principal:** Comprador

**Descrição:**  
Permite que o comprador aplique um cupom de desconto durante a finalização da compra.

**Fluxo principal:**
1. O comprador informa um código de cupom.
2. O sistema verifica o cupom.
3. O sistema valida sua utilização.
4. O sistema aplica o desconto.
5. O sistema atualiza o valor da compra.

**Relacionamento:**
- Estende o caso de uso "Finalizar compra".

---

## UC08 — Validar pagamento

**Ator principal:** Sistema / serviço de pagamento

**Descrição:**  
Realiza a validação do pagamento associado a uma compra.

**Fluxo principal:**
1. O sistema envia os dados necessários para validação.
2. O serviço de pagamento processa a solicitação.
3. O resultado da validação é retornado.
4. O sistema registra o resultado.

**Relacionamento:**
- É incluído durante "Finalizar compra".

---

## UC09 — Avaliar brechó

**Ator principal:** Comprador

**Descrição:**  
Permite que o comprador registre uma avaliação sobre um brechó após uma experiência de compra.

**Fluxo principal:**
1. O comprador acessa uma compra realizada.
2. O comprador seleciona a opção de avaliação.
3. O comprador informa sua avaliação.
4. O sistema registra a avaliação.
5. O sistema associa a avaliação ao brechó.

---

## UC10 — Cadastrar brechó

**Ator principal:** Vendedor

**Descrição:**  
Permite que um vendedor cadastre um brechó na plataforma.

**Fluxo principal:**
1. O vendedor acessa o cadastro de brechó.
2. O sistema solicita os dados do brechó.
3. O vendedor informa os dados.
4. O sistema registra a solicitação.
5. O brechó fica disponível para análise administrativa.

**Pós-condições:**
- O brechó é cadastrado como uma solicitação pendente de aprovação.

---

## UC11 — Gerir produtos

**Ator principal:** Vendedor

**Descrição:**  
Permite que o vendedor gerencie as peças disponibilizadas pelo brechó.

**Operações previstas:**

- Cadastrar produto
- Consultar produto
- Alterar produto
- Remover produto

**Fluxo geral:**
1. O vendedor acessa o gerenciamento de produtos.
2. O sistema apresenta os produtos do brechó.
3. O vendedor seleciona uma operação.
4. O sistema executa a operação solicitada.

**Relacionamento:**
- Inclui "Fazer login".

---

## UC12 — Consultar vendas

**Ator principal:** Vendedor

**Descrição:**  
Permite que o vendedor consulte as vendas realizadas por seu brechó.

**Fluxo principal:**
1. O vendedor acessa a área de vendas.
2. O sistema consulta as vendas associadas ao brechó.
3. O sistema apresenta as vendas.

---

## UC13 — Aprovar brechós

**Ator principal:** Administrador

**Descrição:**  
Permite que o administrador analise e aprove solicitações de cadastro de brechós.

**Fluxo principal:**
1. O administrador acessa as solicitações pendentes.
2. O sistema apresenta os brechós aguardando análise.
3. O administrador analisa uma solicitação.
4. O administrador aprova ou rejeita a solicitação.
5. O sistema registra a decisão.

---

## UC14 — Gerir usuários

**Ator principal:** Administrador

**Descrição:**  
Permite que o administrador consulte e gerencie os usuários cadastrados no sistema.

**Operações previstas:**

- Listar usuários
- Consultar usuário
- Alterar dados
- Gerenciar situação do usuário

**Fluxo geral:**
1. O administrador acessa o gerenciamento de usuários.
2. O sistema apresenta os usuários cadastrados.
3. O administrador seleciona uma operação.
4. O sistema executa a operação solicitada.

---

# 4. Relacionamentos entre casos de uso

| Caso de uso | Relacionamento | Caso de uso relacionado |
|---|---|---|
| Fazer cadastro | extend | Fazer login |
| Filtrar peças | extend | Buscar peças |
| Aplicar cupom | extend | Finalizar compra |
| Validar pagamento | include | Finalizar compra |
| Gerir produtos | include | Fazer login |

---

# 5. Diagrama de casos de uso

O diagrama geral do sistema está representado no arquivo correspondente ao projeto.

O sistema possui os seguintes grupos principais:

**Comprador**
- Cadastro e autenticação
- Pesquisa de produtos
- Compra
- Avaliação

**Vendedor**
- Cadastro do brechó
- Gerenciamento de produtos
- Consulta de vendas

**Administrador**
- Aprovação de brechós
- Gerenciamento de usuários