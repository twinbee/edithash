package org.h3.edithash;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.h3.library.ItemLayout;
import org.h3.library.Prefer;
import org.h3.library.Traffic;

public class HashEventEditor extends JDialog {
   private static final long serialVersionUID = 8948782060613226112L;
private JFrame frame;
   private DaysData daysData;
   private TextPanel kennelName;
   private TextPanel iconName;
   private TextPanel hashTitle;
   private TextPanel runNo;
   private TextPanel hares;
   private TextPanel startTime;
   private TextPanel startAddress;
   private TextPanel mapLink;
   private TextPanel hashCash;
   private TextPanel turds;
   private TextPanel tweet;
   private TextPanel twilight;
   private TextPanel date;
   private TextPanel description;
   private int status;
   private JScrollBar vscrollbar;
   private JPanel editPanel;
   private JScrollPane scrollPane;
   private boolean doTweet;
   private int editHeight;
   private int editWidth;
   private String myName;
   private String username;
   private String password;
   private String host;
   private String hostIconDir;
   private String localIconDir;
   private String year;
   private Calendar calendar;
   private Traffic traffic;
   private ResourceBundle resbundle;
   public static final int SUBMIT = 1;
   public static final int CANCEL = 2;
   public static final int DELETE = 3;
   public static final int NEXT = 4;
   public static final int SUBMIT_THEN_NEXT = 5;

   public int getStatus() {
      return this.status;
   }

   public HashEventEditor(JFrame frame, Calendar calendar, DaysData daysData, Prefer prefer, Traffic traffic) throws Exception {
      super(frame, true);
      this.frame = frame;
      this.daysData = daysData;
      this.calendar = calendar;
      this.traffic = traffic;
      this.resbundle = ResourceBundle.getBundle("org.h3.edithash.strings");
      this.doTweet = prefer.getBoolean("tweet");
      this.editHeight = prefer.getInt("editHeight");
      this.editWidth = prefer.getInt("editWidth");
      this.myName = prefer.getText("myName");
      this.username = prefer.getText("username");
      this.password = prefer.getText("password");
      this.host = prefer.getText("host");
      this.hostIconDir = prefer.getText("hostIconDir");
      this.localIconDir = prefer.getText("localIconDir");
      this.year = String.valueOf(calendar.get(1));
      this.setTitle(String.format("Hash Event for %s, %s %d, %d", calendar.getDisplayName(7, 2, Locale.getDefault()), calendar.getDisplayName(2, 2, Locale.getDefault()), calendar.get(5), calendar.get(1)));
      this.setDefaultCloseOperation(0);
      this.addWindowListener(new HashEventEditor.Closelistener());
      this.setLayout(new BorderLayout());
      this.editPanel = new JPanel(new ItemLayout(0));
      int columns = this.editWidth;
      this.kennelName = new TextPanel("Kennel Name:", 1, columns);
      this.editPanel.add(this.kennelName);
      this.kennelName.setText(StringParser.html2text(daysData.getKennelName()));
      this.iconName = new TextPanel("Icon File Name:", 1, 20, new JButton(new HashEventEditor.uploadActionClass()));
      this.editPanel.add(this.iconName);
      this.iconName.setText(StringParser.html2text(daysData.getIconName()));
      this.date = new TextPanel("Date:", 1, 20);
      this.date.setText(StringParser.html2text(daysData.getDate()));
      this.twilight = new TextPanel("End of Twilight:", 1, 20);
      this.twilight.setText(StringParser.html2text(daysData.getTwilight()));
      this.runNo = new TextPanel("Run No:", 1, 1);
      this.editPanel.add(this.runNo);
      this.runNo.setText(StringParser.html2text(daysData.getRunNo()));
      this.hashTitle = new TextPanel("Title of this Hash:", 1, columns);
      this.editPanel.add(this.hashTitle);
      this.hashTitle.setText(StringParser.html2text(daysData.getHashTitle()));
      this.startTime = new TextPanel("Start Time:", 1, columns);
      this.editPanel.add(this.startTime);
      this.startTime.setText(StringParser.html2text(daysData.getStartTime()));
      this.startAddress = new TextPanel("Start Address:", 1, columns);
      this.editPanel.add(this.startAddress);
      this.startAddress.setText(StringParser.html2text(daysData.getStartAddress()));
      this.mapLink = new TextPanel("Map Link:", 1, columns);
      this.editPanel.add(this.mapLink);
      this.mapLink.setText(StringParser.html2text(daysData.getMapLink()));
      this.hares = new TextPanel("Hares:", 1, columns);
      this.editPanel.add(this.hares);
      this.hares.setText(StringParser.html2text(daysData.getHares()));
      this.hashCash = new TextPanel("Hash Cash:", 1, columns);
      this.editPanel.add(this.hashCash);
      this.hashCash.setText(StringParser.html2text(daysData.getHashCash()));
      this.turds = new TextPanel("TURDs:", 1, columns);
      this.editPanel.add(this.turds);
      this.turds.setText(StringParser.html2text(daysData.getTurds()));
      this.tweet = new TextPanel("Tweet:", 1, columns);
      this.tweet.SetCharacterLimit(140);
      this.tweet.setText(StringParser.html2text(daysData.getTweet()));
      if (this.doTweet) {
         this.editPanel.add(this.tweet);
      }

      this.description = new TextPanel("Description:", 1, columns);
      this.editPanel.add(this.description);
      this.description.setText(StringParser.html2text(daysData.getDescription()));
      this.editPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.WHITE, 6), BorderFactory.createLineBorder(Color.RED, 2)));
      this.editPanel.doLayout();
      this.scrollPane = new JScrollPane(this.editPanel, 22, 31);
      JPanel buttonPanel = new JPanel(new FlowLayout(1, 20, 10));
      JButton submitButton = new JButton(new HashEventEditor.submitActionClass());
      JButton nextButton = new JButton(new HashEventEditor.nextActionClass());
      JButton deleteButton = new JButton(new HashEventEditor.deleteActionClass());
      buttonPanel.add(deleteButton);
      buttonPanel.add(nextButton);
      buttonPanel.add(submitButton);
      this.add(buttonPanel, "South");
      this.add(this.scrollPane, "Center");
      this.setLocation(frame.getLocation());
      this.pack();
      Dimension d = this.getPreferredSize();
      d.height = this.editHeight;
      this.setPreferredSize(d);
      this.vscrollbar = this.scrollPane.getVerticalScrollBar();
      SwingUtilities.invokeLater(new HashEventEditor.AutoScroll());
      this.pack();
      this.setVisible(true);
   }

   private void generateTweet(int timeMinutes) {
      int hours = (int)((double)timeMinutes / 60.0D);
      int minutes = timeMinutes - hours * 60;
      if (hours < 0) {
         hours = 0;
      }

      String amPM;
      if (hours > 12) {
         amPM = "PM";
         hours -= 12;
      } else {
         if (hours == 0) {
            hours = 12;
         }

         amPM = "AM";
      }

      if (hours < 0) {
         hours = 0;
      }

      String start;
      if (minutes == 0) {
         start = String.format("%d%s", hours, amPM);
      } else {
         start = String.format("%d:%02d%s", hours, minutes, amPM);
      }

      String hareText = this.hares.getText().trim().replaceAll("\n", ", ");
      hareText = hareText.equals("") ? "" : "Hares: " + hareText;
      String template = "%s @ %s: %s. %s";
      String text = String.format(template, this.kennelName.getText(), start, this.startAddress.getText(), hareText);
      this.tweet.setText(StringParser.collapseWhitespace(text));
   }

   public boolean checkSave() {
      if (this.kennelName.changed() || this.iconName.changed() || this.hashTitle.changed() || this.runNo.changed() || this.hares.changed() || this.startTime.changed() || this.startAddress.changed() || this.mapLink.changed() || this.hashCash.changed() || this.turds.changed() || this.tweet.changed() || this.description.changed() || this.twilight.changed() || this.date.changed() || this.description.changed()) {
         int sts = JOptionPane.showConfirmDialog(this.frame, "You've made changes! Do you want to save them?", "Warning", 0, 2);
         if (sts == 0) {
            return true;
         }
      }

      return false;
   }

   public boolean saveData() {
      int timeMinutes;
      try {
         timeMinutes = StringParser.parseTime(this.startTime.getText());
      } catch (TimeParseException var5) {
         JOptionPane.showMessageDialog((Component)null, "Error parsing start time\n" + var5.toString(), "Error message", 0);
         return false;
      }

      if (this.doTweet && this.tweet.getText().length() == 0 && this.startAddress.getText().length() != 0) {
         int sts = JOptionPane.showConfirmDialog(this.frame, "There is no tweet! Generate one?", "Warning", 1, 2);
         if (sts == 0) {
            this.generateTweet(timeMinutes);
            return false;
         }

         if (sts == 2) {
            return false;
         }
      }

      GregorianCalendar now = new GregorianCalendar(this.calendar.getTimeZone());
      String update = String.format("%d/%d/%d %d:%02d", now.get(2) + 1, now.get(5), now.get(1) - 2000, now.get(11), now.get(12));
      String[] sa = this.resbundle.getString("appVersion").trim().split(" ");
      update = update + " (" + this.myName + ") " + sa[0];
      this.daysData.setKennelName(StringParser.text2html(this.kennelName.getText()));
      this.daysData.setIconName(StringParser.text2html(this.iconName.getText()));
      this.daysData.setHashTitle(StringParser.text2html(this.hashTitle.getText()));
      this.daysData.setRunNo(StringParser.text2html(this.runNo.getText()));
      this.daysData.setHares(StringParser.text2html(this.hares.getText()));
      this.daysData.setStartTime(StringParser.text2html(this.startTime.getText()));
      this.daysData.setStartAddress(StringParser.text2html(this.startAddress.getText()));
      this.daysData.setMapLink(StringParser.text2html(this.mapLink.getText()));
      this.daysData.setHashCash(StringParser.text2html(this.hashCash.getText()));
      this.daysData.setTurds(StringParser.text2html(this.turds.getText()));
      this.daysData.setTweet(StringParser.text2html(this.tweet.getText()));
      this.daysData.setTwilight(StringParser.text2html(this.twilight.getText()));
      this.daysData.setUpdated(StringParser.text2html(update));
      this.daysData.setDate(StringParser.text2html(this.date.getText()));
      this.daysData.setDescription(StringParser.text2html(this.description.getText()));
      this.daysData.setOrder(this.daysData.getDayOfMonth() * 1440 + timeMinutes);
      return true;
   }

   class AutoScroll implements Runnable {
      public void run() {
         HashEventEditor.this.vscrollbar.setValue(HashEventEditor.this.vscrollbar.getMinimum());
      }
   }

   public class Closelistener extends WindowAdapter {
      public void windowClosing(WindowEvent e) {
         boolean save = HashEventEditor.this.checkSave();
         if (save) {
            boolean dataOK = HashEventEditor.this.saveData();
            if (!dataOK) {
               return;
            }

            HashEventEditor.this.status = 1;
         } else {
            HashEventEditor.this.status = 2;
         }

         HashEventEditor.this.setVisible(false);
         HashEventEditor.this.dispose();
      }
   }

   public class deleteActionClass extends AbstractAction {
      public deleteActionClass() {
         super("Delete");
      }

      public void actionPerformed(ActionEvent e) {
         int sts = JOptionPane.showConfirmDialog(HashEventEditor.this.frame, "Do you really want to delete this event?", "Warning", 0, 2);
         if (sts == 0) {
            HashEventEditor.this.status = 3;
            HashEventEditor.this.setVisible(false);
            HashEventEditor.this.dispose();
         }

      }
   }

   public class nextActionClass extends AbstractAction {
      public nextActionClass() {
         super("Edit Next Entry for Today");
      }

      public void actionPerformed(ActionEvent e) {
         boolean save = HashEventEditor.this.checkSave();
         if (save) {
            boolean dataOK = HashEventEditor.this.saveData();
            if (!dataOK) {
               return;
            }

            HashEventEditor.this.status = 5;
         } else {
            HashEventEditor.this.status = 4;
         }

         HashEventEditor.this.setVisible(false);
         HashEventEditor.this.dispose();
      }
   }

   public class submitActionClass extends AbstractAction {
      public submitActionClass() {
         super("Submit");
      }

      public void actionPerformed(ActionEvent e) {
         boolean dataOK = HashEventEditor.this.saveData();
         if (dataOK) {
            HashEventEditor.this.status = 1;
            HashEventEditor.this.setVisible(false);
            HashEventEditor.this.dispose();
         }
      }
   }

   public class uploadActionClass extends AbstractAction {
      public uploadActionClass() {
         super("Upload Icon");
      }

      public void actionPerformed(ActionEvent e) {
         File localDirFile = new File(HashEventEditor.this.localIconDir);
         File f = new File(localDirFile, HashEventEditor.this.year);
         if (f.exists() && f.isDirectory()) {
            localDirFile = f;
         }

         String hostDirPath = HashEventEditor.this.hostIconDir;
         if (!hostDirPath.endsWith("/")) {
            hostDirPath = hostDirPath + "/";
         }

         hostDirPath = hostDirPath + HashEventEditor.this.year;
         JFileChooser chooser = new JFileChooser();
         chooser.setFileFilter(new FileNameExtensionFilter("Web Image formats", new String[]{"jpg", "jpeg", "gif", "png", "svg"}));
         chooser.setDialogTitle("Select an Icon File to Upload");
         chooser.setCurrentDirectory(localDirFile);
         int sts = chooser.showOpenDialog(HashEventEditor.this.frame);
         if (sts == 0) {
            File file = chooser.getSelectedFile();
            HashEventEditor.this.iconName.setText(file.getName());
            String localDirPath = file.getParentFile().getAbsolutePath();
            String[] files = new String[]{file.getName()};
            new FTPUpload(HashEventEditor.this.host, HashEventEditor.this.username, HashEventEditor.this.password);
         }

      }
   }
}
