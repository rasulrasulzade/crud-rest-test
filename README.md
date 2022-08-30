// Run docker containers\
docker-compose up

// Run application with certain profile\
mvn spring-boot:run -Dspring-boot.run.profiles=dev 
\
mvn spring-boot:run -Dspring-boot.run.arguments=--spring.profiles.active=dev

// Run Single Test\
 mvn test -Dtest=TEST_CLASS

// Run Multiple Tests\
 mvn test -Dtest=TEST_CLASS1, TEST_CLASS2

// Run All Tests\
 mvn test

// Run tests with 'test' profile\
mvn test -Dspring.profiles.active=test

