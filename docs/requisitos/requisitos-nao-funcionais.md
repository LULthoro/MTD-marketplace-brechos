RNF02 – Regras Rigorosas de Validação de Dados (Segurança / Usabilidade)

Descrição: As entradas de dados cadastrais devem ser validadas no nível da aplicação antes de qualquer tentativa de persistência.

Métrica/Critério:

O nome do usuário não pode ser vazio, ter números ou exceder 12 caracteres.

A senha deve ter comprimento entre 8 e 128 caracteres.

A senha deve conter obrigatoriamente pelo menos 3 dos seguintes 4 tipos de caracteres: letras maiúsculas,
letras minúsculas, números e caracteres especiais.

A senha não pode ser idêntica ao nome nem ao e-mail informado.

RNF03 – Desempenho e Tempo de Resposta
Descrição: As operações de consulta e filtragem do catálogo de produtos devem apresentar rápida resposta.

Métrica/Critério: Consultas à base local SQLite e em memória devem ser executadas 
em tempo inferior a 200 ms em condições normais de uso.
