package us.winborn.edithash;

import java.awt.Color;
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

public class TextPanel extends Box implements DocumentListener {
   private JTextArea textArea;
   private String original;
   private Document document;
   private Color highlight = new Color(255, 255, 176);
   private Color flag;
   private int characterLimit;

   public String getText() {
      return this.textArea.getText();
   }

   public void setText(String text) {
      this.original = new String(text);
      this.textArea.setText(text);
   }

   public boolean changed() {
      return !this.textArea.getText().equals(this.original);
   }

   public void SetCharacterLimit(int characterLimit) {
      this.characterLimit = characterLimit;
   }

   public TextPanel(String labelText, int rows, int columns) {
      super(1);
      this.flag = Color.PINK;
      this.characterLimit = 32767;
      JLabel label = new JLabel(labelText);
      label.setForeground(new Color(0, 0, 192));
      this.textArea = new JTextArea(rows, columns);
      this.textArea.setBackground(this.highlight);
      this.textArea.setWrapStyleWord(true);
      this.textArea.setLineWrap(true);
      this.textArea.setBorder(BorderFactory.createLoweredBevelBorder());
      this.document = this.textArea.getDocument();
      this.document.addDocumentListener(this);
      JPanel labelPanel = new JPanel(new FlowLayout(0));
      JPanel textAreaPanel = new JPanel(new FlowLayout(0));
      labelPanel.add(label);
      textAreaPanel.add(this.textArea);
      this.add(labelPanel);
      this.add(textAreaPanel);
   }

   public TextPanel(String labelText, int rows, int columns, JComponent widget) {
      super(1);
      this.flag = Color.PINK;
      this.characterLimit = 32767;
      JLabel label = new JLabel(labelText);
      label.setForeground(new Color(0, 0, 192));
      this.textArea = new JTextArea(rows, columns);
      this.textArea.setBackground(this.highlight);
      this.textArea.setWrapStyleWord(true);
      this.textArea.setLineWrap(true);
      this.textArea.setBorder(BorderFactory.createLoweredBevelBorder());
      this.document = this.textArea.getDocument();
      this.document.addDocumentListener(this);
      JPanel labelPanel = new JPanel(new FlowLayout(0));
      JPanel textAreaPanel = new JPanel(new FlowLayout(0));
      labelPanel.add(label);
      textAreaPanel.add(this.textArea);
      textAreaPanel.add(widget);
      this.add(labelPanel);
      this.add(textAreaPanel);
   }

   public void changedUpdate(DocumentEvent e) {
   }

   public void insertUpdate(DocumentEvent e) {
      if (this.textArea.getText().length() == 0) {
         this.textArea.setBackground(this.highlight);
      } else if (this.textArea.getText().length() > this.characterLimit) {
         this.textArea.setBackground(this.flag);
      } else {
         this.textArea.setBackground(Color.WHITE);
      }

   }

   public void removeUpdate(DocumentEvent e) {
      if (this.textArea.getText().length() == 0) {
         this.textArea.setBackground(this.highlight);
      } else {
         this.textArea.setBackground(Color.WHITE);
      }

   }
}
