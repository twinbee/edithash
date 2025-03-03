package us.winborn.library;

import java.awt.Color;
import java.awt.Font;
import java.util.concurrent.LinkedBlockingQueue;
import javax.swing.JEditorPane;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

public class Traffic extends JScrollPane implements Runnable, Subscriber {
   public static Color DEFAULT_FONT_COLOR;
   public static Font DEFAULT_FONT;
   private Document document;
   private JScrollBar vscrollbar;
   private LinkedBlockingQueue<String> queue;
   private Thread thread;
   private Font font;
   private boolean sticky;
   private Color foregroundColor;
   private JEditorPane editorPane;

   static {
      DEFAULT_FONT_COLOR = Color.BLACK;
      DEFAULT_FONT = new Font("Lucida Console", 0, 10);
   }

   public Traffic(Font font, Color foregroundColor) {
      this.font = DEFAULT_FONT;
      this.sticky = true;
      this.foregroundColor = DEFAULT_FONT_COLOR;
      this.setVerticalScrollBarPolicy(20);
      this.setHorizontalScrollBarPolicy(30);
      this.font = font;
      this.foregroundColor = foregroundColor;
      this.editorPane = new JEditorPane("text/plain", (String)null);
      this.editorPane.setFont(font);
      this.editorPane.setForeground(this.foregroundColor);
      this.setViewportView(this.editorPane);
      this.queue = new LinkedBlockingQueue();
      this.document = this.editorPane.getDocument();
      this.vscrollbar = this.getVerticalScrollBar();
      this.thread = new Thread(this);
      this.thread.setDaemon(true);
      this.thread.start();
   }

   public Document getDocument() {
      return this.document;
   }

   public void setDocument(Document document) {
      this.document = document;
   }

   public Font getFont() {
      return this.font;
   }

   public void setFont(Font font) {
      this.font = font;
   }

   public boolean isSticky() {
      return this.sticky;
   }

   public void setSticky(boolean sticky) {
      this.sticky = sticky;
   }

   public JEditorPane getEditorPane() {
      return this.editorPane;
   }

   public void setEditorPane(JEditorPane editorPane) {
      this.editorPane = editorPane;
   }

   public Traffic() {
      this(DEFAULT_FONT, DEFAULT_FONT_COLOR);
   }

   public void publish(String edition) {
      this.queue.add(edition);
   }

   public void run() {
      String s = null;

      while(true) {
         try {
            s = (String)this.queue.take() + "\n";
         } catch (InterruptedException var4) {
         }

         try {
            this.document.insertString(this.document.getEndPosition().getOffset(), s, (AttributeSet)null);
         } catch (BadLocationException var3) {
            var3.printStackTrace();
            System.exit(1);
         }

         if (this.sticky) {
            SwingUtilities.invokeLater(new Runnable() {
               public void run() {
                  Traffic.this.getVerticalScrollBar().setValue(Traffic.this.vscrollbar.getMaximum());
               }
            });
         }
      }
   }
}
