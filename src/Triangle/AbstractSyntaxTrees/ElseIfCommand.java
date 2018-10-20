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
public class ElseIfCommand extends Command{ //TODO :add the else if command

    public ElseIfCommand (Expression eAST, Expression eAST2, Command cAST,SourcePosition thePosition) {
    super (thePosition);
    E = eAST;
    E2 = eAST2;
    C = cAST;
  }

  public Object visit(Visitor v, Object o) {
    return v.visitElseIfCommand(this, o);
  }

  public Expression E;
  public Expression E2;
  public Command C;
}
