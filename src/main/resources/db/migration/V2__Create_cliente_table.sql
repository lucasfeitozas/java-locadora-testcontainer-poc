-- Criação da tabela cliente
CREATE TABLE cliente (
    id UUID PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    telefone VARCHAR(20),
    cpf VARCHAR(14) NOT NULL UNIQUE,
    criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Índices para melhor performance
CREATE INDEX idx_cliente_nome ON cliente(nome);
CREATE INDEX idx_cliente_email ON cliente(email);
CREATE INDEX idx_cliente_cpf ON cliente(cpf);

-- Comentários para documentação
COMMENT ON TABLE cliente IS 'Tabela para armazenar informações dos clientes da locadora';
COMMENT ON COLUMN cliente.id IS 'Identificador único do cliente (UUID)';
COMMENT ON COLUMN cliente.nome IS 'Nome completo do cliente';
COMMENT ON COLUMN cliente.email IS 'Email do cliente (único)';
COMMENT ON COLUMN cliente.telefone IS 'Telefone de contato do cliente';
COMMENT ON COLUMN cliente.cpf IS 'CPF do cliente (único)';
COMMENT ON COLUMN cliente.criado_em IS 'Data e hora de criação do registro';
COMMENT ON COLUMN cliente.atualizado_em IS 'Data e hora da última atualização do registro';