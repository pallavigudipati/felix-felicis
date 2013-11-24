import syntaxtree.*;
import visitor.*;

public class P5 {
   public static void main(String [] args) {
      try {
         Node root = new microIRParser(System.in).Goal();
         //System.out.println("Program parsed successfully");
         root.accept(new MiniRA()); // Your assignment part is invoked here.
         //System.out.print("");
      }
      catch (ParseException e) {
         System.out.println(e.toString());
      }
   }
} 



