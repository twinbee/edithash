package us.winborn.library;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;

public class ItemLayout implements LayoutManager {
   private int gap;
   private Dimension targetSize;

   public int getGap() {
      return this.gap;
   }

   public void setGap(int gap) {
      this.gap = gap;
   }

   public ItemLayout(int gap) {
      this.gap = gap;
   }

   public void addLayoutComponent(String arg0, Component arg1) {
   }

   public void layoutContainer(Container target) {
      int n = target.getComponentCount();
      synchronized(target.getTreeLock()) {
         Insets insets = target.getInsets();
         int x = insets.left;
         int y = insets.top;
         int maxWidth = 0;
         int maxHeight = 0;

         for(int i = 0; i < n; ++i) {
            Component c = target.getComponent(i);
            if (c.isVisible()) {
               Dimension d = c.getPreferredSize();
               c.setSize(d.width, d.height);
               c.setLocation(x, y);
               y += d.height + this.gap;
               maxHeight = y - insets.top;
               maxWidth = Math.max(maxWidth, d.width);
            }
         }

         this.targetSize = new Dimension(maxWidth + insets.top + insets.bottom, maxHeight + insets.left + insets.right);
         target.setSize(this.targetSize);
         target.setPreferredSize(this.targetSize);
      }
   }

   public Dimension minimumLayoutSize(Container target) {
      return this.preferredLayoutSize(target);
   }

   public Dimension preferredLayoutSize(Container target) {
      int n = target.getComponentCount();
      synchronized(target.getTreeLock()) {
         Insets insets = target.getInsets();
         int y = insets.top;
         int maxWidth = 0;
         int maxHeight = 0;

         for(int i = 0; i < n; ++i) {
            Component c = target.getComponent(i);
            if (c.isVisible()) {
               Dimension d = c.getPreferredSize();
               y += d.height + this.gap;
               maxHeight = y - insets.top;
               maxWidth = Math.max(maxWidth, d.width);
            }
         }

         this.targetSize = new Dimension(maxWidth + insets.top + insets.bottom, maxHeight + insets.left + insets.right);
         return this.targetSize;
      }
   }

   public void removeLayoutComponent(Component arg0) {
   }
}
