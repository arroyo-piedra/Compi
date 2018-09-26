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
public class CharacterCases extends Cases{
    public CharacterCases(CharacterLiteral clAST,SourcePosition thePosition) {
        super(thePosition);
        C = clAST;
    }

    public Object visit(Visitor v, Object o) {
        return v.visitCharacterCases(this, o);
    }
    
    public CharacterLiteral C;
    
}
