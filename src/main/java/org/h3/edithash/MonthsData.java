package org.h3.edithash;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Vector;

public class MonthsData extends Vector<DaysData> {
   private static int MAXFILESIZE = 32768;

   public void readFile(File file) throws Exception {
      BufferedReader in = null;

      try {
         in = new BufferedReader(new InputStreamReader(new FileInputStream(file), Charset.forName("UTF-8")));
      } catch (FileNotFoundException var6) {
      }

      if (in != null) {
         int lineNo = 1;

         while(true) {
            while(true) {
               while(true) {
                  try {
                     String s = in.readLine();
                     if (s == null) {
                        in.close();
                        return;
                     }

                     try {
                        this.add(new DaysData(s));
                     } catch (NumberFormatException var7) {
                        continue;
                     } catch (TimeParseException var8) {
                        continue;
                     }

                     ++lineNo;
                  } catch (Exception var9) {
                     String s = String.format("error = %s\nFile name = %s\nLine Number = %d", var9.toString(), file.getName(), lineNo);
                     throw new Exception(s);
                  }
               }
            }
         }
      }
   }

   public void writeFile(File file) throws Exception {
      Collections.sort(this);
      StringBuffer sb = new StringBuffer(MAXFILESIZE);
      sb.append(DaysData.getHeader());
      Enumeration e = this.elements();

      while(e.hasMoreElements()) {
         sb.append(((DaysData)e.nextElement()).toString());
      }

      OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(file), Charset.forName("UTF-8"));
      out.write(sb.toString(), 0, sb.length());
      out.close();
   }
}
