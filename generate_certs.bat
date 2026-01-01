@echo off
mkdir certs 2>nul
cd certs

echo Generating Self-Signed CA...
openssl genrsa -out ca.key 4096
openssl req -new -x509 -key ca.key -sha256 -subj "/C=US/ST=State/L=City/O=JScience/CN=JScienceRootCA" -days 365 -out ca.crt

echo Generating Server Key and CSR...
openssl genrsa -out server.key 2048
openssl req -new -key server.key -out server.csr -subj "/C=US/ST=State/L=City/O=JScience/CN=localhost"

echo Signing Server Certificate...
openssl x509 -req -in server.csr -CA ca.crt -CAkey ca.key -CAcreateserial -out server.crt -days 365 -sha256

echo Converting keys to PEM format for Java...
openssl pkcs8 -topk8 -inform PEM -outform PEM -in server.key -out server-key.pem -nocrypt
copy server.crt server.pem

echo Done! Certificates generated in certs/ directory.
echo Use server.pem and server-key.pem for gRPC TLS.
pause
