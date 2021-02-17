package k9b9.SpringMultipartRelated;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @PostMapping(value = "/multiRelated", consumes = { MediaType.MULTIPART_RELATED_VALUE })
    public void multiRelated(HttpServletRequest request) throws IOException, ServletException {
        Enumeration<String> headers = request.getHeaderNames();
        // each part is a file
        Collection<Part> parts = request.getParts();
        parts.forEach(part -> {
            System.out.println(part.getName());
            Path path = Paths.get("target/",part.getName());
            try {
                Files.write(path, part.getInputStream().readAllBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        System.out.println(headers);
    }
}
