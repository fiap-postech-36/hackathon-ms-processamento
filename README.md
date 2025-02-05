
# API de Autoatendimento para Processamento de Vídeo

### ✨ Descrição:
O Microserviço de Processamento de Vídeo é uma aplicação desenvolvida para processar vídeos de forma automatizada, extraindo frames em intervalos regulares e compactando-os em um arquivo ZIP. O serviço é projetado para ser escalável, seguro e eficiente, utilizando tecnologias modernas para garantir uma execução confiável e de alta performance.

## 🚀Objetivo Principal:
#### Fornecer uma solução para processamento de vídeos em lote, onde:
- O usuario faz o upload de um vídeo em um sistema de armazenamento de objetos (MinIO direto pelo front.
- No final do upload um dto com os dados do usuario e o caminho do video para o minIO é enviado para API.
- Na api esses dados são salvos em um banco não relacional e gera um elemento na fila do RabbitMQ.
- A fila é escutada pela aplicação, e o processamento é iniciado.
- Frames são extraídos em intervalos de tempo definidos (por exemplo, a cada 20 segundos).
- Os frames são compactados em um arquivo ZIP.
- O arquivo ZIP é armazenado em um sistema de armazenamento de objetos (MinIO).
- O status do processamento é monitorado e notificações são enviadas por e-mail ao usuário.
- Em caso de erro o elemento é enviado para uma dead letter queue no RabbitMQ e marcado como failure no banco.
- Apos processar os itens da fila que ainda não foram executado o RabbitMQ executa novamente o elemento da dead letter.
- O elemento so deixará de ser executado após duas tentativas de falha, totalizando três execuções para considerar uma falha completa.
- Os elementos são escutados em paralelo, executando até três vídeos de uma vez.
- So é possivel fazer acesso a API se tiver um usuário cadastrado no realm auth do Keycloak

### 👋Funcionalidades Principais

- **Upload e Processamento de Vídeos:**  
  O front cuida de fazer o upload do vídeo no minIO, após o upload completar o caminho do vídeo é enviado para o endpoint de processamento juntos com os dados do usuário. Essas informações são salvas em banco e criam um elemento na fila do rabbitMQ. A aplicação escuta a fila, consultando no banco pelo id da solicitação, buscando o vídeo no minIO e começando a extrair os frames do vídeo de acordo com o intervalo. O vídeo é baixado, seus frames são extraídos em intervalos configuráveis e compactados em um arquivo ZIP que ao final é salvo no minIO e seu caminho no banco para o usuário baixar.

- **Armazenamento de dados:**  
  É utilizado o mongoDB para guardar a relação de caminho do video a ser processado com o usuário. Como são informações básicas e sem relação com entidade um banco não relacional foi escolhido.

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
- **Escalabilidade:** O sistema é facilmente escalavel com novos pods no kubernets já que não temos perigos de concorrência de dados pois utilizamos fila.
- **Resiliência:** O uso de RabbitMQ permite que o serviço processe múltiplos vídeos simultaneamente e com a dead letter queue predefinimos um número de execuções para considerarmos falho.
- **Segurança:** Autenticação OAuth2 e JWT garantem que apenas usuários autorizados possam acessar o serviço.
- **Eficiência:** A extração de frames e a compactação em ZIP são realizadas de forma otimizada, reduzindo o tempo de processamento.
- **Notificações em Tempo Real:** Os usuários são notificados imediatamente sobre o status do processamento.

### 😎 Fluxo de Funcionamento
1. O usuário faz o upload de um vídeo para o minIO.
2. No final do upload um dto com os dados do usuario e o caminho do video para o minIO é enviado para API.
3. Na api esses dados são salvos em um banco não relacional e gera um elemento na fila do RabbitMQ.
4. Um worker consome a mensagem e inicia o processamento do vídeo.
5. Frames são extraídos e compactados em um arquivo ZIP.
6. O arquivo ZIP é armazenado no MinIO.
7. O status do processamento é atualizado e uma notificação por e-mail é enviada ao usuário.
8. No caso de erro da primeira execução o sistema tenta executar mais duas vezes, parando de executar na terceira falha.
9. Os elementos são executados em paralelo, até 3 videos por pod.
10. Tanto a quantidade de tentativas como vídeos em paralelo são configuráveis.

### 📋 Tecnologias Utilizadas
- **Spring Boot:** Para desenvolvimento rápido e eficiente da aplicação.
- **RabbitMQ:** Para gerenciar filas de processamento de forma assíncrona.
- **MinIO:** Para armazenamento de vídeos e arquivos ZIP.
- **JavaCV:** Para manipulação de vídeos e extração de frames.
- **OAuth2/JWT:** Para autenticação e autorização.
- **Swagger:** Para documentação da API.
- **MongoDB:** Para armazenamento de dados.

👨‍💻 Requisitos do Sistema
- **Java 17**
- **RabbitMQ**
- **MinIO**
- **MongoDB**
- **Docker** (opcional, para execução em container)

---

## 🛠 Stack Utilizada 
- **Linguagem Principal:** Java 17 com Maven
- **Web:** Spring Boot, Lombok
- **Banco de Dados:** MongoDB
- **Infra:** Kubernetes provisionado pelo Docker (testados nas versões v1.29.2 e 1.30.2)

---

## 🤔 Comentario sobre RabbitMQ e MinIO 
- **RabbitMQ:** Utilizado para gerenciar as filas de processamento, garantindo que os vídeos sejam processados de forma assíncrona e eficiente. Sua implementação garante alta disponibilidade e distribuição das tarefas para os workers.
- **MinIO:** Sistema de armazenamento de objetos compatível com Amazon S3, utilizado para armazenar os vídeos e os arquivos ZIP gerados durante o processamento. Ele é escalável e ideal para armazenar grandes volumes de dados de forma segura.

---

## 🐳 Instalação usando apenas Docker

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

[http://localhost:8082/videoprocessing/swagger-ui/index.html](http://localhost:8082/videoprocessing/swagger-ui/index.html) 🔗

---

## 💻 Instalação usando Kubernetes

⚠️ **Atenção:** Recomenda-se usar as mesmas tecnologias especificadas na Stack para a criação do cluster, conforme descrito neste readme.

---

## 🔗 Links
[Documentação da API](http://localhost:8082/videoprocessing/swagger-ui/index.html)

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
