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
public class CaseCases extends Cases{
    public CaseCases(Cases caseAST2,Command cAST,SourcePosition thePosition) {
        super(thePosition);
        CS = caseAST2;
        C = cAST;
        
    }

    public Object visit(Visitor v, Object o) {
        return v.visitCaseCases(this, o);
    }
    
    public Cases CS;
    public Command C;
}
