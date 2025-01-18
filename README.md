# API de Autoatendimento para Processamento de video
### Descrição
A fazer

## Stack utilizada

**Linguagem principal:** Java 17 com Maven

**Web:** Spring Boot, Lombok

**Banco de Dados:** MongoDB

**Infra:** Kubernetes provisionado pelo Docker (testados na versão v1.29.2 e 1.30.2)

Comentar sobre rabbitMQ e minIO

## Instalação usando apenas Docker

1. Raiz do projeto execute o comando abaixo para buildar o projeto:

```bash
  docker-compose build --no-cache
```

2. Raiz do projeto execute o comando abaixo para subir os containers:

```bash
  docker-compose up
```
Caso não queira bloquear o console, adicione a flag **-d** ao final do comando

3. Após a instalação, a documentação Swagger da API pode encontrada no seguinte link:

[http://localhost:8080/videoprocessing/swagger-ui/index.html](http://localhost:8080/videoprocessing/swagger-ui/index.html)


## Instalação usando Kubernetes

##### ⚠️ Atenção: recomenda-se usar as mesmas tecnologias especificadas na Stack para a criação do cluster, conforme descrito neste readme.
A fazer

## 🔗 Links
A fazer
## Time de desenvolvedores

- [@ulysses903](https://github.com/ulysses903)
- [@samuelmteixeira](https://www.github.com/samuelmteixeira)
- [@kaiquesantos98](https://www.github.com/KaiqueSantos98)
- [@jjbazagajr](https://www.github.com/jjbazagajr)
- [@leandroibraim](https://www.github.com/leandroibraim)
