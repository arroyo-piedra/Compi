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
public class ElseIfCommand extends Command{ //add the else if command

    public ElseIfCommand (Expression eASTAux, Command cASTAux,SourcePosition thePosition) {
    super (thePosition);
    E = eASTAux;
    C = cASTAux;
  }

  public Object visit(Visitor v, Object o) {
    return v.visitElseIfCommand(this, o);
  }

  public Expression E;
  public Command C;
}
