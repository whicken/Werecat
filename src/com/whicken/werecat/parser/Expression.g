grammar Expression;

tokens {
    PLUS = '+' ;
    MINUS= '-' ;
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
        return parser.expr();
    }
    public static void main(String[] args) throws Exception {
        ExpressionLexer lex = new ExpressionLexer(new ANTLRFileStream(args[0]));
        CommonTokenStream tokens = new CommonTokenStream(lex);

        ExpressionParser parser = new ExpressionParser(tokens);

        try {
            Expression e = parser.expr();
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

expr returns [Expression value]
    : v=orExpr { $value = $v.value; }
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
    : lhs=factor { $value = $lhs.value; }
      ( MULT rhs=factor { $value = new MultExpression($value, $rhs.value); }
      | DIV rhs=factor { $value = new DivExpression($value, $rhs.value); }
      )*
    ;

factor returns [Expression value]
    : n=NUMBER { $value = new NumberConstant(Double.parseDouble($n.text)); }
    | n2=method { $value = $n2.value; }
    | n3=value { $value = $n3.value; }
    | n4=STRINGLITERAL { $value = new StringConstant($n4.text); }
    | n5=CHARLITERAL { $value = new NumberConstant($n5.text.charAt(1)); }
    | n6=REGEXPLITERAL { $value = new RegexpConstant($n6.text); }
    | TRUE { $value = new NumberConstant(1); }
    | FALSE { $value = new NumberConstant(0); }
    | NULL { $value = new StringConstant(null); }
    ;

method returns [Expression value]
    : n=IDENTIFIER OPEN CLOSE { $value = factory.createMethod($n.text);
                                if ($value == null) throw new RuntimeException("Unknown method: "+$n.text); }
    ;



value returns [Expression value]
    : n=IDENTIFIER { $value = factory.createExpression($n.text);
        if ($value == null) throw new RuntimeException("Unknown value: "+$n.text); }
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
