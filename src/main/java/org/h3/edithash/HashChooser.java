package org.h3.edithash;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Map.Entry;
import javax.swing.AbstractAction;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class HashChooser extends JDialog {
   public static int CANCEL = 0;
   public static int CHOOSE = 1;
   private ResourceBundle strings;
   private int option;
   private int chooserX;
   private int chooserY;
   private int chooserW;
   private int chooserH;
   KennelButton selection;
   private JPanel panel;

   public int getOption() {
      return this.option;
   }

   public KennelButton getSelection() {
      return this.selection;
   }

   public HashChooser(JFrame frame, Calendar calendar) throws Exception {
      super(frame, true);
      this.setDefaultCloseOperation(0);
      this.addWindowListener(new HashChooser.Closelistener());
      this.strings = ResourceBundle.getBundle("org.h3.edithash.strings");
      this.chooserX = Integer.parseInt(this.strings.getString("chooserX"));
      this.chooserY = Integer.parseInt(this.strings.getString("chooserY"));
      this.chooserW = Integer.parseInt(this.strings.getString("chooserW"));
      this.chooserH = Integer.parseInt(this.strings.getString("chooserH"));
      String title = String.format(this.strings.getString("chooserTitle"), calendar.getDisplayName(7, 1, Locale.getDefault()), calendar.getDisplayName(2, 1, Locale.getDefault()), calendar.get(5), calendar.get(1));
      this.setTitle(title);
      this.panel = new JPanel(new FlowLayout(1));
      this.panel.setBackground(Color.darkGray);
      this.setContentPane(this.panel);
      HashChooser.PressedActionClass action = new HashChooser.PressedActionClass();
      Templates templates = new Templates(this.strings.getString("templatesFile"));
      Iterator var7 = templates.entrySet().iterator();

      while(var7.hasNext()) {
         Entry<String, DaysData> entry = (Entry)var7.next();
         DaysData daysData = (DaysData)entry.getValue();
         KennelButton kb = new KennelButton(action, daysData);
         this.panel.add(kb);
      }

      this.panel.setPreferredSize(new Dimension(this.chooserW, this.chooserH));
      this.setLocation(this.chooserX, this.chooserY);
      this.pack();
      this.setVisible(true);
   }

   public class Closelistener extends WindowAdapter {
      public void windowClosing(WindowEvent e) {
         HashChooser.this.setVisible(false);
         HashChooser.this.dispose();
         HashChooser.this.option = HashChooser.CANCEL;
      }
   }

   public class PressedActionClass extends AbstractAction {
      public void actionPerformed(ActionEvent e) {
         HashChooser.this.setVisible(false);
         HashChooser.this.dispose();
         HashChooser.this.option = HashChooser.CHOOSE;
         HashChooser.this.selection = (KennelButton)e.getSource();
      }
   }
}
