package restGateWay;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;


/**
 * RestGateWay is a REST gateway, providing default features such as GET PUT DELETE POST
 * My goal is to provide an easy navigation mode for users, browsing files and directory of a ftp server
 * 
 * @author DOUAILLE ERWAN & DOUYLLIEZ MAXIME
 */
@Path("/")
public class RestGateWay {

    private static NameStorageBean nameStorage = new NameStorageBean();

    /**
     * ************************************************** CONNECTION PART
     * *******************************************************************************************************************
     * *******************************************************************************************************************
     * *******************************************************************************************************************
     * ******************************************************************************************************************
     */
    
    /**
     * Connection using post parameters Home page
     *
     * @param user login 
     * @param password password 
     * @return String HTML login process, including redirection
     */
    @POST
    @Path("/signIn")
    @Produces("text/html")
    @SuppressWarnings("empty-statement")
    public String postSignIn(@FormParam("user") String user, @FormParam("password") String password) {
        try {
            FTPClient client = new FTPClient();
            client.connect("127.0.0.1", 2121);
            if (client.login(user, password)) {
                this.nameStorage.setLogin(user);
                this.nameStorage.setPassword(password);
                client.disconnect();
                return HTMLGenerator.getInstance().getSignInContent();
            }
        } catch (IOException ex) {
            return HTMLGenerator.getInstance().getSomethingWentWrong(ex.getMessage());
        }
        return HTMLGenerator.getInstance().getBadConnectionContent();
    }

    /**
     * Default login, using anonymous
     *
     * @return String containing HTML content
     */
    @GET
    @Path("/signIn")
    @Produces("text/html")
    public String getDefaultSignIn() {
        try {
            FTPClient client = new FTPClient();
            client.connect("127.0.0.1", 2121);
            if (client.login("anonymous", "")) {
                this.nameStorage.setLogin("anonymous");
                this.nameStorage.setPassword("");
                client.disconnect();
                return HTMLGenerator.getInstance().getSignInContent();
            }
        } catch (IOException ex) {
            return HTMLGenerator.getInstance().getSomethingWentWrong(ex.getMessage());
        }
        return HTMLGenerator.getInstance().getBadConnectionContent();
    }

    /**
     * Diconnection method, set bean's attributes to null, and redirect to the
     * login page
     *
     * @return String containing HTML content for disconnection including redirection to index
     */
    @GET
    @Path("/disconnect")
    @Produces("text/html")
    public String getDisconnection() {
        this.nameStorage.setLogin(null);
        this.nameStorage.setPassword(null);
        return HTMLGenerator.getInstance().getDisconnectionContent();
    }

    /**
     * ************************************************** LIST PART
     * *******************************************************************************************************************
     * *******************************************************************************************************************
     * *******************************************************************************************************************
     * ******************************************************************************************************************
     */
    /**
     * GetFileList return the root file list directory of the server This is the
     * default behavior when you get file/ without any parameter
     *
     * @return String containing HTML content 
     */
    @GET
    @Path("/file")
    @Produces("text/html")
    public String getFileList() {
        FTPClient client = new FTPClient();
        try {
            client.connect("127.0.0.1", 2121);
        } catch (IOException ex) {
            return HTMLGenerator.getInstance().getBadConnectionContent();
        }
        try {
            if (client.login(this.nameStorage.getLogin(), this.nameStorage.getPassword())) {
                FTPFile[] fileList = client.listFiles();
                String cwd = client.printWorkingDirectory();
                client.disconnect();
                return HTMLGenerator.getInstance().getFileListWith(cwd, fileList);
            }
        } catch (IOException ex) {
            return HTMLGenerator.getInstance().getSomethingWentWrong(ex.getMessage());
        }
        return "";
    }

    /**
     * GetFileList return the file list directory of the server using dir
     * parameter as the ftp server working directory
     *
     * @param dir current working directory
     * @return String containing HTML content
     */
    @GET
    @Path("/file/{var: .*}")
    @Produces("text/html")
    public String getFileList(@PathParam("var") String dir) {
        FTPClient client = new FTPClient();
        try {
            client.connect("127.0.0.1", 2121);
        } catch (IOException ex) {
            return HTMLGenerator.getInstance().getBadConnectionContent();
        }
        try {
            if (client.login(this.nameStorage.getLogin(), this.nameStorage.getPassword())) {
                client.cwd(dir);
                FTPFile[] fileList = client.listFiles();
                String cwd = client.printWorkingDirectory();
                client.disconnect();
                return HTMLGenerator.getInstance().getFileListWith(cwd, fileList);
            }
        } catch (IOException ex) {
            return HTMLGenerator.getInstance().getSomethingWentWrong(ex.getMessage());
        }
        return "";
    }
    
    
    /**
     * GetFileList return the file list under JSON format list directory of the server using dir
     * parameter as the ftp server working directory
     *
     * @param dir current working directory
     * @return FTPFile[], some kind of JSON
     */
    @GET
    @Path("/file/{var: .*}")
    @Produces("application/json")
    @SuppressWarnings("empty-statement")
    public FTPFile[] getFileListJSON(@PathParam("var") String dir) {
        FTPClient client = new FTPClient();
        String str = "No errors";
        FTPFile[] retour = new FTPFileEncapsulator[1];

        try {
            client.connect("localhost", 2121);
        } catch (IOException ex) {
            str = "Error during connection to Ftp Server";

            retour[0] = new FTPFileEncapsulator(str);

        }
        try {
            if (client.login(this.nameStorage.getLogin(), this.nameStorage.getPassword())) {
                client.cwd(dir);
                return client.listFiles();

            }
        } catch (IOException ex) {
            str = "Error during login validation";
            retour[0] = new FTPFileEncapsulator(str);


        }
        return retour;
    }

    /**
     * **************************************************DOWNLOAD PART
     * *******************************************************************************************************************
     * *******************************************************************************************************************
     * *******************************************************************************************************************
     * ******************************************************************************************************************
     */
    /**
     * GetFile start the download of fileName parameter include in dir directory
     *
     * @param dir current working directory
     * @param fileName file name than you want to download
     * @return Response for downloading 
     */
    @GET
    @Path("/file/{var: .*}/download/{fileName}")
    @Produces("application/octet-stream")
    public Response getFile(@PathParam("var") String dir, @PathParam("fileName") String fileName) {
        FTPClient client = new FTPClient();
        try {
            client.connect("127.0.0.1", 2121);
        } catch (IOException ex) {
            return null;
        }
        try {
            if (client.login(this.nameStorage.getLogin(), this.nameStorage.getPassword())) {
                client.cwd(dir);
                InputStream in = client.retrieveFileStream(fileName);
                Response reponse = Response.ok(in, MediaType.APPLICATION_OCTET_STREAM).build();
                client.disconnect();
                return reponse;
            }
        } catch (IOException ex) {
            return null;
        }
        return null;
    }

    /**
     * This method is resonsible to redirect the user to an upload form,
     * containing current working directory
     *
     * @param dir current working directory
     * @return String containing HTML content
     */
    @GET
    @Path("/store/{var: .*}")
    @Produces("text/html")
    public String getStoreForm(@PathParam("var") String dir) {
        return HTMLGenerator.getInstance().getUploadContent(dir);
    }

    /**
     * **************************************************DELETE PART
     * *******************************************************************************************************************
     * *******************************************************************************************************************
     * *******************************************************************************************************************
     * ******************************************************************************************************************
     */
    /**
     * This is the GET method use by html to delete files
     *
     * @param dir current working directory
     * @param fileName file which will be deleted
     * @return
     */
    @GET
    @Path("/file/{var: .*}/delete/{fileName}")
    @Produces("text/html")
    public String deleteFile(@PathParam("var") String dir, @PathParam("fileName") String fileName) {
        FTPClient client = new FTPClient();
        try {
            client.connect("127.0.0.1", 2121);
        } catch (IOException ex) {
            return HTMLGenerator.getInstance().getBadConnectionContent();
        }
        try {
            if (client.login(this.nameStorage.getLogin(), this.nameStorage.getPassword())) {
                client.cwd(dir);
                client.deleteFile(fileName);
                client.disconnect();
                return HTMLGenerator.getInstance().getRemoveConfirmation(dir, fileName);
            }
        } catch (IOException ex) {
            return HTMLGenerator.getInstance().getSomethingWentWrong(ex.getMessage());
        }
        return null;
    }

    
    /**
     * This is the method using DELETE keyword, the specified file will be
     * deleted
     *
     * @param file file which will be deleted
     * @return String containing HTML content
     */
    @DELETE
    @Path("/file/{var: .*}")
    @Produces("text/html")
    public String deleteFile(@PathParam("var") String file) {
        FTPClient client = new FTPClient();
        try {
            client.connect("127.0.0.1", 2121);
        } catch (IOException ex) {
            return HTMLGenerator.getInstance().getBadConnectionContent();
        }
        try {
            if (client.login(this.nameStorage.getLogin(), this.nameStorage.getPassword())) {
                client.deleteFile(file);
                client.disconnect();
                return HTMLGenerator.getInstance().getRemoveConfirmation("", file);
            }
        } catch (IOException ex) {
            return HTMLGenerator.getInstance().getSomethingWentWrong(ex.getMessage());
        }
        return "";
    }

    /**
     * ************************************************** UPLOAD PART
     * *******************************************************************************************************************
     * *******************************************************************************************************************
     * *******************************************************************************************************************
     * ******************************************************************************************************************
     */
    /**
     * This is the POST methode than provide you to upload files, param file is
     * the selected file to upload, dir is the directory where file has to be
     * uploaded
     *
     * @param file download file
     * @param fileDetail details of file
     * @param dir current working directory
     * @return String containing HTML content
     */
    @POST
    @Path("/file/{dir: .*}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces("text/html")
    public String upload(@FormDataParam("file") InputStream file, @FormDataParam("file") FormDataContentDisposition fileDetail, @PathParam("dir") String dir) {
        FTPClient client = new FTPClient();
        try {
            client.connect("127.0.0.1", 2121);
        } catch (IOException ex) {
            return HTMLGenerator.getInstance().getBadConnectionContent();
        }
        try {
            if (!client.login(this.nameStorage.getLogin(), this.nameStorage.getPassword())) {
                return HTMLGenerator.getInstance().getBadConnectionContent();
            } else {
                client.cwd(dir);
                client.storeFile(fileDetail.getFileName(), file);
                client.disconnect();
                return HTMLGenerator.getInstance().getUploadConfirmation(dir, fileDetail.getFileName());
            }
        } catch (IOException e) {
            return HTMLGenerator.getInstance().getSomethingWentWrong(e.getMessage());
        }
    }
    
    /**
     * This is the PUT methode than provide you to upload file
     * @param filePath file path which will be uploaded
     * @return  String containing HTML content
     */
    @PUT
    @Path("/upload/{file: .*}")
    public String putUpload(@PathParam("file") String filePath) {
        FTPClient client = new FTPClient();
        try {
            client.connect("127.0.0.1", 2121);
        } catch (IOException ex) {
            return HTMLGenerator.getInstance().getBadConnectionContent();
        }
        try {
            if (!client.login(this.nameStorage.getLogin(), this.nameStorage.getPassword())) {
                return HTMLGenerator.getInstance().getBadConnectionContent();
            } else {
                File file = new File(filePath);
                FileInputStream input = new FileInputStream(file);
                client.storeFile("/" + file.getName(), input);
                client.disconnect();
                return HTMLGenerator.getInstance().getUploadConfirmation("/", file.getName());
            }
        } catch (IOException e) {
            return HTMLGenerator.getInstance().getSomethingWentWrong(e.getMessage());
        }
    }
    
}
