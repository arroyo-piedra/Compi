/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Triangle.AbstractSyntaxTrees;

import Triangle.SyntacticAnalyzer.SourcePosition;

/**
 *
 * @author PCEsteban
 */
public class SelectCommand extends Command {        //TODO :creating Select Command
    public SelectCommand (Expression eAST, CasesCases caseAST,
                    SourcePosition thePosition) {
    super (thePosition);
    E = eAST;
    CS = caseAST;
  }

  public Object visit(Visitor v, Object o) {
    return v.visitSelectCommand(this, o);
  }

  public Expression E;
  public CasesCases CS;
}
