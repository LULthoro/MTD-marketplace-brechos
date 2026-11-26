Documento de Visão

Versão: 1.3 | Status: Revisado aguardando aprovação | Data:
28/10/2026


1. Propósito
Apresentar o problema, os objetivos, o público-alvo, o escopo e as
decisões pendentes do Marketplace de Desapego, servindo como referência
para o desenvolvimento.

2. Visão geral
O projeto propõe uma plataforma digital que conecta compradores a
vendedores e brechós que desejam anunciar produtos usados. A ideia
inicial concentra-se em peças de vestuário, com possibilidade de
expansão para outras categorias após validação do escopo.

A plataforma deverá reunir cadastro e autenticação, gerenciamento de
perfis e lojas, publicação de produtos, catálogo público, pesquisa,
filtros, carrinho, registro de pedidos e consulta ao histórico.

3. Problema e oportunidade
A divulgação de produtos usados pode ocorrer em canais dispersos,
dificultando a descoberta de itens, a comparação de ofertas e a
organização dos anúncios. O marketplace pretende centralizar essas
atividades, facilitar a exposição dos produtos e a navegação dos
compradores, além de incentivar o reaproveitamento de itens.

4. Objetivos
Geral: desenvolver uma plataforma que permita anunciar, pesquisar e
solicitar a compra de produtos usados de maneira organizada.

Específicos: - Permitir cadastro, autenticação e gerenciamento de
perfis. - Possibilitar cadastro e administração de brechós. - Permitir
criar, atualizar e remover anúncios do catálogo ativo. - Disponibilizar
catálogo público, pesquisa por palavras-chave e filtros. - Permitir
adicionar e remover itens do carrinho. - Registrar pedidos vinculados ao
comprador e ao respectivo brechó. - Disponibilizar histórico e status de
pedidos para compradores e vendedores. - Atender aos requisitos de
validação, persistência, desempenho e manutenibilidade.

5. Público-alvo e partes interessadas

- Compradores: procuram produtos usados, consultam ofertas,
  selecionam itens e acompanham pedidos.

- Vendedores/lojistas: pessoas ou responsáveis por brechós que
  divulgam e gerenciam produtos.

- Equipe de desenvolvimento: especifica, implementa, testa e
  mantém a solução.

- Responsável pelo projeto: prioriza necessidades e valida
  decisões de escopo.

A possibilidade de vendedores individuais anunciarem sem cadastrar um
brechó permanece pendente.

6. Escopo
Funcionalidades previstas

- Cadastro, autenticação e gerenciamento de perfil.

- Cadastro e gerenciamento de brechós.

- Cadastro, atualização e remoção de produtos do catálogo ativo.

- Catálogo público organizado por brechó ou categoria.

- Pesquisa por palavras-chave.

- Filtros por faixa de preço, tamanho, categoria de vestuário e
  brechó.

- Gestão do carrinho.

- Finalização e registro de pedidos.

- Histórico e status de pedidos para compradores e vendedores.

Fora do escopo inicial, salvo decisão posterior
Aplicativo móvel nativo, recomendações personalizadas, programa de
fidelidade, integração com múltiplas transportadoras e processamento de
pagamento real caso o projeto se limite ao registro de pedidos.
Pontos não definidos

- Catálogo exclusivo para roupas ou aberto a diferentes produtos
  usados.

- Obrigatoriedade de cadastro de brechó para vendedores.

- Checkout limitado ao registro do pedido ou com pagamento integrado.

- Tratamento de frete, entrega, cancelamentos e atualização de status.

- Carrinhos com produtos de diferentes brechós.

7. Benefícios esperados

- Centralização de anúncios de produtos usados.

- Facilidade para localizar produtos por pesquisa e filtros.

- Maior visibilidade para vendedores e brechós.

- Organização do gerenciamento de anúncios e pedidos.

- Incentivo ao reaproveitamento de produtos.

- Base técnica para evolução futura.

8. Premissas e restrições

Premissas: usuários terão dispositivo compatível e internet;
vendedores fornecerão informações corretas; os requisitos aprovados
orientarão o desenvolvimento; a solução deverá contemplar armazenamento
em memória e SQLite.

Restrições: prazo, equipe e recursos disponíveis; regras de cadastro
definidas nos requisitos; separação entre responsabilidades de negócio,
validação e persistência.

9. Requisitos de qualidade relacionados à visão

Os requisitos não funcionais originais estabelecem alternância entre
memória e SQLite por repositórios; validação cadastral antes da
persistência; tempo inferior a 200 ms para consultas e filtros em
condições normais; e organização em responsabilidades como model,
repository, service e validator, buscando baixo acoplamento e alta
coesão. Os critérios detalhados devem permanecer no documento de RNF.

10. Critérios gerais de sucesso

Os fluxos de cadastro, gerenciamento de anúncios, navegação no catálogo,
pesquisa, carrinho, registro de pedidos e consulta ao histórico deverão
ser executáveis e testados. Também deverá ser demonstrado o atendimento
aos critérios aprovados de validação, persistência, desempenho e
organização arquitetural.
