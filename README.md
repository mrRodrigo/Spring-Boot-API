# Spring-Boot-API

[![Build Status](https://travis-ci.com/mrRodrigo/Spring-Boot-API.svg?branch=master)](https://travis-ci.com/mrRodrigo/Spring-Boot-API)
[![FOSSA Status](https://app.fossa.com/api/projects/git%2Bgithub.com%2FmrRodrigo%2FSpring-Boot-initial.svg?type=shield)](https://app.fossa.com/projects/git%2Bgithub.com%2FmrRodrigo%2FSpring-Boot-initial?ref=badge_shield)

### This application is running on Herokuapp
- A documentação do Swagger pode ser utilizada [aqui](https://api-ponto.herokuapp.com/swagger-ui.html).
- Todos testes unitários e Continuous integration estão no Travis -> [Aqui](https://travis-ci.com/github/mrRodrigo/Spring-Boot-API)


## Ferramentas e notas de desenvolvimento
### Spring boot JPA 📃
O Java possui o JPA (Java Persistence API), que é uma API de persistência para Java, que
traduz um objeto Java para uma tabela no banco de dados, e vice versa. Além do mapeamento, esse framework permite também o desenvolvimento de métodos para o acesso aos dados com pouco ou nenhum código sendo necessário para o seu desenvolvimento, o que facilita muito o desenvolvimento das aplicações, e aumenta a produtividade dos programadores. Junto a isto foi utilizado o ‘JpaRepository’ para executar operações de acesso a uma base de dados de forma automática. [Referência.](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#reference)

Principais anotações utilizadas:

```
A ‘@Entity’ informa ao JPA que essa classe se trata de uma entidade JPA.

A ‘@Table’ é opcional, e permite definir o nome da tabela no banco de dados para a
entidade.

A ‘@Id’ informa que o campo será a chave primária da tabela.

A ‘@GeneratedValue’ informa como a chave primária será incrementada, sendo que o modo automático apenas incrementará o valor em 1 a cada nova inserção.

A ‘@Column’ permite definir um nome para o campo na tabela do banco de dados, assim
como se ele pode ou não ser nulo.

As ‘@PrePersist’ e ‘@PreUpdate’ também são opcionais, e permitem executar uma ação
antes de uma inserção ou atualização de um registro


```

### Banco de dados H2 💾
Nenhuma configuração adicional é necessária, e o Spring se encarregará inclusive de
garantir que as entidades sejam criadas automaticamente. basta inclui-lo no projeto. Neste projeto foi utilizado nos testes e CI. [Referência](https://www.javatpoint.com/spring-boot-h2-database)

### Parâmetros de configuração (application.properties) ⚙️
O Spring Boot oferece por padrão a configuração de um arquivo de propriedades, chamado
‘application.properties’, que serve para o armazenamento de configurações no formato
chave e valor. Para ter acesso ao valor adicionado acima, devemos utilizar a anotação ‘@Value’ em um
componente Spring.
```
@Value("${paginacao.qtd_por_pagina}")
private int qtdPorPagina;
```

### Profiles 🙍
Os ‘profiles’ permitem associar um arquivo de configuração para cada um dos perfis,
permitindo executar a aplicação com distintas configurações. Para executar a aplicação em um profile diferente, devemos fazer isso via linha de
comando, adicionando o parâmetro ‘-Dspring.profiles.active=test’, onde ‘test’ é o nome do
profile. 
```
java -jar -Dspring.profiles.active=test meu-primeiro-projeto-0.0.1-SNAPSHOT.jar
```
Também é possível adiciona-lo no arquivo ‘application.properties';

```
spring.profiles.active=test
```
### BCrypt 🔐

O BCrypt permite encriptar um determinado valor de forma irreversível, sendo o ideal para o
armazenamento seguro de informações no banco de dados. O BCrypt cria hashes diferentes para um mesmo valor se chamado mais de uma vez o que torna cada encriptação única. Foi implementado neste [arquivo](https://github.com/mrRodrigo/Spring-Boot-API/blob/master/src/main/java/com/rodrigo/first/api/utils/PasswordUtils.java).

### Flyway 🐦

O Flyway é um framework que permite o versionamento e automatização no processo de criação de banco de dados. Com ele é possível configurar a criação de tabelas e seeds para popular o banco de dados com valores iniciais. Basicamente foi utilizado uma migration para criar as tabelas iniciais de nossa aplicação. Esta migration deve estar localizada em ``` src/main/resources/db/migration/mysql ``` e foi utilizado este [arquivo](https://github.com/mrRodrigo/Spring-Boot-API/blob/master/src/main/resources/db/migration/mysql/V1__init.sql). 

### Serviços ⛑

O Spring possui uma anotação chamada ‘Service’, que quando uma classe Java é anotada com ela, a mesma passará a ser um componente Spring. Esse componente Spring, deverá conter uma lógica de negócio, e poderá ser injetada como dependência de qualquer outro componente usando a anotação ‘Autowired’. Com a classe criada, basta em qualquer componente ou serviço adicionar a anotação ‘@Autowired’ para injetar um serviço. [Referência](https://www.tutorialspoint.com/spring_boot/spring_boot_service_components.htm)

### Controllers 🎛

O Spring Rest possui a anotação ‘Controller’, que uma vez adicionada a uma classe Java, aceitará um ‘path’ como, tornando esse componente disponível para acesso
HTTP para o ‘path’ adicionado. Também é possível gerenciar os verbos HTTP (GET, POST, PUT, DELETE, etc...). [Referência](http://zetcode.com/springboot/controller/)

### DTO (Data Transfer Object) 📋

Sua grande vantagem é permitir a fácil manipulação dos dados da requisição HTTP, e os DTOs consistem apenas de classes Java com atributos, que representam os parâmetros
das requisições. Em um cenário real, o DTO seria em banco de dados, mas para simplificar ele é apenas retornado. [Referência](https://www.baeldung.com/entity-to-and-from-dto-for-a-java-spring-application).

### Swagger 📖

Ele pode ser facilmente integrado com o Spring Boot, e de modo automático extrairá todas as informações da API do código fonte. O Swagger usa o ‘basePackage’ para buscar por controllers, então altere ele para o local onde seus controllers estão implementados.

### Autenticação e autorização com tokens JWT (Json Web Token)  :key:

APIs Restful eficientes não devem manter estado, e devem permitir que sejam escaláveis horizontalmente, para que assim sejam de alta performance. Por ela não manter sessão em nenhum local, os dados de acesso devem estar armazenados em algum lugar que possa ser compartilhado entre requisições. Para isso que existe o JWT, que é um formato de token seguro e assinado digitalmente, garantindo a integridade dos dados trafegados. Dessa forma manteremos as informações de autenticação no token, para que assim a aplicação seja capaz de validar o acesso a uma
requisição. Esta implementação foi feita através do [package security](https://github.com/mrRodrigo/Spring-Boot-API/tree/master/src/main/java/com/rodrigo/first/api/security).

### EhCache 📈

O EhCache é um mecanismo de cache que se integra muito fácil e rápida com o Spring Boot, permitindo armazenar dados processados na memória para posterior utilização. Basicamente quando executamos uma operação lenta, podemos armazenar seu resultado em cache para que na próxima requisição o mesmo seja apenas retornado. Para marcar o resultado de uma operação a ser adicionada ao cache, você deverá utilizar a anotação ‘@Cachable’, passando como parâmetro o nome de como o resultado será
armazenado no cache. [Referência](https://www.baeldung.com/spring-boot-ehcache).

### Referência técnica 🥇

Todo conteúdo aqui abordado foi consultado e elaborado a partir do curso [API RESTful avançada com Spring Boot e Java 8](https://www.udemy.com/course/api-restful-avancada-spring-boot-java-8/) e pesquisas nos sites referênciados acima.
