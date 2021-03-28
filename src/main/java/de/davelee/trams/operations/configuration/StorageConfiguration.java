package de.davelee.trams.operations.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Configure the uploading and storage of files in Trams Operations.
 * @author Dave Lee
 */
@Configuration("storage")
public class StorageConfiguration {

    @Value("location")
    private String location = "src/main/resources/upload-dir";

    /**
     * Return the location of the path where uploaded files should be stored. The location is relevant to the location
     * where the Trams Operations folder is located.
     * @return a <code>String</code> containing the path to uploaded files.
     */
    public String getLocation() {
        return location;
    }

    /**
     * Set the location of the path where uploaded files should be stored. The location is relevant to the location
     * where the Trams Operations folder is located.
     * @param location a <code>String</code> containing the path to uploaded files.
     */
    public void setLocation(final String location) {
        this.location = location;
    }

}
