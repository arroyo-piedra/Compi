/*
 * @(#)Parser.java                        2.1 2003/10/07
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

import Triangle.ErrorReporter;
import Triangle.AbstractSyntaxTrees.ActualParameter;
import Triangle.AbstractSyntaxTrees.ActualParameterSequence;
import Triangle.AbstractSyntaxTrees.ArrayAggregate;
import Triangle.AbstractSyntaxTrees.ArrayExpression;
import Triangle.AbstractSyntaxTrees.ArrayTypeDenoter;
import Triangle.AbstractSyntaxTrees.ArrayTypeDenoterAux;
import Triangle.AbstractSyntaxTrees.AssignCommand;
import Triangle.AbstractSyntaxTrees.BinaryExpression;
import Triangle.AbstractSyntaxTrees.CallCommand;
import Triangle.AbstractSyntaxTrees.CallExpression;
import Triangle.AbstractSyntaxTrees.CaseCases;
import Triangle.AbstractSyntaxTrees.CaseLiteralsCase;
import Triangle.AbstractSyntaxTrees.Cases;
import Triangle.AbstractSyntaxTrees.CasesCases;
import Triangle.AbstractSyntaxTrees.CharacterCases;
import Triangle.AbstractSyntaxTrees.CharacterExpression;
import Triangle.AbstractSyntaxTrees.CharacterLiteral;
import Triangle.AbstractSyntaxTrees.Command;
import Triangle.AbstractSyntaxTrees.CompoundDeclaration;
import Triangle.AbstractSyntaxTrees.ConstActualParameter;
import Triangle.AbstractSyntaxTrees.ConstDeclaration;
import Triangle.AbstractSyntaxTrees.ConstFormalParameter;
import Triangle.AbstractSyntaxTrees.Declaration;
import Triangle.AbstractSyntaxTrees.DoUntilCommand;
import Triangle.AbstractSyntaxTrees.DoWhileCommand;
import Triangle.AbstractSyntaxTrees.DotVname;
import Triangle.AbstractSyntaxTrees.ElseCase;
import Triangle.AbstractSyntaxTrees.ElseIfCommand;
import Triangle.AbstractSyntaxTrees.EmptyActualParameterSequence;
import Triangle.AbstractSyntaxTrees.EmptyCommand;
import Triangle.AbstractSyntaxTrees.EmptyFormalParameterSequence;
import Triangle.AbstractSyntaxTrees.Expression;
import Triangle.AbstractSyntaxTrees.FieldTypeDenoter;
import Triangle.AbstractSyntaxTrees.ForCommand;
import Triangle.AbstractSyntaxTrees.ForTernaryDeclaration;
import Triangle.AbstractSyntaxTrees.FormalParameter;
import Triangle.AbstractSyntaxTrees.FormalParameterSequence;
import Triangle.AbstractSyntaxTrees.FuncActualParameter;
import Triangle.AbstractSyntaxTrees.FuncDeclaration;
import Triangle.AbstractSyntaxTrees.FuncFormalParameter;
import Triangle.AbstractSyntaxTrees.Identifier;
import Triangle.AbstractSyntaxTrees.IfCommand;
import Triangle.AbstractSyntaxTrees.IfExpression;
import Triangle.AbstractSyntaxTrees.IntegerCases;
//import Triangle.AbstractSyntaxTrees.IntegerDeclaration;
import Triangle.AbstractSyntaxTrees.IntegerExpression;
import Triangle.AbstractSyntaxTrees.IntegerLiteral;
import Triangle.AbstractSyntaxTrees.LetCommand;
import Triangle.AbstractSyntaxTrees.LetExpression;
import Triangle.AbstractSyntaxTrees.LocalDeclaration;
import Triangle.AbstractSyntaxTrees.MultipleActualParameterSequence;
import Triangle.AbstractSyntaxTrees.MultipleArrayAggregate;
import Triangle.AbstractSyntaxTrees.MultipleFieldTypeDenoter;
import Triangle.AbstractSyntaxTrees.MultipleFormalParameterSequence;
import Triangle.AbstractSyntaxTrees.MultipleRecordAggregate;
import Triangle.AbstractSyntaxTrees.Operator;
import Triangle.AbstractSyntaxTrees.ProcActualParameter;
import Triangle.AbstractSyntaxTrees.ProcDeclaration;
import Triangle.AbstractSyntaxTrees.ProcFormalParameter;
import Triangle.AbstractSyntaxTrees.ProcFuncDeclaration;
import Triangle.AbstractSyntaxTrees.Program;
import Triangle.AbstractSyntaxTrees.RecordAggregate;
import Triangle.AbstractSyntaxTrees.RecordExpression;
import Triangle.AbstractSyntaxTrees.RecordTypeDenoter;
import Triangle.AbstractSyntaxTrees.RecursiveDeclaration;
import Triangle.AbstractSyntaxTrees.SelectCommand;
import Triangle.AbstractSyntaxTrees.SequentialCommand;
import Triangle.AbstractSyntaxTrees.SequentialDeclaration;
import Triangle.AbstractSyntaxTrees.SimpleTypeDenoter;
import Triangle.AbstractSyntaxTrees.SimpleVname;
import Triangle.AbstractSyntaxTrees.SingleActualParameterSequence;
import Triangle.AbstractSyntaxTrees.SingleArrayAggregate;
import Triangle.AbstractSyntaxTrees.SingleFieldTypeDenoter;
import Triangle.AbstractSyntaxTrees.SingleFormalParameterSequence;
import Triangle.AbstractSyntaxTrees.SingleRecordAggregate;
import Triangle.AbstractSyntaxTrees.SubscriptVname;
import Triangle.AbstractSyntaxTrees.TypeDeclaration;
import Triangle.AbstractSyntaxTrees.TypeDenoter;
import Triangle.AbstractSyntaxTrees.UnaryExpression;
import Triangle.AbstractSyntaxTrees.UntilCommand;
import Triangle.AbstractSyntaxTrees.VarActualParameter;
import Triangle.AbstractSyntaxTrees.VarDeclaration;
import Triangle.AbstractSyntaxTrees.VarFormalParameter;
import Triangle.AbstractSyntaxTrees.VarIniDeclaration;
import Triangle.AbstractSyntaxTrees.Vname;
import Triangle.AbstractSyntaxTrees.VnameExpression;
import Triangle.AbstractSyntaxTrees.WhileCommand;

public class Parser {

  private Scanner lexicalAnalyser;
  private ErrorReporter errorReporter;
  private Token currentToken;
  private SourcePosition previousTokenPosition;

  public Parser(Scanner lexer, ErrorReporter reporter) {
    lexicalAnalyser = lexer;
    errorReporter = reporter;
    previousTokenPosition = new SourcePosition();
  }

// accept checks whether the current token matches tokenExpected.
// If so, fetches the next token.
// If not, reports a syntactic error.

  void accept (int tokenExpected) throws SyntaxError {
    if (currentToken.kind == tokenExpected) {
      previousTokenPosition = currentToken.position;
      currentToken = lexicalAnalyser.scan();
    } else {
      syntacticError("\"%\" expected here", Token.spell(tokenExpected));
    }
  }

  void acceptIt() {
    previousTokenPosition = currentToken.position;
    currentToken = lexicalAnalyser.scan();
  }

// start records the position of the start of a phrase.
// This is defined to be the position of the first
// character of the first token of the phrase.

  void start(SourcePosition position) {
    position.start = currentToken.position.start;
  }

// finish records the position of the end of a phrase.
// This is defined to be the position of the last
// character of the last token of the phrase.

  void finish(SourcePosition position) {
    position.finish = previousTokenPosition.finish;
  }

  void syntacticError(String messageTemplate, String tokenQuoted) throws SyntaxError {
    SourcePosition pos = currentToken.position;
    errorReporter.reportError(messageTemplate, tokenQuoted, pos);
    throw(new SyntaxError());
  }

///////////////////////////////////////////////////////////////////////////////
//
// PROGRAMS
//
///////////////////////////////////////////////////////////////////////////////

  public Program parseProgram() {

    Program programAST = null;

    previousTokenPosition.start = 0;
    previousTokenPosition.finish = 0;
    currentToken = lexicalAnalyser.scan();

    try {
      Command cAST = parseCommand();
      programAST = new Program(cAST, previousTokenPosition);
      if (currentToken.kind != Token.EOT) {
        syntacticError("\"%\" not expected after end of program",
          currentToken.spelling);
      }
    }
    catch (SyntaxError s) { return null; }
    return programAST;
  }

///////////////////////////////////////////////////////////////////////////////
//
// LITERALS
//
///////////////////////////////////////////////////////////////////////////////

// parseIntegerLiteral parses an integer-literal, and constructs
// a leaf AST to represent it.

  IntegerLiteral parseIntegerLiteral() throws SyntaxError {
    IntegerLiteral IL = null;

    if (currentToken.kind == Token.INTLITERAL) {
      previousTokenPosition = currentToken.position;
      String spelling = currentToken.spelling;
      IL = new IntegerLiteral(spelling, previousTokenPosition);
      currentToken = lexicalAnalyser.scan();
    } else {
      IL = null;
      syntacticError("integer literal expected here", "");
    }
    return IL;
  }

// parseCharacterLiteral parses a character-literal, and constructs a leaf
// AST to represent it.

  CharacterLiteral parseCharacterLiteral() throws SyntaxError {
    CharacterLiteral CL = null;

    if (currentToken.kind == Token.CHARLITERAL) {
      previousTokenPosition = currentToken.position;
      String spelling = currentToken.spelling;
      CL = new CharacterLiteral(spelling, previousTokenPosition);
      currentToken = lexicalAnalyser.scan();
    } else {
      CL = null;
      syntacticError("character literal expected here", "");
    }
    return CL;
  }

// parseIdentifier parses an identifier, and constructs a leaf AST to
// represent it.

  Identifier parseIdentifier() throws SyntaxError {
    Identifier I = null;

    if (currentToken.kind == Token.IDENTIFIER) {
      previousTokenPosition = currentToken.position;
      String spelling = currentToken.spelling;
      I = new Identifier(spelling, previousTokenPosition);
      currentToken = lexicalAnalyser.scan();
    } else {
      I = null;
      syntacticError("identifier expected here", "");
    }
    return I;
  }

// parseOperator parses an operator, and constructs a leaf AST to
// represent it.

  Operator parseOperator() throws SyntaxError {
    Operator O = null;

    if (currentToken.kind == Token.OPERATOR) {
      previousTokenPosition = currentToken.position;
      String spelling = currentToken.spelling;
      O = new Operator(spelling, previousTokenPosition);
      currentToken = lexicalAnalyser.scan();
    } else {
      O = null;
      syntacticError("operator expected here", "");
    }
    return O;
  }
  
///////////////////////////////////////////////////////////////////////////////
//
// CASES //add the cases type
//
///////////////////////////////////////////////////////////////////////////////
  
  Cases parseCase() throws SyntaxError {
      Cases caseAST = null;
      SourcePosition casePosition = new SourcePosition();
      start(casePosition);
      accept(Token.CASE);
      
      return caseAST;
  }
  
  Cases parseCaseLiteral() throws SyntaxError { //TODO :add case litera CASES
      Cases caseAST = null;
      
      SourcePosition casePosition = new SourcePosition();
      start(casePosition);
      
      switch (currentToken.kind) {    
            case Token.INTLITERAL: {    //TODO :Case Literal for IntLiteral
                acceptIt();
                IntegerLiteral ilAST = parseIntegerLiteral();
                finish(casePosition);
                caseAST = new IntegerCases(ilAST,casePosition);
            }					
            break;
            case Token.CHARLITERAL: {   //TODO :Case Literal for CharLiteral
                acceptIt();
                CharacterLiteral clAST = parseCharacterLiteral();
                finish(casePosition);
                caseAST = new CharacterCases(clAST,casePosition);
            break;
        }
      }
        return caseAST;
    }
        
   Cases parseCaseLiterals() throws SyntaxError{ //TODO :creating Case Literals
      Cases caseAST = null;
      SourcePosition casePosition = new SourcePosition();
      start(casePosition);
      
      caseAST = parseCaseLiteral();
      
      while (currentToken.kind == Token.PIPE) {
        Cases caseAST2 = parseCaseLiteral();
        caseAST = new CaseLiteralsCase(caseAST,caseAST2,casePosition); //TODO :add case literal case
      }
      return caseAST;
  }
   
   
    Cases elseCase() throws SyntaxError{ //TODO :add else case command
    Cases caseAST = null; //TODO :in case there's a syntactic error

    SourcePosition casePosition = new SourcePosition();
    start(casePosition);
    accept(Token.ELSE);
    Command cAST = parseCommand();
    finish(casePosition);
    caseAST = new ElseCase(cAST,casePosition);
    return caseAST;
    
    }
    
    Cases parseCaseCases() throws SyntaxError{ //TODO :add case cases  (single case)
    Cases caseAST = null; //TODO :in case there's a syntactic error

    SourcePosition casePosition = new SourcePosition();
    start(casePosition);
    accept(Token.CASE);
    Cases caseAST2 = parseCaseLiterals();
    accept(Token.THEN);
    Command cAST = parseCommand();
    finish(casePosition);
    caseAST = new CaseCases(caseAST2,cAST,casePosition);
    return caseAST;
    
    }
    
    Cases parseCasesCases() throws SyntaxError{   //add cases cases 
        Cases caseAST = null; //TODO :in case there's a syntactic error

        SourcePosition casePosition = new SourcePosition();
        start(casePosition);
       
            
        while(currentToken.kind == Token.CASE){ //TODO :for CASE token (Case+)
        acceptIt();
        accept(Token.THEN);
        Cases caseAST2 = parseCaseCases();
        finish(casePosition);
        caseAST = new CasesCases(caseAST,caseAST2,casePosition);
        
        }if (currentToken.kind == Token.ELSE){  //TODO :for ELSECASE situation [ElseCase]
        acceptIt();
        Cases ecAST = elseCase();
        finish(casePosition);
        caseAST = new CasesCases(caseAST,ecAST,casePosition);
        }
        return caseAST;
    }
    
///////////////////////////////////////////////////////////////////////////////
//
// COMMANDS
//
///////////////////////////////////////////////////////////////////////////////

// parseCommand parses the command, and constructs an AST
// to represent its phrase structure.
  
  Command parseCommand() throws SyntaxError {
    Command commandAST = null; // in case there's a syntactic error

    SourcePosition commandPos = new SourcePosition();

    start(commandPos);
    commandAST = parseSingleCommand();
    while (currentToken.kind == Token.SEMICOLON) {
      acceptIt();
      Command c2AST = parseSingleCommand();
      finish(commandPos);
      commandAST = new SequentialCommand(commandAST, c2AST, commandPos);
    }
    return commandAST;
  }

  Command parseSingleCommand() throws SyntaxError { //TODO :modify 
    Command commandAST = null; // in case there's a syntactic error  
    
    SourcePosition commandPos = new SourcePosition(); 
    start(commandPos); 

    switch (currentToken.kind) {

    case Token.IDENTIFIER:
      {
        Identifier iAST = parseIdentifier();
        if (currentToken.kind == Token.LPAREN) {
          acceptIt();
          ActualParameterSequence apsAST = parseActualParameterSequence();
          accept(Token.RPAREN);
          finish(commandPos);
          commandAST = new CallCommand(iAST, apsAST, commandPos);

        } else {

          Vname vAST = parseRestOfVname(iAST);
          accept(Token.BECOMES);
          Expression eAST = parseExpression();
          finish(commandPos);
          commandAST = new AssignCommand(vAST, eAST, commandPos);
        }
      }
      break;

    /*case Token.BEGIN: //TODO :remove case BEGIN token 
      acceptIt();
      commandAST = parseCommand();
      accept(Token.END);
      break;*/

    case Token.LET:     //TODO :Changing the LET Command
      {
        acceptIt();
        Declaration dAST = parseDeclaration();       
        accept(Token.IN);
        Command cAST = parseCommand();
        accept(Token.END);
        finish(commandPos);
        commandAST = new LetCommand(dAST, cAST, commandPos);
      }
      break;

    case Token.IF: //TODO :change if token, change singleCommand to Command
      {
        acceptIt();
        Expression eAST = parseExpression();
        accept(Token.THEN);
        Command c1AST = parseCommand();
        
        while (currentToken.kind == Token.ELSIF) { //TODO :add the ELSEIF token
            acceptIt();
            Expression eAST2 = parseExpression();
            accept(Token.THEN);
            Command cAST2 = parseCommand();
            finish(commandPos);
            commandAST = new ElseIfCommand(eAST,eAST2, cAST2, commandPos); //TODO :add else if command
          }
        
        accept(Token.ELSE);
        Command c2AST = parseCommand();
        accept(Token.END);
        finish(commandPos);
        commandAST = new IfCommand(eAST, c1AST, c2AST, commandPos);
      }
      break;
    
    case Token.REPEAT: //TODO :creating repeat token
      {
        acceptIt();
        switch (currentToken.kind){
            case Token.WHILE: //TODO :add the WHILE repeat
              {
                acceptIt();
                Expression eAST = parseExpression();
                accept(Token.DO);
                Command cAST = parseCommand(); //TODO :change single command to command
                accept(Token.END); //TODO :check the end token
                finish(commandPos);
                commandAST = new WhileCommand(eAST, cAST, commandPos);
              }
              break;
            case Token.UNTIL : //TODO :add the UNTIL token
              {
               acceptIt();
                Expression eAST = parseExpression();
                accept(Token.DO);
                Command cAST = parseCommand();
                accept(Token.END); 
                finish(commandPos);
                commandAST = new UntilCommand(eAST, cAST, commandPos); //TODO :add until command
              }
              break;
              case Token.DO : //TODO :add the DO token
              {
               acceptIt(); //TODO :first command and then an expression
                Command cAST = parseCommand();
                Expression eAST;
                switch (currentToken.kind){ //TODO :with two tokens UNTIL and WHILE
                    case Token.WHILE: //TODO :add the WHILE token
                    {
                        accept(Token.WHILE);
                        eAST = parseExpression();
                        accept(Token.END); //TODO :finish with END token
                        finish(commandPos);
                        commandAST = new DoWhileCommand(eAST, cAST, commandPos); 
                    }
                    break;
                    
                    case Token.UNTIL: //TODO :add the UNTIL token
                    {
                        accept(Token.UNTIL);
                        eAST = parseExpression();
                        accept(Token.END); //TODO :finish with END token
                        finish(commandPos);
                        commandAST = new DoUntilCommand(eAST, cAST, commandPos);
                    }
                    break;
                    default:   
                        syntacticError("\"%\" invalid syntax, spected \"while\", \"until\" token on",
                        currentToken.spelling); //TODO :Add an Syntactic Error if it don't find WHILE, or UNTIL
                     break;
                }
                
              } 
              break;
              case Token.FOR: // Modified to be ternary
            {
                acceptIt();
                Declaration dAST = parseForTernaryDeclaration();
                accept(Token.TO);
                Expression eAST = parseExpression();
                accept(Token.DO);
                acceptIt();
                Command cAST = parseCommand();
                accept(Token.END);
                finish(commandPos);
                commandAST = new ForCommand(dAST, eAST, cAST, commandPos);
                    }
              /*
              case Token.FOR: //TODO :add the FOR token
              {
                acceptIt();
                Identifier iAST = parseIdentifier();
                accept(Token.FROM);
                Expression eAST = parseExpression();
                accept(Token.TO); 
                Expression eAST2 = parseExpression();
                accept(Token.DO);
                Command cAST = parseCommand();
                accept(Token.END);
                finish(commandPos);
                commandAST = new ForCommand(iAST,eAST,eAST2,cAST,commandPos); //TODO :add for command
              }
              break;
              */
              default:   
              syntacticError("\"%\" invalid syntax, spected \"while\", \"until\", \"do\", \"for\" token on",
              currentToken.spelling); //TODO :Add an Syntactic Error if it don't find WHILE, UNTIL, DO or FOR
              
              break;
            }
        }
        break;
    
    case Token.SELECT: //TODO :add select  to Command 
    {
        acceptIt();
        Expression eAST = parseExpression();
        accept(Token.FROM);
        Cases caseAST = parseCasesCases();
        accept(Token.END);
        finish(commandPos);
        commandAST = new SelectCommand(eAST, caseAST, commandPos);
        
    }
    break;

    case Token.SEMICOLON:
    case Token.END:
    case Token.ELSE:
    case Token.IN:
    case Token.EOT:

      /*TODO :finish(commandPos); remove empty command
      commandAST = new EmptyCommand(commandPos);
      break;*/
      
    case Token.NIL:     //TODO :Creating the new emptyCommand with NIL
    {
        acceptIt();
        finish(commandPos);
        commandAST = new EmptyCommand(commandPos);
    }
    break;

    default:   
      syntacticError("\"%\" cannot start a command",
        currentToken.spelling);
      break;

    }

    return commandAST;
  }

///////////////////////////////////////////////////////////////////////////////
//
// EXPRESSIONS
//
///////////////////////////////////////////////////////////////////////////////

  Expression parseExpression() throws SyntaxError {
    Expression expressionAST = null; // in case there's a syntactic error

    SourcePosition expressionPos = new SourcePosition();

    start (expressionPos);

    switch (currentToken.kind) {

    case Token.LET:
      {
        acceptIt();
        Declaration dAST = parseDeclaration();
        accept(Token.IN);
        Expression eAST = parseExpression();
        finish(expressionPos);
        expressionAST = new LetExpression(dAST, eAST, expressionPos);
      }
      break;

    case Token.IF:
      {
        acceptIt();
        Expression e1AST = parseExpression();
        accept(Token.THEN);
        Expression e2AST = parseExpression();
        accept(Token.ELSE);
        Expression e3AST = parseExpression();
        finish(expressionPos);
        expressionAST = new IfExpression(e1AST, e2AST, e3AST, expressionPos);
      }
      break;

    default:
      expressionAST = parseSecondaryExpression();
      break;
    }
    return expressionAST;
  }

  Expression parseSecondaryExpression() throws SyntaxError {
    Expression expressionAST = null; // in case there's a syntactic error

    SourcePosition expressionPos = new SourcePosition();
    start(expressionPos);

    expressionAST = parsePrimaryExpression();
    while (currentToken.kind == Token.OPERATOR) {
      Operator opAST = parseOperator();
      Expression e2AST = parsePrimaryExpression();
      expressionAST = new BinaryExpression (expressionAST, opAST, e2AST,
        expressionPos);
    }
    return expressionAST;
  }

  Expression parsePrimaryExpression() throws SyntaxError {
    Expression expressionAST = null; // in case there's a syntactic error

    SourcePosition expressionPos = new SourcePosition();
    start(expressionPos);

    switch (currentToken.kind) {

    case Token.INTLITERAL:
      {
        IntegerLiteral ilAST = parseIntegerLiteral();
        finish(expressionPos);
        expressionAST = new IntegerExpression(ilAST, expressionPos);
      }
      break;

    case Token.CHARLITERAL:
      {
        CharacterLiteral clAST= parseCharacterLiteral();
        finish(expressionPos);
        expressionAST = new CharacterExpression(clAST, expressionPos);
      }
      break;

    case Token.LBRACKET:
      {
        acceptIt();
        ArrayAggregate aaAST = parseArrayAggregate();
        accept(Token.RBRACKET);
        finish(expressionPos);
        expressionAST = new ArrayExpression(aaAST, expressionPos);
      }
      break;

    case Token.LCURLY:
      {
        acceptIt();
        RecordAggregate raAST = parseRecordAggregate();
        accept(Token.RCURLY);
        finish(expressionPos);
        expressionAST = new RecordExpression(raAST, expressionPos);
      }
      break;

    case Token.IDENTIFIER:
      {
        Identifier iAST= parseIdentifier();
        if (currentToken.kind == Token.LPAREN) {
          acceptIt();
          ActualParameterSequence apsAST = parseActualParameterSequence();
          accept(Token.RPAREN);
          finish(expressionPos);
          expressionAST = new CallExpression(iAST, apsAST, expressionPos);

        } else {
          Vname vAST = parseRestOfVname(iAST);
          finish(expressionPos);
          expressionAST = new VnameExpression(vAST, expressionPos);
        }
      }
      break;

    case Token.OPERATOR:
      {
        Operator opAST = parseOperator();
        Expression eAST = parsePrimaryExpression();
        finish(expressionPos);
        expressionAST = new UnaryExpression(opAST, eAST, expressionPos);
      }
      break;

    case Token.LPAREN:
      acceptIt();
      expressionAST = parseExpression();
      accept(Token.RPAREN);
      break;

    default:
      syntacticError("\"%\" cannot start an expression",
        currentToken.spelling);
      break;

    }
    return expressionAST;
  }

  RecordAggregate parseRecordAggregate() throws SyntaxError {
    RecordAggregate aggregateAST = null; // in case there's a syntactic error

    SourcePosition aggregatePos = new SourcePosition();
    start(aggregatePos);

    Identifier iAST = parseIdentifier();
    accept(Token.IS);
    Expression eAST = parseExpression();

    if (currentToken.kind == Token.COMMA) {
      acceptIt();
      RecordAggregate aAST = parseRecordAggregate();
      finish(aggregatePos);
      aggregateAST = new MultipleRecordAggregate(iAST, eAST, aAST, aggregatePos);
    } else {
      finish(aggregatePos);
      aggregateAST = new SingleRecordAggregate(iAST, eAST, aggregatePos);
    }
    return aggregateAST;
  }

  ArrayAggregate parseArrayAggregate() throws SyntaxError {
    ArrayAggregate aggregateAST = null; // in case there's a syntactic error

    SourcePosition aggregatePos = new SourcePosition();
    start(aggregatePos);

    Expression eAST = parseExpression();
    if (currentToken.kind == Token.COMMA) {
      acceptIt();
      ArrayAggregate aAST = parseArrayAggregate();
      finish(aggregatePos);
      aggregateAST = new MultipleArrayAggregate(eAST, aAST, aggregatePos);
    } else {
      finish(aggregatePos);
      aggregateAST = new SingleArrayAggregate(eAST, aggregatePos);
    }
    return aggregateAST;
  }
  

///////////////////////////////////////////////////////////////////////////////
//
// VALUE-OR-VARIABLE NAMES
//
///////////////////////////////////////////////////////////////////////////////

  Vname parseVname () throws SyntaxError {
    Vname vnameAST = null; // in case there's a syntactic error
    Identifier iAST = parseIdentifier();
    vnameAST = parseRestOfVname(iAST);
    return vnameAST;
  }

  Vname parseRestOfVname(Identifier identifierAST) throws SyntaxError {
    SourcePosition vnamePos = new SourcePosition();
    vnamePos = identifierAST.position;
    Vname vAST = new SimpleVname(identifierAST, vnamePos);

    while (currentToken.kind == Token.DOT ||
           currentToken.kind == Token.LBRACKET) {

      if (currentToken.kind == Token.DOT) {
        acceptIt();
        Identifier iAST = parseIdentifier();
        vAST = new DotVname(vAST, iAST, vnamePos);
      } else {
        acceptIt();
        Expression eAST = parseExpression();
        accept(Token.RBRACKET);
        finish(vnamePos);
        vAST = new SubscriptVname(vAST, eAST, vnamePos);
      }
    }
    return vAST;
  }

///////////////////////////////////////////////////////////////////////////////
//
// DECLARATIONS
//
///////////////////////////////////////////////////////////////////////////////
  

  Declaration parseProcFunc() throws SyntaxError { //add proc funcs rule
        Declaration declarationAST = null;
        SourcePosition declarationPos = new SourcePosition();
        start(declarationPos);
        switch (currentToken.kind) {
            case Token.PROC: {
                acceptIt();
                Identifier iAST = parseIdentifier();
                accept(Token.LPAREN);
                FormalParameterSequence fpsAST = parseFormalParameterSequence();
                accept(Token.RPAREN);
                accept(Token.IS);
                Command cAST = parseCommand();
                accept(Token.END);
                finish(declarationPos);
                declarationAST = new ProcDeclaration(iAST, fpsAST, cAST, declarationPos);
            }					
            break;
            case Token.FUNC: {
                acceptIt();
                Identifier iAST = parseIdentifier();
                accept(Token.LPAREN);
                FormalParameterSequence fpsAST = parseFormalParameterSequence();
                accept(Token.RPAREN);
                accept(Token.COLON);
                TypeDenoter tAST = parseTypeDenoter();
                accept(Token.IS);
                Expression eAST = parseExpression();
                finish(declarationPos);
                declarationAST = new FuncDeclaration(iAST, fpsAST, tAST, eAST, declarationPos); 
            }						
            break;
        }
        return declarationAST;
    }
  Declaration parseForTernaryDeclaration() throws SyntaxError{ //Add ForTernaryDeclaration
      Declaration declarationAST = null; // in case there's a syntactic error
      SourcePosition declarationPos = new SourcePosition();
      start(declarationPos);
      accept(Token.VAR);
      Identifier iAST = parseIdentifier();
      accept(Token.FROM); //REVISAR BECOMES POR EL FROM
      Expression eAST = parseExpression();
      finish(declarationPos);
      declarationAST = new ForTernaryDeclaration(iAST, eAST, declarationPos);
      return declarationAST;
}

  Declaration parseProcFuncs() throws SyntaxError { //TODO :add proc funcs rule
        Declaration declarationAST = null;
        SourcePosition declarationPos = new SourcePosition();
        start(declarationPos);
        declarationAST = parseProcFunc();
        while (currentToken.kind == Token.PIPE){
            accept(Token.PIPE);
            Declaration dAST2 = parseProcFunc();
            finish(declarationPos);
            declarationAST = new ProcFuncDeclaration(declarationAST, dAST2, declarationPos); //add proc func declaration
        } 

        return declarationAST;
    }
  
  Declaration parseCompoundDeclaration() throws SyntaxError { //TODO :add the compound declaration
      Declaration declarationAST = null; // in case there's a syntactic error

      SourcePosition declarationPos = new SourcePosition();
      start(declarationPos);
      
      switch(currentToken.kind){
        case Token.RECURSIVE: {
              acceptIt();
              declarationAST = parseProcFuncs();
              accept(Token.END);
              finish(declarationPos);
              declarationAST = new RecursiveDeclaration(declarationAST, declarationPos); //TODO :add recursive declaration
          }
          break;

          case Token.LOCAL: 
          {
              acceptIt();
              Declaration dAST1 = parseDeclaration();
              accept(Token.IN);
              Declaration dAST2 = parseDeclaration();
              accept(Token.END);
              finish(declarationPos);
              declarationAST = new LocalDeclaration(dAST1, dAST2, declarationPos); //TODO :add local declaration
          }					
          break;

          default: {
              declarationAST = parseSingleDeclaration();
          }
          break;
      }
      return declarationAST;
  }

  Declaration parseDeclaration() throws SyntaxError { //TODO :modify parse declaration
    Declaration declarationAST = null; // in case there's a syntactic error

    SourcePosition declarationPos = new SourcePosition();
    start(declarationPos);
    declarationAST = parseCompoundDeclaration(); //TODO :modify single to compound
    if (currentToken.kind == Token.SEMICOLON) {
      acceptIt();
      Declaration dAST2 = parseCompoundDeclaration(); //TODO :modify single to compound
      finish(declarationPos);
      declarationAST = new CompoundDeclaration(declarationAST, dAST2,declarationPos); //TODO :add compound declaration
    }
    return declarationAST;
  }

  Declaration parseSingleDeclaration() throws SyntaxError {
    Declaration declarationAST = null; // in case there's a syntactic error

    SourcePosition declarationPos = new SourcePosition();
    start(declarationPos);

    switch (currentToken.kind) {

    case Token.CONST:
      {
        acceptIt();
        Identifier iAST = parseIdentifier();
        accept(Token.IS);
        Expression eAST = parseExpression();
        finish(declarationPos);
        declarationAST = new ConstDeclaration(iAST, eAST, declarationPos);
      }
      break;

    case Token.VAR: //TODO :modify var declaration
      {
        acceptIt();
        Identifier iAST = parseIdentifier();
        
        switch(currentToken.kind){
            
        case Token.COLON:
            acceptIt();
            TypeDenoter tAST = parseTypeDenoter();
            finish(declarationPos);
            declarationAST = new VarDeclaration(iAST, tAST, declarationPos);
            
        case Token.BECOMES: //TODO :add new case with becomes
            acceptIt();
            Expression eAST = parseExpression();
            finish(declarationPos);
            declarationAST = new VarIniDeclaration(iAST, eAST, declarationPos); 
        }
      }
      break;

    case Token.PROC:
      {
        acceptIt();
        Identifier iAST = parseIdentifier();
        accept(Token.LPAREN);
        FormalParameterSequence fpsAST = parseFormalParameterSequence();
        accept(Token.RPAREN);
        accept(Token.IS);
        Command cAST = parseCommand(); //TODO :modify of parseSingleCommand to parseCommand
        accept(Token.END);
        finish(declarationPos);
        declarationAST = new ProcDeclaration(iAST, fpsAST, cAST, declarationPos);
      }
      break;
      

    case Token.FUNC:
      {
        acceptIt();
        Identifier iAST = parseIdentifier();
        accept(Token.LPAREN);
        FormalParameterSequence fpsAST = parseFormalParameterSequence();
        accept(Token.RPAREN);
        accept(Token.COLON);
        TypeDenoter tAST = parseTypeDenoter();
        accept(Token.IS);
        Expression eAST = parseExpression();
        finish(declarationPos);
        declarationAST = new FuncDeclaration(iAST, fpsAST, tAST, eAST,
          declarationPos);
      }
      break;

    case Token.TYPE:
      {
        acceptIt();
        Identifier iAST = parseIdentifier();
        accept(Token.IS);
        TypeDenoter tAST = parseTypeDenoter();
        finish(declarationPos);
        declarationAST = new TypeDeclaration(iAST, tAST, declarationPos);
      }
      break;

    default:
      syntacticError("\"%\" cannot start a declaration",
        currentToken.spelling);
      break;

    }
    return declarationAST;
  }

///////////////////////////////////////////////////////////////////////////////
//
// PARAMETERS
//
///////////////////////////////////////////////////////////////////////////////

  FormalParameterSequence parseFormalParameterSequence() throws SyntaxError {
    FormalParameterSequence formalsAST;

    SourcePosition formalsPos = new SourcePosition();

    start(formalsPos);
    if (currentToken.kind == Token.RPAREN) {
      finish(formalsPos);
      formalsAST = new EmptyFormalParameterSequence(formalsPos);

    } else {
      formalsAST = parseProperFormalParameterSequence();
    }
    return formalsAST;
  }

  FormalParameterSequence parseProperFormalParameterSequence() throws SyntaxError {
    FormalParameterSequence formalsAST = null; // in case there's a syntactic error;

    SourcePosition formalsPos = new SourcePosition();
    start(formalsPos);
    FormalParameter fpAST = parseFormalParameter();
    if (currentToken.kind == Token.COMMA) {
      acceptIt();
      FormalParameterSequence fpsAST = parseProperFormalParameterSequence();
      finish(formalsPos);
      formalsAST = new MultipleFormalParameterSequence(fpAST, fpsAST,
        formalsPos);

    } else {
      finish(formalsPos);
      formalsAST = new SingleFormalParameterSequence(fpAST, formalsPos);
    }
    return formalsAST;
  }

  FormalParameter parseFormalParameter() throws SyntaxError {
    FormalParameter formalAST = null; // in case there's a syntactic error;

    SourcePosition formalPos = new SourcePosition();
    start(formalPos);

    switch (currentToken.kind) {

    case Token.IDENTIFIER:
      {
        Identifier iAST = parseIdentifier();
        accept(Token.COLON);
        TypeDenoter tAST = parseTypeDenoter();
        finish(formalPos);
        formalAST = new ConstFormalParameter(iAST, tAST, formalPos);
      }
      break;

    case Token.VAR:
      {
        acceptIt();
        Identifier iAST = parseIdentifier();
        accept(Token.COLON);
        TypeDenoter tAST = parseTypeDenoter();
        finish(formalPos);
        formalAST = new VarFormalParameter(iAST, tAST, formalPos);
      }
      break;

    case Token.PROC:
      {
        acceptIt();
        Identifier iAST = parseIdentifier();
        accept(Token.LPAREN);
        FormalParameterSequence fpsAST = parseFormalParameterSequence();
        accept(Token.RPAREN);
        finish(formalPos);
        formalAST = new ProcFormalParameter(iAST, fpsAST, formalPos);
      }
      break;

    case Token.FUNC:
      {
        acceptIt();
        Identifier iAST = parseIdentifier();
        accept(Token.LPAREN);
        FormalParameterSequence fpsAST = parseFormalParameterSequence();
        accept(Token.RPAREN);
        accept(Token.COLON);
        TypeDenoter tAST = parseTypeDenoter();
        finish(formalPos);
        formalAST = new FuncFormalParameter(iAST, fpsAST, tAST, formalPos);
      }
      break;

    default:
      syntacticError("\"%\" cannot start a formal parameter",
        currentToken.spelling);
      break;

    }
    return formalAST;
  }


  ActualParameterSequence parseActualParameterSequence() throws SyntaxError {
    ActualParameterSequence actualsAST;

    SourcePosition actualsPos = new SourcePosition();

    start(actualsPos);
    if (currentToken.kind == Token.RPAREN) {
      finish(actualsPos);
      actualsAST = new EmptyActualParameterSequence(actualsPos);

    } else {
      actualsAST = parseProperActualParameterSequence();
    }
    return actualsAST;
  }

  ActualParameterSequence parseProperActualParameterSequence() throws SyntaxError {
    ActualParameterSequence actualsAST = null; // in case there's a syntactic error

    SourcePosition actualsPos = new SourcePosition();

    start(actualsPos);
    ActualParameter apAST = parseActualParameter();
    if (currentToken.kind == Token.COMMA) {
      acceptIt();
      ActualParameterSequence apsAST = parseProperActualParameterSequence();
      finish(actualsPos);
      actualsAST = new MultipleActualParameterSequence(apAST, apsAST,
        actualsPos);
    } else {
      finish(actualsPos);
      actualsAST = new SingleActualParameterSequence(apAST, actualsPos);
    }
    return actualsAST;
  }

  ActualParameter parseActualParameter() throws SyntaxError {
    ActualParameter actualAST = null; // in case there's a syntactic error

    SourcePosition actualPos = new SourcePosition();

    start(actualPos);

    switch (currentToken.kind) {

    case Token.IDENTIFIER:
    case Token.INTLITERAL:
    case Token.CHARLITERAL:
    case Token.OPERATOR:
    case Token.LET:
    case Token.IF:
    case Token.LPAREN:
    case Token.LBRACKET:
    case Token.LCURLY:
      {
        Expression eAST = parseExpression();
        finish(actualPos);
        actualAST = new ConstActualParameter(eAST, actualPos);
      }
      break;

    case Token.VAR:
      {
        acceptIt();
        Vname vAST = parseVname();
        finish(actualPos);
        actualAST = new VarActualParameter(vAST, actualPos);
      }
      break;

    case Token.PROC:
      {
        acceptIt();
        Identifier iAST = parseIdentifier();
        finish(actualPos);
        actualAST = new ProcActualParameter(iAST, actualPos);
      }
      break;

    case Token.FUNC:
      {
        acceptIt();
        Identifier iAST = parseIdentifier();
        finish(actualPos);
        actualAST = new FuncActualParameter(iAST, actualPos);
      }
      break;

    default:
      syntacticError("\"%\" cannot start an actual parameter",
        currentToken.spelling);
      break;

    }
    return actualAST;
  }

///////////////////////////////////////////////////////////////////////////////
//
// TYPE-DENOTERS
//
///////////////////////////////////////////////////////////////////////////////

  TypeDenoter parseTypeDenoter() throws SyntaxError {
    TypeDenoter typeAST = null; // in case there's a syntactic error
    SourcePosition typePos = new SourcePosition();

    start(typePos);

    switch (currentToken.kind) {

    case Token.IDENTIFIER:
      {
        Identifier iAST = parseIdentifier();
        finish(typePos);
        typeAST = new SimpleTypeDenoter(iAST, typePos);
      }
      break;

    
    case Token.ARRAY: {//modify arrary token, add dotdot
        acceptIt();
        IntegerLiteral ilAST = parseIntegerLiteral();
        if (currentToken.kind == Token.LBRACKET) {
            accept(Token.DOTDOT);
            IntegerLiteral ilASTAux = parseIntegerLiteral();
            accept(Token.RBRACKET);
            accept(Token.OF);
            TypeDenoter tASTAux = parseTypeDenoter();
            finish(typePos);
            typeAST = new ArrayTypeDenoterAux(ilAST, ilASTAux, tASTAux, typePos);
        } else {
            accept(Token.OF);
            TypeDenoter tAST = parseTypeDenoter();
            finish(typePos);
            typeAST = new ArrayTypeDenoter(ilAST, tAST, typePos);
        }

    }
    break;

    case Token.RECORD:
      {
        acceptIt();
        FieldTypeDenoter fAST = parseFieldTypeDenoter();
        accept(Token.END);
        finish(typePos);
        typeAST = new RecordTypeDenoter(fAST, typePos);
      }
      break;

    default:
      syntacticError("\"%\" cannot start a type denoter",
        currentToken.spelling);
      break;

    }
    return typeAST;
  }

  FieldTypeDenoter parseFieldTypeDenoter() throws SyntaxError {
    FieldTypeDenoter fieldAST = null; // in case there's a syntactic error

    SourcePosition fieldPos = new SourcePosition();

    start(fieldPos);
    Identifier iAST = parseIdentifier();
    accept(Token.COLON);
    TypeDenoter tAST = parseTypeDenoter();
    if (currentToken.kind == Token.COMMA) {
      acceptIt();
      FieldTypeDenoter fAST = parseFieldTypeDenoter();
      finish(fieldPos);
      fieldAST = new MultipleFieldTypeDenoter(iAST, tAST, fAST, fieldPos);
    } else {
      finish(fieldPos);
      fieldAST = new SingleFieldTypeDenoter(iAST, tAST, fieldPos);
    }
    return fieldAST;
  }
}
