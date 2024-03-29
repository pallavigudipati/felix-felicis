//
// Generated by JTB 1.3.2
//

package visitor;
import syntaxtree.*;

import java.text.ParseException;
import java.util.*;
import java.util.Map.Entry;

/**
 * Provides default methods which visit each node in the tree in depth-first
 * order.  Your visitors may extend this class.
 */
public class TypeChecker<R> implements GJNoArguVisitor<R> {
	// My code.
	private HashMap<String, String> symbolTable = new HashMap<String, String>();
	private HashMap<String, String> methodReturn = new HashMap<String, String>();
	private HashSet<String> classes = new HashSet<String>();
	private HashMap<String, List<String>> arguments =
			new HashMap<String, List<String>>();
	private HashMap<String, String> extendClass = new HashMap<String, String>();
	private HashMap<String, String> typeCast = new HashMap<String, String>();
	private String currentClass = new String();
	private String currentMethod = new String();
	private Stack<String> activationStack = new Stack<String>();
	//private String callingClassMethod = new String();
	int paramIt;
	boolean globalFlag = false;
	// takes all the extends cases and checks valid declarations in VarDeclarations
	boolean onlyForExtends = false;
	boolean allocationFlag = false;
	boolean enteringExtends = false;
	boolean thisFlag = false;
	boolean bracketFlag = false;
	
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
   
   public boolean Match(String a, String b) {
	   if (a == b) {
		   return true;
	   }
	   if (FindExtend(a, b)) {
		   return true;
	   }
	   return false;
   }

   /**
    * f0 -> MainClass()
    * f1 -> ( TypeDeclaration() )*
    * f2 -> <EOF>
    */
   public R visit(Goal n) {
      R _ret=null;
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      //System.out.print("QQQ " + arguments.get("A meth"));
      
      onlyForExtends = true;
      
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      
      //System.out.print("QQQ " + arguments.get("A meth"));
      
      onlyForExtends = false;
      
      globalFlag = true;
      
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      return _ret;
   }

   /**
    * f0 -> "class"
    * f1 -> Identifier()
    * f2 -> "{"
    * f3 -> "public"
    * f4 -> "static"
    * f5 -> "void"
    * f6 -> "main"
    * f7 -> "("
    * f8 -> "String"
    * f9 -> "["
    * f10 -> "]"
    * f11 -> Identifier()
    * f12 -> ")"
    * f13 -> "{"
    * f14 -> PrintStatement()
    * f15 -> "}"
    * f16 -> "}"
    */
   public R visit(MainClass n) {
      R _ret=null;
      n.f0.accept(this);
      n.f1.accept(this);
      currentClass = (String)n.f1.f0.toString();
      classes.add(currentClass);
      currentMethod = "";
      //callingClassMethod = "";
      n.f2.accept(this);
      n.f3.accept(this);
      n.f4.accept(this);
      n.f5.accept(this);
      n.f6.accept(this);
      n.f7.accept(this);
      n.f8.accept(this);
      n.f9.accept(this);
      n.f10.accept(this);
      n.f11.accept(this);
      n.f12.accept(this);
      n.f13.accept(this);
      n.f14.accept(this);
      n.f15.accept(this);
      n.f16.accept(this);
      return _ret;
   }

   /**
    * f0 -> ClassDeclaration()
    *       | ClassExtendsDeclaration()
    */
   public R visit(TypeDeclaration n) {
      R _ret=null;
      n.f0.accept(this);
      return _ret;
   }

   /**
    * f0 -> "class"
    * f1 -> Identifier()
    * f2 -> "{"
    * f3 -> ( VarDeclaration() )*
    * f4 -> ( MethodDeclaration() )*
    * f5 -> "}"
    */
   public R visit(ClassDeclaration n) {
      R _ret=null;
      //System.out.print("sss " + arguments.get("A meth"));
      n.f0.accept(this);
      n.f1.accept(this);
      currentClass = n.f1.f0.toString();
      classes.add(currentClass);
      currentMethod = "";
      n.f2.accept(this);
      n.f3.accept(this);
      n.f4.accept(this);
      n.f5.accept(this);
      return _ret;
   }

   /**
    * f0 -> "class"
    * f1 -> Identifier()
    * f2 -> "extends"
    * f3 -> Identifier()
    * f4 -> "{"
    * f5 -> ( VarDeclaration() )*
    * f6 -> ( MethodDeclaration() )*
    * f7 -> "}"
    */
   //TODO Extends ka funda
   public R visit(ClassExtendsDeclaration n) {
      R _ret=null;
      enteringExtends = true;
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      n.f3.accept(this);
      
      currentClass = n.f1.f0.toString();
      classes.add(currentClass);
      currentMethod = "";
      String class1 = n.f1.f0.toString();
      String class2 = n.f3.f0.toString();
      
      //System.out.print("ttT " + class1 + " " + class2);
      extendClass.put(class1, class2);
      
      
      if (onlyForExtends) {
	      // transferring variables
    	  //System.out.print("RRR " + arguments.get("A meth"));
	      String temp = new String();
	      Set set = symbolTable.entrySet();
	      Iterator it = set.iterator();
	      HashMap<String, String> toPutV = new HashMap<String, String>();
	      while(it.hasNext()) {
	         Map.Entry symbol = (Map.Entry)it.next();
	         if (((String)symbol.getKey()).startsWith(class2 + " ")) {
	        	 temp = ((String)symbol.getKey());
	        	 temp = temp.replaceFirst(class2 + " ", class1 + " ");
	        	 toPutV.put(temp, (String)symbol.getValue());
	         }
	      }
	      
	      Set setq = toPutV.entrySet();
	      Iterator itq = setq.iterator();
	      while(itq.hasNext()) {
	         Map.Entry symbol = (Map.Entry)itq.next();
	         symbolTable.put((String)symbol.getKey(), (String)symbol.getValue());
		  }
	      
	      HashMap<String, String> toPutM = new HashMap<String, String>();
	      Set setm = methodReturn.entrySet();
	      Iterator itm = setm.iterator();
	      while(itm.hasNext()) {
	         Map.Entry symbol = (Map.Entry)itm.next();
	         if (((String)symbol.getKey()).startsWith(class2 + " ")) {
	        	 temp = ((String)symbol.getKey());
	        	 temp = temp.replace(class2 + " ", class1 + " ");
	        	 toPutM.put(temp, (String)symbol.getValue());
	        	 arguments.put(temp, arguments.get((String)symbol.getKey()));
	         }
	      }
	      
	      Set setp = toPutM.entrySet();
	      Iterator itp = setp.iterator();
	      while(itp.hasNext()) {
	         Map.Entry symbol = (Map.Entry)itp.next();
	         methodReturn.put((String)symbol.getKey(), (String)symbol.getValue());
		  }	      
	            
	      n.f4.accept(this);
	      n.f5.accept(this);
	      n.f6.accept(this);
	      n.f7.accept(this);
      }
      
      enteringExtends = false;
      return _ret;
   }
   
   public boolean FindExtend (String a, String b) {
	   boolean sentinel = true;
	   String temp = new String();
	   temp = a;
	   while (sentinel) {
		   if (extendClass.get(temp) != null) {
			   if (b == extendClass.get(temp)) {
				   return true;
			   } else {
				   temp = extendClass.get(temp);
			   }
		   } else {
			   sentinel = false;
		   }
	   }
	   return false;
   }

   /**
    * f0 -> Type()
    * f1 -> Identifier()
    * f2 -> ";"
    */
   public R visit(VarDeclaration n) {
      R _ret=null;
      String type = (String)n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      if (!globalFlag) {
    	  // NO OVERLOADING similar thing 
    	  // for classes and methods but keep in mind overriding
    	  // and overloading
	      if (!onlyForExtends) {
	    	  if (symbolTable.get(currentClass + " " + currentMethod + " "
	    			  			  + n.f1.f0.toString()) != null) {
		    	  System.out.print("Type error");
		    	  System.exit(-1);
	    	  }
	    	  symbolTable.put(currentClass + " " + currentMethod + " "
	    			  			+ n.f1.f0.toString(), type);
	      } else {
    		  if ((type != "int") && (type != "int[]") && (type != "boolean") &&
    				  !classes.contains(type)) {
    	    	  System.out.print("Type error");
    	    	  System.exit(-1);
    		  }
    	  }
      }
      return _ret;
   }

   /**
    * f0 -> "public"
    * f1 -> Type()
    * f2 -> Identifier()
    * f3 -> "("
    * f4 -> ( FormalParameterList() )?
    * f5 -> ")"
    * f6 -> "{"
    * f7 -> ( VarDeclaration() )*
    * f8 -> ( Statement() )*
    * f9 -> "return"
    * f10 -> Expression()
    * f11 -> ";"
    * f12 -> "}"
    */
   
   // DID this.Method
   public R visit(MethodDeclaration n) {
      R _ret=null;
      List<String> params = new ArrayList<String>();
      List<String> paramsTypes = new ArrayList<String>();
      n.f0.accept(this);
      String type = (String)n.f1.accept(this);
      n.f2.accept(this);
      currentMethod = n.f2.f0.toString();
	  
      if (!globalFlag) {
    	  if (!enteringExtends && !onlyForExtends) {
	    	  if (methodReturn.get(currentClass + " " + currentMethod) != null) {
		    	  System.out.print("Type error");
		    	  System.exit(-1);
	    	  }
		      methodReturn.put(currentClass + " " + currentMethod, type);
    	  } else if (enteringExtends && onlyForExtends) {
    		  if (methodReturn.get(currentClass + " " + currentMethod) != null) {
    			   if (!Match(type, methodReturn.get(currentClass + " " +
    					   								currentMethod))) {
    			    	  System.out.print("Type error");
    			      	  System.exit(-1);
    			   }
    			   params = arguments.get(currentClass + " " + currentMethod);
    			   for (int i = 0; i < params.size(); ++i) {
    				   paramsTypes.add(symbolTable.get(currentClass + " "
    						   			+ currentMethod + " " + params.get(i))); 
    			   }
    			   
    			   String temp = new String();
    			   String toRemove = new String();
    			   toRemove = currentClass + " " + currentMethod + " ";
    			   HashMap <String, String> ToR = new HashMap<String, String>();
    			   Set set = symbolTable.entrySet();
    			   Iterator it = set.iterator();
    			   while(it.hasNext()) {
    				   Map.Entry symbol = (Map.Entry)it.next();
    				   if (((String)symbol.getKey()).startsWith(toRemove)) {
    					   ToR.put((String)symbol.getKey(), (String)symbol.getValue());
    				   }
    			    }
    			   
    			   Set setn = ToR.entrySet();
    			   Iterator itn = setn.iterator();
    			   while(itn.hasNext()) {
    				   Map.Entry symbol = (Map.Entry)itn.next();
    				   symbolTable.remove((String)symbol.getKey());
    			    }
    			   
    			      
    		  } else {
    			  methodReturn.put(currentClass + " " + currentMethod, type);
    		  }
    	  }
      }
      
	  n.f3.accept(this);
	  
	  if (!globalFlag && (!onlyForExtends || (onlyForExtends && enteringExtends))) {
    	  List<String> params2 = new ArrayList<String>();
    	  arguments.put(currentClass + " " + currentMethod, params2);
	  }
	  
	  n.f4.accept(this);
	  
      if (!globalFlag) {   	  
    	  if (enteringExtends && onlyForExtends){
    		  List<String> params2Types = new ArrayList<String>();
    		  List<String> params2 = new ArrayList<String>();
    		  params2 = arguments.get(currentClass + " " + currentMethod);
			  for (int i = 0; i < params2.size(); ++i) {
				  params2Types.add(symbolTable.get(currentClass + " "
					   				+ currentMethod + " " + params2.get(i))); 
			  }
			   
			  if (paramsTypes.size() != params2Types.size()) {
		    	  System.out.print("Type error");
		    	  System.exit(-1);
			  }
			  
			  for (int i = 0; i < paramsTypes.size(); ++i) {
	    		  if (!Match(paramsTypes.get(i), params2Types.get(i))) {
	    	    	  System.out.print("Type error");
	    	    	  System.exit(-1);
	    		  }
			  }
    	  }
      }
      
      n.f5.accept(this);
      n.f6.accept(this);
      n.f7.accept(this);
      n.f8.accept(this);
      n.f9.accept(this);
      n.f10.accept(this);
      n.f11.accept(this);
      n.f12.accept(this);
      
      return _ret;
   }

   /**
    * f0 -> FormalParameter()
    * f1 -> ( FormalParameterRest() )*
    */
   public R visit(FormalParameterList n) {
      R _ret=null;
      n.f0.accept(this);
      n.f1.accept(this);
      return _ret;
   }

   /**
    * f0 -> Type()
    * f1 -> Identifier()
    */
   public R visit(FormalParameter n) {
      R _ret=null;
      String type = (String)n.f0.accept(this);
      n.f1.accept(this);
      if (!globalFlag && (!onlyForExtends || (onlyForExtends && enteringExtends))) {
    	  arguments.get(currentClass + " " + currentMethod).add(n.f1.f0.toString());
    	  symbolTable.put(currentClass + " " + currentMethod + " " + n.f1.f0.toString(),
    			  			type);
      }
      return _ret;
   }

   /**
    * f0 -> ","
    * f1 -> FormalParameter()
    */
   public R visit(FormalParameterRest n) {
      R _ret=null;
      n.f0.accept(this);
      n.f1.accept(this);
      return _ret;
   }

   /**
    * f0 -> ArrayType()
    *       | BooleanType()
    *       | IntegerType()
    *       | Identifier()
    */
   public R visit(Type n) {
      // P
      return n.f0.accept(this);
   }

   /**
    * f0 -> "int"
    * f1 -> "["
    * f2 -> "]"
    */
   public R visit(ArrayType n) {
      R _ret=null;
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      // P
      return (R)"int[]";
   }

   /**
    * f0 -> "boolean"
    */
   public R visit(BooleanType n) {
      R _ret=null;
      n.f0.accept(this);
      // P
      return (R)"boolean";
   }

   /**
    * f0 -> "int"
    */
   public R visit(IntegerType n) {
      R _ret=null;
      n.f0.accept(this);
      return (R)"int";
   }

   /**
    * f0 -> Block()
    *       | AssignmentStatement()
    *       | ArrayAssignmentStatement()
    *       | IfStatement()
    *       | WhileStatement()
    *       | PrintStatement()
    */
   public R visit(Statement n) {
      R _ret=null;
      n.f0.accept(this);
      return _ret;
   }

   /**
    * f0 -> "{"
    * f1 -> ( Statement() )*
    * f2 -> "}"
    */
 
   public R visit(Block n) {
      R _ret=null;
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      return _ret;
   }

   /**
    * f0 -> Identifier()
    * f1 -> "="
    * f2 -> Expression()
    * f3 -> ";"
    */
   public R visit(AssignmentStatement n) {
      R _ret=null;
      String type1 = new String();
      String var = (String)n.f0.accept(this);
      n.f1.accept(this);
      String type2 = (String)n.f2.accept(this);
      n.f3.accept(this);
      boolean flag = false; 
      if (globalFlag) {
	      if (symbolTable.get(currentClass + " " + currentMethod + " " + var)
		       != null) {
		    	  type1 = symbolTable.get(currentClass + " " + currentMethod + " "
		    	          + var);
		    	  flag = true;
		  }
		  if (symbolTable.get(currentClass + " " + " " + var) != null) {
		    	  type1 = symbolTable.get(currentClass + " " + " " + var);
		  }
	      if ((type1 != type2) && !(FindExtend(type2, type1))) {
	    	  System.out.print("Type error");
	    	  System.exit(-1);
	      }
      }
      return (R)type1;
   }

   /**
    * f0 -> Identifier()
    * f1 -> "["
    * f2 -> Expression()
    * f3 -> "]"
    * f4 -> "="
    * f5 -> Expression()
    * f6 -> ";"
    */
   // NO ONLY EXTENDS WALA typecasting hai kya????
   public R visit(ArrayAssignmentStatement n) {
      R _ret=null;
      String type1 = new String();
      String var = (String)n.f0.accept(this);
      n.f1.accept(this);
      String type2 = (String)n.f2.accept(this);
      n.f3.accept(this);
      n.f4.accept(this);
      String type3 = (String)n.f5.accept(this);
      n.f6.accept(this);
	  if (globalFlag) {
	      if (symbolTable.get(currentClass + " " + currentMethod + " " + var)
		  	   != null) {
		      type1 = symbolTable.get(currentClass + " " + currentMethod + " "
		              + var);
		  }
		  if (symbolTable.get(currentClass + " " + " " + var) != null) {
		      type1 = symbolTable.get(currentClass + " " + " " + var);
		  }
	      if ((type1 != "int[]") || (type2 != "int") || (type3 != "int")) {
	    	  System.out.print("Type error");
	    	  System.exit(-1);
	      }
	  }
      return (R)"int";
   }

   /**
    * f0 -> "if"
    * f1 -> "("
    * f2 -> Expression()
    * f3 -> ")"
    * f4 -> Statement()
    * f5 -> "else"
    * f6 -> Statement()
    */
   public R visit(IfStatement n) {
      R _ret=null;
      n.f0.accept(this);
      n.f1.accept(this);
      String type = (String)n.f2.accept(this);
      if (globalFlag) {
    	  if (type != "boolean") {
	    	  System.out.print("Type error");
	    	  System.exit(-1);
    	  }
      }
      n.f3.accept(this);
      n.f4.accept(this);
      n.f5.accept(this);
      n.f6.accept(this);
      return _ret;
   }

   /**
    * f0 -> "while"
    * f1 -> "("
    * f2 -> Expression()
    * f3 -> ")"
    * f4 -> Statement()
    */
   public R visit(WhileStatement n) {
      R _ret=null;
      n.f0.accept(this);
      n.f1.accept(this);
      String type = (String)n.f2.accept(this);
      if (globalFlag) {
    	  if (type != "boolean") {
	    	  System.out.print("Type error");
	    	  System.exit(-1);
    	  }
      }
      n.f3.accept(this);
      n.f4.accept(this);
      return _ret;
   }

   /**
    * f0 -> "System.out.println"
    * f1 -> "("
    * f2 -> Expression()
    * f3 -> ")"
    * f4 -> ";"
    */
   // ONLY BOOLEAN what about while etc??
   // only bool ya int, int[] bhi chalega?
   public R visit(PrintStatement n) {
      R _ret=null;
      n.f0.accept(this);
      n.f1.accept(this);
      String type = (String)n.f2.accept(this);
      if (globalFlag) {
    	  if (type != "int") {
	    	  System.out.print("Type error");
	    	  System.exit(-1);
    	  }
      }
      n.f3.accept(this);
      n.f4.accept(this);
      return _ret;
   }

   // TODO check all the stuff below PROPERLY
   /**
    * f0 -> AndExpression()
    *       | CompareExpression()
    *       | PlusExpression()
    *       | MinusExpression()
    *       | TimesExpression()
    *       | ArrayLookup()
    *       | ArrayLength()
    *       | MessageSend()
    *       | PrimaryExpression()
    */
   public R visit(Expression n) {
      R _ret=null;
      String type = (String)n.f0.accept(this);
      return (R)type;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "&"
    * f2 -> PrimaryExpression()
    */
   // TODO int and boolean ka jhol
   public R visit(AndExpression n) {
      R _ret=null;
      String type1 = (String)n.f0.accept(this);
      n.f1.accept(this);
      String type2 = (String)n.f2.accept(this);
	  if (globalFlag) {
		  if ((type1 == type2) && (type1 == "boolean")) {
			  //System.out.print("# " + type1 + " " + type2);
			  return (R)"boolean";
		  }
    	  System.out.print("Type error");
    	  System.exit(-1);
	  }
      return (R)type1;
   }

   // NO check if int a = boolean f is possible
   /**
    * f0 -> PrimaryExpression()
    * f1 -> "<"
    * f2 -> PrimaryExpression()
    */

   public R visit(CompareExpression n) {
      R _ret=null;
      String lhsType = (String)n.f0.accept(this);
      n.f1.accept(this);
      String rhsType = (String)n.f2.accept(this);
      if (globalFlag) {
      	  if((lhsType != rhsType) || (lhsType != "int") || (rhsType != "int")) {
	    	  System.out.print("Type error");
	    	  System.exit(-1);
	      }
	  }
      return (R)"boolean";
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "+"
    * f2 -> PrimaryExpression()
    */
   // NO can we do boolean + boolean
   public R visit(PlusExpression n) {
      R _ret=null;
      String type1 = (String)n.f0.accept(this);
      n.f1.accept(this);
      String type2 = (String)n.f2.accept(this);
      if (globalFlag) {
	      if ((type1 != type2) || (type1 != "int")) {
	    	  System.out.print("Type error");
	    	  System.exit(-1);
	      }
      }

      return (R)type1;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "-"
    * f2 -> PrimaryExpression()
    */
   public R visit(MinusExpression n) {
	  R _ret=null;
      String type1 = (String)n.f0.accept(this);
      n.f1.accept(this);
      String type2 = (String)n.f2.accept(this);
      if (globalFlag) {
	      if ((type1 != type2) || ((type1 != "boolean") && (type1 != "int"))) {
	    	  System.out.print("Type error");
	    	  System.exit(-1);
	      }
      }
      return (R)type1;
   }

   // Allowed only for int
   /**
    * f0 -> PrimaryExpression()
    * f1 -> "*"
    * f2 -> PrimaryExpression()
    */
   public R visit(TimesExpression n) {
      R _ret=null;
      String type1 = (String)n.f0.accept(this);
      n.f1.accept(this);
      String type2 = (String)n.f2.accept(this);
      if (globalFlag) {
	      if ((type1 != type2) || (type1 != "int")) {
	    	  System.out.print("Type error");
	    	  System.exit(-1);
	      }
      }
      return (R)"int";
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "["
    * f2 -> PrimaryExpression()
    * f3 -> "]"
    */
   public R visit(ArrayLookup n) {
      R _ret=null;
      String type1 = (String)n.f0.accept(this);
      n.f1.accept(this);
      String type2 = (String)n.f2.accept(this);
      n.f3.accept(this);
	  if (globalFlag) {
	      if ((type1 != "int[]") || (type2 != "int")) {
	    	  System.out.print("Type error");
	    	  System.exit(-1);
	      }
	  }
      return (R)"int";
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "."
    * f2 -> "length"
    */
   public R visit(ArrayLength n) {
      R _ret=null;
      String type = (String)n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      if (globalFlag) {
	      if (type != "int[]") {
	    	  System.out.print("Type error");
	    	  System.exit(-1);
	      }
      }
      return (R)"int";
   }

   // Should we check whether a method belongs to a particular class
   /**
    * f0 -> PrimaryExpression()
    * f1 -> "."
    * f2 -> Identifier()
    * f3 -> "("
    * f4 -> ( ExpressionList() )?
    * f5 -> ")"
    */
   public R visit(MessageSend n) {
      R _ret=null;
      String type2 = new String();
      String type1 = (String)n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      String var = n.f2.f0.toString();
      
      if (globalFlag) {
    	  paramIt = 0;
	      if (methodReturn.get(type1 + " " + var) != null) {	    	  
	    	  type2 = methodReturn.get(type1 + " " + var);
	      } else {
	    	  System.out.print("Type error");
	    	  System.exit(-1);
	      }
	  }
      
      if (globalFlag) {
    	  activationStack.push(type1 + " " + var);
      }
      
      n.f3.accept(this);
      n.f4.accept(this);
      n.f5.accept(this);
      
      if (globalFlag) {
	      if (paramIt != arguments.get(type1 + " " + var).size()) {
	    	  System.out.print("Type error");
	    	  System.exit(-1);
	      }
	      paramIt = 0;
	      activationStack.pop();
      }
      return (R)type2;
   }

   /**
    * f0 -> Expression()
    * f1 -> ( ExpressionRest() )*
    */
   public R visit(ExpressionList n) {
      R _ret=null;
      String var = new String();
      
      String paramType = (String)n.f0.accept(this);
      
      if (globalFlag) {
    	  String callingFunction = new String();
    	  callingFunction = activationStack.pop();
      	  if (arguments.get(callingFunction) == null) {
      		System.out.print("Type error");
		  	  System.exit(-1);
      	  }
      	  paramIt = 0;
    	  
    	  var = arguments.get(callingFunction).get(paramIt);
    	  if (!Match(paramType, symbolTable.get(callingFunction + " " + var))) {
    		  System.out.print("Type error");
  		  	  System.exit(-1);
    	  }
    	  activationStack.push(callingFunction);    			  		
    	  paramIt++;    	  
      }
      n.f1.accept(this);
      return _ret;
   }

   /**
    * f0 -> ","
    * f1 -> Expression()
    */
   //TODO check all the print statements
   public R visit(ExpressionRest n) {
      R _ret=null;
      n.f0.accept(this);
      String paramType = (String)n.f1.accept(this);
      String var = new String();
      if (globalFlag) {
    	  String callingFunction = new String();
    	  callingFunction = activationStack.pop();
    	  if (paramIt > arguments.get(callingFunction).size() - 1) {
    		  System.out.print("Type error");
  		  	  System.exit(-1);
    	  }
    	  var = arguments.get(callingFunction).get(paramIt);
    	  if (!Match(paramType, symbolTable.get(callingFunction + " " + var))) {
    		  System.out.print("Type error");
  		  	  System.exit(-1);
    	  }
    	  activationStack.push(callingFunction);		  		
    	  paramIt++;    	  
      }
      return _ret;
   }

   /**
    * f0 -> IntegerLiteral()
    *       | TrueLiteral()
    *       | FalseLiteral()
    *       | Identifier()
    *       | ThisExpression()
    *       | ArrayAllocationExpression()
    *       | AllocationExpression()
    *       | NotExpression()
    *       | BracketExpression()
    */
   // Hoe to differentiate between variable name and class name type
   public R visit(PrimaryExpression n) {
      R _ret=null;
      String type = (String)n.f0.accept(this);
      if (allocationFlag) {
    	  allocationFlag = false;
    	  return (R)type;
      }
      if (thisFlag) {
    	  thisFlag = false;
    	  return (R)type;
      }
      if (bracketFlag) {
    	  bracketFlag = false;
    	  return (R)type;
      }
      if ((type != "int") && (type != "boolean") && (type != "int[]")) {
	      if (globalFlag) {
      		  if (symbolTable.get(currentClass + " " + currentMethod + " " + type)
      				  != null) {
     	    	  return (R)symbolTable.get(currentClass + " " + currentMethod
     	    			                    + " " + type);
     	      }
      		  if (symbolTable.get(currentClass + " " + " " + type) != null) {
     	    	  return (R)symbolTable.get(currentClass + " " + " " + type);
     	      }
	    	  System.out.print("Type error");
	    	  System.exit(-1);
	      }
      }
      return (R)type;
   }

   /**
    * f0 -> <INTEGER_LITERAL>
    */
   public R visit(IntegerLiteral n) {
      R _ret=null;
      n.f0.accept(this);
      return (R)"int";
   }

   /**
    * f0 -> "true"
    */
   public R visit(TrueLiteral n) {
      R _ret=null;
      n.f0.accept(this);
      return (R)"boolean";
   }

   /**
    * f0 -> "false"
    */
   public R visit(FalseLiteral n) {
      R _ret=null;
      n.f0.accept(this);
      return (R)"boolean";
   }

   /**
    * f0 -> <IDENTIFIER>
    */
   public R visit(Identifier n) {
      R _ret=null;
      n.f0.accept(this);
	  return (R)n.f0.toString();
   }

   /**
    * f0 -> "this"
    */
   // CORRECT how do you handle this?
   public R visit(ThisExpression n) {
      R _ret=null;
      n.f0.accept(this);
      thisFlag = true;
      return (R)currentClass;
   }

   /**
    * f0 -> "new"
    * f1 -> "int"
    * f2 -> "["
    * f3 -> Expression()
    * f4 -> "]"
    */
   public R visit(ArrayAllocationExpression n) {
      R _ret=null;
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      String type = (String)n.f3.accept(this);
      n.f4.accept(this);
      if (globalFlag) {
	      if (type != "int") {
	    	  System.out.print("Type error");
	    	  System.exit(-1);
	      }
      }  	  
      return (R)"int[]";
   }

   /**
    * f0 -> "new"
    * f1 -> Identifier()
    * f2 -> "("
    * f3 -> ")"
    */
   public R visit(AllocationExpression n) {
      R _ret=null;
      n.f0.accept(this);
      if (globalFlag) {
    	  String var = (String)n.f1.accept(this);
    	  if (classes.contains(var)) {
    		  allocationFlag = true;
    		  return (R)var;
    	  }
    	  System.out.print("Type error");
    	  System.exit(-1);
      } 
      n.f2.accept(this);
      n.f3.accept(this);
      return _ret;
   }

   /**
    * f0 -> "!"
    * f1 -> Expression()
    */
   public R visit(NotExpression n) {
      R _ret=null;
      n.f0.accept(this);
      String type = (String)n.f1.accept(this);
      if (globalFlag) {
	      if (type != "boolean") {
	    	  System.out.print("Type error");
	    	  System.exit(-1);
	      }
      }
      return (R)"boolean";
   }

   /**
    * f0 -> "("
    * f1 -> Expression()
    * f2 -> ")"
    */
   public R visit(BracketExpression n) {
      R _ret=null;
      n.f0.accept(this);
      String type = (String)n.f1.accept(this);
      n.f2.accept(this);
      bracketFlag = true;
      return (R)type;
   }

}
