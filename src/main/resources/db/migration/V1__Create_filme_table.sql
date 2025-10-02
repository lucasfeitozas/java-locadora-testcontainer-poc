-- Criação da tabela filme
CREATE TABLE filme (
    id UUID PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    diretor VARCHAR(255) NOT NULL,
    ano INTEGER NOT NULL,
    detalhes JSONB,
    criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Índices para melhor performance
CREATE INDEX idx_filme_nome ON filme(nome);
CREATE INDEX idx_filme_diretor ON filme(diretor);
CREATE INDEX idx_filme_ano ON filme(ano);

-- Índices GIN para consultas JSONB
CREATE INDEX idx_filme_detalhes_gin ON filme USING GIN (detalhes);

-- Índices específicos para campos do JSONB
CREATE INDEX idx_filme_detalhes_atores ON filme USING GIN ((detalhes->'atores'));
CREATE INDEX idx_filme_detalhes_genero ON filme USING BTREE ((detalhes->>'genero'));
CREATE INDEX idx_filme_detalhes_duracao ON filme USING BTREE (((detalhes->>'duracao')::integer));
CREATE INDEX idx_filme_detalhes_classificacao ON filme USING BTREE ((detalhes->>'classificacao'));

-- Comentários para documentação
COMMENT ON TABLE filme IS 'Tabela para armazenar informações dos filmes da locadora';
COMMENT ON COLUMN filme.id IS 'Identificador único do filme (UUID)';
COMMENT ON COLUMN filme.nome IS 'Nome do filme';
COMMENT ON COLUMN filme.diretor IS 'Nome do diretor do filme';
COMMENT ON COLUMN filme.ano IS 'Ano de lançamento do filme';
COMMENT ON COLUMN filme.detalhes IS 'Detalhes do filme em formato JSON (atores, sinopse, gênero, etc.)';
COMMENT ON COLUMN filme.criado_em IS 'Data e hora de criação do registro';
COMMENT ON COLUMN filme.atualizado_em IS 'Data e hora da última atualização do registro';