package org.dbms.database.ui;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class DoubleInput {
   private JTextField xField;
   private JTextField yField;

   public DoubleInput() {
      xField = new JTextField(5);
      yField = new JTextField(5);
   }

   public InputResult showInputDialog() {
      JPanel myPanel = new JPanel();
      myPanel.add(new JLabel("min:"));
      myPanel.add(xField);
      myPanel.add(Box.createHorizontalStrut(15)); // a spacer
      myPanel.add(new JLabel("max:"));
      myPanel.add(yField);

      while (true) {
         int result = JOptionPane.showConfirmDialog(null, myPanel,
                 "Please Enter X and Y Values", JOptionPane.OK_CANCEL_OPTION);

         if (result == JOptionPane.OK_OPTION) {
            String xValue = xField.getText();
            String yValue = yField.getText();

            return new InputResult(xValue, yValue);

         } else {
            // User canceled or closed the dialog
            return null;
         }
      }
   }

   public static class InputResult {
      private String min;
      private String max;

      public InputResult(String min, String max) {
         this.min = min;
         this.max = max;
      }

      public String getMin() {
         return min;
      }

      public String getMax() {
         return max;
      }
   }

   public static void main(String[] args) {
      DoubleInput dialog = new DoubleInput();
      InputResult result = dialog.showInputDialog();
      if (result != null) {
         System.out.println("x value: " + result.getMin());
         System.out.println("y value: " + result.getMax());
      }
   }
}