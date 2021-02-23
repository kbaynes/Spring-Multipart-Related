package k9b9.SpringMultipartRelated;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Enumeration;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@MultipartConfig
public class TestController {

    @PostMapping(value = "/multiRelated", consumes = { MediaType.MULTIPART_RELATED_VALUE })
    public void multiRelated(HttpServletRequest request) throws IOException, ServletException {
        System.out.println("Begin Receive");
        System.out.println("Input.finished: " + request.getInputStream().isFinished());
        System.out.println("Input.ready: " + request.getInputStream().isReady());
        byte[] bodyBytes = request.getInputStream().readAllBytes();
        // System.out.println("Body: "+new String(bodyBytes));
        // byte[] bytes = new byte[request.getContentLength()];
        // request.getInputStream().read(bytes, 0, request.getContentLength());
        // System.out.println("Body: "+new String(bytes));
        // MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest)
        // request;
        // CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        // System.out.println("isMultiPart: "+resolver.isMultipart(request));
        // MultipartHttpServletRequest resolveMultipart =
        // resolver.resolveMultipart(request);
        Enumeration<String> headers = request.getHeaderNames();
        System.out.println("Parameters: " + request.getParameterMap().values().stream().collect(Collectors.toList()));
        System.out.println("ContentLength: " + request.getContentLength());
        System.out.println("ContentType: " + request.getContentType());
        System.out.println("Protocol: " + request.getProtocol());
        String contentType = request.getContentType();
        String boundary = contentType.substring(contentType.indexOf("\"") + 1, contentType.lastIndexOf("\""));
        boundary = "--" + boundary;
        // int idx = 0;
        // read array in chunks (bigger than boundary)
        ByteArrayInputStream in = new ByteArrayInputStream(bodyBytes);
        byte[] buffer = new byte[1024];
        int read = 0;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int fileCount = 0;
        String type = null;
        String charset = null;
        while ((read = in.read(buffer)) > 0) {
            String s = new String(buffer);
            System.out.println(s);
            int idxBoundary = s.indexOf(boundary);
            int idxBoundaryEnd = s.indexOf(boundary + "--");
            int offset = -1;
            if (idxBoundary != -1 && out.size() == 0) {
                // we are starting a new boundary
                // go to the end of the boundary plus one (the carriage return)
                offset = idxBoundary + boundary.length() + 2;
                String bufferString = new String(buffer);
                String line = bufferString.substring(offset, bufferString.lastIndexOf("\n"));
                type = line.substring("Content-Type: ".length(), line.indexOf(";"));
                charset = line.substring(line.indexOf("charset=") + "charset=".length(), line.indexOf("\n"));
                offset = line.length() + 1;
                out.write(buffer, offset, buffer.length - offset);
            } else if (idxBoundary != -1 && out.size() > 0) {
                // we just found a new boundary
                // finish up the last file
                offset = idxBoundary - 1;
                out.write(buffer, 0, offset);
                // out buffer contains a full file now
                // write it out
                String dotThree = type.contains("xml") ? ".xml" : ".pdf";
                Files.write(Paths.get("./target/File" + fileCount + dotThree), out.toByteArray());
                fileCount++;
                if (idxBoundary == idxBoundaryEnd) {
                    // that was the last file
                } else {
                    // start a new file
                    out = new ByteArrayOutputStream();
                    // go to the end of the boundary plus one (the carriage return)
                    offset = idxBoundary + boundary.length() + 2;
                    String bufferString = new String(buffer);
                    String line = bufferString.substring(offset, bufferString.lastIndexOf("\n"));
                    if (line.indexOf(";")!=-1) {
                        type = line.substring("Content-Type: ".length(), line.indexOf(";"));
                    } else {
                        type = line.substring("Content-Type: ".length(), line.length());
                    }
                    if (line.indexOf("charset=")!=-1) {
                        charset = line.substring(line.indexOf("charset=") + "charset=".length(), line.indexOf("\n"));
                    }
                    offset = line.length() + 1;
                    out.write(buffer, offset, buffer.length - offset);
                }
            }
        }
        in.close();

        System.out.println(headers);
        System.out.println("End Receive");
    }
}
