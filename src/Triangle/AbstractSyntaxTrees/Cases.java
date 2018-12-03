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

public abstract class Cases extends Command{ //add case command
    
    public Cases(Command c1AST,SourcePosition thePosition) {
        super(thePosition);
        CC = c1AST;
    }
    
    public Command CC;
    
}