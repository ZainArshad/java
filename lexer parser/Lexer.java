/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lex;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lex.Token;
import lex.Token.Type;



/**
 * The lexer works through three main functions:
 *
 *  - {@link #lex()}, which repeatedly calls lexToken() and skips whitespace
 *  - {@link #lexToken()}, which lexes the next token
 *  - {@link CharStream}, which manages the state of the lexer and literals
 *
 * If the lexer fails to parse something (such as an unterminated string) you
 * should throw a {@link ParseException}.
 *
 * The {@link #peek(String...)} and {@link #match(String...)} functions are
 * helpers, they're not necessary but their use will make the implementation a
 * lot easier. Regex isn't the most performant way to go but it gets the job
 * done, and the focus here is on the concept.
 */
public final class Lexer {

    final CharStream chars;
    private final String source;
    private final List<Token> tokens = new ArrayList<>();
    public int start = 0;
    private int current = 0;
    private int line = 1;
    
    private static final Map<String, Type> keywords;

  static {
    keywords = new HashMap<>();
    keywords.put("and",    Token.Type.IDENTIFIER);
    keywords.put("class",  Token.Type.IDENTIFIER);
    keywords.put("else",   Token.Type.IDENTIFIER);
    keywords.put("false",  Token.Type.IDENTIFIER);
    keywords.put("for",    Token.Type.IDENTIFIER);
    keywords.put("fun",    Token.Type.IDENTIFIER);
    keywords.put("if",     Token.Type.IDENTIFIER);
    keywords.put("nil",    Token.Type.IDENTIFIER);
    keywords.put("or",     Token.Type.IDENTIFIER);
    keywords.put("print",  Token.Type.IDENTIFIER);
    keywords.put("return", Token.Type.IDENTIFIER);
    keywords.put("super",  Token.Type.IDENTIFIER);
    keywords.put("this",   Token.Type.IDENTIFIER);
    keywords.put("true",   Token.Type.IDENTIFIER);
    keywords.put("var",    Token.Type.IDENTIFIER);
    keywords.put("while",  Token.Type.IDENTIFIER);
  }

    Lexer(String input) {
        chars = new CharStream(input);
        source=input;
    }

    /**
     * Lexes the input and returns the list of tokens.
     */
    public  List<Token> lex(String input) throws ParseException {
        return scanTokens();
        //return new Lexer(input).lex();
    }
    
    private boolean isAtEnd() {
    return current >= source.length();
  }
  
    
    private char advance() {
    current++;
    
    return source.charAt(current - 1);
  }
    
  private void addToken(Type type, Object literal) {
    String text = source.substring(start, current);
    tokens.add(new Token(type, text, literal, current));
  }
    
   private void addToken(Type type) {
    addToken(type, null);
  }
   
   private char peek() {
    if (isAtEnd()) return '\0';
    return source.charAt(current);
  }
   
  private char peekNext() {
    if (current + 1 >= source.length()) return '\0';
    return source.charAt(current + 1);
  }
  
  private boolean isAlpha(char c) {
    return (c >= 'a' && c <= 'z') ||
           (c >= 'A' && c <= 'Z') ||
            c == '_';
  }

  private boolean isAlphaNumeric(char c) {
    return isAlpha(c) || isDigit(c);
  }
   
  private void string() {
    while (peek() != '"' && !isAtEnd()) {
      if (peek() == '\n') line++;
      advance();
    }

    // Unterminated string.
    if (isAtEnd()) {
      //Lox.error(line, "Unterminated string.");
      return;
    }

    // The closing ".
    advance();

    // Trim the surrounding quotes.
    String value = source.substring(start + 1, current - 1);
    addToken(Token.Type.STRING, value);
  }
  
  private boolean isDigit(char c) {
    return c >= '0' && c <= '9';
  } 
   
   private void scanToken() {
    char c = advance();
    switch (c) {
        default:
        if (isDigit(c)) {
          number();
        }
        else if (isAlpha(c)) {
          identifier();
        }
        else{
        ParseException.error(line, "Unexpected character.");
        System.out.println("Wrong input! error");
        }
        break;
        case ' ':
      case '\r':
      case '\t':
        // Ignore whitespace.
        break;
      case '(': addToken(Token.Type.OPERATOR,source.substring(start, current)); break;
      case '\'': addToken(Token.Type.OPERATOR,source.substring(start, current)); break;
      case ')': addToken(Token.Type.OPERATOR,source.substring(start, current)); break;
      case '{': addToken(Token.Type.OPERATOR,source.substring(start, current)); break;
      case '}': addToken(Token.Type.OPERATOR,source.substring(start, current)); break;
      case '[': addToken(Token.Type.OPERATOR,source.substring(start, current)); break;
      case ']': addToken(Token.Type.OPERATOR,source.substring(start, current)); break;
      case ',': addToken(Token.Type.OPERATOR,source.substring(start, current)); break;
      case '.': addToken(Token.Type.OPERATOR,source.substring(start, current)); break;
      case '-': addToken(Token.Type.IDENTIFIER,source.substring(start, current)); break;
      case '+': addToken(Token.Type.IDENTIFIER,source.substring(start, current)); break;
      case ';': addToken(Token.Type.OPERATOR,source.substring(start, current)); break;
      case '*': addToken(Token.Type.IDENTIFIER,source.substring(start, current)); break; 
      case ':': addToken(Token.Type.OPERATOR,source.substring(start, current)); break;
       case '/':
        if (match('/')) {
          // A comment goes until the end of the line.
          while (peek() != '\n' && !isAtEnd()) advance();
        } else {
          addToken(Token.Type.OPERATOR);
        }
        break;
       case '\n':
        line++;
        break;
        case '!': addToken((match('=') ? Token.Type.IDENTIFIER : Token.Type.IDENTIFIER),source.substring(start, current)); break;
        case '&': addToken((match('&') ? Token.Type.IDENTIFIER : Token.Type.IDENTIFIER),source.substring(start, current)); break;
        case '|': addToken((match('|') ? Token.Type.IDENTIFIER : Token.Type.IDENTIFIER),source.substring(start, current)); break;
      case '=': addToken((match('=') ? Token.Type.IDENTIFIER : Token.Type.IDENTIFIER),source.substring(start, current)); break;
      case '<': addToken((match('=') ? Token.Type.IDENTIFIER : Token.Type.IDENTIFIER),source.substring(start, current)); break;
      case '>': addToken((match('=') ? Token.Type.IDENTIFIER : Token.Type.IDENTIFIER),source.substring(start, current)); break;
      case '"': lexString(); break;

      
     
       
       
    }
  }
   
   private void identifier() {
    while (isAlphaNumeric(peek())) advance();
String text = source.substring(start, current);

    Type type = keywords.get(text);
    if (type == null) type = Token.Type.IDENTIFIER;
    addToken(type,source.substring(start, current));
  }
    
   private void number() {
    while (isDigit(peek())) advance();

    // Look for a fractional part.
    if (peek() == '.' && isDigit(peekNext())) {
      // Consume the "."
      advance();

      while (isDigit(peek())) advance();
    }

    addToken(Token.Type.NUMBER,
        Double.parseDouble(source.substring(start, current)));
  }
   
   
    List<Token> scanTokens() {
    while (!isAtEnd()) {
      // We are at the beginning of the next lexeme.
      start = current;
      scanToken();
    }

    //tokens.add(new Token(Token.Type.EOF, "", null, line));
   // for (int counter = 0; counter < tokens.size(); counter++) { 		      
   //       System.out.println(tokens.get(counter)); 
  //  }
    return tokens;
    
  }

    /**
     * Repeatedly lexes the next token using {@link #lexToken()} until the end
     * of the input is reached, returning the list of tokens lexed. This should
     * also handle skipping whitespace.
     */
    List<Token> lex() throws ParseException {
        throw new UnsupportedOperationException(); //TODO
    }

    /**
     * Lexes the next token. It may be helpful to have this call other methods,
     * such as {@code lexIdentifier()} or {@code lexNumber()}, based on the next
     * character(s).
     *
     * Additionally, here is an example of lexing a character literal (not used
     * in this assignment) using the peek/match methods below.
     *
     * <pre>
     * {@code
     *     Token lexCharacter() {
     *         if (!match("\'")) {
     *             //Your lexer should prevent this from happening, as it should
     *             // only try to lex a character literal if the next character
     *             // begins a character literal.
     *             //Additionally, the index being passed back is a 'ballpark'
     *             // value. If we were doing proper diagnostics, we would want
     *             // to provide a range covering the entire error. It's really
     *             // only for debugging / proof of concept.
     *             throw new ParseException("Next character does not begin a character literal.", chars.index);
     *         }
     *         if (!chars.has(0) || match("\'")) {
     *             throw new ParseException("Empty character literal.",  chars.index);
     *         } else if (match("\\")) {
     *             //lex escape characters...
     *         } else {
     *             chars.advance();
     *         }
     *         if (!match("\'")) {
     *             throw new ParseException("Unterminated character literal.", chars.index);
     *         }
     *         return chars.emit(Token.Type.CHARACTER);
     *     }
     * }
     * </pre>
     */
    Token lexToken() throws ParseException {
        scanToken();
        return null;
        //throw new UnsupportedOperationException(); //TODO
    }

    Token lexIdentifier(){
        return null;
        //throw new UnsupportedOperationException(); //TODO
    }

    Token lexNumber() {
        number();
        //throw new UnsupportedOperationException(); //TODO
        return null;
    }

    Token lexString() throws ParseException {
        string();
        return null;
    }

    /**
     * Returns true if the next sequence of characters match the given patterns,
     * which should be a regex. For example, {@code peek("a", "b", "c")} would
     * return true for the sequence {@code 'a', 'b', 'c'}
     */
    boolean peek(String... patterns) {
        throw new UnsupportedOperationException(); //TODO
    }

    /**
     * Returns true in the same way as peek, but also advances the CharStream to
     * if the characters matched.
     */
    boolean match(char expected) {
        if (isAtEnd()) return false;
    if (source.charAt(current) != expected) return false;

    current++;
    return true;
    }

    /**
     * This is basically a sequence of characters. The index is used to maintain
     * where in the input string the lexer currently is, and the builder
     * accumulates characters into the literal value for the next token.
     */
    static final class CharStream {

        final String input;
        int index = 0;
        int length = 0;

        CharStream(String input) {
            this.input = input;
           
        }

        /**
         * Returns true if there is a character at index + offset.
         */
        boolean has(int offset) {
            if(offset<input.length())
            {
                return true;
            }
            else
                return false;
            //throw new UnsupportedOperationException(); //TODO
        }

        /**
         * Gets the character at index + offset.
         */
        char get(int offset) {
            return input.charAt(offset);
        }

        /**
         * Advances to the next character, incrementing the current index and
         * length of the literal being built.
         */
        void advance() {
            index++;
            //hrow new UnsupportedOperationException(); //TODO
        }

        /**
         * Resets the length to zero, skipping any consumed characters.
         */
        void reset() {
            index=0;
            length=0;
            //throw new UnsupportedOperationException(); //TODO
        }

        /**
         * Returns a token of the given type with the built literal and resets
         * the length to zero. The index of the token should be the
         * <em>starting</em> index.
         */
        Token emit(Token.Type type) {
            return null;
            //throw new UnsupportedOperationException(); //TODO
        }
        
        
        

    }

}
