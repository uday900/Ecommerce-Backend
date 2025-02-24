package com.uday.service;

import org.springframework.stereotype.Service;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.uday.Dto.ImageResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.Collections;


@Service
public class ImageStorageToDriveService {
	
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String SERVICE_ACOUNT_KEY_PATH = getPathToGoogleCredentials();
    
    private static String getPathToGoogleCredentials() {
        String currentDirectory = System.getProperty("user.dir");
        Path filePath = Paths.get(currentDirectory, "ecommerce-application-cloud-storage-images.json");
        return filePath.toString();
    }

    
    public ImageResponse uploadImageToDrive(File file) throws GeneralSecurityException, IOException {
    	ImageResponse res = new ImageResponse();

        try{
            String folderId = "1KsSfPzPVwG_qFJwrrSmNS6tG2n1iZZPs";
            Drive drive = createDriveService();
            com.google.api.services.drive.model.File fileMetaData = new com.google.api.services.drive.model.File();
            fileMetaData.setName(file.getName());
            fileMetaData.setParents(Collections.singletonList(folderId));
            FileContent mediaContent = new FileContent("image/jpeg", file);
            com.google.api.services.drive.model.File uploadedFile = drive.files().create(fileMetaData, mediaContent)
                    .setFields("id").execute();
            //setting url
            //https://drive.google.com/thumbnail?id=1b6_KYko9hsOPk-vCJ1UPI5F1Me2EXkDV&sz=w1000
            
            
//            String imageUrl = "https://drive.google.com/uc?export=view&id="+uploadedFile.getId(); //not working
            String imageUrl = "https://drive.google.com/thumbnail?id="+uploadedFile.getId(); //+"&sz=w1000"
            System.out.println("IMAGE URL: " + imageUrl);
            file.delete();
            
            res.setStatus(200);
            res.setMessage("Image Successfully Uploaded To Drive");
            res.setUrl(imageUrl);
            
        }catch (Exception e){
            System.out.println("error message uploading image" + e.getMessage());
            res.setStatus(500);
            res.setMessage(e.getMessage());
        }
        return  res;

    }
    
    public ImageResponse updateImageOnDrive(String fileId, File newImageFile) throws GeneralSecurityException, IOException {
    	ImageResponse res = new ImageResponse();
        try {
            Drive drive = createDriveService();

            // Set metadata for the file (if you want to change name or other properties)
            com.google.api.services.drive.model.File fileMetadata = new com.google.api.services.drive.model.File();
            fileMetadata.setName(newImageFile.getName());

            // Create a FileContent object for the new image file
            FileContent mediaContent = new FileContent("image/jpeg", newImageFile);

            // Update the file content and metadata
            com.google.api.services.drive.model.File updatedFile = drive.files().update(fileId, fileMetadata, mediaContent)
                    .setFields("id, name, webViewLink").execute();

//            String updatedImageUrl = "https://drive.google.com/uc?export=view&id=" + updatedFile.getId();
            String updatedImageUrl = "https://drive.google.com/thumbnail?id="+updatedFile.getId();
            System.out.println("Updated IMAGE URL: " + updatedImageUrl);

            res.setStatus(200);
            res.setMessage("Image Successfully Updated on Drive");
            res.setUrl(updatedImageUrl);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            res.setStatus(500);
            res.setMessage(e.getMessage());
        }
        return res;
    }
    
    
    
    public ImageResponse deleteImageFromDrive(String fileId) throws GeneralSecurityException, IOException {
    	ImageResponse res = new ImageResponse();
        try {
            Drive drive = createDriveService();
            drive.files().delete(fileId).execute();
            res.setStatus(200);
            res.setMessage("Image Successfully Deleted From Drive");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            res.setStatus(500);
            res.setMessage(e.getMessage());
        }
        return res;
    }

    private Drive createDriveService() throws GeneralSecurityException, IOException {

        GoogleCredential credential = GoogleCredential.fromStream(new FileInputStream(SERVICE_ACOUNT_KEY_PATH))
                .createScoped(Collections.singleton(DriveScopes.DRIVE));

        return new Drive.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JSON_FACTORY,
                credential)
        		.setApplicationName("shopwithdarla")
                .build();

    }

    public String extractDriveId(String url) {
	    String prefix = "id=";
	    int startIndex = url.indexOf(prefix) + prefix.length();
	    int endIndex = url.indexOf("&", startIndex);

	    if (endIndex == -1) { // If no '&' is found, take till the end of the string
            return  url.substring(startIndex);
        } 
	    return url.substring(startIndex, endIndex);
        
	}
}
