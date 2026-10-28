REQUISITOS FUNCIONAIS 

Usuários e Autenticação 

RF01 - Cadastro de Usuários: O sistema deve permitir o registro de novos usuários (compradores e vendedores/brechós), 
validando dados obrigatórios (como nome, email e senha) por meio de regras de validação específicas. 


Cenário 1.1 (Cadastro de Sucesso):

Dado que o visitante está na tela de cadastro,
Quando ele insere o nome "Ana Maria", e-mail "ana@email.com" e a senha "Senha@123" (atendendo ao mínimo de 8 caracteres e 3 tipos de caracteres),
Então o sistema realiza o cadastro com sucesso e o redireciona para a tela de login/boas-vindas.


RF02 - Autenticação de Usuários: O sistema deve fornecer um mecanismo de login para que os usuários cadastrados acessem suas 
respectivas contas com segurança. 


Cenário 2.1 (Login Bem-Sucedido):

Dado que o usuário possui conta cadastrada,
Quando informa seu e-mail e senha corretos e clica em "Entrar",
Então o sistema autentica a sessão e concede acesso às funcionalidades do seu perfil.





RF03 - Gerenciamento de Perfil: O usuário deve poder consultar e atualizar as informações cadastrais do seu perfil.


Cenário 3.1 (Edição de Perfil):

Dado que o usuário está autenticado em sua conta,
Quando ele navega até a seção "Meu Perfil", altera seu e-mail e clica em "Salvar Alterações",
Então o sistema valida os novos dados e atualiza o cadastro com sucesso.






Lojas / Brechós

RF04 - Cadastro de Brechó: O usuário com perfil de vendedor deve poder cadastrar os dados do seu brechó
(nome comercial, descrição, políticas de troca/envio).

Cenário 4.1 (Criação de Loja):

Dado que o usuário vendedor ainda não possui loja cadastrada,
Quando ele preenche o nome comercial "Vintage Club", descrição e políticas de entrega,
Então o sistema cadastra o brechó e vincula a loja ao perfil do vendedor.Cenário 4.1 (Caminho Feliz - Criação de Loja):
Dado que o usuário vendedor ainda não possui loja cadastrada,
Quando ele preenche o nome comercial "Vintage Club", descrição e políticas de entrega,
Então o sistema cadastra o brechó e vincula a loja ao perfil do vendedor.



RF05 - Gerenciamento da Loja: O lojista deve conseguir editar as informações do seu brechó e gerenciar a vitrine de seus produtos.

Cenário 5.1 (Atualização de Dados da Loja):

Dado que o lojista deseja atualizar suas políticas de devolução,
Quando acessa o painel do brechó, insere o novo texto e salva,
Então a informação atualizada passa a ser exibida publicamente para os compradores na página da loja

Produtos e Catálogo

RF06 - Cadastro de Peças/Produtos: O lojista deve poder cadastrar novos itens de vestuário informando título, descrição detalhada, categoria, 
tamanho, estado de conservação, preço e imagens ilustrativas.

Cenário 6.1 (Publicação de Peça Unipessoal):

Dado que o lojista acessa a aba "Novo Produto",
Quando preenche os dados: "Jaqueta Jeans Anos 90", categoria "Casacos", tamanho "M", estado "Seminovo", preço R$ 120,00 e envia as fotos,
Então a peça é salva e adicionada imediatamente à vitrine ativa da loja.

RF07 - Atualização e Remoção de Produtos: O vendedor deve ter a capacidade de alterar os dados de um produto existente ou removê-lo do catálogo ativo.

RF08 - Visualização do Catálogo: O sistema deve exibir uma vitrine pública com todos os produtos disponíveis na plataforma, organizados por brechós ou categorias.

Busca e Filtros

RF09 - Pesquisa de Produtos: Os usuários devem poder buscar peças específicas utilizando barra de pesquisa por palavras-chave.

RF10 - Filtragem de Itens: O sistema deve disponibilizar filtros por faixa de preço, tamanho,categoria de vestuário e brechó de origem para facilitar a navegação do comprador. 


Carrinho e Pedidos

RF11 - Gestão do Carrinho de Compras: O comprador deve poder adicionar ou remover produtos selecionados em seu carrinho de compras 
antes de finalizar a transação.

RF12 - Finalização de Pedido (Checkout): O sistema deve processar a compra dos itens selecionados no carrinho, registrando o pedido 
vinculado ao comprador e ao respectivo brechó.

RF13 - Histórico de Pedidos: Tanto compradores quanto vendedores devem ter acesso a uma área com o histórico e o status dos pedidos realizados. 
Busca e Filtros
