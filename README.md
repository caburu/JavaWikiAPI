# Java Wiki API

Implementação de uma API simples em Java para consultar a Wikipedia (na verdade, qualquer site do tipo Wiki).

Essa API é um simples *wrapper* (adaptador) implementado em Java para acessar a API da própria Wiki via web.

## Como usar a API

As classes necessárias para acessar uma Wiki se encontram no pacote `br.ufla.gac106.JavaWikiAPI`.
A maneira mais simples de usar a JavaWikiAPI é clonar este repositório e usar as classes do pacote.

A principal classe é a [`Wiki`](./src/br/ufla/gac106/JavaWikiAPI/Wiki.java), cujos principais métodos são `consultarPagina` e `pesquisarTitulosDePaginas`.

### Consultando dados de uma página pelo título

Se quisermos, por exemplo, consultar a página de título "Brasil", na Wikipedia em português, basta fazer o código abaixo.

```java
// Cria o objeto que consulta a API da Wiki (Wikipedia em português por padrão)
Wiki wiki = new Wiki();

// Consulta a página de título "Brasil"
PaginaWiki pagina = wiki.consultarPagina("Brasil");

// A classe PaginaWiki tem 4 atributos principais: título, identificador na Wiki, resumo e imagem
// Podemos então exibir o resumo da página (o texto que vem antes da primeira seção)
System.out.println(pagina.getResumo());

// Libera os recursos do objeto usado para fazer a consulta à API
wiki.close();
```

> Obs. 1: o título da página tem que ser quase exatamente o título na Wiki (exceto por maiúscula/minúscula, por ex.).

> Obs. 2: é importante notar que o resumo da página nem sempre está disponível.

### Buscando títulos de páginas

Se quisermos fazer uma busca por todos os títulos de página que tenham a palavra "Brasil", tendo um objeto do tipo `Wiki` já criado, basta fazer o código abaixo.
O método vai retornar a lista de títulos de página válidos, retornados pela busca da própria Wiki (são retornados no máximo 10 resultados).

```java
List<String> titulosDePagina = wiki.pesquisarTitulosDePaginas("gato");
```

Tal método é útil quando queremos consultar uma página, mas não sabemos exatamente como é o título na Wiki.
Podemos então fazer a pesquisa para obter o título correto, e então usá-lo para chamar o método `consultarPagina`.

### Exibindo imagem de uma página

Caso seja retornada uma imagem (*thumbnail*) da página buscada, é possível consultá-la através do método `getImagem` da classe `Wiki`.

> Obs.: nem todas as páginas têm imagem desse tipo, e nem todas as Wikis permitem a consulta da imagem via API.

### Acessando outras Wikis

A Java Wiki API permite acessar qualquer Wiki que possua API no padrão da Wikipedia.
Para isso, basta criar o objeto da classe `Wiki` passando o *endpoint* (endereço) da API da Wiki que se deseja consultar (ou usar o método `setEndPoint` depois do objeto wiki ter sido criado).

Por exemplo, se quiser consultar páginas da Wikipedia em inglês, basta fazer como no exemplo abaixo.

```java
Wiki wiki = new Wiki("https://en.wikipedia.org/w/api.php");
```

Ou:

```java
Wiki wiki = new Wiki();
// ...
wiki.setEndPoint("https://en.wikipedia.org/w/api.php");
```

Abaixo estão alguns outros exemplos de Wikis que podem ser acessadas (conhece mais alguma? Fique à vontade para contribuir!):

- Wiki sobre As Crônicas de Gelo e Fogo: https://awoiaf.westeros.org/api.php
- Wiki sobre Harry Potter: https://harrypotter.fandom.com/api.php
- Wiki sobre Star Wars: https://starwars.fandom.com/api.php

**Importante**

- Vale ressaltar que nem todas elas retornam resumos e/ou imagens para as páginas, ou elas podem ter limitação de banda bem restrita para consultas desse tipo.
- O código foi testado principalmente com a Wikipedia, portanto, esses outros exemplos podem não funcionar como esperado.

### Outros exemplos

Este projeto possui duas classes que demonstram mais detalhes sobre a utilização da API.

- A classe [AppMinimo](./src/AppMinimo.java) traz um programa simples que utiliza os métodos principais da classe `Wiki`.
- Já a classe [App](./src/App.java) traz um exemplo mais completo que exibe imagens das páginas, permite ligar o modo de depuração e acessar outras wikis.

## Detalhes técnicos da requisição e dados retornados

Para saber quais são os parâmetros utilizados para montar a URL de requisição à API da Wiki, verifique a implementação do método `definirParametrosPadroes` da class `Wiki`.

Já sobre os dados retornados pela requisição, a API utiliza os seguintes campos do JSON de resposta:

- `title`: título da página.
- `pageId`: identificador da página na Wiki.
- `extract`: resumo da página (texto antes da primeira seção)
- `source` (dentro de `thumbnail`): endereço URL da imagem representativa da página.

## Como contribuir

Este projeto foi criado principalmente como apoio às aulas de Programação Orientada a Objetos da [Universidade Federal de Lavras](https://www.ufla.br).
Toda contribuição é bem-vinda, e a melhor maneira de fazer isso é:

- Abrindo *issues* para possíveis bugs na implementação.
- Abrindo *issues* com proposta de melhoria que, após discutida e aprovada, pode ser submetida via *Pull Request*.

Lembre-se apenas que o principal objetivo do projeto é o uso didático nas aulas de POO :)
