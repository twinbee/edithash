package org.h3.edithash;

import java.io.File;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimeZone;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.h3.library.Prefer;
import org.h3.library.Traffic;

public class ReaderGenerator {
   public ReaderGenerator(Calendar calendar, Prefer prefer, EditHash frame, Traffic traffic) throws Exception {
      ResourceBundle strings = ResourceBundle.getBundle("org.h3.edithash.strings");
      String username = prefer.getText("username");
      String password = prefer.getText("password");
      String host = prefer.getText("host");
      String localDir = prefer.getText("localDir");
      String hostDir = prefer.getText("hostDir");
      boolean autodown = prefer.getBoolean("autodown");
      boolean autoup = prefer.getBoolean("autoup");
      boolean remind = prefer.getBoolean("remind");
      String newfileName = String.format(strings.getString("newFileFormat"), calendar.get(1), calendar.get(2) + 1);
      File newFile = new File(localDir, newfileName);
      DaysData d = null;
      if (autodown) {
         new FTPDownload(frame, username, password, host, hostDir, localDir, newfileName, traffic, FTPDownload.TYPE_ASCII);
      }

      int day = calendar.get(5);
      MonthsData m = new MonthsData();
      m.readFile(newFile);
      Enumeration<DaysData> e = m.elements();
      Vector v = new Vector();

      while(e.hasMoreElements()) {
         DaysData dd = (DaysData)e.nextElement();
         if (dd.getDayOfMonth() == day) {
            v.add(dd);
         }
      }

      Enumeration ed = v.elements();

      HashEventEditor hee;
      do {
         boolean generated;
         if (!ed.hasMoreElements()) {
            d = this.generateDaysData(frame, calendar);
            if (d == null) {
               return;
            }

            generated = true;
         } else {
            d = (DaysData)ed.nextElement();
            generated = false;
         }

         hee = new HashEventEditor(frame, calendar, d, prefer, traffic);
         if (hee.getStatus() == 2) {
            break;
         }

         if (generated || hee.getStatus() == 1 || hee.getStatus() == 5 || hee.getStatus() == 3) {
            if (generated) {
               m.add(0, d);
            }

            if (hee.getStatus() == 3) {
               m.remove(d);
            }

            m.writeFile(newFile);
            if (autoup) {
               String[] files = new String[]{newfileName};
               new FTPUpload(frame, prefer.getText("username"), prefer.getText("password"), prefer.getText("host"), prefer.getText("hostDir"), localDir, files, traffic, FTPUpload.TYPE_ASCII);
            } else if (remind) {
               int sts = JOptionPane.showConfirmDialog(frame, "Would you like to upload changes to the host?", "Warning", 0, 2);
               if (sts == 0) {
                  String[] files = new String[]{newfileName};
                  new FTPUpload(prefer.getText("host"), prefer.getText("username"), prefer.getText("password") );
               }
            }
         }
      } while(hee.getStatus() != 3 && hee.getStatus() != 1);

   }

   public DaysData generateDaysData(JFrame frame, Calendar calendar) throws Exception {
      HashChooser chooser = new HashChooser(frame, calendar);
      if (chooser.getOption() == HashChooser.CANCEL) {
         return null;
      } else {
         KennelButton kennel = chooser.getSelection();
         DaysData d = kennel.getDaysData();
         d.setDayOfMonth(calendar.get(5));
         d.setTwilight(this.getDaylightEnd(calendar));
         String date = String.format("%s, %s %d, %d", calendar.getDisplayName(7, 2, Locale.getDefault()), calendar.getDisplayName(2, 2, Locale.getDefault()), calendar.get(5), calendar.get(1));
         d.setDate(date);
         return d;
      }
   }

   public String getDaylightEnd(Calendar calendar) {
      int[][] endOfTwilight = new int[][]{{1759, 1826, 1849, 1912, 1935, 1959, 2008, 1952, 1917, 1836, 1802, 1748}, {1800, 1827, 1850, 1913, 1936, 1959, 2008, 1952, 1916, 1835, 1801, 1748}, {1801, 1827, 1851, 1914, 1937, 2000, 2008, 1951, 1914, 1834, 1800, 1748}, {1801, 1828, 1851, 1914, 1938, 2000, 2007, 1950, 1913, 1832, 1759, 1748}, {1802, 1829, 1852, 1915, 1938, 2001, 2007, 1949, 1912, 1831, 1758, 1748}, {1803, 1830, 1853, 1916, 1939, 2001, 2007, 1948, 1910, 1830, 1758, 1748}, {1804, 1831, 1854, 1917, 1940, 2002, 2007, 1947, 1909, 1829, 1757, 1748}, {1805, 1832, 1855, 1917, 1941, 2002, 2007, 1946, 1908, 1827, 1756, 1748}, {1805, 1833, 1855, 1918, 1942, 2003, 2006, 1945, 1906, 1826, 1756, 1748}, {1806, 1834, 1856, 1919, 1942, 2003, 2006, 1944, 1905, 1825, 1755, 1748}, {1807, 1834, 1857, 1920, 1943, 2004, 2006, 1943, 1903, 1824, 1754, 1749}, {1808, 1835, 1858, 1920, 1944, 2004, 2005, 1942, 1902, 1822, 1754, 1749}, {1809, 1836, 1858, 1921, 1945, 2005, 2005, 1940, 1901, 1821, 1753, 1749}, {1810, 1837, 1859, 1922, 1946, 2005, 2004, 1939, 1859, 1820, 1753, 1750}, {1811, 1838, 1900, 1923, 1946, 2005, 2004, 1938, 1858, 1819, 1752, 1750}, {1811, 1839, 1900, 1923, 1947, 2006, 2004, 1937, 1857, 1818, 1752, 1750}, {1812, 1840, 1901, 1924, 1948, 2006, 2003, 1936, 1855, 1817, 1751, 1751}, {1813, 1840, 1902, 1925, 1949, 2006, 2003, 1935, 1854, 1815, 1751, 1751}, {1814, 1841, 1903, 1926, 1950, 2007, 2002, 1933, 1853, 1814, 1750, 1751}, {1815, 1842, 1903, 1927, 1950, 2007, 2001, 1932, 1851, 1813, 1750, 1752}, {1816, 1843, 1904, 1927, 1951, 2007, 2001, 1931, 1850, 1812, 1750, 1752}, {1817, 1844, 1905, 1928, 1952, 2007, 2000, 1930, 1848, 1811, 1749, 1753}, {1818, 1844, 1906, 1929, 1952, 2007, 1959, 1929, 1847, 1810, 1749, 1753}, {1819, 1845, 1906, 1930, 1953, 2008, 1959, 1927, 1846, 1809, 1749, 1754}, {1819, 1846, 1907, 1930, 1954, 2008, 1958, 1926, 1844, 1808, 1748, 1755}, {1820, 1847, 1908, 1931, 1955, 2008, 1957, 1925, 1843, 1807, 1748, 1755}, {1821, 1848, 1908, 1932, 1955, 2008, 1957, 1923, 1842, 1806, 1748, 1756}, {1822, 1848, 1909, 1933, 1956, 2008, 1956, 1922, 1840, 1805, 1748, 1756}, {1823, 1849, 1910, 1934, 1957, 2008, 1955, 1921, 1839, 1804, 1748, 1757}, {1824, 9999, 1911, 1934, 1957, 2008, 1954, 1920, 1838, 1803, 1748, 1758}, {1825, 9999, 1911, 9999, 1958, 9999, 1953, 1918, 9999, 1802, 9999, 1758}};
      int eot = endOfTwilight[calendar.get(5) - 1][calendar.get(2)];
      int minute = eot % 100;
      int hour = eot / 100;
      String amPM = hour < 12 ? "AM" : "PM";
      hour %= 12;
      if (hour == 0) {
         hour = 12;
      }

      TimeZone tz = calendar.getTimeZone();
      boolean isDST = tz.inDaylightTime(calendar.getTime());
      if (isDST) {
         ++hour;
      }

      return String.format("%d:%02d %s", hour, minute, amPM);
   }
}
