import syntaxtree.*;
import visitor.*;

public class P3 {
   public static void main(String [] args) {
      try {
         Node root = new MiniJavaParser(System.in).Goal();
         //System.out.println("Program parsed successfully");
         root.accept(new MiniIR()); // Your assignment part is invoked here.
         //System.out.print("");
      }
      catch (ParseException e) {
         //System.out.println(e.toString());
      }
   }
} 



