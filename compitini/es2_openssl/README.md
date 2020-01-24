# CONDIVISIONE DI SEGRETO CON CHIAVE SIMMETRICA

## Richiesta:

Ripetere l'esercizio N1 con la differenza che il documento deve ora essere criptato con una chiave simmetrica.

Per comunicare la chiave simmetrica al docente usare la sua chiave pubblica esposta tramite l'apposito link all'inizio della sezione degli esercizi.

## Svolgimento:

Per completare l'esercizio si sono creati i seguenti 2 file

[create_secure_message.sh](create_secure_message.sh) : script che genera una file `message_file.aes256` che contiene il testo in `message_file` firmato con la chiave simmetrica in `password_file` e un file `password_file.rsa` che contiene il segreto firmato con `public_key`
> Usage: create_secure_message.sh `message_file` `password_file` `public_key` 


[read_secure_message.sh](read_secure_message.sh) : script che estrae il segreto da `secret_password_file` con `private_key` e lo usa per stampare in chiaro il contenuto di `secret_message_file` 

> Usage: read_secure_message.sh `secret_message_file` `secret_password_file` `private_key`

