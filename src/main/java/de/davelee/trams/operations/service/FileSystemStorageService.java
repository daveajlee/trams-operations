package de.davelee.trams.operations.service;

import de.davelee.trams.operations.configuration.StorageConfiguration;
import de.davelee.trams.operations.exception.StorageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * This class provides a service for storing and extracting zip files.
 */
@Service
public class FileSystemStorageService {

    private final Path rootLocation;

    /**
     * Create a new service with the location that should be used for storing files as a configuration parameter.
     * @param storageConfiguration a <code>StorageConfiguration</code> object with the location for storing files.
     */
    @Autowired
    public FileSystemStorageService(final StorageConfiguration storageConfiguration) {
        this.rootLocation = Paths.get(storageConfiguration.getLocation());
    }

    /**
     * Initialise the directory for storing files by creating it. If there are any errors in creating the directory,
     * then throw a new <code>StorageException</code> with an appropriate error message.
     */
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        }
        catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }

    /**
     * Delete all files containing in the specified location (including subfolders and files contained in them).
     */
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    /**
     * Store the supplied file in the configured upload directory. The file will be automatically extracted into
     * a directory with the same name as the file name (except without file extension).
     * @param file a <code>MultipartFile</code> object containing the file to upload to the directory.
     * @return a <code>String</code> containing the path to the directory where the extracted file contents are stored.
     */
    public String store(final MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file.");
            } else if ( file.getOriginalFilename() == null ) {
                throw new StorageException("Filename was not valid. Please verify and try again");
            }
            Path destinationFile = this.rootLocation.resolve(
                    Paths.get(file.getOriginalFilename()))
                    .normalize().toAbsolutePath();
            if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
                // This is a security check
                throw new StorageException(
                        "Cannot store file outside current directory.");
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile,
                        StandardCopyOption.REPLACE_EXISTING);
                return extractZipFile(destinationFile.toString());
            } catch ( FileAlreadyExistsException fileAlreadyExistsException ) {
                return extractZipFile(destinationFile.toString());
            }
        }
        catch (IOException e) {
            throw new StorageException("Failed to store file.", e);
        }
    }

    /**
     * This method extracts the supplied zip file path to a directory with the same name (except without file extension)
     * Method based on code from https://www.baeldung.com/java-compress-and-uncompress
     * @param zipFilePath a <code>String</code> with the path to the zip file to extract.
     * @return a <code>String</code> with the path to the directory containing the extracted files.
     */
    private String extractZipFile(final String zipFilePath) {
        File destDir = new File(zipFilePath.replace(".zip", ""));
        try {
            if ( !destDir.mkdir() && !destDir.exists() ) {
                throw new StorageException("Failed to create directory to contain extracted files");
            }
            byte[] buffer = new byte[1024];
            ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFilePath));
            ZipEntry zipEntry = zis.getNextEntry();
            while (zipEntry != null) {
                File newFile = new File(destDir + "/" + zipEntry.getName());
                if (zipEntry.isDirectory()) {
                    if (!newFile.isDirectory() && !newFile.mkdirs() && !newFile.exists()) {
                        throw new IOException("Failed to create directory " + newFile);
                    }
                } else {
                    // fix for Windows-created archives
                    File parent = newFile.getParentFile();
                    if (!parent.isDirectory() && !parent.mkdirs() && !parent.exists()) {
                        throw new IOException("Failed to create directory " + parent);
                    }

                    // write file content
                    FileOutputStream fos = new FileOutputStream(newFile);
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }
                    fos.close();
                }
                zipEntry = zis.getNextEntry();
            }
            zis.closeEntry();
            zis.close();
        } catch ( Exception exception ) {
            exception.printStackTrace();
        }
        return destDir.getAbsolutePath();
    }

}
