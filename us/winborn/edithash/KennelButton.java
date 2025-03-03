package us.winborn.edithash;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.TranscodingHints;
import org.apache.batik.transcoder.image.ImageTranscoder;

public class KennelButton extends JButton {
   private DaysData daysData;

   public DaysData getDaysData() {
      return this.daysData;
   }

   public void setDaysData(DaysData daysData) {
      this.daysData = daysData;
   }

   public KennelButton(Action arg0, DaysData daysData) throws Exception {
      super(arg0);
      this.daysData = daysData;
      String path = "images/" + daysData.getIconName();
      URL url = this.getClass().getResource(path);
      if (url == null) {
         throw new Exception("Internal resource not found: " + path);
      } else {
         if (path.trim().endsWith(".svg")) {
            KennelButton.SVGTranscoder transcoder = new KennelButton.SVGTranscoder();
            TranscodingHints hints = new TranscodingHints();
            hints.put(ImageTranscoder.KEY_WIDTH, new Float(130.0F));
            hints.put(ImageTranscoder.KEY_HEIGHT, new Float(50.0F));
            hints.put(ImageTranscoder.KEY_DOM_IMPLEMENTATION, SVGDOMImplementation.getDOMImplementation());
            hints.put(ImageTranscoder.KEY_DOCUMENT_ELEMENT_NAMESPACE_URI, "http://www.w3.org/2000/svg");
            hints.put(ImageTranscoder.KEY_DOCUMENT_ELEMENT, "svg");
            hints.put(ImageTranscoder.KEY_XML_PARSER_VALIDATING, false);
            transcoder.setTranscodingHints(hints);
            transcoder.transcode(new TranscoderInput(url.toURI().toString()), (TranscoderOutput)null);
            Image img = transcoder.getImage();
            this.setIcon(new ImageIcon(img));
         } else {
            this.setIcon(new ImageIcon(url));
         }

      }
   }

   public class SVGTranscoder extends ImageTranscoder {
      private BufferedImage image = null;

      public BufferedImage createImage(int w, int h) {
         this.image = new BufferedImage(w, h, 2);
         return this.image;
      }

      public BufferedImage getImage() {
         return this.image;
      }

      public void writeImage(BufferedImage arg0, TranscoderOutput arg1) throws TranscoderException {
      }
   }
}
