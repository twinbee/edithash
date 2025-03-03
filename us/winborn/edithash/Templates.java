package us.winborn.edithash;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.TreeMap;

public class Templates extends TreeMap<String, DaysData> {
   File file;

   public Templates(String fileName) throws Exception {
      BufferedReader in;
      try {
         ClassLoader loader = Thread.currentThread().getContextClassLoader();
         InputStream inputStream = loader.getResourceAsStream(fileName);
         in = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
      } catch (Exception var6) {
         throw new Exception("Resource not found: " + fileName);
      }

      while(true) {
         String s = in.readLine();
         if (s == null) {
            in.close();
            return;
         }

         String[] sa = s.split("\t");
         if (!sa[0].equals("DAY")) {
            DaysData d = new DaysData();
            d.initialize(s);
            this.put(sa[0], d);
         }
      }
   }
}
