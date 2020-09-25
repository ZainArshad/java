/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lex;

/**
 *
 * @author z
 */
public final class Token {

    public enum Type {
        // Single-character tokens.
  

  // Literals.
  IDENTIFIER, STRING, NUMBER, OPERATOR

  // Keywords.
  
    }

    private final Type type;
    private final Object literal;
    private final String lexeme;
    private final int index;

    public Token(Type type, String literal, int index) {
        this.type = type;
        this.literal = literal;
        this.index = index;
        this.lexeme=null;
    }
    
    Token(Type type, String lexeme, Object literal, int line) {
    this.type = type;
    this.lexeme = lexeme;
    this.literal = literal;
    this.index = line;
  }

    public Type getType() {
        return type;
    }

    public Object getLiteral() {
        return literal;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Token && type == ((Token) obj).type
                && literal.equals(((Token) obj).literal)
                && index == ((Token) obj).index;
    }

    @Override
    public String toString() {
        return type + "=`" + literal + "`@" + index;
    }

}
