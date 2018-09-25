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
public class RecursiveDeclaration extends Declaration{

   public RecursiveDeclaration(Declaration dAST, SourcePosition thePosition){
    super (thePosition);
    D = dAST;
  }  
    
  public Object visit(Visitor v, Object o) {
    return v.visitReursiveDeclaration(this, o);
  }
  
  public Declaration D;
}
