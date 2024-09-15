Email simulator online
https://www.wpoven.com/tools/free-smtp-server-for-testing
# Run jar
java -Dmail.smtp.host=smtp.freesmtpservers.com \
     -Dmail.smtp.port=25 \
     -Dmail.smtp.username=csp@example.com \
     -Dmail.smtp.password=null \
     -Dmail.to.address=recipient@example.com \
     -Dmail.subject="Test Email 3rd" \
     -Dmail.message="Testing 3rd" \
     -jar TestEmail.jar

