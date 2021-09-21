package de.davelee.trams.operations.service;

import de.davelee.trams.operations.configuration.StorageConfiguration;
import de.davelee.trams.operations.exception.StorageException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

/**
 * This class tests the FileSystemStorageService class and ensures that the file operations work successfully.
 * @author Dave Lee
 */
@SpringBootTest
public class FileSystemStorageServiceTest {

    /**
     * Verify that a zip file can be loaded and extracted successfully.
     */
    @Test
    public void testStoreAndExtractFile ( ) {
        try {
            StorageConfiguration storageConfiguration = new StorageConfiguration();
            storageConfiguration.setLocation("src/test/resources/upload-dir");
            FileSystemStorageService fileSystemStorageService = new FileSystemStorageService(storageConfiguration);
            fileSystemStorageService.deleteAll();
            fileSystemStorageService.init();
            FileInputStream inputFile = new FileInputStream("src/test/resources/testfiles.zip");
            //This ensures that we can work on different computers but still verify results by checking only relative part.
            assertThat(fileSystemStorageService.store(
                    new MockMultipartFile("file", "testfiles.zip", "multipart/form-data", inputFile)),
                    containsString("/src/test/resources/upload-dir/testfiles"));
            fileSystemStorageService.deleteAll();
        } catch ( Exception exception ) {
            exception.printStackTrace();
        }

    }

    /**
     * Verify that an invalid directory cannot be created.
     */
    @Test
    public void testInvalidDirectory ( ) {
        StorageConfiguration storageConfiguration = new StorageConfiguration();
        storageConfiguration.setLocation("/#$");
        FileSystemStorageService fileSystemStorageService = new FileSystemStorageService(storageConfiguration);
        fileSystemStorageService.deleteAll();
        Assertions.assertThrows(StorageException.class, () -> fileSystemStorageService.init());
    }

    /**
     * Verify that an invalid file cannot be stored.
     */
    @Test
    public void testNullEmptyMultipartFile ( ) {
        StorageConfiguration storageConfiguration = new StorageConfiguration();
        storageConfiguration.setLocation("src/test/resources/upload-dir");
        FileSystemStorageService fileSystemStorageService = new FileSystemStorageService(storageConfiguration);
        try {
            FileInputStream inputFile = new FileInputStream("src/test/resources/testemptyfiles.zip");
            Assertions.assertThrows(StorageException.class, () -> fileSystemStorageService.store((
                new MockMultipartFile("file", "testfiles.zip", "multipart/form-data", inputFile))));
        } catch ( Exception exception ) {
            exception.printStackTrace();
        }
    }

    /**
     * Verify that an invalid file cannot be stored.
     */
    @Test
    public void testNullOriginalFilename ( ) {
        StorageConfiguration storageConfiguration = new StorageConfiguration();
        storageConfiguration.setLocation("src/test/resources/upload-dir");
        FileSystemStorageService fileSystemStorageService = new FileSystemStorageService(storageConfiguration);
        try {
            FileInputStream inputFile = new FileInputStream("src/test/resources/testfiles.zip");
            Assertions.assertThrows(StorageException.class, () -> fileSystemStorageService.store((
                    new MockMultipartFile("file", null, "multipart/form-data", inputFile))));
        } catch ( Exception exception ) {
            exception.printStackTrace();
        }
    }

    /**
     * Verify that an empty file cannot be stored.
     */
    @Test
    public void testNullEmptyMultipartFileStore ( ) {
        StorageConfiguration storageConfiguration = new StorageConfiguration();
        storageConfiguration.setLocation("src/test/resources/upload-dir");
        FileSystemStorageService fileSystemStorageService = new FileSystemStorageService(storageConfiguration);
        try {
            InputStream inputFile = this.getClass().getResourceAsStream("testemptyfiles.zip");
            Assertions.assertThrows(StorageException.class, () -> fileSystemStorageService.store((
                    new MockMultipartFile("file", "testfiles.zip", "multipart/form-data", inputFile))));
        } catch ( Exception exception ) {
            exception.printStackTrace();
        }
    }

}
