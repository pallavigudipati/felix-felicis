import syntaxtree.*;
import visitor.*;

public class P2 {
   public static void main(String [] args) {
      try {
         Node root = new MiniJavaParser(System.in).Goal();
         //System.out.println("Program parsed successfully");
         root.accept(new TypeChecker()); // Your assignment part is invoked here.
         System.out.print("Program type checked successfully");
      }
      catch (ParseException e) {
         //System.out.println(e.toString());
      }
   }
} 



