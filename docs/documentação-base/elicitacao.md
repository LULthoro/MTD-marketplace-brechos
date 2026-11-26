Versão: 1.2 | Status: Revisado aguardando aprovação |
Data: 18/10
/2026
1. Objetivo e nota de integridade
Registrar as necessidades e informações utilizadas para identificar os
requisitos, suas fontes, ambiguidades e decisões pendentes.

Este documento foi elaborado a partir dos requisitos funcionais e não
funcionais fornecidos e das discussões sobre o projeto. Não há
comprovação disponível de entrevistas, questionários ou observações
realizadas com usuários; portanto, essas atividades não são apresentadas
como já executadas.

2. Contexto
O projeto pretende desenvolver um marketplace para divulgação e
comercialização de produtos usados, inicialmente pensado para peças de
vestuário e brechós. A plataforma deverá permitir cadastro,
gerenciamento de lojas e anúncios, pesquisa de produtos, carrinho,
registro de pedidos e consulta ao histórico.

3. Partes interessadas
  Stakeholder                         Interesse ou necessidade
  Comprador                           Encontrar produtos, pesquisar,
filtrar, selecionar itens e
consultar pedidos.

  Vendedor/lojista                    Cadastrar e administrar loja e
produtos, além de acompanhar
pedidos.

  Equipe de desenvolvimento           Compreender regras, implementar e
testar o sistema.
Responsável pelo projeto            Validar escopo, prioridades e
decisões pendentes.
A distinção entre vendedor individual e responsável por brechó permanece
em aberto.

4. Fontes consideradas
- Documento original de Requisitos Funcionais (RF01--RF13).
- Documento original de Requisitos Não Funcionais (RNF01--RNF04).
- Discussões de definição do produto, Documento de Visão e TAP.
- Revisões sugeridas dos requisitos, consideradas propostas para
  validação, não decisões automaticamente aprovadas.

5. Técnicas de elicitação
As informações disponíveis foram consolidadas por análise documental e
discussão do escopo. Para completar a elicitação com usuários reais,
recomenda-se: - Entrevistas com compradores sobre busca, filtros e
informações necessárias antes da compra. - Entrevistas com vendedores
sobre publicação, disponibilidade e administração de pedidos. -
Questionários sobre categorias, filtros e experiência de compra. -
Análise de cenários como item vendido fora da plataforma,
indisponibilidade no checkout e carrinho com lojas diferentes.

Essas são atividades recomendadas para o futuro; não há registro de
execução neste levantamento.

6. Necessidades identificadas
  ID                Necessidade         Origem            Requisito
relacionado
  NEC01             Criar conta e       RF01--RF03        Cadastro,
acessar                               autenticação e
funcionalidades                       perfil
associadas.                           

  NEC02             Cadastrar e         RF04--RF05        Gestão de loja
administrar brechó.                   

  NEC03             Publicar e manter   RF06--RF07        Gestão de produtos
anúncios.                             

  NEC04             Consultar produtos  RF08              Catálogo
disponíveis.                          

  NEC05             Localizar produtos  RF09              Pesquisa
por palavras-chave.                   

  NEC06             Refinar resultados  RF10              Filtros
por preço, tamanho,
categoria e brechó.                   

  NEC07             Selecionar e        RF11              Carrinho
remover produtos
antes da compra.                      

  NEC08             Registrar pedidos   RF12              Checkout
associados ao
comprador e ao
brechó.                               

  NEC09             Consultar histórico RF13              Histórico
e status dos
pedidos.                              

  NEC10             Alternar            RNF01             Persistência
persistência em
memória e SQLite.                     

  NEC11             Validar entradas    RNF02             Validação
cadastrais antes da
persistência.                         

  NEC12             Consultar e filtrar RNF03             Desempenho
catálogo em menos
de 200 ms nas
condições
definidas.                            
NEC13             Manter separação de RNF04             Manutenibilidade
responsabilidades e
baixo acoplamento.

7. Requisitos levantados

7.1 Funcionais
- RF01 --- Cadastro de usuários: registro de compradores e
  vendedores/brechós com validação dos dados obrigatórios.
- RF02 --- Autenticação: login para acesso às contas.
- RF03 --- Gerenciamento de perfil: consulta e atualização de
  informações cadastrais.
- RF04 --- Cadastro de brechó: cadastro de nome comercial,
  descrição e políticas de troca/envio.
- RF05 --- Gerenciamento da loja: edição das informações e
  gerenciamento da vitrine.
- RF06 --- Cadastro de peças/produtos: publicação de itens de
  vestuário com título, descrição, categoria, tamanho, estado de
  conservação, preço e imagens.
- RF07 --- Atualização e remoção: alteração de dados ou remoção do
  catálogo ativo.
- RF08 --- Catálogo: vitrine pública organizada por brechó ou
  categoria.
- RF09 --- Pesquisa: busca por palavras-chave.
- RF10 --- Filtragem: faixa de preço, tamanho, categoria de
  vestuário e brechó.
- RF11 --- Carrinho: adicionar e remover produtos antes da
  finalização.
- RF12 --- Checkout: registrar pedido vinculado ao comprador e ao
  respectivo brechó.
- RF13 --- Histórico: consulta de histórico e status por
  compradores e vendedores.

7.2 Não funcionais
- RNF01 --- Persistência pluggável: alternância entre memória e
  SQLite por interfaces/repositórios.
- RNF02 --- Validação: nome não vazio, sem números e com até 12
  caracteres; senha entre 8 e 128 caracteres, contendo pelo menos três
  dos quatro grupos (maiúsculas, minúsculas, números e caracteres
  especiais), e diferente do nome e do e-mail.
- RNF03 --- Desempenho: consultas e filtros em menos de 200 ms em
  condições normais de uso.
- RNF04 --- Extensibilidade e manutenibilidade: organização em
  model, repository, service e validator, com baixo acoplamento e alta
  coesão.
