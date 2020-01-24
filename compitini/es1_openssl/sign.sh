[ $# -lt 2 ] && { echo "Usage: $0 {private_key} {message_file}"; exit 1; }
 
openssl dgst -sha256 -sign "$1" -out tmp_sign.sha256 "$2"
openssl base64 -in tmp_sign.sha256 -out "$2".sha256
rm tmp_sign.sha256