//
// Generated by JTB 1.3.2
//

package visitor;
import syntaxtree.*;

import java.util.*;

/**
 * Provides default methods which visit each node in the tree in depth-first
 * order.  Your visitors may extend this class.
 */
public class MIPS<R> implements GJNoArguVisitor<R> {
   //
   // Auto class visitors--probably don't need to be overridden.
   //
   public R visit(NodeList n) {
      R _ret=null;
      int _count=0;
      for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
         e.nextElement().accept(this);
         _count++;
      }
      return _ret;
   }

   public R visit(NodeListOptional n) {
      if ( n.present() ) {
         R _ret=null;
         int _count=0;
         for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
            e.nextElement().accept(this);
            _count++;
         }
         return _ret;
      }
      else
         return null;
   }

   public R visit(NodeOptional n) {
      if ( n.present() )
         return n.node.accept(this);
      else
         return null;
   }

   public R visit(NodeSequence n) {
      R _ret=null;
      int _count=0;
      for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
         e.nextElement().accept(this);
         _count++;
      }
      return _ret;
   }

   public R visit(NodeToken n) { return null; }

   //
   // User-generated visitor methods below
   //
   boolean labelFlag = false;
   boolean idFlag = false;
   boolean intLitFlag = false;
   boolean binOpFlag = false;
   int[] currProc = {0, 0, 0};
   /**
    * f0 -> "MAIN"
    * f1 -> "["
    * f2 -> IntegerLiteral()
    * f3 -> "]"
    * f4 -> "["
    * f5 -> IntegerLiteral()
    * f6 -> "]"
    * f7 -> "["
    * f8 -> IntegerLiteral()
    * f9 -> "]"
    * f10 -> StmtList()
    * f11 -> "END"
    * f12 -> ( Procedure() )*
    * f13 -> VariablePackingInfo()
    * f14 -> <EOF>
    */
   public R visit(Goal n) {
      R _ret=null;
      
      System.out.println("\t.text\n\t.globl\tmain");
      
      n.f0.accept(this);
      
      System.out.println("main:\n\tmove $fp, $sp");
      
      n.f1.accept(this);
      String arg1Str = (String)n.f2.accept(this);
      n.f3.accept(this);
      n.f4.accept(this);
      String arg2Str = (String)n.f5.accept(this);
      n.f6.accept(this);
      n.f7.accept(this);
      String arg3Str = (String)n.f8.accept(this);
      n.f9.accept(this);
      
      int arg1 = Integer.parseInt(arg1Str);
      int arg2 = Integer.parseInt(arg2Str);
      int arg3 = Integer.parseInt(arg3Str);
      
      int sp;
      if (arg3 > 4) {
    	  sp = 4 * ((arg3 - 4) + arg2 + 1);
      } else {
    	  sp = 4 * (arg2 + 1);
      }
      
      System.out.println("\tsubu $sp, $sp, " + sp);
      System.out.println("\tsw $ra, -4($fp)");
      
      n.f10.accept(this);
      n.f11.accept(this);
      
      /*lw $ra, -4($fp)
         addu $sp, $sp, 4
         j $ra
       */
      System.out.println("\tlw $ra, -4($fp)");
      System.out.println("\taddu $sp, $sp, " + sp);
      System.out.println("\tj $ra");
      
      n.f12.accept(this);
      n.f13.accept(this);
      n.f14.accept(this);

      System.out.println("\n\t.text\n\t.globl _halloc\n_halloc:\n\tli $v0, 9\n\tsyscall\n\tj $ra");
      System.out.println("\n\t.text\n\t.globl _print\n_print:\n\tli $v0, 1\n\tsyscall");
      System.out.println("\tla $a0, newl\n\tli $v0, 4\n\tsyscall\n\tj $ra");
      System.out.println("\n\t.data\n\t.align   0\nnewl:\t.asciiz \"\\n\"\n\t.data\n\t.align   0");
      System.out.println("str_er:  .asciiz \" ERROR: abnormal termination\\n\"");
      return _ret;
   }

   /**
    * f0 -> ( ( Label() )? Stmt() )*
    */
   public R visit(StmtList n) {
      R _ret=null;
           
      if (n.f0.present()) {
          int _count=0;
          for ( Enumeration<Node> e = n.f0.elements(); e.hasMoreElements(); ) {
        	  labelFlag =true;
        	  e.nextElement().accept(this);
             _count++;
          }
          return _ret;
      } else {
          return null;
      }
   }

   /**
    * f0 -> Label()
    * f1 -> "["
    * f2 -> IntegerLiteral()
    * f3 -> "]"
    * f4 -> "["
    * f5 -> IntegerLiteral()
    * f6 -> "]"
    * f7 -> "["
    * f8 -> IntegerLiteral()
    * f9 -> "]"
    * f10 -> StmtList()
    * f11 -> "END"
    */
   /*   sw $fp, -8($sp)
         move $fp, $sp
         subu $sp, $sp, 20
           sw $ra, -4($fp)
         */
   public R visit(Procedure n) {
      R _ret=null;
      String name = (String)n.f0.accept(this);
      n.f1.accept(this);
      String arg1Str = (String)n.f2.accept(this);
      n.f3.accept(this);
      n.f4.accept(this);
      String arg2Str = (String)n.f5.accept(this);
      n.f6.accept(this);
      n.f7.accept(this);
      String arg3Str = (String)n.f8.accept(this);
      n.f9.accept(this);
      
      System.out.println("\n\t.text\n\t.globl\t" + name);
      System.out.println(name + ":");
      System.out.println("\tsw $fp, -8($sp)");
      System.out.println("\tmove $fp, $sp");
      
      int arg1 = Integer.parseInt(arg1Str);
      int arg2 = Integer.parseInt(arg2Str);
      int arg3 = Integer.parseInt(arg3Str);
      currProc[0] = arg1;
      currProc[1] = arg2;
      currProc[2] = arg3;
      int sp;
      if (arg3 > 4) {
    	  sp = 4 * ((arg3 - 4) + arg2 + 2);
      } else {
    	  sp = 4 * (arg2 + 2);
      }
      // TODO PAKKA!!!! check the below formula
      //int fp = 4 * (arg3 + arg2 - 2) - 8;
      int fp;
      if (arg3 > 4) {
    	  fp = 4 * arg2 + 4 * (arg3 - 4);
      } else {
    	  fp = 4 * arg2;
      }
      System.out.println("\tsubu $sp, $sp, " + sp);
      System.out.println("\tsw $ra, -4($fp)");
      
      /*
       * lw $ra, -4($fp)
         lw $fp, 12($sp)
         addu $sp, $sp, 20
         j $ra
       */
      n.f10.accept(this);
      n.f11.accept(this);     
      
      System.out.println("\tlw $ra, -4($fp)");
      System.out.println("\tlw $fp, " + fp + "($sp)");
      System.out.println("\taddu $sp, $sp, " + sp);
      System.out.println("\tj $ra");
      return _ret;
   }

   /**
    * f0 -> NoOpStmt()
    *       | ErrorStmt()
    *       | CJumpStmt()
    *       | JumpStmt()
    *       | HStoreStmt()
    *       | HLoadStmt()
    *       | MoveStmt()
    *       | PrintStmt()
    *       | ALoadStmt()
    *       | AStoreStmt()
    *       | PassArgStmt()
    *       | CallStmt()
    */
   public R visit(Stmt n) {
      R _ret=null;
      labelFlag = false;
      n.f0.accept(this);
      return _ret;
   }

   /**
    * f0 -> "NOOP"
    */
   public R visit(NoOpStmt n) {
      R _ret=null;
      n.f0.accept(this);
      System.out.println("\tnop");
      return _ret;
   }

   /**
    * f0 -> "ERROR"
    */
   // TODO
   // TODO some extra operators
   public R visit(ErrorStmt n) {
      R _ret=null;
      n.f0.accept(this);
      return _ret;
   }

   /**
    * f0 -> "CJUMP"
    * f1 -> Reg()
    * f2 -> Label()
    */
   public R visit(CJumpStmt n) {
      R _ret=null;
      n.f0.accept(this);
      String reg = (String)n.f1.accept(this);
      String label = (String)n.f2.accept(this);
      System.out.println("\tbeqz " + reg + " " + label); 
      return _ret;
   }

   /**
    * f0 -> "JUMP"
    * f1 -> Label()
    */
   public R visit(JumpStmt n) {
      R _ret=null;
      n.f0.accept(this);
      String label = (String)n.f1.accept(this);
      System.out.println("\tb " + label);
      return _ret;
      
   }

   /**
    * f0 -> "HSTORE"
    * f1 -> Reg()
    * f2 -> IntegerLiteral()
    * f3 -> Reg()
    */
   public R visit(HStoreStmt n) {
      R _ret=null;
      n.f0.accept(this);
      String reg1 = (String)n.f1.accept(this);
      String intLit = (String)n.f2.accept(this);
      String reg2 = (String)n.f3.accept(this);
      System.out.println("\tsw " + reg2 + ", " + intLit + "(" + reg1 + ")");
      return _ret;
   }

   /**
    * f0 -> "HLOAD"
    * f1 -> Reg()
    * f2 -> Reg()
    * f3 -> IntegerLiteral()
    */
   // lw $t2 0($t1)
   public R visit(HLoadStmt n) {
      R _ret=null;
      n.f0.accept(this);
      String reg1 = (String)n.f1.accept(this);
      String reg2 = (String)n.f2.accept(this);
      String intLit = (String)n.f3.accept(this);
      System.out.println("\tlw " + reg1 + ", " + intLit + "(" + reg2 + ")");
      return _ret;
   }

   /**
    * f0 -> "MOVE"
    * f1 -> Reg()
    * f2 -> Exp()
    */
   public R visit(MoveStmt n) {
      R _ret=null;
      n.f0.accept(this);
      String reg = (String)n.f1.accept(this);
      idFlag = false;
      intLitFlag = false;
      binOpFlag = false;
      String exp = (String)n.f2.accept(this);
      if (idFlag) {
    	  System.out.println("\tla " + reg + " " + exp);
      } else if (intLitFlag) {
    	  System.out.println("\tli " + reg + " " + exp);
      } else if (binOpFlag) {
    	  //System.err.println(exp);
    	  String[] part = exp.split("@", 2);
    	  System.out.println("\t" + part[0] + " " + reg + ", " + part[1]);
      } else {
    	  System.out.println("\tmove " + reg + " " + exp);
      }
      idFlag = false;
      intLitFlag = false;
      binOpFlag = false;
      return _ret;
   }

   /**
    * f0 -> "PRINT"
    * f1 -> SimpleExp()
    */
   public R visit(PrintStmt n) {
      R _ret=null;
      n.f0.accept(this);
      String simExp = (String)n.f1.accept(this);
      System.out.println("\tmove $a0 " + simExp);
      System.out.println("\tjal _print");
      return _ret;
   }

   /**
    * f0 -> "ALOAD"
    * f1 -> Reg()
    * f2 -> SpilledArg()
    */
    // lw $s0, 0($sp)
   public R visit(ALoadStmt n) {
      R _ret=null;
      n.f0.accept(this);
      String reg = (String)n.f1.accept(this);
      String stack = (String)n.f2.accept(this);
      int add = Integer.parseInt(stack);
      
      if (currProc[0] > 4 && add < (currProc[0] - 4))  {
    	  System.out.println("\tlw " + reg + ", " + (4 * add) + "($fp)");
      } else if (currProc[2] > 4) {
    	  System.out.println("\tlw " + reg + ", " + (4 * (add + currProc[2] - 4)) + "($sp)");
      } else {      
    	  System.out.println("\tlw " + reg + ", " + (4 * add) + "($sp)");
      }
      return _ret;
   }

   /**
    * f0 -> "ASTORE"
    * f1 -> SpilledArg()
    * f2 -> Reg()
    */
   // sw $s2, 8($sp)
   public R visit(AStoreStmt n) {
      R _ret=null;
      n.f0.accept(this);
      String stack = (String)n.f1.accept(this);
      String reg = (String)n.f2.accept(this);
      int add = Integer.parseInt(stack);
      
      if (currProc[0] > 4 && add < (currProc[0] - 4))  {
    	  System.out.println("\tsw " + reg + ", " + (4 * add) + "($fp)");
      } else if (currProc[2] > 4) {
    	  System.out.println("\tsw " + reg + ", " + (4 * (add + currProc[2] - 4)) + "($sp)");
      } else {      
    	  System.out.println("\tsw " + reg + ", " + (4 * add) + "($sp)");
      }
      return _ret;
   }

   /**
    * f0 -> "PASSARG"
    * f1 -> IntegerLiteral()
    * f2 -> Reg()
    */
    // sw $t5, 0($sp)
   public R visit(PassArgStmt n) {
      R _ret=null;
      n.f0.accept(this);
      String intLit = (String)n.f1.accept(this);
      String reg = (String)n.f2.accept(this);
      int ptr = 4 * (Integer.parseInt(intLit) - 1);
      System.out.println("\tsw " + reg + ", " + ptr + "($sp)");
      return _ret;
   }

   /**
    * f0 -> "CALL"
    * f1 -> SimpleExp()
    */
   // jalr $t2
   public R visit(CallStmt n) {
      R _ret=null;
      n.f0.accept(this);
      String simExp = (String)n.f1.accept(this);
      System.out.println("\tjalr " + simExp);
      return _ret;
   }

   /**
    * f0 -> HAllocate()
    *       | BinOp()
    *       | SimpleExp()
    */
   public R visit(Exp n) {
      R _ret=null;
      String exp = (String)n.f0.accept(this);
      return (R)exp;
   }

   /**
    * f0 -> "HALLOCATE"
    * f1 -> SimpleExp()
    */
   public R visit(HAllocate n) {
      R _ret=null;
      n.f0.accept(this);   
      intLitFlag = false;
      String halloc = (String)n.f1.accept(this);
      
      if (intLitFlag) {
    	  System.out.println("\tli $a0 " + halloc);
      } else {
    	  System.out.println("\tmove $a0 " + halloc);
      }
      
      System.out.println("\tjal _halloc");
      String ret = new String();
      ret = "$v0";
      idFlag = false;
      intLitFlag = false;
      binOpFlag = false;
      return (R)ret;
   }

   /**
    * f0 -> Operator()
    * f1 -> Reg()
    * f2 -> SimpleExp()
    */
   // TODO
   public R visit(BinOp n) {
      R _ret=null;
      String opStr = (String)n.f0.accept(this);
      String reg = (String)n.f1.accept(this);
      String simExp = (String)n.f2.accept(this);
      int op = Integer.parseInt(opStr);
      String finalStr  = new String();
      // TODO check bitor, all
      switch(op) {
      	case 0: finalStr = "slt"; break;
      	case 1: finalStr = "add"; break;
      	case 2: finalStr = "sub"; break;
      	case 3: finalStr = "mul"; break;
      	case 4: finalStr = "or"; break;
      	case 5: finalStr = "and"; break;
      	case 6: finalStr = "sll"; break;
      	case 7: finalStr = "srl"; break;
      	case 8: finalStr = "xor"; break;
      		
      }
      finalStr += "@" + reg + ", " + simExp;
      // System.err.println(finalStr);
      binOpFlag = true;
      idFlag = false;
      intLitFlag = false;
      return (R)finalStr;
   }

   /**
    * f0 -> "LT"
    *       | "PLUS"
    *       | "MINUS"
    *       | "TIMES"
    *       | "BITOR"
    *       | "BITAND"
    *       | "LSHIFT"
    *       | "RSHIFT"
    *       | "BITXOR"
    */
   // slt Rdest, Rsrc1, Src2
   public R visit(Operator n) {
      R _ret=null;
      n.f0.accept(this);
      int binOp = n.f0.which;
      String op = new String(); 
      op = Integer.toString(binOp);
      return (R)op;
   }

   /**
    * f0 -> "SPILLEDARG"
    * f1 -> IntegerLiteral()
    */
   public R visit(SpilledArg n) {
      R _ret=null;
      n.f0.accept(this);
      String intLit = (String)n.f1.accept(this);
      return (R)intLit;
   }

   /**
    * f0 -> Reg()
    *       | IntegerLiteral()
    *       | Label()
    */
   public R visit(SimpleExp n) {
      R _ret=null;
      String simExp = (String)n.f0.accept(this);
      return (R)simExp;
   }

   /**
    * f0 -> "a0"
    *       | "a1"
    *       | "a2"
    *       | "a3"
    *       | "t0"
    *       | "t1"
    *       | "t2"
    *       | "t3"
    *       | "t4"
    *       | "t5"
    *       | "t6"
    *       | "t7"
    *       | "s0"
    *       | "s1"
    *       | "s2"
    *       | "s3"
    *       | "s4"
    *       | "s5"
    *       | "s6"
    *       | "s7"
    *       | "t8"
    *       | "t9"
    *       | "v0"
    *       | "v1"
    */
   public R visit(Reg n) {
      R _ret=null;
      n.f0.accept(this);
      String reg = new String();
      // TODO check this
      reg = "$";
      reg += n.f0.choice.toString();
      intLitFlag = false;
      idFlag = false;
      binOpFlag = false;
      return (R)reg;
   }

   /**
    * f0 -> <INTEGER_LITERAL>
    */
   public R visit(IntegerLiteral n) {
      R _ret=null;
      n.f0.accept(this);
      Integer intLit = Integer.parseInt(n.f0.tokenImage);
      intLitFlag = true;
      idFlag = false;
      binOpFlag = false;
      return (R)Integer.toString(intLit);
   }

   /**
    * f0 -> <IDENTIFIER>
    */
   public R visit(Label n) {
      R _ret=null;
      n.f0.accept(this);
      String label = new String();
      label = n.f0.toString();
      if (labelFlag) {
    	  System.out.print(label + ":");
      }
      idFlag = true;
      intLitFlag = false;
      binOpFlag = false;
      return (R)label;
   }

   /**
    * f0 -> "// Number of  vars after packing ="
    * f1 -> IntegerLiteral()
    * f2 -> "; Number of Spilled vars ="
    * f3 -> IntegerLiteral()
    */
   public R visit(VariablePackingInfo n) {
      R _ret=null;
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      n.f3.accept(this);
      return _ret;
   }

}
