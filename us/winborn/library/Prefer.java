package us.winborn.library;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.prefs.Preferences;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class Prefer extends JDialog {
   public static final int SAVE = 1;
   public static final int CANCEL = 2;
   private Preferences preferences;
   private JFrame frame;
   private int status;
   private String nodeName;
   private ArrayList<Box> boxes;

   public String getNodeName() {
      return this.nodeName;
   }

   public void setNodeName(String nodeName) {
      this.nodeName = nodeName;
   }

   public Preferences getPreferences() {
      return this.preferences;
   }

   public int getStatus() {
      return this.status;
   }

   public void addString(String key, String defaultValue, String labelText) {
      this.boxes.add(new Prefer.TextBox(key, defaultValue, labelText));
   }

   public void addBoolean(String key, boolean defaultValue, String labelText) {
      this.boxes.add(new Prefer.BooleanBox(key, defaultValue, labelText));
   }

   public void addInteger(String key, int defaultValue, String labelText) {
      this.boxes.add(new Prefer.IntegerBox(key, defaultValue, labelText));
   }

   public void addChoice(String key, String defaultValue, String labelText, String[] choices) {
      this.boxes.add(new Prefer.ChoiceBox(key, defaultValue, labelText, choices));
   }

   public String getText(String key) throws Exception {
      for(int i = 0; i < this.boxes.size(); ++i) {
         Box b = (Box)this.boxes.get(i);
         if (b instanceof Prefer.TextBox) {
            Prefer.TextBox box = (Prefer.TextBox)b;
            if (key.equals(box.getKey())) {
               return this.preferences.get(key, box.getDefaultValue());
            }
         }
      }

      throw new Exception("Text preference key \"" + key + "\" not found.");
   }

   public boolean getBoolean(String key) throws Exception {
      for(int i = 0; i < this.boxes.size(); ++i) {
         Box b = (Box)this.boxes.get(i);
         if (b instanceof Prefer.BooleanBox) {
            Prefer.BooleanBox box = (Prefer.BooleanBox)b;
            if (key.equals(box.getKey())) {
               return this.preferences.getBoolean(key, box.isDefaultTrue());
            }
         }
      }

      throw new Exception("Boolean preference key \"" + key + "\" not found.");
   }

   public int getInt(String key) throws Exception {
      for(int i = 0; i < this.boxes.size(); ++i) {
         Box b = (Box)this.boxes.get(i);
         if (b instanceof Prefer.IntegerBox) {
            Prefer.IntegerBox box = (Prefer.IntegerBox)b;
            if (key.equals(box.getKey())) {
               return this.preferences.getInt(key, box.getDefaultValue());
            }
         }
      }

      throw new Exception("Integer preference key \"" + key + "\" not found.");
   }

   public String getChoice(String key) throws Exception {
      for(int i = 0; i < this.boxes.size(); ++i) {
         Box b = (Box)this.boxes.get(i);
         if (b instanceof Prefer.ChoiceBox) {
            Prefer.ChoiceBox box = (Prefer.ChoiceBox)b;
            if (key.equals(box.getKey())) {
               return this.preferences.get(key, box.getDefaultValue());
            }
         }
      }

      throw new Exception("Integer preference key \"" + key + "\" not found.");
   }

   public void setText(String key, String value) throws Exception {
      for(int i = 0; i < this.boxes.size(); ++i) {
         Box b = (Box)this.boxes.get(i);
         if (b instanceof Prefer.TextBox) {
            Prefer.TextBox box = (Prefer.TextBox)b;
            if (key.equals(box.getKey())) {
               this.preferences.put(key, value);
               return;
            }
         }
      }

      throw new Exception("Text preference key \"" + key + "\" not found.");
   }

   public void setBoolean(String key, boolean value) throws Exception {
      for(int i = 0; i < this.boxes.size(); ++i) {
         Box b = (Box)this.boxes.get(i);
         if (b instanceof Prefer.BooleanBox) {
            Prefer.BooleanBox box = (Prefer.BooleanBox)b;
            if (key.equals(box.getKey())) {
               this.preferences.putBoolean(key, value);
               return;
            }
         }
      }

      throw new Exception("Boolean preference key \"" + key + "\" not found.");
   }

   public void setInt(String key, int value) throws Exception {
      for(int i = 0; i < this.boxes.size(); ++i) {
         Box b = (Box)this.boxes.get(i);
         if (b instanceof Prefer.IntegerBox) {
            Prefer.IntegerBox box = (Prefer.IntegerBox)b;
            if (key.equals(box.getKey())) {
               this.preferences.putInt(key, value);
               return;
            }
         }
      }

      throw new Exception("Integer preference key \"" + key + "\" not found.");
   }

   public void setChoice(String key, String value) throws Exception {
      for(int i = 0; i < this.boxes.size(); ++i) {
         Box b = (Box)this.boxes.get(i);
         if (b instanceof Prefer.ChoiceBox) {
            Prefer.ChoiceBox box = (Prefer.ChoiceBox)b;
            if (key.equals(box.getKey())) {
               this.preferences.put(key, value);
               return;
            }
         }
      }

      throw new Exception("Integer preference key \"" + key + "\" not found.");
   }

   public Prefer(JFrame frame, String nodeName) {
      super(frame, true);
      this.frame = frame;
      this.preferences = Preferences.userNodeForPackage(Prefer.class).node(nodeName);
      this.boxes = new ArrayList();
   }

   public void showPreferences(String title) {
      this.setTitle(title);
      JPanel mainPanel = new JPanel(new BorderLayout());
      this.setContentPane(mainPanel);
      this.setLocation(this.frame.getLocation());
      JPanel editPanel = new JPanel(new ItemLayout(0));

      for(int i = 0; i < this.boxes.size(); ++i) {
         Box b = (Box)this.boxes.get(i);
         String key;
         String defaultValue;
         if (b instanceof Prefer.TextBox) {
            Prefer.TextBox box = (Prefer.TextBox)b;
            key = box.getDefaultValue();
            defaultValue = box.getKey();
            box.setText(this.preferences.get(defaultValue, key));
            editPanel.add(box);
         } else if (b instanceof Prefer.BooleanBox) {
            Prefer.BooleanBox box = (Prefer.BooleanBox)b;
            key = box.getKey();
            boolean isDefaultTrue = box.isDefaultTrue();
            box.setSelected(this.preferences.getBoolean(key, isDefaultTrue));
            editPanel.add(box);
         } else if (b instanceof Prefer.IntegerBox) {
            Prefer.IntegerBox box = (Prefer.IntegerBox)b;
            key = box.getKey();
            int defaultValue = box.getDefaultValue();
            box.setInt(this.preferences.getInt(key, defaultValue));
            editPanel.add(box);
         } else if (b instanceof Prefer.ChoiceBox) {
            Prefer.ChoiceBox box = (Prefer.ChoiceBox)b;
            key = box.getKey();
            defaultValue = box.getDefaultValue();
            box.setText(this.preferences.get(key, defaultValue));
            editPanel.add(box);
         }
      }

      mainPanel.add(editPanel, "Center");
      this.setDefaultCloseOperation(0);
      this.addWindowListener(new Prefer.Closelistener());
      JPanel buttonPanel = new JPanel(new FlowLayout(1, 100, 10));
      JButton submitButton = new JButton(new Prefer.SubmitActionClass());
      JButton cancelButton = new JButton(new Prefer.CancelActionClass());
      buttonPanel.add(cancelButton);
      buttonPanel.add(submitButton);
      mainPanel.add(buttonPanel, "South");
      mainPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.WHITE, 6), BorderFactory.createLineBorder(Color.DARK_GRAY, 2)));
      this.pack();
      Dimension d = this.getPreferredSize();
      this.setSize(d);
      Point p = this.frame.getLocation();
      p.translate(50, 50);
      this.setLocation(p);
      this.setVisible(true);
   }

   public boolean saveData() {
      for(int i = 0; i < this.boxes.size(); ++i) {
         Box b = (Box)this.boxes.get(i);
         if (b instanceof Prefer.TextBox) {
            Prefer.TextBox box = (Prefer.TextBox)b;
            this.preferences.put(box.getKey(), box.getText());
         } else if (b instanceof Prefer.BooleanBox) {
            Prefer.BooleanBox box = (Prefer.BooleanBox)b;
            this.preferences.putBoolean(box.getKey(), box.isSelected());
         } else if (b instanceof Prefer.IntegerBox) {
            Prefer.IntegerBox box = (Prefer.IntegerBox)b;

            int x;
            try {
               x = box.getInt();
            } catch (NumberFormatException var6) {
               JOptionPane.showMessageDialog(this.frame, "Number Format Error:\n \"" + box.getLabel() + "\" must be an integer");
               return false;
            }

            this.preferences.putInt(box.getKey(), x);
         } else if (b instanceof Prefer.ChoiceBox) {
            Prefer.ChoiceBox box = (Prefer.ChoiceBox)b;
            this.preferences.put(box.getKey(), box.getText());
         }
      }

      return true;
   }

   public class BooleanBox extends Box {
      private JCheckBox checkBox;
      private String key;
      private boolean defaultValue;
      private boolean original;
      private String labelText;

      public boolean isSelected() {
         return this.checkBox.isSelected();
      }

      public String getKey() {
         return this.key;
      }

      public String getLabel() {
         return this.labelText;
      }

      public void setSelected(boolean b) {
         this.checkBox.setSelected(b);
         this.original = b;
      }

      public boolean isDefaultTrue() {
         return this.defaultValue;
      }

      public boolean changed() {
         return this.checkBox.isSelected() ^ this.original;
      }

      public BooleanBox(String key, boolean defaultValue, String labelText) {
         super(1);
         this.key = key;
         this.defaultValue = defaultValue;
         this.labelText = labelText;
         JLabel label = new JLabel(labelText);
         label.setForeground(new Color(0, 0, 192));
         this.checkBox = new JCheckBox();
         JPanel labelPanel = new JPanel(new FlowLayout(0));
         JPanel checkboxPanel = new JPanel(new FlowLayout(0));
         labelPanel.add(label);
         checkboxPanel.add(this.checkBox);
         this.add(labelPanel);
         this.add(checkboxPanel);
      }
   }

   public class CancelActionClass extends AbstractAction {
      public CancelActionClass() {
         super("Cancel");
      }

      public void actionPerformed(ActionEvent e) {
         Prefer.this.status = 2;
         Prefer.this.setVisible(false);
         Prefer.this.dispose();
      }
   }

   public class ChoiceBox extends Box {
      private JComboBox<String> combo;
      private String key;
      private String defaultValue;
      private String original;
      private String labelText;

      public void setText(String t) {
         int n = this.combo.getItemCount();
         int match = 0;

         for(int i = 0; i < n; ++i) {
            String s = (String)this.combo.getItemAt(i);
            if (s.equals(t)) {
               match = i;
            }
         }

         this.combo.setSelectedIndex(match);
         this.original = t;
      }

      public String getText() {
         return (String)this.combo.getSelectedItem();
      }

      public String getKey() {
         return this.key;
      }

      public String getLabel() {
         return this.labelText;
      }

      public String getDefaultValue() {
         return this.defaultValue;
      }

      public boolean changed() {
         return !this.combo.getSelectedItem().equals(this.original);
      }

      public ChoiceBox(String key, String defaultValue, String labelText, String[] choices) {
         super(1);
         this.key = key;
         this.defaultValue = defaultValue;
         this.labelText = labelText;
         JLabel label = new JLabel(labelText);
         label.setForeground(new Color(0, 0, 192));
         this.combo = new JComboBox(choices);
         JPanel labelPanel = new JPanel(new FlowLayout(0));
         JPanel comboBoxPanel = new JPanel(new FlowLayout(0));
         labelPanel.add(label);
         comboBoxPanel.add(this.combo);
         this.add(labelPanel);
         this.add(comboBoxPanel);
      }
   }

   public class Closelistener extends WindowAdapter {
      public void windowClosing(WindowEvent e) {
         boolean changed = false;

         for(int i = 0; i < Prefer.this.boxes.size(); ++i) {
            Box b = (Box)Prefer.this.boxes.get(i);
            if (b instanceof Prefer.TextBox) {
               Prefer.TextBox box = (Prefer.TextBox)b;
               if (box.changed()) {
                  changed = true;
               }
            } else if (b instanceof Prefer.BooleanBox) {
               Prefer.BooleanBox boxx = (Prefer.BooleanBox)b;
               if (boxx.changed()) {
                  changed = true;
               }
            } else if (b instanceof Prefer.IntegerBox) {
               Prefer.IntegerBox boxxx = (Prefer.IntegerBox)b;
               if (boxxx.changed()) {
                  changed = true;
               }
            } else if (b instanceof Prefer.ChoiceBox) {
               Prefer.ChoiceBox boxxxx = (Prefer.ChoiceBox)b;
               if (boxxxx.changed()) {
                  changed = true;
               }
            }
         }

         if (changed) {
            int sts = JOptionPane.showConfirmDialog(Prefer.this.frame, "You've made changes! Do you want to save them?", "Warning", 0, 2);
            if (sts == 0) {
               if (Prefer.this.saveData()) {
                  Prefer.this.status = 1;
                  Prefer.this.setVisible(false);
                  Prefer.this.dispose();
               }
            } else if (sts == 1) {
               Prefer.this.status = 2;
               Prefer.this.setVisible(false);
               Prefer.this.dispose();
            }
         } else {
            Prefer.this.status = 2;
            Prefer.this.setVisible(false);
            Prefer.this.dispose();
         }

      }
   }

   public class IntegerBox extends Box {
      private JTextArea textArea;
      private String key;
      private int defaultValue;
      private String original;
      private String labelText;

      public int getInt() throws NumberFormatException {
         return Integer.parseInt(this.textArea.getText());
      }

      public void setInt(int i) {
         String t = String.valueOf(i);
         this.textArea.setText(t);
         this.original = t;
      }

      public String getKey() {
         return this.key;
      }

      public String getLabel() {
         return this.labelText;
      }

      public int getDefaultValue() {
         return this.defaultValue;
      }

      public boolean changed() {
         return !this.textArea.getText().equals(this.original);
      }

      public IntegerBox(String key, int defaultValue, String labelText) {
         super(1);
         this.key = key;
         this.defaultValue = defaultValue;
         this.labelText = labelText;
         JLabel label = new JLabel(labelText);
         label.setForeground(new Color(0, 0, 192));
         this.textArea = new JTextArea(1, 40);
         this.textArea.setBorder(BorderFactory.createLoweredBevelBorder());
         JPanel labelPanel = new JPanel(new FlowLayout(0));
         JPanel textAreaPanel = new JPanel(new FlowLayout(0));
         labelPanel.add(label);
         textAreaPanel.add(this.textArea);
         this.add(labelPanel);
         this.add(textAreaPanel);
      }
   }

   public class SubmitActionClass extends AbstractAction {
      public SubmitActionClass() {
         super("Save");
      }

      public void actionPerformed(ActionEvent e) {
         if (Prefer.this.saveData()) {
            Prefer.this.status = 1;
            Prefer.this.setVisible(false);
            Prefer.this.dispose();
         }

      }
   }

   public class TextBox extends Box {
      private JTextArea textArea;
      private String key;
      private String defaultValue;
      private String original;
      private String labelText;

      public String getText() {
         return this.textArea.getText();
      }

      public void setText(String t) {
         this.textArea.setText(t);
         this.original = t;
      }

      public String getKey() {
         return this.key;
      }

      public String getLabel() {
         return this.labelText;
      }

      public String getDefaultValue() {
         return this.defaultValue;
      }

      public boolean changed() {
         return !this.textArea.getText().equals(this.original);
      }

      public TextBox(String key, String defaultValue, String labelText) {
         super(1);
         this.key = key;
         this.defaultValue = defaultValue;
         this.labelText = labelText;
         JLabel label = new JLabel(labelText);
         label.setForeground(new Color(0, 0, 192));
         this.textArea = new JTextArea(1, 40);
         this.textArea.setBorder(BorderFactory.createLoweredBevelBorder());
         JPanel labelPanel = new JPanel(new FlowLayout(0));
         JPanel textAreaPanel = new JPanel(new FlowLayout(0));
         labelPanel.add(label);
         textAreaPanel.add(this.textArea);
         this.add(labelPanel);
         this.add(textAreaPanel);
      }
   }
}
