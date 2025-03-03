package us.winborn.library;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ResourceBundle;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class AboutBox extends JFrame implements ActionListener {
   protected JLabel titleLabel;
   protected JLabel[] aboutLabel;
   protected static int labelCount = 8;
   protected static int aboutWidth = 280;
   protected static int aboutHeight = 230;
   protected static int aboutTop = 200;
   protected static int aboutLeft = 350;
   protected Font titleFont;
   protected Font bodyFont;
   protected ResourceBundle resbundle;

   public AboutBox(String appName, String appVersion, String created) {
      super("");
      this.setResizable(false);
      AboutBox.SymWindow aSymWindow = new AboutBox.SymWindow();
      this.addWindowListener(aSymWindow);
      this.titleFont = new Font("Lucida Grande", 1, 14);
      if (this.titleFont == null) {
         this.titleFont = new Font("SansSerif", 1, 14);
      }

      this.bodyFont = new Font("Lucida Grande", 0, 10);
      if (this.bodyFont == null) {
         this.bodyFont = new Font("SansSerif", 0, 10);
      }

      this.getContentPane().setLayout(new BorderLayout(15, 15));
      this.aboutLabel = new JLabel[labelCount];
      this.aboutLabel[0] = new JLabel("");
      this.aboutLabel[1] = new JLabel(appName);
      this.aboutLabel[1].setFont(this.titleFont);
      this.aboutLabel[2] = new JLabel(appVersion);
      this.aboutLabel[2].setFont(this.bodyFont);
      this.aboutLabel[3] = new JLabel("");
      this.aboutLabel[4] = new JLabel("");
      this.aboutLabel[5] = new JLabel("JDK " + System.getProperty("java.version"));
      this.aboutLabel[5].setFont(this.bodyFont);
      this.aboutLabel[6] = new JLabel(created);
      this.aboutLabel[6].setFont(this.bodyFont);
      this.aboutLabel[7] = new JLabel("");
      Panel textPanel2 = new Panel(new GridLayout(labelCount, 1));

      for(int i = 0; i < labelCount; ++i) {
         this.aboutLabel[i].setHorizontalAlignment(0);
         textPanel2.add(this.aboutLabel[i]);
      }

      this.getContentPane().add(textPanel2, "Center");
      this.pack();
      this.setLocation(aboutLeft, aboutTop);
      this.setSize(aboutWidth, aboutHeight);
      this.setResizable(false);
      this.setVisible(true);
   }

   public void actionPerformed(ActionEvent newEvent) {
      this.setVisible(false);
   }

   class SymWindow extends WindowAdapter {
      public void windowClosing(WindowEvent event) {
         AboutBox.this.setVisible(false);
      }
   }
}
