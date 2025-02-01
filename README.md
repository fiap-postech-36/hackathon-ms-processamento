
# API de Autoatendimento para Processamento de Vídeo

### ✨ Descrição:
O Microserviço de Processamento de Vídeo é uma aplicação desenvolvida para processar vídeos de forma automatizada, extraindo frames em intervalos regulares e compactando-os em um arquivo ZIP. O serviço é projetado para ser escalável, seguro e eficiente, utilizando tecnologias modernas para garantir uma execução confiável e de alta performance.

## 🚀Objetivo Principal:
#### Fornecer uma solução para processamento de vídeos em lote, onde:
- Um vídeo é enviado para processamento.
- Frames são extraídos em intervalos de tempo definidos (por exemplo, a cada 20 segundos).
- Os frames são compactados em um arquivo ZIP.
- O arquivo ZIP é armazenado em um sistema de armazenamento de objetos (MinIO).
- O status do processamento é monitorado e notificações são enviadas por e-mail ao usuário.

### 👋Funcionalidades Principais

- **Upload e Processamento de Vídeos:**  
  O vídeo é enviado para o serviço, que inicia o processamento automaticamente. Frames são extraídos em intervalos configuráveis e compactados em um arquivo ZIP.

- **Armazenamento de Arquivos:**  
  O vídeo original e o arquivo ZIP gerado são armazenados em um sistema de armazenamento de objetos (MinIO).

- **Notificações por E-mail:**  
  O usuário recebe notificações por e-mail sobre o status do processamento (sucesso ou falha).

- **Autenticação e Autorização:**  
  O serviço é protegido por autenticação OAuth2 com JWT, garantindo que apenas usuários autorizados possam acessar os endpoints.

- **Comunicação Assíncrona:**  
  O processamento de vídeos é realizado de forma assíncrona, utilizando RabbitMQ para gerenciar filas de tarefas e garantir a escalabilidade do sistema.

- **Monitoramento de Status:**  
  O status do processamento (em andamento, concluído ou falha) é armazenado e pode ser consultado a qualquer momento.

### 😉 Casos de Uso
Este microserviço pode ser utilizado em diversos cenários, como:
- **Plataformas de Edição de Vídeo:** Para extrair frames de vídeos enviados pelos usuários.
- **Sistemas de Análise de Vídeo:** Para processar vídeos e gerar insumos para análise de imagens.
- **Aplicações de Backup:** Para compactar e armazenar frames de vídeos de forma eficiente.

### 🤌 Benefícios
- **Escalabilidade:** O uso de RabbitMQ permite que o serviço processe múltiplos vídeos simultaneamente.
- **Segurança:** Autenticação OAuth2 e JWT garantem que apenas usuários autorizados possam acessar o serviço.
- **Eficiência:** A extração de frames e a compactação em ZIP são realizadas de forma otimizada, reduzindo o tempo de processamento.
- **Notificações em Tempo Real:** Os usuários são notificados imediatamente sobre o status do processamento.

### 😎 Fluxo de Funcionamento
1. O usuário envia um vídeo para processamento via API.
2. O serviço salva o vídeo no MinIO e envia uma mensagem para a fila do RabbitMQ.
3. Um worker consome a mensagem e inicia o processamento do vídeo.
4. Frames são extraídos e compactados em um arquivo ZIP.
5. O arquivo ZIP é armazenado no MinIO.
6. O status do processamento é atualizado e uma notificação por e-mail é enviada ao usuário.

### 🛠 Tecnologias Utilizadas
- **Spring Boot:** Para desenvolvimento rápido e eficiente da aplicação.
- **RabbitMQ:** Para gerenciar filas de processamento de forma assíncrona.
- **MinIO:** Para armazenamento de vídeos e arquivos ZIP.
- **JavaCV:** Para manipulação de vídeos e extração de frames.
- **OAuth2/JWT:** Para autenticação e autorização.
- **Swagger:** Para documentação da API.

👨‍💻 Requisitos do Sistema
- **Java 17**
- **RabbitMQ**
- **MinIO**
- **Docker** (opcional, para execução em container)

---

## Stack Utilizada 🛠
- **Linguagem Principal:** Java 17 com Maven
- **Web:** Spring Boot, Lombok
- **Banco de Dados:** MongoDB
- **Infra:** Kubernetes provisionado pelo Docker (testados nas versões v1.29.2 e 1.30.2)

---

## Comentar sobre RabbitMQ e MinIO 🤔
- **RabbitMQ:** Utilizado para gerenciar as filas de processamento, garantindo que os vídeos sejam processados de forma assíncrona e eficiente. Sua implementação garante alta disponibilidade e distribuição das tarefas para os workers.
- **MinIO:** Sistema de armazenamento de objetos compatível com Amazon S3, utilizado para armazenar os vídeos e os arquivos ZIP gerados durante o processamento. Ele é escalável e ideal para armazenar grandes volumes de dados de forma segura.

---

## Instalação usando apenas Docker

1. Na raiz do projeto, execute o comando abaixo para buildar o projeto:

```bash
  docker-compose build --no-cache
```

2. Na raiz do projeto, execute o comando abaixo para subir os containers:

```bash
  docker-compose up
```
>* Caso não queira bloquear o console, adicione a flag **-d** ao final do comando.

3. Após a instalação, a documentação Swagger da API pode ser encontrada no seguinte link:

[http://localhost:8080/videoprocessing/swagger-ui/index.html](http://localhost:8080/videoprocessing/swagger-ui/index.html) 🔗

---

## Instalação usando Kubernetes

⚠️ **Atenção:** Recomenda-se usar as mesmas tecnologias especificadas na Stack para a criação do cluster, conforme descrito neste readme.

---

## 🔗 Links
[Documentação da API](http://localhost:8080/videoprocessing/swagger-ui/index.html)

⚠️ VERIFICAR!!! // LINK DA APRESENTAÇÃO

⚠️ VERIFICAR!!!

⚠️ VERIFICAR!!!



---

## Time de Desenvolvedores 🔗
🧑🏽‍💻[@ulysses903](https://github.com/ulysses903)

🧑🏾‍💻[@samuelmteixeira](https://www.github.com/samuelmteixeira)

👩‍💻[@kaiquesantos98](https://www.github.com/KaiqueSantos98)

🧑🏽‍💻[@jjbazagajr](https://www.github.com/jjbazagajr)

🧑🏻‍💻[@leandroibraim](https://www.github.com/leandroibraim) 
