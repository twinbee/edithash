package org.h3.edithash;

import java.awt.image.BufferedImage;
import java.net.URL;

import javax.swing.Action;
import javax.swing.JButton;

public class KennelButton extends JButton {
   private static final long serialVersionUID = 5141273210661987683L;
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
      } 
      
   }
}
