package org.h3.edithash;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class FTPDownload {
    private String server;
    private int port = 21;
    private String user;
    private String password;

    public FTPDownload(String server, String user, String password) {
        this.server = server;
        this.user = user;
        this.password = password;
    }

    public boolean downloadFile(String remoteFilePath, String localFilePath) {
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(server, port);
            ftpClient.login(user, password);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            OutputStream outputStream = new FileOutputStream(localFilePath);
            boolean success = ftpClient.retrieveFile(remoteFilePath, outputStream);
            outputStream.close();
            ftpClient.logout();
            ftpClient.disconnect();
            return success;
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
