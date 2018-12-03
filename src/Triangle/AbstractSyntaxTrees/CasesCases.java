/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Triangle.AbstractSyntaxTrees;

import Triangle.SyntacticAnalyzer.SourcePosition;

/**
 *
 * @author Andre
 */
public class CasesCases extends Cases{
   
  public CasesCases(Cases case1, Cases case2,SourcePosition thePosition) {
        super(null,thePosition);
        this.c1 = case1;
        this.c2 = case2;
  }
 
    
  @Override
  public Object visit(Visitor v, Object o) {
    return v.visitCasesCommand(this, o);
  } 
  
  public Cases c1;
  public Cases c2; 
    
}
