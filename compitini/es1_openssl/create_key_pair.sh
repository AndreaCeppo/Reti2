[ $# -eq 0 ] && { echo "Usage: $0 passfrase"; exit 1; }
 
openssl req -newkey rsa:4096 -passout pass:"$1"  -nodes -keyout private.pem -x509 -days 365 -out certificate.pem
openssl rsa -in private.pem -passin pass:"$1" -pubout -out public.pem