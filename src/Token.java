
public class Token {
    private TokenType type;
    private String lexeme;
    private int line;
    private int column;

    public Token(TokenType type, String lexeme, int line, int column) {
        this.type = type;
        this.lexeme = lexeme;
        this.line = line;
        this.column = column;
    }

    public void PrintToken() {
        System.out.print("<" + type + ", \"" +  lexeme + "\", Line: " + line + ", Col: " + column + ">\n");
    }

    public TokenType getType() {
        return type;
    }
}
