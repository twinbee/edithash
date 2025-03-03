package us.winborn.edithash;

public class DaysData implements Comparable<DaysData> {
   private String[] sa;
   private int order = 0;

   public DaysData() {
      this.sa = new String[DaysData.Field.LENGTH];
   }

   public DaysData(String str) throws NumberFormatException, TimeParseException {
      this.sa = new String[DaysData.Field.LENGTH];
      String[] line = str.split("\t");

      for(int i = 0; i < DaysData.Field.LENGTH; ++i) {
         if (i < line.length) {
            this.sa[i] = line[i];
         } else {
            this.sa[i] = "";
         }
      }

      this.removeTimezone();
      this.calculateOrder();
   }

   public void initialize(String str) {
      String[] line = str.split("\t");

      for(int i = 1; i < DaysData.Field.LENGTH; ++i) {
         if (i < line.length) {
            this.sa[i] = line[i];
         } else {
            this.sa[i] = "";
         }
      }

      this.removeTimezone();
   }

   public String toString() {
      StringBuilder sb = new StringBuilder(8192);

      for(int i = 0; i < this.sa.length; ++i) {
         sb.append(this.sa[i]);
         if (i < this.sa.length - 1) {
            sb.append("\t");
         } else {
            sb.append("\n");
         }
      }

      return sb.toString();
   }

   public static String getHeader() {
      String[] sa = new String[DaysData.Field.LENGTH];
      sa[DaysData.Field.DAY.n] = DaysData.Field.DAY.toString();
      sa[DaysData.Field.KENNEL.n] = DaysData.Field.KENNEL.toString();
      sa[DaysData.Field.ICON.n] = DaysData.Field.ICON.toString();
      sa[DaysData.Field.TITLE.n] = DaysData.Field.TITLE.toString();
      sa[DaysData.Field.RUN.n] = DaysData.Field.RUN.toString();
      sa[DaysData.Field.HARES.n] = DaysData.Field.HARES.toString();
      sa[DaysData.Field.TIME.n] = DaysData.Field.TIME.toString();
      sa[DaysData.Field.START.n] = DaysData.Field.START.toString();
      sa[DaysData.Field.MAP.n] = DaysData.Field.MAP.toString();
      sa[DaysData.Field.HASHCASH.n] = DaysData.Field.HASHCASH.toString();
      sa[DaysData.Field.TURDS.n] = DaysData.Field.TURDS.toString();
      sa[DaysData.Field.TWEET.n] = DaysData.Field.TWEET.toString();
      sa[DaysData.Field.TWILIGHT.n] = DaysData.Field.TWILIGHT.toString();
      sa[DaysData.Field.DATE.n] = DaysData.Field.DATE.toString();
      sa[DaysData.Field.DESC.n] = DaysData.Field.DESC.toString();
      sa[DaysData.Field.UPDATE.n] = DaysData.Field.UPDATE.toString();
      StringBuilder sb = new StringBuilder(8192);

      for(int i = 0; i < sa.length; ++i) {
         sb.append(sa[i]);
         if (i < sa.length - 1) {
            sb.append("\t");
         } else {
            sb.append("\n");
         }
      }

      return sb.toString();
   }

   public int getDayOfMonth() throws NumberFormatException {
      return Integer.parseInt(this.sa[DaysData.Field.DAY.n]);
   }

   public void setDayOfMonth(int dayOfMonth) {
      this.sa[DaysData.Field.DAY.n] = Integer.toString(dayOfMonth);
   }

   public String getKennelName() {
      return this.sa[DaysData.Field.KENNEL.n];
   }

   public void setKennelName(String kennelName) {
      this.sa[DaysData.Field.KENNEL.n] = kennelName;
   }

   public String getIconName() {
      return this.sa[DaysData.Field.ICON.n];
   }

   public void setIconName(String typeCode) {
      this.sa[DaysData.Field.ICON.n] = typeCode;
   }

   public String getHashTitle() {
      return this.sa[DaysData.Field.TITLE.n];
   }

   public void setHashTitle(String hashTitle) {
      this.sa[DaysData.Field.TITLE.n] = hashTitle;
   }

   public String getRunNo() {
      return this.sa[DaysData.Field.RUN.n];
   }

   public void setRunNo(String runNo) {
      this.sa[DaysData.Field.RUN.n] = runNo;
   }

   public String getHares() {
      return this.sa[DaysData.Field.HARES.n];
   }

   public void setHares(String hares) {
      this.sa[DaysData.Field.HARES.n] = hares;
   }

   public String getStartTime() {
      return this.sa[DaysData.Field.TIME.n];
   }

   public void setStartTime(String startTime) {
      this.sa[DaysData.Field.TIME.n] = startTime;
   }

   public String getStartAddress() {
      return this.sa[DaysData.Field.START.n];
   }

   public void setStartAddress(String startAddress) {
      this.sa[DaysData.Field.START.n] = startAddress;
   }

   public String getMapLink() {
      return this.sa[DaysData.Field.MAP.n];
   }

   public void setMapLink(String mapLink) {
      this.sa[DaysData.Field.MAP.n] = mapLink;
   }

   public String getHashCash() {
      return this.sa[DaysData.Field.HASHCASH.n];
   }

   public void setHashCash(String hashCash) {
      this.sa[DaysData.Field.HASHCASH.n] = hashCash;
   }

   public String getTurds() {
      return this.sa[DaysData.Field.TURDS.n];
   }

   public void setTurds(String turds) {
      this.sa[DaysData.Field.TURDS.n] = turds;
   }

   public String getTweet() {
      return this.sa[DaysData.Field.TWEET.n];
   }

   public void setTweet(String tweet) {
      this.sa[DaysData.Field.TWEET.n] = tweet;
   }

   public String getTwilight() {
      return this.sa[DaysData.Field.TWILIGHT.n];
   }

   public void setTwilight(String twilight) {
      this.sa[DaysData.Field.TWILIGHT.n] = twilight;
   }

   public String getDate() {
      return this.sa[DaysData.Field.DATE.n];
   }

   public void setDate(String date) {
      this.sa[DaysData.Field.DATE.n] = date;
   }

   public String getDescription() {
      return this.sa[DaysData.Field.DESC.n];
   }

   public void setDescription(String description) {
      this.sa[DaysData.Field.DESC.n] = description;
   }

   public String getUpdated() {
      return this.sa[DaysData.Field.UPDATE.n];
   }

   public void setUpdated(String updated) {
      this.sa[DaysData.Field.UPDATE.n] = updated;
   }

   public int getOrder() {
      return this.order;
   }

   public void setOrder(int order) {
      this.order = order;
   }

   public void removeTimezone() {
      this.sa[DaysData.Field.TIME.n] = this.sa[DaysData.Field.TIME.n].replaceAll("CDT|CST", "").trim();
   }

   public void calculateOrder() throws NumberFormatException, TimeParseException {
      int minutes = StringParser.parseTime(this.sa[DaysData.Field.TIME.n]);
      this.order = Integer.parseInt(this.sa[DaysData.Field.DAY.n]) * 1440 + minutes;
   }

   public int compareTo(DaysData arg1) {
      return this.getOrder() - arg1.getOrder();
   }

   static enum Field {
      DAY(0),
      KENNEL(1),
      ICON(2),
      TITLE(3),
      RUN(4),
      HARES(5),
      TIME(6),
      START(7),
      MAP(8),
      HASHCASH(9),
      TURDS(10),
      TWEET(11),
      TWILIGHT(12),
      DATE(13),
      DESC(14),
      UPDATE(15);

      public static int LENGTH = 16;
      public int n;

      private Field(int n) {
         this.n = n;
      }
   }
}
