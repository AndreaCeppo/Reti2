# FIRMA DIGITALE

## Richiesta:

Scrivere un script (bash, o altro) che, dato un file specificato da un parametro in ingresso,ne generi una copia "firmata".

Quindi inviare la copia firmata al docente per e-mail, corredata delle informazioni necessarie a verificare la firma del file.

NB: l'operazione richiede di generarsi un copia di chiavi simmetriche, di crearsi un certificato selfsigned.

## Svolgimento:

Per completare l'esercizio si sono creati i seguenti 3 file

[create_key_pairs.sh](create_key_pair.sh) : script che genera una coppia di chiavi asimmetriche
> Usage: create_key_pairs.sh `passfrase` 


[sign.sh](sign.sh) : script che dato un `message_file` e una `private_key` genera un file  `message_file.sha256` che è la firma dell'importa SHA256 di 

> Usage: sign.sh `private_key` `message_file`

[verify.sh](verify.sh) : script che verifica se la firma `sign_file` corrisponde con `message_file` data `public_key`

> Usage: verify.sh `public_key` `sign_file` `message_file`