# Exemplos de Consultas JSONB - Sistema de Locadora

Este documento demonstra como usar consultas JSONB no PostgreSQL para buscar filmes com base em critérios específicos armazenados em campos JSON.

## Estrutura dos Dados

Os filmes são armazenados com detalhes em formato JSONB:

```json
{
  "atores": ["Keanu Reeves", "Laurence Fishburne", "Carrie-Anne Moss"],
  "sinopse": "Um hacker descobre a verdade sobre sua realidade",
  "genero": "Ficção Científica",
  "duracao": 136,
  "classificacao": "PG-13",
  "idioma": "Inglês",
  "pais": "EUA"
}
```

## Métodos Implementados

O sistema possui os seguintes métodos para consultas JSONB:

### Métodos Básicos
- `findByGenero(String genero)` - Busca por gênero
- `findByAtor(String ator)` - Busca por ator específico

### Métodos Avançados
- `findByDuracaoMaiorQue(Integer minutos)` - Busca filmes com duração maior que X minutos
- `findByPais(String pais)` - Busca filmes por país de origem
- `findByClassificacao(String classificacao)` - Busca por classificação etária
- `findByMultiplosAtores(List<String> atores)` - Busca filmes que contenham todos os atores especificados
- `findBySinopseContendo(String palavra)` - Busca por palavra na sinopse

## Exemplos de Consultas Implementadas

### 1. Buscar por Gênero
```sql
SELECT * FROM filme 
WHERE detalhes->>'genero' = 'Ficção Científica';
```

### 2. Buscar por Ator
```sql
SELECT * FROM filme 
WHERE detalhes->'atores' ? 'Keanu Reeves';
```

### 3. Buscar por Duração (filmes com mais de 120 minutos)
```sql
SELECT * FROM filme 
WHERE (detalhes->>'duracao')::int > 120;
```

### 4. Buscar por País
```sql
SELECT * FROM filme 
WHERE detalhes->>'pais' = 'Brasil';
```

### 5. Buscar por Classificação
```sql
SELECT * FROM filme 
WHERE detalhes->>'classificacao' = 'PG-13';
```

## Operadores JSONB Úteis

- `->` : Extrai valor JSON (retorna JSON)
- `->>` : Extrai valor como texto
- `?` : Verifica se uma chave existe
- `?&` : Verifica se todas as chaves existem
- `?|` : Verifica se alguma das chaves existe
- `@>` : Contém (o JSON da esquerda contém o da direita)
- `<@` : Está contido em
- `||` : Concatena JSONs

## Exemplos Avançados

### 4. Busca por Duração (findByDuracaoMaiorQue)
```sql
-- Filmes com duração maior que 120 minutos
SELECT id, nome, diretor, ano, detalhes, criado_em, atualizado_em
FROM filme
WHERE (detalhes->>'duracao')::int > :minutos
ORDER BY (detalhes->>'duracao')::int DESC;
```

### 5. Busca por País (findByPais)
```sql
-- Filmes por país de origem
SELECT id, nome, diretor, ano, detalhes, criado_em, atualizado_em
FROM filme
WHERE detalhes->>'pais' = :pais
ORDER BY nome;
```

### 6. Busca por Classificação (findByClassificacao)
```sql
-- Filmes por classificação etária
SELECT id, nome, diretor, ano, detalhes, criado_em, atualizado_em
FROM filme
WHERE detalhes->>'classificacao' = :classificacao
ORDER BY nome;
```

### 7. Busca por Múltiplos Atores (findByMultiplosAtores)
```sql
-- Filmes que contenham todos os atores especificados
SELECT id, nome, diretor, ano, detalhes, criado_em, atualizado_em
FROM filme
WHERE jsonb_exists(detalhes->'atores', :ator0) 
  AND jsonb_exists(detalhes->'atores', :ator1)
ORDER BY nome;
```

### 8. Busca na Sinopse (findBySinopseContendo)
```sql
-- Busca por palavra na sinopse (case-insensitive)
SELECT id, nome, diretor, ano, detalhes, criado_em, atualizado_em
FROM filme
WHERE detalhes->>'sinopse' ILIKE :palavra
ORDER BY nome;
```

### 9. Busca por Múltiplos Critérios
```sql
-- Filmes de ficção científica com duração maior que 120 minutos
SELECT * FROM filme 
WHERE detalhes->>'genero' = 'Ficção Científica' 
  AND (detalhes->>'duracao')::int > 120;
```

### Buscar filmes com múltiplos atores específicos
```sql
SELECT * FROM filme 
WHERE detalhes->'atores' ?& array['Keanu Reeves', 'Laurence Fishburne'];
```

### Buscar filmes por faixa de duração
```sql
SELECT * FROM filme 
WHERE (detalhes->>'duracao')::int BETWEEN 90 AND 150;
```

### Buscar filmes com sinopse contendo palavra específica
```sql
SELECT * FROM filme 
WHERE detalhes->>'sinopse' ILIKE '%realidade%';
```

### Atualizar dados JSONB
```sql
UPDATE filme 
SET detalhes = detalhes || '{"premio": "Oscar"}'::jsonb 
WHERE id = '550e8400-e29b-41d4-a716-446655440001';
```

## Como Usar no Código Java

### Exemplo no FilmeRepositoryImpl:

```java
@Override
public List<Filme> findByGenero(String genero) {
    String sql = """
        SELECT id, nome, diretor, ano, detalhes, criado_em, atualizado_em
        FROM filme
        WHERE detalhes->>'genero' = ?
        ORDER BY nome
        """;
    
    return jdbcTemplate.query(sql, filmeRowMapper, genero);
}

@Override
public List<Filme> findByAtor(String ator) {
    String sql = """
        SELECT id, nome, diretor, ano, detalhes, criado_em, atualizado_em
        FROM filme
        WHERE detalhes->'atores' ? ?
        ORDER BY nome
        """;
    
    return jdbcTemplate.query(sql, filmeRowMapper, ator);
}
```

## Performance

Para melhor performance em consultas JSONB, considere criar índices:

```sql
-- Índice GIN para consultas gerais em JSONB
CREATE INDEX idx_filme_detalhes_gin ON filme USING GIN (detalhes);

-- Índice específico para gênero
CREATE INDEX idx_filme_genero ON filme USING BTREE ((detalhes->>'genero'));

-- Índice específico para atores
CREATE INDEX idx_filme_atores ON filme USING GIN ((detalhes->'atores'));
```

## Vantagens do JSONB

1. **Flexibilidade**: Permite armazenar dados semi-estruturados
2. **Performance**: Índices GIN permitem consultas rápidas
3. **Validação**: Pode usar constraints para validar estrutura JSON
4. **Atomicidade**: Operações são atômicas
5. **Consultas Complexas**: Suporte a consultas SQL complexas em dados JSON