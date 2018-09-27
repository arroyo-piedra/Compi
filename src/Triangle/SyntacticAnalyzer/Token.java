/*
 * @(#)Token.java                        2.1 2003/10/07
 *
 * Copyright (C) 1999, 2003 D.A. Watt and D.F. Brown
 * Dept. of Computing Science, University of Glasgow, Glasgow G12 8QQ Scotland
 * and School of Computer and Math Sciences, The Robert Gordon University,
 * St. Andrew Street, Aberdeen AB25 1HG, Scotland.
 * All rights reserved.
 *
 * This software is provided free for educational use only. It may
 * not be used for commercial purposes without the prior written permission
 * of the authors.
 */

package Triangle.SyntacticAnalyzer;


final class Token extends Object {

  protected int kind;
  protected String spelling;
  protected SourcePosition position;

  public Token(int kind, String spelling, SourcePosition position) {

    if (kind == Token.IDENTIFIER) {
      int currentKind = firstReservedWord;
      boolean searching = true;

      while (searching) {
        int comparison = tokenTable[currentKind].compareTo(spelling);
        if (comparison == 0) {
          this.kind = currentKind;
          searching = false;
        } else if (comparison > 0 || currentKind == lastReservedWord) {
          this.kind = Token.IDENTIFIER;
          searching = false;
        } else {
          currentKind ++;
        }
      }
    } else
      this.kind = kind;

    this.spelling = spelling;
    this.position = position;

  }

  public static String spell (int kind) {
    return tokenTable[kind];
  }

  public String toString() {
    return "Kind=" + kind + ", spelling=" + spelling +
      ", position=" + position;
  }

  // Token classes...

  public static final int

    // literals, identifiers, operators...
    INTLITERAL	= 0,
    CHARLITERAL	= 1,
    IDENTIFIER	= 2,
    OPERATOR	= 3,

    // reserved words - must be in alphabetical order...
    // We add new reseved words (case, elsif, for, from, local, nil, repeat, recursive, select, to, until)
    // We eliminate the reserved word BEGIN
    // Words now have new order 
    ARRAY		= 4,
    //BEGIN		= 5,    //TODO :ELiminate BEGIN
    CASE                = 5,    //TODO :Adding reserved word CASE
    CONST		= 6,
    DO			= 7,
    ELSE		= 8,
    ELSIF               = 9,    //TODO :Adding reserved word ELSIF 
    END			= 10,   //TODO :Changing order 
    FOR                 = 11,   //TODO :Adding reserved word FOR
    FROM                = 12,   //TODO :Adding reserved word FROM
    FUNC		= 13,
    IF			= 14,
    IN			= 15,
    LET			= 16,
    LOCAL               = 17,   //TODO :Adding reserved word LOCAL 
    OF			= 18,
    NIL                 = 19,   //TODO :Adding reserved word NIL
    PROC		= 20,
    RECORD		= 21,
    RECURSIVE           = 22,   //TODO :Adding reserved word RECURSIVE
    REPEAT              = 23,   //TODO :Adding reserved word REPEAT
    SELECT              = 24,   //TODO :Adding reserved word SELECT
    THEN		= 25,
    TO                  = 26,   //TODO :Adding reserved word TO
    TYPE		= 27,
    UNTIL               = 28,   //TODO :Adding reserved word UNTIL
    VAR			= 29,
    WHILE		= 30,

    // punctuation...
    DOT			= 31,
    DOTDOT              = 32,       //TODO :Adding DOTDOT
    COLON		= 33,
    SEMICOLON	= 34,
    COMMA		= 35,
    BECOMES		= 36,
    IS			= 37,
    PIPE                = 38,       //TODO :Adding PIPE

    // brackets...
    LPAREN		= 39,
    RPAREN		= 40,
    LBRACKET	= 41,
    RBRACKET	= 42,
    LCURLY		= 43,
    RCURLY		= 44,

    // special tokens...
    EOT			= 45,
    ERROR		= 46;

  private static String[] tokenTable = new String[] {
    "<int>",
    "<char>",
    "<identifier>",
    "<operator>",
    "array",
    //"begin",    //TODO :removing the begin token
    "case",     //TODO :Adding CASE
    "const",
    "do",
    "else",
    "elsif",   //TODO :Adding ELSIF
    "end",
    "for",      //TODO :Adding FOR
    "from",     //TODO :Adding FROM
    "func",
    "if",
    "in",
    "let",
    "local",    //TODO :Adding LOCAL
    "of",
    "nil",      //TODO :Adding NIL
    "proc",
    "record",
    "recursive",    //TODO :Adding RECURSIVE
    "repeat",       //TODO :Adding REPEAT
    "select",       //TODO :Adding SELECT
    "then",
    "to",           //TODO :Adding TO
    "type",
    "until",        //TODO :Adding UNTIL
    "var",
    "while",
    ".",
    ":",
    ";",
    ",",
    ":=",
    "~",
    "(",
    ")",
    "[",
    "]",
    "{",
    "}",
    "|",        //TODO :Adding PIPE
    "..",       //TODO :Adding DOTDOT
    "",
    "<error>",
    
    
    
  };

  private final static int	firstReservedWord = Token.ARRAY,
  				lastReservedWord  = Token.WHILE;

}
