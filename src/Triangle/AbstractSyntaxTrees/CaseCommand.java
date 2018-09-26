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
public class CaseCommand extends Command{
    
  public CaseCommand (Expression clAST, Command cAST,SourcePosition thePosition) {
    super (thePosition);
    E = clAST;
    C = cAST;
  }

  public Object visit(Visitor v, Object o) {
    return v.visitCaseCommand(this, o);
  }

  public Expression E;
  public Command C;
    
}
