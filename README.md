# Spring-boot Project to Demonstrate HTTP/2 Protocol.

## HTTP/2 over HTTP/1.1.
**Performance Improvements**: HTTP/2 offers significant performance advantages over HTTP/1.1 through.  
    **Multiplexing**: Allows multiple requests and responses to be sent concurrently over a single connection, eliminating head-of-line blocking that bottlenecks HTTP/1.1's sequential processing.  
    **Header Compression**: Uses HPACK to reduce header size, minimizing data transfer and improving performance.  
    **Server Push**: Enables servers to proactively send resources to clients before they're requested. Deprecated in HTTP/3 in favor of rel=preload.    
    **Request Prioritization**: Supports prioritizing certain requests over others.  

**Connection Efficiency**: While HTTP/1.1 processes requests and responses sequentially, HTTP/2 handles multiple concurrent streams on one connection.  
**Streaming Mechanism**: HTTP/2 replaces HTTP/1.1's chunked-transfer encoding with its own more efficient mechanism for data streaming.  

## Prerequisites
- Java 17 or above
- Maven
- HTTPS certificate (included: keystore.p12)
- 
## Enabling HTTP/2 in Spring Boot  
SSL Setup is mandatory to enable HTTP/2 protocol version.

## Create Self-Signed Certificate to enable SSL.
```bash
keytool -genkeypair -alias tomcat -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore src/main/resources/keystore.p12 -validity 365
#make sure CN=localhost.
```
### Importing the self-signed public cert to the Java keystore if we invoke the API from java client.
````bash
openssl s_client -connect localhost:8443 -servername localhost < /dev/null | sed -ne '/-BEGIN CERTIFICATE-/,/-END CERTIFICATE-/p' > src/main/resources/public.crt

keytool -importcert -alias tomcat -file src/main/resources/public.crt -keystore "$JAVA_HOME/lib/security/cacerts" -storepass mysuperpassword
````
![img.png](img.png)

## Project structure.

src/main/resources/static/
├── index.html          # Fixed main HTML file
├── js/
│   ├── main.js         # Full JavaScript file
├── css/
│   └── main.css        # Styles
└── downloads/          # Sample files