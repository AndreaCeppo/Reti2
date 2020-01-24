[ $# -lt 3 ] && { echo "Usage: $0 {public_key} {sign_file} {message_file}"; exit 1; }
 
openssl base64 -d -in "$2" -out tmp_sign.sha256
openssl dgst -sha256 -verify "$1" -signature tmp_sign.sha256 "$3"
rm tmp_sign.sha256