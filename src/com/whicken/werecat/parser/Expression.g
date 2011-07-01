grammar Expression;

tokens {
    PLUS = '+' ;
    MINUS= '-' ;
    MOD= '%' ;
    MULT= '*' ;
    DIV= '/' ;
    OPEN= '(' ;
    CLOSE= ')' ;
}

@header {
package com.whicken.werecat.parser;

import com.whicken.werecat.*;
import com.whicken.werecat.expr.*;
}

@lexer::header {
package com.whicken.werecat.parser;

import com.whicken.werecat.*;
import com.whicken.werecat.expr.*;
}

@members {
    RuleFactory factory;
    public static Expression parse(String text, RuleFactory factory)
        throws Exception
    {
        ExpressionLexer lex = new ExpressionLexer(new ANTLRStringStream(text));
        CommonTokenStream tokens = new CommonTokenStream(lex);
        ExpressionParser parser = new ExpressionParser(tokens);
        parser.factory = factory;
        return parser.top();
    }
    public static void main(String[] args) throws Exception {
        ExpressionLexer lex = new ExpressionLexer(new ANTLRFileStream(args[0]));
        CommonTokenStream tokens = new CommonTokenStream(lex);

        ExpressionParser parser = new ExpressionParser(tokens);

        try {
            Expression e = parser.top();
            System.out.println("Got: "+e);
            double d = e.asDouble(null);
            System.out.println("="+d);
        } catch (RecognitionException e)  {
            e.printStackTrace();
        }
}
}

/*------------------------------------------------------------------
 * PARSER RULES
 *------------------------------------------------------------------*/

// Top of the tree

top returns [Expression value]
    : v=expr { $value = $v.value; }
    ;

expr returns [Expression value]
    : v=orExpr { $value = $v.value; }
    ;

parExpr returns [Expression value]
    : '(' v=expr { $value = $v.value; } ')'
    ;

/* TODO */
arguments
    : '(' (exprList )? ')'
    ;

exprList returns [Expression value]
    : lhs=expr { $value = $lhs.value; }
        ( ',' rhs=expr { /* TODO */ } )*
    ;

orExpr returns [Expression value]
    :   lhs=andExpr { $value = $lhs.value; }
        (
          '||' rhs=andExpr { $value = new OrExpression($value, $rhs.value); }
        | OR rhs=andExpr { $value = new OrExpression($value, $rhs.value); }
        )*
    ;

andExpr returns [Expression value]
    :   lhs=eqExpr { $value = $lhs.value; }
        (
          '&&' rhs=eqExpr { $value = new AndExpression($value, $rhs.value); }
        | AND rhs=eqExpr { $value = new AndExpression($value, $rhs.value); }
        )*
    ;


eqExpr returns [Expression value]
    : lhs=relExpr { $value = $lhs.value; } 
      ( '=' rhs=relExpr { $value=new EqualExpression($value, $rhs.value); }
      | '==' rhs=relExpr { $value=new EqualExpression($value, $rhs.value); }
      | '!=' rhs=relExpr { $value=new NEExpression($value, $rhs.value); }
      | '=~' rhs=relExpr { $value=new RegexpMatchExpression($value, $rhs.value); }
      | '!~' rhs=relExpr { $value=new RegexpNoMatchExpression($value, $rhs.value); }
      )*
    ;

relExpr returns [Expression value]
    : lhs=addExpr { $value = $lhs.value; } 
      ( '<' rhs=addExpr { $value=new LTExpression($value, $rhs.value); }
      | '<=' rhs=addExpr { $value=new LEExpression($value, $rhs.value); }
      | '>' rhs=addExpr { $value=new GTExpression($value, $rhs.value); }
      | '>=' rhs=addExpr { $value=new GEExpression($value, $rhs.value); }
      )*
    ;

addExpr returns [Expression value]
    : lhs=mulExpr { $value = $lhs.value; } 
      ( PLUS rhs=mulExpr { $value=new PlusExpression($value, $rhs.value); }
      | MINUS rhs=mulExpr { $value=new MinusExpression($value, $rhs.value); }
      )*
    ;

mulExpr returns [Expression value]
    : lhs=unaryExpr { $value = $lhs.value; }
      ( MULT rhs=unaryExpr { $value = new MultExpression($value, $rhs.value); }
      | DIV rhs=unaryExpr { $value = new DivExpression($value, $rhs.value); }
      | MOD rhs=unaryExpr { $value = new ModExpression($value, $rhs.value); }
      )*
    ;

unaryExpr returns [Expression value]
    :
    '+' v=unaryExpr { $value = $v.value; }
    | '-' v=unaryExpr { $value = new NegateExpression($v.value); }
    | v=unaryExprOther { $value = $v.value; }
    ;

unaryExprOther returns [Expression value]
    :
    '!' v=unaryExpr { $value = new NotExpression($v.value); }
    | v=factor { $value = $v.value; }
        (rhs=selector { $value = factory.createCompoundExpression($value, $rhs.value); } )*
    ;

factor returns [Expression value]
    : n=NUMBER { $value = new SimpleConstant(new Integer($n.text)); }
    | n2=method { $value = $n2.value; }
    | n2=value { $value = $n2.value; }
    | n=STRINGLITERAL { $value = new SimpleConstant($n.text); }
    | n=CHARLITERAL { $value = new SimpleConstant(new Character($n.text.charAt(1))); }
    | n=REGEXPLITERAL { $value = new RegexpConstant($n.text); }
    | n2=parExpr { $value = $n2.value; }
    | TRUE { $value = new SimpleConstant(Boolean.TRUE); }
    | FALSE { $value = new SimpleConstant(Boolean.FALSE); }
    | NULL { $value = new SimpleConstant(null); }
    ;

method returns [Expression value]
    : n=IDENTIFIER OPEN CLOSE { $value = factory.createMethod($n.text);
                                if ($value == null) throw new RuntimeException("Unknown method: "+$n.text); }
    ;


value returns [Expression value]
    : n=IDENTIFIER { $value = factory.createExpression($n.text);
        if ($value == null) throw new RuntimeException("Unknown value: "+$n.text); }
    ;

selector returns [Object value]
    : '.' n=IDENTIFIER { $value = $n.text; }
    | '[' e=expr ']' { $value = $e.value; }
    ;


/*------------------------------------------------------------------
 * LEXER RULES
 *------------------------------------------------------------------*/

AND: 'and';

OR: 'or';

TRUE: 'true';

FALSE: 'false';

NULL: 'null';

IDENTIFIER: IdentifierStart IdentifierPart* ;

NUMBER: (DIGIT)+ ;

WHITESPACE : ( '\t' | ' ' | '\r' | '\n'| '\u000C' )+ { $channel = HIDDEN; } ;

CHARLITERAL
    :   '\''
        (   EscapeSequence
        |   ~( '\'' | '\\' | '\r' | '\n' )
        )
        '\''
    ;

STRINGLITERAL
    :   '"'
        (   EscapeSequence
        |   ~( '\\' | '"' | '\r' | '\n' )
        )*
        '"'
    ;

REGEXPLITERAL
    :   '/'
        (   EscapeSequence
        |   ~( '\\' | '"' | '\r' | '\n' )
        )*
        '/'
    ;


fragment
EscapeSequence
    :   '\\' (
                 'b'
        |   't'
        |   'n'
        |   'f'
        |   'r'
        |   '\"'
        |   '\''
        |   '\\'
        |
            ('0'..'3') ('0'..'7') ('0'..'7')
        |
            ('0'..'7') ('0'..'7')
        |
            ('0'..'7')
        )
    ;


fragment DIGIT: '0'..'9' ;

fragment IdentifierStart: 'a'..'z' | '_' | 'A'..'Z';

fragment IdentifierPart: 'a'..'z' | '_' | 'A'..'Z' | '0'..'9';
