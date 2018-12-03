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
public class CaseCommand extends Cases{

    public CaseCommand(Expression expresion, Command comando,SourcePosition thePosition) {
        super(comando,thePosition);
        EC = expresion;
    }


    public Object visit(Visitor v, Object o) {
      return v.visitCaseCommand(this, o);
    }
    
        public Expression EC;
    
}
