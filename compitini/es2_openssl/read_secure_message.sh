[ $# -lt 3 ] && { echo "Usage: $0 {secret_message_file} {secret_password_file} {private_key}"; exit 1; }


password=$(openssl pkeyutl -decrypt -inkey "$3" -in "$2")
openssl aes-256-ecb -in "$1" -d -k $password -pbkdf2