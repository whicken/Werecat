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

arguments returns [List<Expression> value]
    : '(' (n=exprList { $value = $n.value; } )? ')'
    ;

exprList returns [List<Expression> value]
    : lhs=expr {
         $value = new ArrayList<Expression>();
         $value.add($lhs.value); }
        ( ',' rhs=expr { $value.add($rhs.value); } )*
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
    : n=INTLITERAL { $value = new SimpleConstant(new Integer($n.text)); }
    | n=LONGLITERAL { $value = new SimpleConstant(new Long($n.text)); }
    | n=FLOATLITERAL { $value = new SimpleConstant(new Float($n.text)); }
    | n=DOUBLELITERAL { $value = new SimpleConstant(new Double($n.text)); }
    | n2=reference { $value = $n2.value; }
    | n=STRINGLITERAL { $value = new SimpleConstant($n.text.substring(1, $n.text.length()-1)); }
    | n=CHARLITERAL { $value = new SimpleConstant(new Character($n.text.charAt(1))); }
    | n=REGEXPLITERAL { $value = new RegexpConstant($n.text); }
    | n2=parExpr { $value = $n2.value; }
    | TRUE { $value = new SimpleConstant(Boolean.TRUE); }
    | FALSE { $value = new SimpleConstant(Boolean.FALSE); }
    | NULL { $value = new SimpleConstant(null); }
    ;

reference returns [Expression value]
    : n=IDENTIFIER { boolean field = true; }
      ( a=arguments { field = false; } )? 
    { 
        if (field) {
            $value = factory.createField($n.text);
            if ($value == null) throw new RuntimeException("Unknown field: "+$n.text); 
        } else {
            $value = factory.createMethod($n.text, $a.value);
            if ($value == null) throw new RuntimeException("Unknown method: "+$n.text); 
        }
    }
    ;

selector returns [Object value]
    : '.' n=IDENTIFIER { boolean field = true; }
            ( a=arguments { field = false; } )?
        {
            if (field) {
                $value = $n.text; 
            } else {
                $value = new IdentifierCall($n.text, $a.value);
            }
        }
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

LONGLITERAL
    :   IntegerNumber LongSuffix
    ;

INTLITERAL
    :   IntegerNumber 
    ;
    
fragment
IntegerNumber
    :   '0' 
    |   '1'..'9' ('0'..'9')*    
    ;

fragment
LongSuffix
    :   'l' | 'L'
    ;

fragment
NonIntegerNumber
    :   ('0' .. '9')+ '.' ('0' .. '9')* Exponent?  
    |   '.' ( '0' .. '9' )+ Exponent?  
    |   ('0' .. '9')+ Exponent  
    |   ('0' .. '9')+ 
    ;
        
fragment 
Exponent    
    :   ( 'e' | 'E' ) ( '+' | '-' )? ( '0' .. '9' )+ 
    ;
    
fragment 
FloatSuffix
    :   'f' | 'F' 
    ;     

fragment
DoubleSuffix
    :   'd' | 'D'
    ;
        
FLOATLITERAL
    :   NonIntegerNumber FloatSuffix
    ;
    
DOUBLELITERAL
    :   NonIntegerNumber DoubleSuffix?
    ;

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


fragment IdentifierStart: 'a'..'z' | '_' | 'A'..'Z';

fragment IdentifierPart: 'a'..'z' | '_' | 'A'..'Z' | '0'..'9';
