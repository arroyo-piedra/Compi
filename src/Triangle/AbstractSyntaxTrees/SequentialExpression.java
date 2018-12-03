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
public class SequentialExpression extends Expression{
    


    public SequentialExpression(Expression eAST1, Expression eAST2, SourcePosition thePosition) {
        super(thePosition);
        E1 = eAST1;
        E2 = eAST2;
    }

   
    public Object visit(Visitor v, Object o) {
        return v.visitSequentialExpression(this, o);
    }
    
    public Expression E1,E2;
    
}
