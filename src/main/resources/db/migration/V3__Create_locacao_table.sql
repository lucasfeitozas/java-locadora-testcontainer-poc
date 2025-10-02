-- Criação do tipo enum para status da locação
CREATE TYPE status_locacao AS ENUM ('ATIVA', 'DEVOLVIDA', 'ATRASADA', 'CANCELADA');

-- Criação da tabela locacao
CREATE TABLE locacao (
    id UUID PRIMARY KEY,
    cliente_id UUID NOT NULL,
    filme_id UUID NOT NULL,
    data_locacao DATE NOT NULL,
    data_prevista_devolucao DATE NOT NULL,
    data_devolucao DATE,
    status status_locacao NOT NULL DEFAULT 'ATIVA',
    valor_locacao DECIMAL(10,2) NOT NULL,
    valor_multa DECIMAL(10,2) DEFAULT 0.00,
    criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    -- Chaves estrangeiras
    CONSTRAINT fk_locacao_cliente FOREIGN KEY (cliente_id) REFERENCES cliente(id),
    CONSTRAINT fk_locacao_filme FOREIGN KEY (filme_id) REFERENCES filme(id),
    
    -- Constraints de validação
    CONSTRAINT chk_data_prevista_devolucao CHECK (data_prevista_devolucao >= data_locacao),
    CONSTRAINT chk_data_devolucao CHECK (data_devolucao IS NULL OR data_devolucao >= data_locacao),
    CONSTRAINT chk_valor_locacao_positivo CHECK (valor_locacao > 0),
    CONSTRAINT chk_valor_multa_nao_negativo CHECK (valor_multa >= 0)
);

-- Índices para melhor performance
CREATE INDEX idx_locacao_cliente_id ON locacao(cliente_id);
CREATE INDEX idx_locacao_filme_id ON locacao(filme_id);
CREATE INDEX idx_locacao_data_locacao ON locacao(data_locacao);
CREATE INDEX idx_locacao_data_prevista_devolucao ON locacao(data_prevista_devolucao);
CREATE INDEX idx_locacao_status ON locacao(status);
CREATE INDEX idx_locacao_data_devolucao ON locacao(data_devolucao);

-- Índice composto para consultas frequentes
CREATE INDEX idx_locacao_cliente_status ON locacao(cliente_id, status);
CREATE INDEX idx_locacao_filme_status ON locacao(filme_id, status);

-- Comentários para documentação
COMMENT ON TABLE locacao IS 'Tabela para armazenar informações das locações de filmes';
COMMENT ON COLUMN locacao.id IS 'Identificador único da locação (UUID)';
COMMENT ON COLUMN locacao.cliente_id IS 'Referência ao cliente que fez a locação';
COMMENT ON COLUMN locacao.filme_id IS 'Referência ao filme locado';
COMMENT ON COLUMN locacao.data_locacao IS 'Data em que a locação foi realizada';
COMMENT ON COLUMN locacao.data_prevista_devolucao IS 'Data prevista para devolução do filme';
COMMENT ON COLUMN locacao.data_devolucao IS 'Data efetiva de devolução do filme';
COMMENT ON COLUMN locacao.status IS 'Status atual da locação';
COMMENT ON COLUMN locacao.valor_locacao IS 'Valor cobrado pela locação';
COMMENT ON COLUMN locacao.valor_multa IS 'Valor da multa por atraso (se houver)';
COMMENT ON COLUMN locacao.criado_em IS 'Data e hora de criação do registro';
COMMENT ON COLUMN locacao.atualizado_em IS 'Data e hora da última atualização do registro';