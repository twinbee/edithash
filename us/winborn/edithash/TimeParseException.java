package us.winborn.edithash;

public class TimeParseException extends Exception {
   public TimeParseException(String errorText, String timeText) {
      super(errorText + ": \"" + timeText + "\"");
   }
}
