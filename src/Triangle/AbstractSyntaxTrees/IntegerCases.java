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
public class IntegerCases extends Cases{ //TODO :creating IntegerCases class

    public IntegerCases(IntegerLiteral ilAST,SourcePosition thePosition) {
        super(thePosition);
        I = ilAST;
    }

    public Object visit(Visitor v, Object o) {
        return v.visitIntegerCases(this, o);
    }
    
    public IntegerLiteral I;
    
}
