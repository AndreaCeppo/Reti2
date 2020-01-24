[ $# -lt 3 ] && { echo "Usage: $0 {message_file} {password_file} {public_key}"; exit 1; }

openssl aes-256-ecb -in "$1" -out "$1".aes256 -k "$2" -pbkdf2
echo "$2" | openssl pkeyutl -encrypt -pubin -inkey "$3" -out password.txt.rsa

