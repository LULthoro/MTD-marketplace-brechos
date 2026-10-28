REQUISITOS NAO FUNCIONAIS 

RNF01 – Persistência Pluggável / Suporte a Múltiplos Motores (Arquitetura & Design)
Descrição: A arquitetura do sistema deve suportar a alternância entre modos de persistência (em memória RAM para testes/desenvolvimento e banco de dados relacional SQLite para produção/persistência).

Métrica/Critério: Implementação do padrão Repository com interfaces abstratas aplicadas a todas as entidades (Usuários, Lojas, Produtos e Pedidos), permitindo o chaveamento global do motor de dados no arranque da aplicação via parâmetro de inicialização (mvn compile exec:java "-Dexec.mainClass=mtd.Main" "-Dexec.args=bd").

RNF02 – Regras Rigorosas de Validação de Dados (Segurança / Usabilidade)
Descrição: As entradas de dados cadastrais devem ser validadas no nível da aplicação (Validator) antes de qualquer tentativa de persistência.

Métrica/Critério:
O Nome Completo não pode ser vazio, conter números nem ultrapassar 100 caracteres (caso seja adotado um Username/Apelido, o limite é de até 12 caracteres sem números).
A Senha deve ter comprimento entre 8 e 128 caracteres e conter obrigatoriamente pelo menos 3 dos 4 tipos de caracteres: maiúsculas, minúsculas, números e caracteres especiais.
A senha não pode ser idêntica ao nome nem ao e-mail informado.

RNF03 – Desempenho e Tempo de Resposta (Eficiência)
Descrição: As operações de consulta e filtragem do catálogo de produtos no backend devem apresentar tempo de resposta reduzido.

Métrica/Critério: Consultas de busca e ordenação no backend (seja em RAM ou em SQLite) devem ser processadas em tempo inferior a 200 ms em ambiente de teste com massa de dados de até 1.000 produtos cadastrados.

RNF04 – Extensibilidade e Manutenibilidade (Arquitetura de Software)
Descrição: O código-fonte deve ser organizado em uma arquitetura em camadas clara e de baixo acoplamento (model, repository, service, validator), alinhada ao processo de Spec-Driven Development (SDD).

Métrica/Critério: Alterações nas regras de negócio/validação não devem impactar a camada de persistência e vice-versa. A inclusão de um novo motor de dados deve exigir apenas a criação de uma nova implementação de repositório, sem alterar os serviços da aplicação.


RNF05 — Proteção e Criptografia de Senhas (Segurança)
Descrição: As senhas dos usuários nunca devem ser armazenadas ou trafegadas em texto simples (plain text) na camada de persistência.

Métrica/Critério: As senhas devem passar por algoritmo de hashing seguro (ex: BCrypt, Argon2 ou SHA-256 com salt) 
antes de serem gravadas no banco de dados SQLite ou mantidas na memória.

RNF06 — Integridade e Imutabilidade de Pedidos (Integridade de Dados)
Descrição: Após a finalização de uma compra (Checkout), os dados financeiros e os itens do histórico de transações tornam-se imutáveis.

Métrica/Critério: Registros de pedidos finalizados (itens, preços unitários e comprador) não podem ser alterados ou excluídos. Alterações de fluxo operacionais (ex: transição de status para "Enviado" ou "Entregue") devem ser registradas exclusivamente no histórico de auditoria do pedido.
