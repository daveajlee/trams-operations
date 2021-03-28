package de.davelee.trams.operations.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class ImportGtfsZipRequest {

    private MultipartFile zipFile;

    private String routesToImport;

}
