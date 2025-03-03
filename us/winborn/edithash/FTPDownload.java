package us.winborn.edithash;

import com.myjavaworld.ftp.ControlConnectionEvent;
import com.myjavaworld.ftp.ControlConnectionListener;
import com.myjavaworld.ftp.DefaultFTPClient;
import com.myjavaworld.ftp.DefaultListParser;
import com.myjavaworld.ftp.DefaultRemoteFile;
import com.myjavaworld.ftp.FTPConnectionEvent;
import com.myjavaworld.ftp.FTPConnectionListener;
import com.myjavaworld.ftp.RemoteFile;
import java.io.File;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import us.winborn.library.Traffic;

public class FTPDownload implements FTPConnectionListener, ControlConnectionListener, Runnable {
   static int TYPE_ASCII = 1;
   static int TYPE_BINARY = 2;
   private Traffic traffic;
   private EditHash frame;
   private String username;
   private String password;
   private String host;
   private String hostDir;
   private String localDir;
   private String regex;
   private Thread thread;
   private int fileType;

   public FTPDownload(EditHash frame, String username, String password, String host, String hostDir, String localDir, String regex, Traffic traffic, int fileType) {
      this.username = username;
      this.frame = frame;
      this.password = password;
      this.host = host;
      this.hostDir = hostDir;
      this.localDir = localDir;
      this.regex = regex;
      this.traffic = traffic;
      this.fileType = fileType;
      this.thread = new Thread(this);
      this.thread.setDaemon(true);
      this.thread.start();

      try {
         this.thread.join();
      } catch (InterruptedException var11) {
      }

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
         client.setWorkingDirectory(new DefaultRemoteFile(this.hostDir));
         RemoteFile[] files = client.list();

         for(int i = 0; i < files.length; ++i) {
            String name = files[i].getName();
            if (name.matches(this.regex)) {
               File dest = new File(this.localDir, name);
               client.download(files[i], dest, this.fileType, false);
            }
         }

         client.disconnect();
      } catch (final Exception var7) {
         SwingUtilities.invokeLater(new Runnable() {
            public void run() {
               var7.printStackTrace();
               JOptionPane.showMessageDialog(FTPDownload.this.frame, var7.toString(), "Error message", 0);
               System.exit(1);
            }
         });
      }

   }

   public void connectionOpened(FTPConnectionEvent evt) {
      this.traffic.publish(evt.getMessage());
   }

   public void connectionClosed(FTPConnectionEvent evt) {
      this.traffic.publish(evt.getMessage());
   }

   public void commandSent(ControlConnectionEvent evt) {
   }

   public void replyReceived(ControlConnectionEvent evt) {
      this.traffic.publish(evt.getMessage());
   }
}
