/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Triangle.AbstractSyntaxTrees;

import Triangle.SyntacticAnalyzer.SourcePosition;

public class ArrayTypeDenoterAux extends TypeDenoter { //add ArrayTypeDetonatorAux

  public ArrayTypeDenoterAux (IntegerLiteral ilAST,IntegerLiteral ilASTAux ,TypeDenoter tASTAux,SourcePosition thePosition) {
    super (thePosition);
    IL = ilAST;
    ILAux = ilASTAux;
    T = tASTAux;
  }

  public Object visit(Visitor v, Object o) {
    return v.visitArrayTypeDenoterAux(this, o);
  }

  public boolean equals (Object obj) {
    if (obj != null && obj instanceof ErrorTypeDenoter)
      return true;
    else if (obj != null && obj instanceof ArrayTypeDenoter)
      return this.IL.spelling.compareTo(((ArrayTypeDenoter) obj).IL.spelling) == 0 &&
             this.T.equals(((ArrayTypeDenoter) obj).T);
    else
      return false;
  }

  public IntegerLiteral IL;
  public IntegerLiteral ILAux;
  public TypeDenoter T;
}