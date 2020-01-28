# REST API

## Richiesta 
Usando l’httpserver introdotto a lezione, sviluppare un semplice servizio
Basato su API REST che consenta di gestire un repository di informazioni. 
Per semplicità supponiamo che le informazioni siano relativi a una scuola
La risorsa è lo studente caratterizzato dagli opportuni parametri per 
inquadrarlo nel corso e nell’anno di studi del suo curriculum. 
Le seguenti funzionalità devono essere gestite: 
1. Il deposito di una risorsa nel repository 
2. La sovrascrittura di una risorsa   
3. La cancellazione di una risorsa
4. Il retrieval di uno o più risorse per ID oppure usando un Query su un 
URI 
Il sistema deve poter gestire due linguaggi di rappresentazione:XML, JSON

## Svolgimento
Si è sviluppato un progetto JAVA con Spring Framework

La documentaizione delle api è disponibile (una volta avviato il server) su
http://localhost:80/

## Uso

> docker-compose up
 
#### Oppure

!! Modificare il file [application.properties](application.properties) alla voce 
```
spring.datasource.url=jdbc:h2:file:/app/data/sample;DB_CLOSE_ON_EXIT=FALSE;AUTO_RECONNECT=TRUE

deve diventare 

spring.datasource.url=jdbc:h2:file:./data/sample;DB_CLOSE_ON_EXIT=FALSE;AUTO_RECONNECT=TRUE

```
poi lanciare il comando

> java -jar students-0.0.1-REST.jar --spring.config.location=./application.properties

## Sorgenti 
[students-old](../../esperimenti/students-old)