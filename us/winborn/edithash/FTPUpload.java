package us.winborn.edithash;

import com.myjavaworld.ftp.ControlConnectionEvent;
import com.myjavaworld.ftp.ControlConnectionListener;
import com.myjavaworld.ftp.DefaultFTPClient;
import com.myjavaworld.ftp.DefaultListParser;
import com.myjavaworld.ftp.DefaultRemoteFile;
import com.myjavaworld.ftp.FTPConnectionEvent;
import com.myjavaworld.ftp.FTPConnectionListener;
import java.io.File;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import us.winborn.library.Traffic;

public class FTPUpload implements FTPConnectionListener, ControlConnectionListener, Runnable {
   static int TYPE_ASCII = 1;
   static int TYPE_BINARY = 2;
   private JFrame frame;
   private String username;
   private String password;
   private String host;
   private String hostDir;
   private String[] files;
   private String localDir;
   private Thread thread;
   private Traffic traffic;
   private int fileType;

   public FTPUpload(JFrame frame, String username, String password, String host, String hostDir, String localDir, String[] files, Traffic traffic, int fileType) {
      this.frame = frame;
      this.username = username;
      this.password = password;
      this.host = host;
      this.hostDir = hostDir;
      this.localDir = localDir;
      this.files = files;
      this.traffic = traffic;
      this.fileType = fileType;
      this.thread = new Thread(this);
      this.thread.setDaemon(true);
      this.thread.start();
   }

   public void run() {
      try {
         DefaultFTPClient client = new DefaultFTPClient();
         DefaultListParser parser = new DefaultListParser();
         client.setListParser(parser);
         client.setPassive(true);
         client.connect(this.host);
         client.login(this.username, this.password);
         client.addControlConnectionListener(this);
         client.addFTPConnectionListener(this);

         for(int i = 0; i < this.files.length; ++i) {
            File source = new File(this.localDir, this.files[i]);
            DefaultRemoteFile dest = new DefaultRemoteFile(this.hostDir, this.files[i]);
            client.upload(source, dest, this.fileType, false, 0L);
         }

         client.disconnect();
      } catch (final Exception var6) {
         SwingUtilities.invokeLater(new Runnable() {
            public void run() {
               var6.printStackTrace();
               JOptionPane.showMessageDialog(FTPUpload.this.frame, var6.toString(), "Error message", 0);
               System.exit(1);
            }
         });
      }

   }

   public void connectionClosed(FTPConnectionEvent evt) {
      this.traffic.publish(evt.getMessage());
   }

   public void connectionOpened(FTPConnectionEvent evt) {
      this.traffic.publish(evt.getMessage());
   }

   public void commandSent(ControlConnectionEvent evt) {
   }

   public void replyReceived(ControlConnectionEvent evt) {
      this.traffic.publish(evt.getMessage());
   }
}
