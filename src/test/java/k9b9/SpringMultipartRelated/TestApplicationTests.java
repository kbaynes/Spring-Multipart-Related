package k9b9.SpringMultipartRelated;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class TestApplicationTests {

	@Value("${local.server.port}")
	int port;

	String host = "localhost";

	String endpoint = "multiRelated";

	@Test
	void contextLoads() throws IOException {

		// the files we are uploading
		List<String> files = List.of("IntegratingOutboundFaxServices.pdf",
			"ProposedArchitecture.pdf");
		
		// delete the previous output
		for (String file : files) {
			Path path = Paths.get("target/",file);
			Files.deleteIfExists(path);
		}

		MediaType multipartRelated = new MediaType("multipart", "related");

		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().stream()
			.filter(FormHttpMessageConverter.class::isInstance)
			.map(FormHttpMessageConverter.class::cast)
			.findFirst()
			.orElseThrow(() -> new IllegalStateException("Failed to find FormHttpMessageConverter"))
			.addSupportedMediaTypes(multipartRelated);
	   
		MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
		for (String file : files) {
			parts.add(file, new ClassPathResource(file));
		}
	   
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(multipartRelated);
	   
		restTemplate.postForLocation(String.format("http://%s:%s/%s",host,port,endpoint),
			new HttpEntity<>(parts, requestHeaders));
	}

}
