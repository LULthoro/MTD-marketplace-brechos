Versão: 1.2 | Status: Aguardando validação de alterações |
Data: 11/11/2026

1. Objetivo
Padronizar os principais termos utilizados na documentação e no
desenvolvimento. As definições representam o significado pretendido no
contexto do projeto e devem ser revisadas quando decisões de escopo
forem aprovadas.

2. Termos do domínio
  Termo                   Definição no projeto    Observações
  Marketplace             Plataforma digital que  Refere-se ao sistema
aproxima compradores e  como um todo.
vendedores e reúne
anúncios e
funcionalidades de
compra.                 

  Usuário                 Pessoa cadastrada que   Pode atuar como
utiliza o sistema.      comprador ou vendedor,
conforme permissões
definidas.

  Comprador               Usuário que pesquisa    Perfil funcional.
produtos, utiliza o
carrinho e realiza
pedidos.                

  Vendedor                Usuário responsável por Ainda é necessário
anunciar e              definir se pode
comercializar produtos. anunciar sem loja.

  Brechó                  Loja ou estabelecimento Obrigatoriedade de
associado a um          cadastro pendente.
vendedor, que reúne e
divulga produtos
usados.                 

  Loja                    Perfil comercial que    Pode ser tratado como
apresenta informações   equivalente a brechó.
do brechó e sua
vitrine.                

  Perfil                  Conjunto de informações O usuário pode
cadastrais associadas a consultar e atualizar
um usuário.             dados permitidos.

  Produto                 Item anunciado para     Os RF originais
consulta e possível     enfatizam vestuário.
compra.                 

  Peça                    Produto de vestuário    Termo dos requisitos
anunciado no sistema.   originais para roupas.

  Anúncio                 Publicação de um        Remover do catálogo não
produto no catálogo,    implica necessariamente
com informações e       apagar o registro.
imagens.                

  Catálogo                Vitrine pública que     Pode ser organizado por
reúne produtos          brechó ou categoria.
disponíveis.            

  Vitrine                 Apresentação visual dos Usada para navegação e
produtos de uma loja ou descoberta.
do marketplace.         

  Categoria               Classificação utilizada Exemplos de vestuário
para organizar          ainda dependem do
produtos.               catálogo definido.

  Tamanho                 Informação de           Um dos filtros
dimensão/numeração de   previstos.
uma peça.               

  Estado de conservação   Indicação da condição   Exemplo citado:
física do produto       seminovo.
usado.                  

  Pesquisa por            Busca de produtos a     Pode considerar título
palavras-chave          partir de termos        ou descrição conforme
informados pelo         detalhamento do
usuário.                requisito.

  Filtro                  Critério aplicado ao    Preço, tamanho,
catálogo para           categoria e brechó.
restringir produtos
exibidos.               

  Faixa de preço          Intervalo de valores    Um dos filtros
usado para restringir   previstos.
resultados.             

  Carrinho                Lista temporária de     Não é, por si só, um
produtos selecionados   pedido confirmado.
antes da finalização.   

  Subtotal                Soma dos valores dos    Atualizado ao
produtos presentes no   adicionar/remover
carrinho antes de       itens.
eventuais custos
adicionais.             

  Checkout                Etapa de finalização em Pagamento real ainda
que os itens            não foi definido.
selecionados são
encaminhados para
registro do pedido.     

  Pedido                  Registro de uma         Significado de "compra
solicitação de compra,  processada" precisa ser
associado ao comprador  validado.
e ao brechó
correspondente.         

  Histórico de pedidos    Área em que compradores Previsto no RF13.
e vendedores consultam
pedidos anteriores e
seus status.            

  Status do pedido        Estado atual de um      Estados e regras de
pedido durante seu      atualização pendentes.
ciclo de vida.          

  Persistência            Armazenamento de dados  Projeto prevê memória e
para recuperação        SQLite.
posterior.              

  Armazenamento em        Armazenamento           Previsto para
memória (RAM)           temporário durante a    desenvolvimento e
execução, sem garantia  testes.
de persistência após
encerramento.           

  SQLite                  Banco de dados          Previsto no RNF01.
relacional utilizado
como opção de
persistência.           

  Repositório             Abstração/componente    Desacopla serviços da
(Repository)            que intermedeia o       implementação de
acesso e a persistência armazenamento.
dos dados.              

  Validação               Verificação de dados e  Deve ocorrer antes da
regras antes de aceitar persistência.
uma operação.           

  Desempenho              Capacidade de executar  RNF03 estabelece menos
operações dentro de     de 200 ms para
limites de tempo        consultas e filtros.
definidos.              

  Manutenibilidade        Facilidade de           Relacionada à separação
compreender, corrigir e de responsabilidades.
evoluir o código.       

  Baixo acoplamento       Característica de       Objetivo arquitetural
componentes que         do RNF04.
dependem pouco das
implementações internas
uns dos outros.         
Alta coesão             Característica de um    Objetivo arquitetural
componente cujas        do RNF04.
responsabilidades estão
relacionadas entre si.

3. Siglas e termos técnicos
  Sigla/termo                         Significado
  RF                                  Requisito Funcional: descreve
funcionalidade ou comportamento que
o sistema deve oferecer.

  RNF                                 Requisito Não Funcional: descreve
condição de qualidade, restrição ou
característica técnica.

  TAP                                 Termo de Abertura do Projeto:
documento que formaliza a proposta
e estabelece referência inicial.

  RAM                                 Random Access Memory; memória de
acesso aleatório, utilizada como
armazenamento temporário durante a
execução.

  SQLite                              Sistema de gerenciamento de banco
de dados relacional incorporável à
aplicação.

  Model                               Camada/pacote que representa dados
e entidades do domínio.

  Repository                          Camada/pacote responsável pelo
acesso e armazenamento de dados.

  Service                             Camada/pacote que concentra
operações e regras de negócio.

  Validator                           Componente/camada responsável pela
validação de entradas e regras.
Checkout                            Processo de finalização do carrinho
e registro da compra/pedido.


5. Convenções de uso
- Utilizar produto como termo genérico e peça quando o
  contexto tratar especificamente de vestuário.
- Utilizar brechó ou loja para o perfil comercial, mantendo um
  termo predominante na interface após decisão da equipe.
- Diferenciar carrinho (seleção temporária) de pedido
  (registro gerado na finalização).
- Diferenciar remover do catálogo ativo de excluir
  permanentemente um registro; o comportamento definitivo precisa
  ser especificado.
- Não utilizar "pagamento aprovado" como sinônimo de "pedido
  registrado" sem integração de pagamento e confirmação
  correspondente.