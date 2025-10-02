# Sistema de Locadora - POC com TestContainers

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2-green.svg)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue.svg)](https://www.postgresql.org/)
[![TestContainers](https://img.shields.io/badge/TestContainers-1.19-yellow.svg)](https://www.testcontainers.org/)

## 📋 Sobre o Projeto

Este é um sistema de locadora de filmes desenvolvido como Proof of Concept (POC) para demonstrar o uso de **TestContainers** em testes de integração com Spring Boot e PostgreSQL. O projeto implementa funcionalidades completas de CRUD para clientes, filmes e locações, com foco especial em consultas JSONB avançadas.

## 🏗️ Arquitetura

O projeto segue os princípios de **Domain-Driven Design (DDD)** e **Clean Architecture**:

```
src/main/java/com/locadora/
├── cliente/
│   ├── domain/          # Entidades e regras de negócio
│   ├── infrastructure/  # Implementação de repositórios
│   └── presentation/    # Controllers REST
├── filme/
│   ├── domain/
│   ├── infrastructure/
│   └── presentation/
├── locacao/
│   ├── domain/
│   ├── infrastructure/
│   └── presentation/
└── config/             # Configurações da aplicação
```

## 🚀 Tecnologias Utilizadas

- **Java 17** - Linguagem de programação
- **Spring Boot 3.2** - Framework principal
- **Spring Data JDBC** - Acesso a dados
- **PostgreSQL 15** - Banco de dados principal
- **TestContainers** - Testes de integração com containers
- **JUnit 5** - Framework de testes
- **AssertJ** - Assertions para testes
- **Maven** - Gerenciamento de dependências
- **Docker** - Containerização

## 🎯 Funcionalidades

### 👥 Gestão de Clientes
- ✅ Cadastro de clientes
- ✅ Consulta por ID
- ✅ Listagem de todos os clientes
- ✅ Busca por nome
- ✅ Atualização de dados
- ✅ Exclusão de clientes

### 🎬 Gestão de Filmes
- ✅ Cadastro de filmes com detalhes JSONB
- ✅ Consulta por ID
- ✅ Listagem de todos os filmes
- ✅ Busca por nome, diretor, ano
- ✅ **Consultas JSONB avançadas**:
  - Busca por gênero
  - Busca por ator
  - Busca por duração maior que X minutos
  - Busca por país de origem
  - Busca por classificação etária
  - Busca por múltiplos atores
  - Busca na sinopse
- ✅ Atualização de filmes
- ✅ Exclusão de filmes

### 📅 Gestão de Locações
- ✅ Criação de locações
- ✅ Consulta por ID
- ✅ Listagem de todas as locações
- ✅ Devolução de filmes
- ✅ Cálculo automático de multas por atraso
- ✅ Validação de clientes e filmes existentes

## 🗄️ Estrutura do Banco de Dados

### Tabela `cliente`
```sql
CREATE TABLE cliente (
    id UUID PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    telefone VARCHAR(20),
    cpf VARCHAR(11) UNIQUE NOT NULL,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### Tabela `filme`
```sql
CREATE TABLE filme (
    id UUID PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    diretor VARCHAR(255) NOT NULL,
    ano INTEGER NOT NULL,
    detalhes JSONB,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### Tabela `locacao`
```sql
CREATE TABLE locacao (
    id UUID PRIMARY KEY,
    cliente_id UUID NOT NULL REFERENCES cliente(id),
    filme_id UUID NOT NULL REFERENCES filme(id),
    data_locacao DATE NOT NULL,
    data_prevista_devolucao DATE NOT NULL,
    data_devolucao DATE,
    status status_locacao NOT NULL,
    valor_locacao DECIMAL(10,2) NOT NULL,
    valor_multa DECIMAL(10,2) DEFAULT 0.00,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

## 🔍 Consultas JSONB

O projeto demonstra o poder das consultas JSONB do PostgreSQL. Veja exemplos detalhados no arquivo [JSONB_EXAMPLES.md](./JSONB_EXAMPLES.md).

### Exemplo de Dados JSONB
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

## 🧪 Testes

O projeto possui **51 testes de integração** que utilizam TestContainers para garantir a qualidade:

- **FilmeRepositoryIntegrationTest**: 19 testes
- **ClienteControllerIntegrationTest**: 11 testes  
- **FilmeControllerIntegrationTest**: 8 testes
- **LocacaoControllerIntegrationTest**: 13 testes

### Executar Testes
```bash
# Executar todos os testes
mvn test

# Executar testes específicos
mvn test -Dtest=FilmeRepositoryIntegrationTest
```

## 🚀 Como Executar

### Pré-requisitos
- Java 17+
- Maven 3.8+
- Docker (para testes e execução com containers)

### Execução Local
```bash
# Clonar o repositório
git clone git@github.com:lucasfeitozas/java-locadora-testcontainer-poc.git
cd java-locadora-testcontainer-poc

# Compilar o projeto
mvn clean compile

# Executar testes
mvn test

# Executar a aplicação (requer PostgreSQL local)
mvn spring-boot:run
```

### Execução com Docker
```bash
# Construir e executar com docker-compose
docker-compose up --build

# A aplicação estará disponível em http://localhost:8080
```

## 📚 API Endpoints

### Clientes
- `GET /api/clientes` - Listar todos os clientes
- `GET /api/clientes/{id}` - Buscar cliente por ID
- `GET /api/clientes/buscar?nome={nome}` - Buscar por nome
- `POST /api/clientes` - Criar novo cliente
- `PUT /api/clientes/{id}` - Atualizar cliente
- `DELETE /api/clientes/{id}` - Excluir cliente

### Filmes
- `GET /api/filmes` - Listar todos os filmes
- `GET /api/filmes/{id}` - Buscar filme por ID
- `GET /api/filmes/buscar?nome={nome}` - Buscar por nome
- `GET /api/filmes/diretor/{diretor}` - Buscar por diretor
- `GET /api/filmes/ano/{ano}` - Buscar por ano
- `GET /api/filmes/genero/{genero}` - Buscar por gênero
- `GET /api/filmes/ator/{ator}` - Buscar por ator
- `POST /api/filmes` - Criar novo filme
- `PUT /api/filmes/{id}` - Atualizar filme
- `DELETE /api/filmes/{id}` - Excluir filme

### Locações
- `GET /api/locacoes` - Listar todas as locações
- `GET /api/locacoes/{id}` - Buscar locação por ID
- `POST /api/locacoes` - Criar nova locação
- `PUT /api/locacoes/{id}/devolver` - Devolver filme

## 🐳 Docker

O projeto inclui configuração completa para Docker:

- **Dockerfile** - Para containerizar a aplicação
- **docker-compose.yml** - Para orquestração com PostgreSQL

## 🤝 Contribuindo

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

## 👨‍💻 Autor

**Lucas Feitoza**
- GitHub: [@lucasfeitozas](https://github.com/lucasfeitozas)
- LinkedIn: [Lucas Feitoza](https://linkedin.com/in/lucasfeitozas)

---

⭐ Se este projeto foi útil para você, considere dar uma estrela!