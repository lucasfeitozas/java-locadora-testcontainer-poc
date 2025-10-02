-- Dados de teste para filmes
INSERT INTO filme (id, nome, diretor, ano, detalhes)
VALUES 
    ('550e8400-e29b-41d4-a716-446655440001', 'Matrix', 'Lana Wachowski', 1999, 
     '{
        "atores": ["Keanu Reeves", "Laurence Fishburne", "Carrie-Anne Moss"],
        "sinopse": "Um hacker descobre a verdade sobre sua realidade",
        "genero": "Ficção Científica",
        "duracao": 136,
        "classificacao": "PG-13",
        "idioma": "Inglês",
        "pais": "EUA"
     }'::jsonb),
    
    ('550e8400-e29b-41d4-a716-446655440002', 'Cidade de Deus', 'Fernando Meirelles', 2002,
     '{
        "atores": ["Alexandre Rodrigues", "Leandro Firmino", "Phellipe Haagensen"],
        "sinopse": "A história da criminalidade na Cidade de Deus",
        "genero": "Drama",
        "duracao": 130,
        "classificacao": "18",
        "idioma": "Português",
        "pais": "Brasil"
     }'::jsonb);

-- Dados de teste para clientes
INSERT INTO cliente (id, nome, email, telefone, cpf)
VALUES 
    ('660e8400-e29b-41d4-a716-446655440001', 'João Silva', 'joao.silva@email.com', '11999999999', '12345678901'),
    ('660e8400-e29b-41d4-a716-446655440002', 'Maria Santos', 'maria.santos@email.com', '11888888888', '98765432109');

-- Dados de teste para locações
INSERT INTO locacao (id, cliente_id, filme_id, data_locacao, data_prevista_devolucao, data_devolucao, status, valor_locacao, valor_multa)
VALUES 
    ('770e8400-e29b-41d4-a716-446655440001', 
     '660e8400-e29b-41d4-a716-446655440001', 
     '550e8400-e29b-41d4-a716-446655440001',
     '2024-01-15', '2024-01-22', NULL, 'ATIVA'::status_locacao, 15.00, 0.00),
    
    ('770e8400-e29b-41d4-a716-446655440002',
     '660e8400-e29b-41d4-a716-446655440002',
     '550e8400-e29b-41d4-a716-446655440002',
     '2024-01-10', '2024-01-17', '2024-01-20', 'DEVOLVIDA'::status_locacao, 12.00, 3.60);