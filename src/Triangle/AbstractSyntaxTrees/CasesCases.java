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
public class CasesCases extends Cases{  //TODO :creating CasesCases class
    public CasesCases(Cases caseAST,Cases caseAST2,SourcePosition thePosition) {
        super(thePosition);
        CS = caseAST;
        CS2 = caseAST2;
        
    }

    public Object visit(Visitor v, Object o) {
        return v.visitCasesCases(this, o);
    }
    
    public Cases CS;
    public Cases CS2;
}
