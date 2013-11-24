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
public class MiniRA<R> implements GJNoArguVisitor<R> {
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
         String list = new String();
         list = "";
         for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
            String temp = (String)e.nextElement().accept(this);
            list += temp + "@";
            _count++;
         }
         return (R)list;
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

   int lineIter = 0;
   int maxLineIter;
   int stackPtr = 8;
   boolean live = true;
   boolean flagSimExp = false;
   boolean labelFlag = false;
   boolean v1Use;
   int v1Temp = -1;
   int v0Temp = -1;
   int globalNumArgs;
   private Map<Integer, Integer> maxCallArgs = new LinkedHashMap<Integer, Integer>();
   private Map<Integer, HashSet<Integer>> inStackAllProc = 
		   new LinkedHashMap<Integer, HashSet<Integer>>();
   private HashSet<Integer> inStack;
   private Map<Integer, Map<Integer, List<Integer>>> analysisAllProcs =
		   new LinkedHashMap<Integer, Map<Integer, List<Integer>>>();
   private Map<Integer, List<Integer>> analysis;
   //private Map<Integer, String> allocation = new LinkedHashMap<Integer, String>();
   private Map<Integer, Map<Integer, String>> allocationAllProcs =
		   new LinkedHashMap<Integer, Map<Integer, String>>();
   private Map<Integer, String> allocation;
   // TODO putting arbitrary large values for num_spills and max_args
   private Map<Integer, Integer> procArgs = new LinkedHashMap<Integer, Integer>();
   private Map<String, Integer> labelLine = new LinkedHashMap<String, Integer>();
   private List<String> freeReg = new ArrayList<String>();
   //private List<Integer> sortedTime = new ArrayList<Integer>();
   private List<Map.Entry<Integer, Integer>> sortedTime;
   private int currProcLine = 0;
   public void populateFreeList() {
	   freeReg.clear();
	   freeReg.add("s0");
	   freeReg.add("s1");
	   freeReg.add("s2");
	   freeReg.add("s3");
	   freeReg.add("s4");
	   freeReg.add("s5");
	   freeReg.add("s6");
	   freeReg.add("s7");
	   freeReg.add("t0");
	   freeReg.add("t1");
	   freeReg.add("t2");
	   freeReg.add("t3");
	   freeReg.add("t4");
	   freeReg.add("t5");
	   freeReg.add("t6");
	   freeReg.add("t7");
	   freeReg.add("t8");
	   freeReg.add("t9");
   }
   
   public void freeOldReg(int i) {
	   for (Integer key : analysis.keySet()) {
			 if (analysis.get(key).get(1) == i - 1) {
				 if (!inStack.contains(key)) {
					 //freeReg.add(allocation.get(key));
					 freeReg.add(0, allocation.get(key));
				 } 
			 }
	   }
   }
   // TODO write back v1 and v0 correctly
   
   // TODO check if it's actually sorting or not.
   public void createSortedTime() {
	   Map<Integer, Integer> tempTime = new LinkedHashMap<Integer, Integer>();
	   for (Integer key : analysis.keySet()) {
		   tempTime.put(key, analysis.get(key).get(1) - analysis.get(key).get(0));
	   }
	   
	   sortedTime = new ArrayList<Map.Entry<Integer, Integer>>(tempTime.entrySet());
	   
	   Collections.sort(sortedTime, new Comparator<Map.Entry<Integer, Integer>>() {
		   public int compare(Map.Entry<Integer, Integer> a, Map.Entry<Integer, Integer> b){
			   //return a.getValue().compareTo(b.getValue());
			   return b.getValue().compareTo(a.getValue());
		   }
	   });
   }
   
   public void spillReg() {
	   for (int i = 0; i < sortedTime.size(); ++i) {
		   if (allocation.get(sortedTime.get(i).getKey()) != null) {
			   Integer key = sortedTime.get(i).getKey();
			   freeReg.add(allocation.get(key));
			   allocation.put(key, "" + stackPtr);
			   stackPtr++;
			   sortedTime.remove(i);
			   inStack.add(key);
			   return;
		   }
	   }
   }
  
   public void setStackPtr(int lineNumber) {
	   if (procArgs.get(lineNumber) != null) {
		   stackPtr = 8;
		   /*if (procArgs.get(lineNumber) > 4) {
			   stackPtr += procArgs.get(lineNumber) - 4;
		   }*/
		   stackPtr += procArgs.get(lineNumber);
		   allocation = new LinkedHashMap<Integer, String>();
		   allocationAllProcs.put(lineNumber, allocation);
		   analysis = analysisAllProcs.get(lineNumber);
		   inStack = new HashSet<Integer>();
		   inStackAllProc.put(lineNumber, inStack);
		   populateFreeList();
		   createSortedTime();
	   }
   }
  
   public void incArgsLife() {
	   /*
	   List<List<Integer>> change = new ArrayList<List<Integer>>();
	   for (Integer proc : analysisAllProcs.keySet()) {
		   Map<Integer, List<Integer>>argsAnalysis = analysisAllProcs.get(proc);
		   for (Integer key : argsAnalysis.keySet()) {
			   	List<Integer> changeTemp = new ArrayList<Integer>();
			   	changeTemp.add(proc);
			   	changeTemp.add(key);
			   	change.add(changeTemp);
		   }
	   }
	   
	   for (int i = 0; i < change.size(); ++i) {
		   analysisAllProcs.get(change.get(i).get(0)).get(change.get(i).get(1)).set(1, maxLineIter);
	   }
	   */
	   for (Integer proc : procArgs.keySet()) {
		   for (int i = 0; i < procArgs.get(proc); ++i) {
			   List<Integer> life = new ArrayList<Integer>();
			   life.add(proc);
			   life.add(maxLineIter);
			   analysisAllProcs.get(proc).put(i, life);
		   }
	   }
   }
   
   public void allocate() {
	   incArgsLife();
	   populateFreeList();
	   analysis = analysisAllProcs.get(0);
	   createSortedTime();
	   allocation = new LinkedHashMap<Integer, String>();
	   allocationAllProcs.put(0, allocation);
	   inStack = new HashSet<Integer>();
	   inStackAllProc.put(0, inStack);
	   for (int i = 0; i <= maxLineIter; ++i) {
		   setStackPtr(i);
		   freeOldReg(i);
		   for (Integer key : analysis.keySet()) {
				 if (analysis.get(key).get(0) == i) {
					 if (freeReg.isEmpty()) {
						 spillReg();
					 }
					 allocation.put(key, freeReg.get(0));
					 freeReg.remove(0);
				 }
		   }
	   }
   }
   
   public void updateEndTime(int label, int jump) {
	    	
	   List<Integer> changedTemp = new ArrayList<Integer>();
	   for (Integer key : analysis.keySet()) {
		   if (analysis.get(key).get(1) >= label &&  analysis.get(key).get(1) <= jump) {
			   changedTemp.add(key);
		   }
	   }
	   
	   for (int i = 0; i < changedTemp.size(); ++i) {
		   analysis.get(changedTemp.get(i)).set(1, jump + 1);
	   }
	   
   }
   /**
    * f0 -> "MAIN"
    * f1 -> StmtList()
    * f2 -> "END"
    * f3 -> ( Procedure() )*
    * f4 -> <EOF>
    */
   
   public R visit(Goal n) {
      R _ret=null;
      //System.err.println("in main");
      analysis = new LinkedHashMap<Integer, List<Integer>>();
	  analysisAllProcs.put(0, analysis);
	  currProcLine = 0;
	  maxCallArgs.put(0, 0);

      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);   
      n.f3.accept(this);
      n.f4.accept(this);
      /*
      for (Integer proc : analysisAllProcs.keySet()) {
    	  Map<Integer, List<Integer>>analysis1 = analysisAllProcs.get(proc);
    	  System.err.println(proc);
	      for (Integer key : analysis1.keySet()) {
			  System.err.println("temp " + key + " " + analysis1.get(key).get(0) + " " 
					  				+ analysis1.get(key).get(1));
		  }
      }
      
      System.err.println("analysis finished");
      */
      live = false;
      maxLineIter = lineIter;
      lineIter = 0;
      
      allocate();
      allocation = allocationAllProcs.get(0);
      inStack = inStackAllProc.get(0);
      currProcLine = 0;
      /*
      for (Integer proc : allocationAllProcs.keySet()) {
    	  Map<Integer, String>allocation1 = allocationAllProcs.get(proc);
    	  System.err.println(proc);
	      for (Integer key : allocation1.keySet()) {
			  System.err.println("temp " + key + " " + allocation1.get(key));
		  }
      }
      */
      n.f0.accept(this);
      
      // TODO remove the 100
      System.out.println("MAIN[0][" + (18 + inStack.size()) + "][" + maxCallArgs.get(0) + "]"); 
      
      n.f1.accept(this);
      n.f2.accept(this);
      
      System.out.println("END"); 
      
      n.f3.accept(this);
      n.f4.accept(this);
      
      /*
	  for (Integer key : analysis.keySet()) {
		  System.out.println("temp " + key + " " + analysis.get(key).get(0) + " " 
				  				+ analysis.get(key).get(1));
	  }
	  */
      return _ret;
   }

   /**
    * f0 -> ( ( Label() )? Stmt() )*
    */
   public R visit(StmtList n) {
      R _ret=null;
      
      if (!live) {
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
      
      if (live) {
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
      
      return _ret;
   }

   /**
    * f0 -> Label()
    * f1 -> "["
    * f2 -> IntegerLiteral()
    * f3 -> "]"
    * f4 -> StmtExp()
    */
   // TODO loops and stack spilling!
   public R visit(Procedure n) {
      R _ret=null;
      String label = (String)n.f0.accept(this);
      n.f1.accept(this);
      String numArgs = (String)n.f2.accept(this);
      n.f3.accept(this);
      
      if (live) {
    	  Integer args = Integer.parseInt(numArgs);
    	  procArgs.put(lineIter, args);
    	  analysis = new LinkedHashMap<Integer, List<Integer>>();
    	  analysisAllProcs.put(lineIter, analysis);
    	  currProcLine = lineIter;
    	  maxCallArgs.put(lineIter, 0);
      }
      
      if (!live) {
    	  Integer args = Integer.parseInt(numArgs);
    	  globalNumArgs = args;
    	  currProcLine = lineIter;
    	  allocation = allocationAllProcs.get(currProcLine);
    	  inStack = inStackAllProc.get(currProcLine);
    	  int numPassArg;
    	  if (args > 4) {
    		  numPassArg = args - 4;
    	  } else {
    		  numPassArg = 0;
    	  }
    	  System.out.println("\n" + label + "[" + numArgs + "]" + "[" 
	  			  				+ (18 + inStack.size() + numPassArg) + "][" 
	  			  				+ maxCallArgs.get(currProcLine) + "]");
      }
      
      n.f4.accept(this);
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
    */
   public R visit(Stmt n) {
      R _ret=null;
      labelFlag = false;
      n.f0.accept(this);
      lineIter++;
      return _ret;
   }

   /**
    * f0 -> "NOOP"
    */
   public R visit(NoOpStmt n) {
      R _ret=null;
      n.f0.accept(this);
      
      if (!live) {
    	  System.out.println("NOOP");
      }
      
      return _ret;
   }

   /**
    * f0 -> "ERROR"
    */
   public R visit(ErrorStmt n) {
      R _ret=null;
      n.f0.accept(this);
      
      if (!live) {
    	  System.out.println("ERROR");
      }
      
      return _ret;
   }

   /**
    * f0 -> "CJUMP"
    * f1 -> Temp()
    * f2 -> Label()
    */
   public R visit(CJumpStmt n) {
      R _ret=null;
      n.f0.accept(this);
      String tempStr = (String)n.f1.accept(this);
      String label = (String)n.f2.accept(this);
      
      if (!live) {
    	  Integer temp = Integer.parseInt(tempStr);    	  
    	  // TODO what if we need three at once?
    	  if (inStack.contains(temp)) {
    		  System.out.println("ALOAD v1 SPILLEDARG " + allocation.get(temp));
    		  System.out.println("CJUMP v1 " + label);
    	  } else {
    		  System.out.println("CJUMP " + allocation.get(temp) + " " + label);
    	  }
      }
      
      if (live) {
    	  //System.err.println("!" + label);
    	  if (labelLine.get(label) != null) {
    		  //System.err.println(label);
    		  updateEndTime(labelLine.get(label), lineIter);
    	  }
      }
      
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
      
      if (!live) {
    	  System.out.println("JUMP " + label);
      }
      
      if (live) {
    	  if (labelLine.get(label) != null) {
    		  //System.err.println(label);
    		  updateEndTime(labelLine.get(label), lineIter);
    	  }
      }
      
      return _ret;
   }

   /**
    * f0 -> "HSTORE"
    * f1 -> Temp()
    * f2 -> IntegerLiteral()
    * f3 -> Temp()
    */
   public R visit(HStoreStmt n) {
      R _ret=null;
      n.f0.accept(this);
      String tempStr1 = (String)n.f1.accept(this);
      String intLit = (String)n.f2.accept(this);
      String tempStr2 = (String)n.f3.accept(this);
          
      if (!live) {
    	  boolean v1Occ = false;
    	  Integer temp1 = Integer.parseInt(tempStr1);
    	  Integer temp2 = Integer.parseInt(tempStr2);    
    	  String finalStr = new String();
    	  finalStr = "HSTORE ";
    	  if (inStack.contains(temp1)) {
    		  System.out.println("ALOAD v1 SPILLEDARG " + allocation.get(temp1));
    		  v1Occ = true;
    		  finalStr += "v1 " + intLit + " ";
    	  } else {
    		  finalStr += allocation.get(temp1) + " " + intLit + " ";
    	  }
    	  
    	  if (inStack.contains(temp2)) {
    		  if (v1Occ) {
    			  System.out.println("ALOAD v0 SPILLEDARG " + allocation.get(temp2));
    			  finalStr += "v0";
    		  } else {
    			  System.out.println("ALOAD v1 SPILLEDARG " + allocation.get(temp2));
    			  finalStr += "v1";
    		  }
    	  } else {
    		  finalStr += allocation.get(temp2);
    	  }
    	  System.out.println(finalStr);
    	  
    	  // TODO check
    	  /*
    	  if (v1Occ) {
    		  System.out.println("ASTORE SPILLEDARG " + allocation.get(temp1) + " v1");
    	  }
    	  */
      }
      
      return _ret;
   }

   /**
    * f0 -> "HLOAD"
    * f1 -> Temp()
    * f2 -> Temp()
    * f3 -> IntegerLiteral()
    */
   public R visit(HLoadStmt n) {
      R _ret=null;
      n.f0.accept(this);
      String tempStr1 = (String)n.f1.accept(this);
      String tempStr2 = (String)n.f2.accept(this);
      String intLit = (String)n.f3.accept(this);
      
      if (!live) {
    	  boolean v1Occ = false;
    	  Integer temp1 = Integer.parseInt(tempStr1);
    	  Integer temp2 = Integer.parseInt(tempStr2);    
    	  String finalStr = new String();
    	  finalStr = "HLOAD ";
    	  if (inStack.contains(temp1)) {
    		  System.out.println("ALOAD v1 SPILLEDARG " + allocation.get(temp1));
    		  v1Occ = true;
    		  finalStr += "v1 ";
    	  } else {
    		  finalStr += allocation.get(temp1) + " ";
    	  }
    	  
    	  if (inStack.contains(temp2)) {
    		  if (v1Occ) {
    			  System.out.println("ALOAD v0 SPILLEDARG " + allocation.get(temp2));
    			  finalStr += "v0 " + intLit;
    		  } else {
    			  System.out.println("ALOAD v1 SPILLEDARG " + allocation.get(temp2));
    			  finalStr += "v1 " + intLit;
    		  }
    	  } else {
    		  finalStr += allocation.get(temp2) + " " + intLit;
    	  }
    	  System.out.println(finalStr);
    	  
    	  if (v1Occ) {
    		  System.out.println("ASTORE SPILLEDARG " + allocation.get(temp1) + " v1");
    	  }
      }
      
      return _ret;
   }

   /**
    * f0 -> "MOVE"
    * f1 -> Temp()
    * f2 -> Exp()
    */
   public R visit(MoveStmt n) {
      R _ret=null;
      n.f0.accept(this);
      //v1Temp = -1;
      String tempStr = (String)n.f1.accept(this);
      String exp = (String)n.f2.accept(this);
      
      if (!live) {
    	  Integer temp = Integer.parseInt(tempStr);    	  
    	  if (inStack.contains(temp)) {
    		//  if (v1Temp == -1) {
    			  //System.out.println("ALOAD v1 SPILLEDARG " + allocation.get(temp));
    			  System.out.println("MOVE v1 " + exp);
    			  System.out.println("ASTORE SPILLEDARG " + allocation.get(temp) + " v1");
    		/*  } else {
    			  System.out.println("ALOAD v0 SPILLEDARG " + allocation.get(temp));
    			  System.out.println("MOVE v0 " + exp);
    			  System.out.println("ASTORE SPILLEDARG " + allocation.get(temp) + " v0");
    		  }
    		  */
    	  } else {
    		  System.out.println("MOVE " + allocation.get(temp) + " " + exp);
    	  }
      }
      
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
      
      if (!live) {
    	  System.out.println("PRINT " + simExp);
      }
      
      return _ret;
   }

   /**
    * f0 -> Call()
    *       | HAllocate()
    *       | BinOp()
    *       | SimpleExp()
    */
   public R visit(Exp n) {
      R _ret=null;
      String exp = (String)n.f0.accept(this);
      return (R)exp;
   }

   /**
    * f0 -> "BEGIN"
    * f1 -> StmtList()
    * f2 -> "RETURN"
    * f3 -> SimpleExp()
    * f4 -> "END"
    */
   //TODO change allocation to v0
   public R visit(StmtExp n) {
      R _ret=null;
      n.f0.accept(this);
      
  	  int start = 0;
      if (!live) {
    	  //System.out.println("BEGIN");
       	  int args = globalNumArgs;
    	  if (args > 4) {
    		  start = args - 4;
    	  }
    	  for (int i = start; i < start + 8; ++i) {
    	       System.out.println("ASTORE SPILLEDARG " + i + " s" + (i - start));
    	  }

    	  for (int i = 0; i < args; ++i) {
    		  if (i < 4) {
	    		  if (inStack.contains(i)) {
	    			  System.out.println("ASTORE SPILLEDARG " + allocation.get(i) + " a" + i);
	    		  } else {
	    			  System.out.println("MOVE " + allocation.get(i) + " a" + i);
	    		  }
    		  } else {
	    		  if (inStack.contains(i)) {
	    			  System.out.println("ALOAD v1 SPILLEDARG " + (i - 4));
	    			  System.out.println("ASTORE SPILLEDARG " + allocation.get(i) + " v1");
	    		  } else {
	    			  // TODO
	    			  System.out.println("ALOAD v1 SPILLEDARG " + (i - 4));
	    			  System.out.println("MOVE " + allocation.get(i) + " v1");
	    		  }    			  
    		  }
    	  }
      }
      
      n.f1.accept(this);
      n.f2.accept(this);
      String simExp = (String)n.f3.accept(this);
      n.f4.accept(this);
      
      if (!live) {	 
    	  System.out.println("MOVE v0 " + simExp);
    	  for (int i = start; i < start + 8; ++i) {
    		  System.out.println("ALOAD s" + (i - start) +" SPILLEDARG " + i);
    	  }
    	  System.out.println("END\n");
      }
      
      return _ret;
   }

   /**
    * f0 -> "CALL"
    * f1 -> SimpleExp()
    * f2 -> "("
    * f3 -> ( Temp() )*
    * f4 -> ")"
    */
   // TODO take care! temp and simpexp in same place
   public R visit(Call n) {
      R _ret=null;
      n.f0.accept(this);
      v1Use = false;
      String simExp = (String)n.f1.accept(this);
      n.f2.accept(this);
      String tempStrList = (String)n.f3.accept(this);
      n.f4.accept(this);
      
      if (live) {
    	  String[] tempStr = tempStrList.split("@");
    	  if (tempStr.length > maxCallArgs.get(currProcLine)) {
    		  maxCallArgs.put(currProcLine, tempStr.length);
    	  }
      }
      
      if (!live) {
    	  String[] tempStr = tempStrList.split("@");
          for (int i = 0; i < tempStr.length; ++i) {
        	  Integer temp = Integer.parseInt(tempStr[i]);
        	  String var = new String();
        	  if (inStack.contains(temp)) {
        		  if (v1Use) {
        			  System.out.println("ALOAD v0 SPILLEDARG " + allocation.get(temp));
        			  var = "v0";
        			  v0Temp = temp;
        		  } else {
        			  System.out.println("ALOAD v1 SPILLEDARG " + allocation.get(temp));
        			  var = "v1";
        			  v1Temp = temp;
        		  }
        	  } else {
        		  var = allocation.get(temp);
        	  }
        	  
        	  if (i < 4) {
        		  System.out.println("MOVE a" + i + " " + var);
        	  } else {
        		  System.out.println("PASSARG " + (i - 3) + " " + var);
        	  }
          }
          
       // TODO total num spills....for now taking 10
    	  int start = globalNumArgs;
    	  if (start > 4) {
    		  start = 8 + start - 4;
    	  } else {
    		  start = 8;
    	  }
    	  //start += 10;
    	  start += inStack.size();
    	  for (int j = start; j < start + 10; ++j) {
    		  System.out.println("ASTORE SPILLEDARG " + j + " t" + (j - start));
    	  }
          
          System.out.println("CALL " + simExp);
        	
          for (int j = start; j < start + 10; ++j) {
    		  System.out.println("ALOAD t" + (j - start) + " SPILLEDARG " + j);
    	  }
          
          String ret = new String();
          ret = "v0";
          return (R)ret;
      }
	     
      return _ret;
   }

   // TODO stack not correct. Need to clear the stack after each procedure....and start from
   // 7 + the passed args (ie num_args - 4)
   /**
    * f0 -> "HALLOCATE"
    * f1 -> SimpleExp()
    */
   public R visit(HAllocate n) {
      R _ret=null;
      n.f0.accept(this);
      String simExp = (String)n.f1.accept(this);
      
      if (!live) {
    	  String ret = new String();
    	  ret = "HALLOCATE " + simExp;
    	  return (R)ret;
      }
      
      return _ret;
   }

   /**
    * f0 -> Operator()
    * f1 -> Temp()
    * f2 -> SimpleExp()
    */
   public R visit(BinOp n) {
      R _ret=null;
      String op = (String)n.f0.accept(this);
      String tempStr = (String)n.f1.accept(this);
      
      if (!live) {
    	  Integer temp = Integer.parseInt(tempStr);
    	  if (inStack.contains(temp)) {
    		  v1Use = true;
    	  }
      }
      
      String simExp = (String)n.f2.accept(this);
      
      v1Use = false;
      
      if (!live) {
    	  Integer temp = Integer.parseInt(tempStr);    	  
    	  String ret = new String();
    	  ret = op + " ";
    	  if (inStack.contains(temp)) {
    		  System.out.println("ALOAD v1 SPILLEDARG " + allocation.get(temp));
    		  ret += "v1 " + simExp;
    		  v1Temp = temp;
    		  return (R)ret;
    	  } else {
    		  ret += allocation.get(temp) + " " + simExp;
    		  return (R)ret;
    	  }
      }
      
      return _ret;
   }

   /**
    * f0 -> "LT"
    *       | "PLUS"
    *       | "MINUS"
    *       | "TIMES"
    */
   public R visit(Operator n) {
      R _ret=null;
      n.f0.accept(this);
      String op = new String();
      int choice = n.f0.which;
      switch (choice) {
      	case 0: op = "LT"; break; 
      	case 1: op = "PLUS"; break; 
      	case 2: op = "MINUS"; break; 
      	case 3: op = "TIMES"; break; 
      } 
      return (R)op;
   }

   /**
    * f0 -> Temp()
    *       | IntegerLiteral()
    *       | Label()
    */
   public R visit(SimpleExp n) {
      R _ret=null;
      flagSimExp = true;
      String simExp = (String)n.f0.accept(this);
      flagSimExp = false;
      return (R)simExp;
   }

   /**
    * f0 -> "TEMP"
    * f1 -> IntegerLiteral()
    */
   public R visit(Temp n) {
      R _ret=null;
      n.f0.accept(this);
      String intStr = (String)n.f1.accept(this);
      Integer intLit = Integer.parseInt(n.f1.f0.tokenImage);
      
      if (live) {
    	  if (analysis.get(intLit) == null) {
    		  List<Integer> tempStart = new ArrayList<Integer>();
    		  tempStart.add(lineIter);
    		  tempStart.add(lineIter);
    		  analysis.put(intLit, tempStart);
    	  } else {
    		  analysis.get(intLit).set(1, lineIter);
    	  }
      }
      
      if (!live) {
    	  if (flagSimExp) {
    	  	  if (inStack.contains(intLit)) {
        		  if (v1Use) {
        			  System.out.println("ALOAD v0 SPILLEDARG " + allocation.get(intLit));
        			  String ret = new String();
        			  ret = "v0";
        			  v0Temp = intLit;
        			  return (R)ret;
        		  } else {
        			  v1Use = true;
        			  System.out.println("ALOAD v1 SPILLEDARG " + allocation.get(intLit));
        			  String ret = new String();
        			  ret = "v1";
        			  v1Temp = intLit;
        			  return (R)ret;
        		  }
    	  	  } else {
    	  		  String ret = new String();
    	  		  ret = allocation.get(intLit);
    	  		  return (R)ret;
    	  	  }
    	  }
      }  
      return (R)intStr;
   }

   /**
    * f0 -> <INTEGER_LITERAL>
    */
   public R visit(IntegerLiteral n) {
      R _ret=null;
      n.f0.accept(this);
      Integer intLit = Integer.parseInt(n.f0.tokenImage);
      return (R)Integer.toString(intLit);
   }

   /**
    * f0 -> <IDENTIFIER>
    */
   // TODO what about C style commenting?????
   public R visit(Label n) {
      R _ret=null;
      n.f0.accept(this);
      String id = n.f0.toString();
      
      if (!live) {
    	  if (labelFlag) {
    		  System.out.println(id);
    	  }
      }
      
      if (live) {
    	  if (labelFlag) {
    		  labelLine.put(id, lineIter);
    		  labelFlag = false;
    		  //System.err.println(id + " " + lineIter);
    	  }
      }
      
      return (R)id;
   }

}
