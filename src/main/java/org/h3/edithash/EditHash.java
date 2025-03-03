package org.h3.edithash;

import com.toedter.calendar.JCalendar;
import com.toedter.calendar.JDayChooser;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.SimpleTimeZone;
import java.util.TimeZone;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import org.h3.library.AboutBox;
import org.h3.library.Prefer;
import org.h3.library.Traffic;

public class EditHash extends JFrame implements PropertyChangeListener {
   private JCalendar calendarPanel;
   private Prefer prefer;
   private Traffic traffic;
   private int frameX;
   private int frameY;
   private int calendarW;
   private int calendarH;
   private String localDir;
   private ResourceBundle resbundle;
   private JMenu fileMenu;
   private JMenu editMenu;
   private JMenu helpMenu;
   static final JMenuBar mainMenuBar = new JMenuBar();

   public EditHash() throws HeadlessException {
      try {
         this.resbundle = ResourceBundle.getBundle("org.h3.edithash.strings");
         this.setupPreferences();
         this.setupMenus();
         this.setTitle(this.resbundle.getString("mainTitle"));
         this.frameX = Integer.parseInt(this.resbundle.getString("frameX"));
         this.frameY = Integer.parseInt(this.resbundle.getString("frameY"));
         this.calendarW = Integer.parseInt(this.resbundle.getString("calendarW"));
         this.calendarH = Integer.parseInt(this.resbundle.getString("calendarH"));
         this.setDefaultCloseOperation(3);
         this.calendarPanel = new JCalendar();
         Calendar calendar = this.calendarPanel.getCalendar();
         TimeZone timeZone = calendar.getTimeZone();
         timeZone = new SimpleTimeZone(-21600000, "America/Dallas", 2, 8, -1, 7200000, 10, 1, -1, 7200000, 3600000);
         calendar.setTimeZone(timeZone);
         JDayChooser chooser = this.calendarPanel.getDayChooser();
         chooser.setWeekOfYearVisible(false);
         chooser.setWeekdayForeground(Color.BLUE);
         chooser.setSundayForeground(Color.RED);
         chooser.setForeground(Color.BLACK);
         chooser.setAlwaysFireDayProperty(true);
         chooser.setFont(new Font("Lucida Console", 1, 15));
         chooser.addPropertyChangeListener(this);
         this.calendarPanel.setPreferredSize(new Dimension(this.calendarW, this.calendarH));
         this.traffic = new Traffic();
         this.traffic.setPreferredSize(new Dimension(Integer.parseInt(this.resbundle.getString("trafficW")), Integer.parseInt(this.resbundle.getString("trafficH"))));
         this.traffic.publish("FTP LOG:\n");
         JSplitPane splitPane = new JSplitPane(0, this.calendarPanel, this.traffic);
         this.setContentPane(splitPane);
         this.setLocation(this.frameX, this.frameY);
         this.pack();
         this.setVisible(true);
         this.checkLocalDir();
         this.checkPassword();
      } catch (Exception var5) {
         this.fatalError(var5);
      }

   }

   public void setupMenus() {
         this.fileMenu = new JMenu(this.resbundle.getString("fileMenu"));
         mainMenuBar.add(this.fileMenu);
         this.editMenu = new JMenu(this.resbundle.getString("editMenu"));
         mainMenuBar.add(this.editMenu);
         this.fileMenu.addSeparator();
         this.fileMenu.add(new JMenuItem(new EditHash.ExitActionClass()));
         this.editMenu.addSeparator();
         this.editMenu.add(new JMenuItem(new EditHash.PreferencesActionClass()));
         this.helpMenu = new JMenu(this.resbundle.getString("helpMenu"));
         this.helpMenu.add(new JMenuItem(new EditHash.AboutActionClass()));
         mainMenuBar.add(this.helpMenu);

      this.setJMenuBar(mainMenuBar);
   }

   private void setupPreferences() {
      this.prefer = new Prefer(this, this.resbundle.getString("preferencesNode"));
      String defaultLocalDir;
      if (System.getProperty("os.name").toLowerCase().trim().contains("win")) {
         defaultLocalDir = this.resbundle.getString("defaultLocalDir").replace("~/", System.getProperty("user.home") + "\\");
      } else {
         defaultLocalDir = this.resbundle.getString("defaultLocalDir").replace("~", System.getProperty("user.home"));
      }

      this.prefer.addString("localDir", defaultLocalDir, "Local directory for calendar files");
      this.prefer.addString("localIconDir", defaultLocalDir, "Local directory for icon files");
      this.prefer.addString("hostDir", this.resbundle.getString("defaultHostDir"), "Host directory for calendar files");
      this.prefer.addString("hostIconDir", this.resbundle.getString("defaultHostIconDir"), "Host directory for icon files");
      this.prefer.addString("host", this.resbundle.getString("defaultHost"), "Host domain name");
      this.prefer.addString("username", this.resbundle.getString("defaultUsername"), "Username on host");
      this.prefer.addString("password", this.resbundle.getString("defaultPassword"), "Password on host");
      this.prefer.addInteger("editHeight", Integer.parseInt(this.resbundle.getString("defaultEditHeight")), "Height of editor box in pixels");
      this.prefer.addInteger("editWidth", Integer.parseInt(this.resbundle.getString("defaultEditWidth")), "Width of editor box in characters");
      this.prefer.addString("myName", System.getProperty("user.name").split(" ")[0], "Name that appears on posts");
      this.prefer.addBoolean("autodown", this.resbundle.getString("defaultAutodown").equals("true"), "Automatically download data from host before editing");
      this.prefer.addBoolean("tweet", this.resbundle.getString("defaultTweet").equals("true"), "Enable tweets");
      this.prefer.addBoolean("autoup", this.resbundle.getString("defaultAutoup").equals("true"), "Automatically upload data to host on submit");
      this.prefer.addBoolean("remind", this.resbundle.getString("defaultRemind").equals("true"), "Remind the user to upload if autoup is false");
   }

   public void fatalError(Exception ex) {
      ex.printStackTrace();
      JOptionPane.showMessageDialog(this, ex.toString(), "Error message", 0);
      Date now = new Date();
      String filename = String.format("EditHashStackDump-%d.txt", now.getTime());

      try {
         PrintWriter pw = new PrintWriter(new File(this.localDir, filename));
         ex.printStackTrace(pw);
         pw.flush();
         pw.close();
      } catch (Exception var5) {
      }

      System.exit(1);
   }

   public void checkLocalDir() {
      try {
         this.localDir = this.prefer.getText("localDir");
         File localDirectory = new File(this.localDir);

         Object[] option;
         int sts;
         while(!localDirectory.exists()) {
            option = new Object[]{"Yes", "No", "Edit Preferences"};
            sts = JOptionPane.showOptionDialog(this, "Local directory “" + localDirectory.getAbsolutePath() + "” Does not exist. Create it?" + "\nClick “Edit Preferences” to pick a different directory location", "Warning", 1, 2, (Icon)null, option, option[0]);
            if (sts == 0) {
               localDirectory.mkdirs();
            } else if (sts == 1) {
               System.exit(0);
            } else if (sts == 2) {
               this.prefer.showPreferences("Edit Preferences");
               this.localDir = this.prefer.getText("localDir");
               localDirectory = new File(this.localDir);
            } else if (sts == -1) {
               System.exit(0);
            }
         }

         if (!this.prefer.getBoolean("autodown")) {
            option = new Object[]{"Edit Preferences", "I know what I’m doing"};
            sts = JOptionPane.showOptionDialog(this, "Warning: Automatic download is not enabled!", "Watch Out!", 0, 2, (Icon)null, option, option[0]);
            if (sts == 0) {
               this.prefer.showPreferences("Edit Preferences");
            }
         }
      } catch (Exception var4) {
         this.fatalError(var4);
      }

   }

   public void checkPassword() {
      try {
         String password = this.prefer.getText("password");
         String username = this.prefer.getText("username");

         while(password.length() == 0 || username.length() == 0) {
            Object[] options = new Object[]{"Edit Preferences", "Ignore"};
            int sts = JOptionPane.showOptionDialog(this, "The website's username and/or password have not been set in the preferences.\nYou must get this information from Clodhopper.\nClick “Edit Preferences” if you have this information and wish to set it.", "Warning", 0, 2, (Icon)null, options, options[0]);
            if (sts == 1) {
               break;
            }

            if (sts == 0) {
               this.prefer.showPreferences("Edit Preferences");
               password = this.prefer.getText("password");
               username = this.prefer.getText("username");
            }
         }
      } catch (Exception var5) {
         this.fatalError(var5);
      }

   }

   public void propertyChange(PropertyChangeEvent evt) {
      Calendar calendar = this.calendarPanel.getCalendar();
      if (evt.getPropertyName().equals("day")) {
         try {
            new ReaderGenerator(calendar, this.prefer, this, this.traffic);
         } catch (Exception var4) {
            this.fatalError(var4);
         }
      }

   }

   public static void main(String[] args) {
      SwingUtilities.invokeLater(new Runnable() {
         public void run() {
            new EditHash();
         }
      });
   }

   public class AboutActionClass extends AbstractAction {
      public AboutActionClass() {
         super(EditHash.this.resbundle.getString("aboutItem"));
      }

      public void actionPerformed(ActionEvent e) {
         AboutBox about = new AboutBox(EditHash.this.resbundle.getString("appName"), EditHash.this.resbundle.getString("appVersion"), EditHash.this.resbundle.getString("created"));
         about.setResizable(false);
         about.setVisible(true);
      }
   }

   public class ExitActionClass extends AbstractAction {
      public ExitActionClass() {
         super(EditHash.this.resbundle.getString("exitItem"));
         this.putValue("AcceleratorKey", KeyStroke.getKeyStroke(79, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
      }

      public void actionPerformed(ActionEvent e) {
         System.exit(0);
      }
   }

   public class PreferencesActionClass extends AbstractAction {
      public PreferencesActionClass() {
         super(EditHash.this.resbundle.getString("preferencesItem"));
         this.putValue("AcceleratorKey", KeyStroke.getKeyStroke(80, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
      }

      public void actionPerformed(ActionEvent e) {
         EditHash.this.prefer.showPreferences("Preferences");
         EditHash.this.checkLocalDir();
      }
   }
}
