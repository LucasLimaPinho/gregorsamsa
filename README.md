# gregorsamsa

[A Guide to Apache Kafka](https://kafka.apache.org/)

[Quick Start for Apache Kafka](https://docs.confluent.io/current/quickstart/ce-docker-quickstart.html)

[Docker Image Installation](https://docs.confluent.io/current/installation/docker/image-reference.html)

### Ports

Zookeeper port: 2181

KApache Kafka Brokers: 9092

Confluent Control Center: 9021

Kafka Connect REST API: 8083

KSQL Server REST API: 8088

REST Proxy: 8082

Schema Registry REST API: 8081


* O Kafka foi originalmente desenvolvido pelo LinkedIn.
* É uma plataforma de Streaming distribuída Open-Source.
* Você pode **assinar** e **publicar** registros
  * Processar em tempo real, ou;
  * Armazenar (Fault-Tolerant);
* Kafka não pode ser chamado só de Mensageria devido sua capacidade de armazenamento - é uma plataforma de Streaming Distribuída;
* Kafka é desenvolvido em Scala e Java 
* O Kafka é executado como um cluster em um ou mais servidores que podem abranger vários datacenters;
* O cluster Kafka armazena fluxos de registros em categorias denominadas **tópicos**;
* Cada registro consiste em uma **chave, um valor e um timestamp de data e hora**;
* Apache Kafka é o sistema para gerenciamento de fluxos de dados em tempo real, gerados a partir de web sites, aplicações e sensores (IoT);
* O que é um Kafka Broker? Nó Kafka.
* Até a versão 2.6.x, Kafka ainda utilizada Java 8;


#### Producer API

Permite que um aplicativo publique um fluxo de registros em um ou mais tópicos do Kafka

#### Consumer API

Permite que um aplicativo assine um ou mais tópicos do Kafka e processe o fluxo de registro produzido por eles

#### Streams API

Permite que um aplicativo transforme os fluxos de entrada em fluxos de saída

#### Connector API

Permite criar e executar produtores ou consumidores reutilizáveis que conectam tópicos do Kafka a aplicativos ou sistemas de dados existentes

#### Apache Stream Fundamentals

* When DataLake came up, the idea behind was to save it in its raw format and then process it;
* More and more, companies are taking everything that happens in their environment (Business Events) and throwing into a Event Bus. Then applications can consume this events; We are talking about Real-time Stream Processing Applications;
* Data Lake are stationary, but Stream is data in motion; When we process data that is already sitting on a Data Lake, we called it simply Data Processing;



