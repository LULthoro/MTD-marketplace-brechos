REQUISITOS FUNCIONAIS 

USUARIOS E AUTENTICAÇÃO

RF01 - Cadastro de Usuários: O sistema deve permitir o registro de novos usuários (compradores e vendedores/brechós), 
validando dados obrigatórios (como nome, email e senha) por meio de regras de validação específicas. 

Perfil Único de Conta: Todo usuário cadastrado possui uma conta única que permite atuar simultaneamente 
como comprador e como vendedor, sem a necessidade de criar contas separadas ou fazer alternância (switch) de perfil.

Validação de E-mail Único: O sistema garante a unicidade do e-mail no ato do cadastro. Tentar cadastrar um e-mail já existente gera rejeição imediata.


Cenário 1.1 (Perfil Unificado):

Dado que o visitante está na tela de cadastro,
Quando ele insere o nome "Ana Maria", o e-mail "ana@email.com" e a senha "Senha@123",
Então o sistema realiza o cadastro e libera o acesso tanto para comprar itens quanto para cadastrar anúncios/lojas.

Cenário 1.2 (E-mail Já Cadastrado):

Dado que o e-mail "ana@email.com" já está registrado no sistema,
Quando outro visitante tenta se cadastrar com o mesmo e-mail,
Então o sistema bloqueia a operação e exibe a mensagem: "E-mail já cadastrado no sistema."


RF02 - Autenticação de Usuários: O sistema deve fornecer um mecanismo de login para que os usuários cadastrados acessem suas 
respectivas contas com segurança. 


Cenário 2.1 (Login Bem-Sucedido):
Dado que o usuário possui conta cadastrada,
Quando informa seu e-mail e senha corretos e clica em "Entrar",
Então o sistema autentica a sessão e concede acesso às funcionalidades do seu perfil.


RF03 - Gerenciamento de Perfil: O usuário deve poder consultar e atualizar as informações cadastrais do seu perfil.

Campos Editáveis: O usuário pode atualizar exclusivamente o seu nome comercial/de exibição e o seu e-mail.
O identificador do usuário (ID) e as chaves de histórico de pedidos não podem ser modificados.

Tratamento de Alterações Inválidas: Se o usuário tentar alterar o e-mail para um endereço já utilizado por outra conta,
ou informar um nome que viole as regras de validação (ex: conter números ou ultrapassar o limite),
as alterações são descartadas e o estado anterior do perfil é mantido.


Cenário 3.1 (Edição de Perfil):
Dado que o usuário está autenticado em sua conta,
Quando ele navega até a seção "Meu Perfil", altera seu e-mail e clica em "Salvar Alterações",
Então o sistema valida os novos dados e atualiza o cadastro com sucesso.

Cenário 3.2 (Tentativa de E-mail Duplicado no Perfil):
Dado que o e-mail "novo@email.com" pertence a outro usuário,
Quando o usuário logado tenta atualizar seu e-mail para "novo@email.com",
Então o sistema recusa a edição e exibe a mensagem de erro: "Este e-mail já está em uso por outra conta."

LOJAS / BRECHOS

RF04 - Cadastro de Brechó: O usuário com perfil de vendedor deve poder cadastrar os dados do seu brechó
(nome comercial, descrição, políticas de troca/envio).

Cenário 4.1 (Criação de Loja):
Dado que o usuário vendedor ainda não possui loja cadastrada,
Quando ele preenche o nome comercial "Vintage Club", descrição e políticas de entrega,
Então o sistema cadastra o brechó e vincula a loja ao perfil do vendedor.


RF05 - Gerenciamento da Loja: O lojista deve conseguir editar as informações do seu brechó e gerenciar a vitrine de seus produtos.

Cenário 5.1 (Atualização de Dados da Loja):
Dado que o lojista deseja atualizar suas políticas de devolução,
Quando acessa o painel do brechó, insere o novo texto e salva,
Então a informação atualizada passa a ser exibida publicamente para os compradores na página da loja.

PRODUTOS E CATALOGO 

RF06 - Cadastro de Peças/Produtos: O lojista deve poder cadastrar novos itens de vestuário informando título, descrição detalhada, categoria, 
tamanho, estado de conservação, preço e imagens ilustrativas.

Cenário 6.1 (Publicação de Peça Unipessoal):
Dado que o lojista acessa a aba "Novo Produto",
Quando preenche os dados: "Jaqueta Jeans Anos 90", categoria "Casacos", tamanho "M", estado "Seminovo", preço R$ 120,00 e envia as fotos,
Então a peça é salva e adicionada imediatamente à vitrine ativa da loja.

RF07 - Atualização e Remoção de Produtos: O vendedor deve ter a capacidade de alterar os dados de um produto existente ou removê-lo do catálogo ativo.

Cenário 7.1 (Remoção/Baixa de Item):
Dado que uma peça cadastrada foi vendida fora da plataforma,
Quando o vendedor seleciona o item e clica em "Remover do Catálogo",
Então o produto deixa de ser exibido na vitrine e nas buscas.

RF08 - Visualização do Catálogo: O sistema deve exibir uma vitrine pública com todos os produtos disponíveis na plataforma, organizados por brechós ou categorias.

Cenário 8.1 (Navegação na Vitrine):
Dado que qualquer visitante acessa a página inicial do marketplace,
Quando navega pela vitrine,
Então o sistema exibe os produtos disponíveis organizados em cards com imagem, título, preço, tamanho e nome do brechó.

BUSCA E FILTRO

RF09 - Pesquisa de Produtos: Os usuários devem poder buscar peças específicas utilizando barra de pesquisa por palavras-chave.

Cenário 9.1 (Caminho Feliz - Busca por Palavra-Chave):
Dado que o comprador digita "Vestido Floral" na barra de busca,
Quando confirma a pesquisa,
Então o sistema exibe a lista de produtos cujos títulos ou descrições contenham o termo digitado.

RF10 - Filtragem de Itens: O sistema deve disponibilizar filtros por faixa de preço, tamanho,categoria de vestuário e brechó de origem para facilitar a navegação do comprador.

Cenário 10.1 (Aplicação de Filtros Combinados):
Dado que o comprador está navegando no catálogo geral,
Quando seleciona a categoria "Calças", tamanho "G" e faixa de preço "Até R$ 80,00",
Então o sistema recarrega a exibição trazendo apenas os itens que cumprem simultaneamente todos os critérios aplicados.

CARRINHO E PEDIDO

RF11 - Gestão do Carrinho de Compras: O comprador deve poder adicionar ou remover produtos selecionados em seu carrinho de compras 
antes de finalizar a transação.

Cenário 11.1 (Adição ao Carrinho):
Dado que o comprador está na página do detalhe de um produto,
Quando clica no botão "Adicionar ao Carrinho",
Então o item é incluído na lista temporária de compras e o subtotal é atualizado.

RF12 - Finalização de Pedido (Checkout): O sistema deve processar a compra dos itens selecionados no carrinho, registrando o pedido 
vinculado ao comprador e ao respectivo brechó.

Cenário 12.1 (Finalização com Sucesso):
Dado que o comprador possui itens no carrinho e está autenticado,
Quando ele confirma o endereço de entrega, escolhe a forma de pagamento e clica em "Finalizar Pedido",
Então o sistema gera o número do pedido, limpa o carrinho e envia o pedido para a fila de processamento do brechó vendedor.

RF13 - Histórico de Pedidos: Tanto compradores quanto vendedores devem ter acesso a uma área com o histórico e o status dos pedidos realizados. 

Cenário 13.1 (Consulta de Histórico pelo Comprador):
Dado que o comprador acessa a aba "Meus Pedidos",
Quando a página carrega,
Então o sistema exibe a listagem dos seus pedidos ordenados por data, mostrando o status (ex: "Aguardando Envio", "Entregue"), itens compondo cada pedido e valor total.
