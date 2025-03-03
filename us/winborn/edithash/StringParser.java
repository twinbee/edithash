package us.winborn.edithash;

import java.text.ParseException;

public class StringParser {
   private String snippet;
   private int base;
   private int length;

   public String getSnippet() {
      return this.snippet;
   }

   public int getBase() {
      return this.base;
   }

   public int getLength() {
      return this.length;
   }

   public StringParser(String str) {
      this.snippet = str.trim();
      this.base = 0;
      this.length = this.snippet.length();
   }

   public StringParser(StringParser parser, int beginIndex, int endIndex) {
      this.snippet = parser.getSnippet().substring(beginIndex, endIndex);
      this.base = parser.getBase() + beginIndex;
      this.length = parser.getLength();
   }

   public int findString(String key, int fromIndex, String errorMsg) throws ParseException {
      if (fromIndex >= 0 && fromIndex < this.length) {
         int index = this.snippet.indexOf(key, fromIndex);
         if (index == -1) {
            int line = 1;

            for(int i = 0; i <= index; ++i) {
               if (this.snippet.charAt(i) == '\r') {
                  ++line;
               }
            }

            throw new ParseException(String.format("findString: %s \"%s\" not found, line %0d", errorMsg, key, line), this.base + fromIndex);
         } else {
            return index;
         }
      } else {
         throw new ParseException(String.format("findString: %s \"%s\" fromIndex out of bounds", errorMsg, key), this.base);
      }
   }

   public static String html2text(String str) {
      String s = str.replace("<br />", "\n").replace("\f", "\n");
      return s;
   }

   public static String text2html(String str) {
      String result = "";
      int len = str.length();
      int start = 0;

      while(start != len) {
         int ul = str.indexOf("<ul>", start);
         int ol = str.indexOf("<ol>", start);
         int end = len;
         int next = -1;
         if (ol != -1) {
            if (ul != -1 && ul < ol) {
               end = ul;
               next = str.indexOf("</ul>", start);
            } else {
               end = ol;
               next = str.indexOf("</ol>", start);
            }
         } else if (ul != -1) {
            end = ul;
            next = str.indexOf("</ul>", start);
         }

         result = result.concat(str.substring(start, end).replace("\n", "<br />"));
         if (next != -1) {
            result = result.concat(str.substring(end, next + 5).replace("\n", "\f"));
            start = next + 5;
         } else {
            start = end;
         }
      }

      return result.replace("\t", " ");
   }

   public static String html2android(String str) {
      String s = str.replace("<br />", " ");
      s = collapseWhitespace(s);
      return s;
   }

   public static int parseTime(String timeString) throws TimeParseException {
      String s = deTag(timeString);
      s = s.toUpperCase().replace(" ", "");
      int i = 0;

      int j;
      for(j = 0; j < s.length() && Character.isDigit(s.charAt(j)); ++j) {
      }

      int hours;
      try {
         hours = Integer.parseInt(s.substring(i, j));
      } catch (Exception var8) {
         throw new TimeParseException("Error parsing hours", timeString);
      }

      if (s.charAt(j) != ':') {
         throw new TimeParseException("Missing colan", timeString);
      } else {
         ++j;

         int i;
         for(i = j; j < s.length() && Character.isDigit(s.charAt(j)); ++j) {
         }

         if (j - i != 2) {
            throw new TimeParseException("Minutes must be 2 digits", timeString);
         } else {
            int minutes;
            try {
               minutes = Integer.parseInt(s.substring(i, j));
            } catch (Exception var7) {
               throw new TimeParseException("Error parsing minutes", timeString);
            }

            if (j >= s.length()) {
               throw new TimeParseException("Missing AM or PM", timeString);
            } else if (s.charAt(j) != 'A' && s.charAt(j) != 'P') {
               throw new TimeParseException("Missing AM or PM", timeString);
            } else if (hours >= 1 && hours <= 12 && minutes <= 59) {
               if (s.charAt(j) == 'A' && hours == 12) {
                  hours = 0;
               }

               if (s.charAt(j) == 'P' && hours != 12) {
                  hours += 12;
               }

               return hours * 60 + minutes;
            } else {
               throw new TimeParseException("Not a valid time", timeString);
            }
         }
      }
   }

   public static String collapseWhitespace(String str) {
      StringBuilder result = new StringBuilder();
      boolean lastWasWhitespace = true;

      for(int i = 0; i < str.length(); ++i) {
         if (Character.isWhitespace(str.charAt(i))) {
            if (!lastWasWhitespace) {
               result.append(' ');
            }

            lastWasWhitespace = true;
         } else {
            result.append(str.charAt(i));
            lastWasWhitespace = false;
         }
      }

      return result.toString().trim();
   }

   public static String deTag(String str) {
      StringBuilder result = new StringBuilder();
      boolean inTag = false;

      for(int i = 0; i < str.length(); ++i) {
         if (str.charAt(i) == '<') {
            inTag = true;
         } else if (str.charAt(i) == '>') {
            inTag = false;
            continue;
         }

         if (!inTag) {
            result.append(str.charAt(i));
         }
      }

      return result.toString();
   }
}
