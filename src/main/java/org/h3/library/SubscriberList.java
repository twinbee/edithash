package org.h3.library;

public class SubscriberList {
   protected volatile Object[] list = new Object[0];

   public synchronized <T extends Subscriber> void add(T l) {
      for(int i = 0; i < this.list.length; ++i) {
         if (this.list[i].equals(l)) {
            return;
         }
      }

      Object[] newList = new Object[this.list.length + 1];

      for(int i = 0; i < this.list.length; ++i) {
         newList[i] = this.list[i];
      }

      newList[this.list.length] = l;
      this.list = newList;
   }

   public synchronized <T extends Subscriber> void remove(T l) {
      int index = -1;

      for(int i = 0; i < this.list.length; ++i) {
         if (this.list[i].equals(l)) {
            index = i;
         }
      }

      if (index != -1) {
         Object[] newList = new Object[this.list.length - 1];
         int j = 0;

         for(int i = 0; i < this.list.length; ++i) {
            if (i != index) {
               newList[j++] = this.list[i];
            }
         }

         this.list = newList;
      }
   }

   public void publish(String edition) {
      int i = 0;

      while(i < this.list.length) {
         Subscriber s = (Subscriber)this.list[i++];
         s.publish(edition);
      }

   }
}
