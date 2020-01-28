# BENCHMARK MosQuiTTo

## Richiesta 
Considerando la coppia di client Java (pub/sub) sperimentati a lezione per comunicare tramite Mosquito via MQTT,

organizzare un test che valuti le prestazioni di Mosquitto rispetto al variare del carico. 

In particolare, si tratta di misurare il numero di messaggi al secondo che Mosquito riesce a smaltire in funzione di:

- numero di pubblicatori e numero di sottoscrittori.
- numero di "topic" differenti, data una configurazione di pubblicatori e sottoscrittori. 

## Svolgimento

Si è sviluppato un progetto JAVA che:
Dati i seguenti paramentri, presenti nel file [Benchmark.java](src/Benchmark.java) `brokerUrl`,`nSubs4Topic`,`nPubs4Topic`,`nTopics`,`nMessaged4Pub`,`qos`:
``` 
 for `nTopics`
    genera `nSubs4Topic` sottoscrittori che loggano e raccolgono i dati
 for `nTopics`
  genera `nPubs4Topic` pubblicatori ognuno dei quali pubblica `nMessaged4Pub` messaggi a qualità `qos`
```
Ogni pub e sub e connesso al server `brokerUrl`.
Dai dati raccolti vengono stampate a vidio le statistiche 
- expectedArrived
- totArrived
- timeAVG

### Uso

Nel file [src/Benchmark.java](src/Benchmark.java) sono presenti le variabili da impostare:
* private final String brokerUrl = "tcp://193.206.55.23:1883";
* private final int nSubs4Topic = 10;
* private final int nPubs4Topic = 10;
* private final int nTopics = 5;
* private final int nMessages4Pub = 100;
* private final int qos = 2;

Per compilare e lanciare il programma si è utilizzato un makefile.

Per avviare il benchmark utilizzare il comando

> make run
