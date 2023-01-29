keytool -genkey -keystore keystore.jks -keyalg RSA

java -Djavax.net.ssl.keyStore=/path/to/keystore.jks -Djavax.net.ssl.keyStorePassword=password ServerSocketExample localhost 10025

java -Djavax.net.ssl.trustStore=/path/to/keystore.jks -Djavax.net.ssl.trustStorePassword=password SslPokeConnectivity localhost 10025
