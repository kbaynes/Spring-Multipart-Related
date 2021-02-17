# Spring-Multipart-Related

Test application for Spring Boot with Multipart/Related endpoint.

The Test file TestApplicationTests builds a multipart/related request and posts it to a given endpoint. It attaches the PDFs found in the 'resources' folder. By default the test targets the provided endpoint in TestController, which receives the request and writes the PDFs into the 'target' folder.

Run the test with 'mvn test' (or 'mvn -Dtest=TestApplicationTests test' to run a single test class). This test replaces any PostMan type test. It can be run against any running server, as the host, port and endpoint can be changed to point to your server.

The provided Test is a SpringBoot test, but it could be rewritten as a plain test or even a POJO to make executing it even faster.

Reference:

https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/converter/FormHttpMessageConverter.html


