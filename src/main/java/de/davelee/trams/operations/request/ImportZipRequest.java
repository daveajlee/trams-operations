package de.davelee.trams.operations.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

/**
 * This class represents a request to import data from a zip file into TraMS.
 * The data can either be in the GTFS or CSV format and must contain the format in the request.
 * Optionally a list of routes, valid from and valid to dates can be provided.
 */
@Getter
@Setter
public class ImportZipRequest {

    private MultipartFile zipFile;

    private String routesToImport;

    private String fileFormat;

    private String validFromDate;

    private String validToDate;

}
