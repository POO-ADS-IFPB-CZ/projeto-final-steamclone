# Steam Clone — Trabalho de Finalização de Semestre (POO)

Clone da loja de jogos **Steam** em Java, desenvolvido como projeto de Programação Orientada a Objetos.

## Estrutura do Projeto

```
src/main/java/com/steamclone/
├── Main.java                    # Menu interativo com as consultas
├── model/                       # Entidades do dicionário conceitual
│   ├── Pessoa.java              # Classe abstrata base
│   ├── Cliente.java             # Especialização de Pessoa
│   ├── Desenvolvedor.java       # Especialização de Pessoa
│   ├── Desenvolvedora.java
│   ├── Jogo.java
│   ├── Plataforma.java
│   ├── Acessorio.java
│   ├── Complemento.java
│   ├── Pedido.java
│   ├── Pagamento.java
│   ├── ItemPedido.java
│   ├── Avaliacao.java
│   └── enums/
├── repository/
│   └── LojaRepository.java      # Persistência em memória
├── service/
│   └── ConsultaService.java     # Consultas do plano de negócio (2.2)
└── data/
    └── DadosExemplo.java        # Dados de demonstração
```

## Conceitos POO Aplicados

| Conceito | Implementação |
|----------|---------------|
| **Herança** | `Cliente` e `Desenvolvedor` estendem `Pessoa` |
| **Polimorfismo** | Método abstrato `getTipo()` em `Pessoa` |
| **Encapsulamento** | Atributos privados com getters/setters |
| **Associações** | Relacionamentos entre entidades (Realiza, Gera, Contém, etc.) |
| **Composição** | `Pedido` contém `ItemPedido`, `Pagamento` |

## Relacionamentos

- **Realiza**: Cliente → Pedido (1:N)
- **Gera**: Pedido → Pagamento (1:1)
- **Contém**: Pedido → Jogo via ItemPedido (N:N)
- **Desenvolve**: Desenvolvedora → Jogo (1:N)
- **Trabalha**: Desenvolvedor → Desenvolvedora (N:1)
- **Portabilidade**: Jogo ↔ Plataforma (N:N)
- **Acompanha**: Jogo ↔ Acessório (N:N)
- **Fabricado**: Plataforma → Desenvolvedora (N:1)
- **Avalia**: Cliente → Jogo via Avaliacao (N:N)
- **Possui**: Jogo → Complemento (1:N)

## Consultas Disponíveis (2.2)

1. Detalhes de um pedido (cliente + status do pagamento)
2. Clientes que compraram um jogo específico
3. Total de vendas por mês
4. Jogos compatíveis com uma plataforma
5. Desenvolvedores ativos ordenados por salário
6. Jogo com maior média de notas
7. Pagamentos pendentes
8. Histórico de pedidos por CPF
9. Quantidade de jogos de uma desenvolvedora
10. Acessórios disponíveis para um jogo

## Requisitos

- Java 17+
- Maven 3.6+

## Como Executar

```bash
# Compilar
mvn compile

# Executar
mvn exec:java

# Ou gerar JAR e executar
mvn package
java -jar target/projeto-final-steamclone-1.0-SNAPSHOT.jar
```

## Dados de Exemplo

O sistema carrega automaticamente dados fictícios incluindo:

- Jogos: Sky: Children of the Light, Counter-Strike 2, Elden Ring, Zelda
- Plataformas: PC, PlayStation 5, Nintendo Switch, Steam Deck
- Clientes, pedidos, pagamentos e avaliações para testar todas as consultas

### Exemplos de teste

| Consulta | Entrada sugerida |
|----------|------------------|
| Detalhes do pedido | ID: `1001` |
| Clientes por jogo | `"Sky: Children of the Light"` |
| Vendas do mês | Ano: `2025`, Mês: `7` |
| Jogos por plataforma | `"PC"` |
| Histórico por CPF | `555.666.777-88` |
| Jogos da desenvolvedora | `"thatgamecompany"` |
