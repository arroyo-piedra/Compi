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
public class VarIniDeclaration extends Declaration{ //TODO :add var declaration
    
  public VarIniDeclaration (Identifier iAST, Expression eAST,SourcePosition thePosition) {
    super (thePosition);
    I = iAST;
    E = eAST;
  }

  public Object visit(Visitor v, Object o) {
    return v.visitVarIniDeclaration(this, o);
  }

  public Identifier I;
  public Expression E;
    
}
