# RabbitMQ, Mqtt e REST + Microservices

## Richiesta

L’obietivo è quello	di	implementare una versione	del	DB	REST	
impementato nell Esercizio	4	che funzioni usando	un	collegamento
fornito	da	un	message	Broker	(RabbitMQ ed eventualmente
MosquiGo).	I’esercizio richiede	lo	sviluppi dei seguenK	moduli:	
1. Implementare una Libreria	Java,	ad	uso	client	che supporti una
interfaccia	REST	via	AMQP.	
2. Implementare una estensione	del	DB	REST	dell’ES4	che supporti	le	
chiamate	via	AMQP.	L’estensione può essere implemetata aggiungendo
una	thread	al	server	che offra	la	nuova funzionalità	(vedi	ARCH1)	oppure
introducendo	un	“micro-server”	che funga	da	proxy	(vedi	ARCH2)	
3. Implementare infine una versione della soluzione effettuata	con	AMQP	
che funzioni sulla	base	di	MQTT	(MosquiTTo).	

## Svolgimento

All'interno del progetto dell'es4 si è aggiunta un interfaccia per rabbitMq sfruttando le feauture per AMQP di Spring
con la classe [StudentInterface.java](../../esperimenti/students/src/main/java/uniupo/gaborgalazzo/students/controller/amqp/StudentInterface.java)

Si è poi creato un progetto che emula un client che effettua chiamate tramite AMQP, MQTT e HTTP REST.
All'interno di esso si è aggiunto un MiddleWare tra MQTT e REST ([StudentMqttSingleQueueRestProxy](../../esperimenti/students-client/src/main/java/uniupo/gaborgalazzo/studentamqpclient/proxy/StudentMqttSingleQueueRestProxy.java))
dove si è implementata la RPC con MQTT.

### Uso

#### Per i microservices (incluso il proxy per versionare le API)
> docker-compose up -d
#### Per il client
> java -jar ./client/student-amqp-client-0.0.1-SNAPSHOT.jar --spring.config.location=./client/application.properties

Il client può implementare la comunicazione con le seguenti interfacce

1. REST (comunicazione rest verso api.baseurl=http://localhost/v2 - cambiabile con v1)
2. AMQP SERVER (connessione al server diretta)
3. AMQP PROXY (connessione al server tramite un proxy verso HTTP)
4. MQTT PROXY (connessione al server tramite un proxy verso HTTP - vecchio metodo non  ottimale)
5. MQTT SingleQueue PROXY (connessione al server tramite un proxy verso HTTP)

## Sorgenti 
Server STUDENTI  con AMQP: [students](../../esperimenti/students)

Client: [students-client](../../esperimenti/students-client)