package org.h3.edithash;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FTPUpload {
    private String server;
    private int port = 21;
    private String user;
    private String password;

    public FTPUpload(String server, String user, String password) {
        this.server = server;
        this.user = user;
        this.password = password;
    }

    public boolean uploadFile(String localFilePath, String remoteFilePath) {
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(server, port);
            ftpClient.login(user, password);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            File localFile = new File(localFilePath);
            FileInputStream inputStream = new FileInputStream(localFile);

            boolean done = ftpClient.storeFile(remoteFilePath, inputStream);
            inputStream.close();
            ftpClient.logout();
            ftpClient.disconnect();
            return done;
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}