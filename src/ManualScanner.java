import java.util.ArrayList;


public class ManualScanner {
    private String input;
    private int position;
    private int lineNumber;
    private int columnNumber;
    private SymbolTable symbolTable;
    private ArrayList<Token> tokens;
    private ArrayList<ErrorHandler> errors;
    private int processedCommentsCounter;

    public ManualScanner(String data) {
        position = 0;
        lineNumber = 1;
        columnNumber = 1;
        symbolTable = new SymbolTable();
        errors = new ArrayList<>();
        tokens = new ArrayList<>();
        processedCommentsCounter = 0;
        input = data;
    }

    public int getPosition() {
        return position;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public int getColumnNumber() {
        return columnNumber;
    }

    public void SetNewLineToProcess(String NewLineOfCode) {
        input = NewLineOfCode;
    }

    private char Peek() {
        if (position + 1 >= input.length()) {
            return '\0';
        }

        return input.charAt(position + 1);
    }

    private void Advance() {
        if (position >= input.length()) {
            return;
        }

        char nextChar = input.charAt(position++);
        if (nextChar == '\n') {
            lineNumber++;
            columnNumber = 1;
        }
        else  {
            columnNumber++;
        }
    }

    private boolean IsKeyword(String inputLine) {
        return inputLine.matches("start|finish|loop|condition|declare|output|input|function|return|break|continue|else");
    }

    private boolean IsBooleanLiteral(String inputLine) {
        return inputLine.matches("true|false");
    }

    private boolean IsIntegerLiteral(String inputLine) {
        return inputLine.matches("[-+]?[0-9]+");
    }

    private  boolean IsFloatLiteral(String inputLine) {
        return inputLine.matches("[-+]?[0-9]+\\.[0-9]{1,6}([eE][-+]?[0-9]+)?");
    }

    private boolean IsValidIdentifier(String inputLine) {
        return inputLine.matches("[A-Z][a-zA-Z0-9_]*");
    }

    private boolean IsStringLiteral(String inputLine) {
        return inputLine.matches("\"([^\"\\\\\\n]|\\\\[\"\\\\ntr])*\"");
    }

    private boolean IsCharLiteral(String inputLine) {
        return inputLine.matches("'([^'\\\\\\n]|\\\\[\\\\\"'ntr])'");
    }

    private void SkipWhiteSpace() {
        while (position < input.length()) {
            if (input.charAt(position) == ' ' || input.charAt(position) == '\t' || input.charAt(position) == '\r') {
                position++;
                columnNumber++;
            }
            else if (input.charAt(position) == '\n') {
                lineNumber++;
                position++;
                columnNumber = 1;
            }
            else
                break;
        }
    }

    private void ProcessSingleLineComment() {

        while(position < input.length()) {
            if (Peek() == '\n') {
                Advance();
                break;
            }
            Advance();
        }

        processedCommentsCounter++;
    }

    private void ProcessWord() {
        StringBuilder sb = new StringBuilder();
        int startColumn = columnNumber;
        int startLine = lineNumber;
        while (position < input.length() && (Character.isLetterOrDigit(input.charAt(position)) ||  input.charAt(position) == '_')) {
            sb.append(input.charAt(position));
            Advance();
        }

        String lexeme = sb.toString();

        if (IsKeyword(lexeme)) {
            tokens.add(new Token(TokenType.KEYWORD, lexeme, startLine, startColumn));
        }
        else if (IsBooleanLiteral(lexeme)) {
            tokens.add(new Token(TokenType.BOOLEAN_LITERAL, lexeme, startLine, startColumn));
        }
        else if (IsValidIdentifier(lexeme)) {
            if (lexeme.length() <= 31) {
                tokens.add(new Token(TokenType.IDENTIFIER, lexeme, startLine, startColumn));
                symbolTable.InsertIdentifier(lexeme, startLine);
            }
            else {
                //handle error
                ErrorHandler error = new ErrorHandler("Invalid Identifier", startLine, startColumn, lexeme, "Identifier exceeds length.");
                error.PrintError();
                errors.add(error);
            }
        }
        else {
            //handle error
            ErrorHandler error = new ErrorHandler("Invalid Identifier", startLine, startColumn, lexeme, "Identifier does not begin with a capital letter.");
            error.PrintError();
            errors.add(error);
        }
    }

    private void ProcessNumber() {
        StringBuilder sb = new StringBuilder();
        int startColumn = columnNumber;
        int startLine = lineNumber;
        while (position < input.length() && (Character.isDigit(input.charAt(position)) || input.charAt(position) == '-' || input.charAt(position) == '+') || input.charAt(position) == '.' || input.charAt(position) == 'e' || input.charAt(position) == 'E') {
            sb.append(input.charAt(position));
            Advance();
        }

        String lexeme = sb.toString();

        if (IsFloatLiteral(lexeme)) {
            tokens.add(new Token(TokenType.FLOAT_LITERAL, lexeme, startLine, startColumn));
        }
        else if (IsIntegerLiteral(lexeme)) {
            tokens.add(new Token(TokenType.INTEGER_LITERAL, lexeme, startLine, startColumn));
        }
        else {
            //handle error
            ErrorHandler error = new ErrorHandler("Malformed literal", startLine, startColumn, lexeme, "Numeric Literal is incorrectly defined.");
            error.PrintError();
            errors.add(error);
        }
    }

    private void ProcessString() {
        StringBuilder sb = new StringBuilder();
        int startColumn = columnNumber;
        int startLine = lineNumber;
        boolean IsStringTerminated = false;

        do {
            if (position >= input.length()) {
                break;
            }
            if (input.charAt(position) == '\n') {
                break;
            }
            else if (input.charAt(position) == '\\' && (Peek() == '\"' || Peek() == '\'' || Peek() == '\\' || Peek() == 'r' || Peek() == 'n' || Peek() == 't')) {
                sb.append(input.charAt(position));
                Advance();
                sb.append(input.charAt(position));
                Advance();
                continue;
            }
            sb.append(input.charAt(position));
            Advance();
        } while (position < input.length() && input.charAt(position) != '\"');

        if (position < input.length() && input.charAt(position) == '\"') {
            sb.append(input.charAt(position));
            Advance();
            IsStringTerminated = true;
        }

        String lexeme = sb.toString();
        if (IsStringLiteral(lexeme) && IsStringTerminated) {
            tokens.add(new Token(TokenType.STRING_LITERAL, lexeme, startLine, startColumn));
        }
        else {
            //handle error
            ErrorHandler error = new ErrorHandler("Malformed literal", startLine, startColumn, lexeme, "String does not terminate.");
            error.PrintError();
            errors.add(error);
        }
    }

    private void ProcessChar() {
        StringBuilder sb = new StringBuilder();
        int startColumn = columnNumber;
        int startLine = lineNumber;
        boolean IsCharTerminated = false;
        do {
            if (position >= input.length()) {
                break;
            }
            if (input.charAt(position) == '\\' && (Peek() == '\"' || Peek() == '\'' || Peek() == '\\' || Peek() == 'r' || Peek() == 'n' || Peek() == 't')) {
                sb.append(input.charAt(position));
                Advance();
                sb.append(input.charAt(position));
                Advance();
                break;
            }
            sb.append(input.charAt(position));
            Advance();
        } while (position < input.length() && input.charAt(position) != '\'');

        if  (position < input.length() && input.charAt(position) == '\'') {
            sb.append(input.charAt(position));
            Advance();
            IsCharTerminated = true;
        }

        String lexeme = sb.toString();
        if (IsCharLiteral(lexeme) && IsCharTerminated) {
            tokens.add(new Token(TokenType.CHAR_LITERAL, lexeme, startLine, startColumn));
        }
        else {
            //handle error
            ErrorHandler error = new ErrorHandler("Malformed literal", startLine, startColumn, lexeme, "Character is either of length greater or less than 1 or does not terminate.");
            error.PrintError();
            errors.add(error);
        }
    }

    public void PrintStatistics() {
        int intLiteralCount = 0, floatLiteralCount = 0, stringLiteralCount = 0, charLiteralCount = 0, identifierCount = 0, keywordCount = 0, booleanLiteralCount = 0;
        for (Token token : tokens) {
            if (token.getType() == TokenType.IDENTIFIER) {
                identifierCount++;
            }
            else if (token.getType() == TokenType.FLOAT_LITERAL) {
                floatLiteralCount++;
            }
            else if (token.getType() == TokenType.STRING_LITERAL) {
                stringLiteralCount++;
            }
            else if (token.getType() == TokenType.CHAR_LITERAL) {
                charLiteralCount++;
            }
            else if (token.getType() == TokenType.KEYWORD) {
                keywordCount++;
            }
            else if (token.getType() == TokenType.INTEGER_LITERAL) {
                intLiteralCount++;
            }
            else
                booleanLiteralCount++;
        }
        System.out.print("Frequency for each token type:\n");
        System.out.print("Int Literals: " + intLiteralCount + "\n");
        System.out.print("Float Literals: " + floatLiteralCount + "\n");
        System.out.print("String Literals: " + stringLiteralCount + "\n");
        System.out.print("Character Literals: " + charLiteralCount + "\n");
        System.out.print("Identifiers: " + identifierCount + "\n");
        System.out.print("Keywords: " + keywordCount + "\n");
        System.out.print("Booleans: " + booleanLiteralCount + "\n\n");
        System.out.print("Lines processed: " + lineNumber + "\n\n");
        System.out.print("Comments processed: " + processedCommentsCounter + "\n\n");

        System.out.print("Total tokens: " + tokens.size() + "\n");
        for (Token token : tokens) {
            token.PrintToken();
        }
        System.out.print("\n");

        System.out.print("All Identifiers and their frequency:\n\n");
        symbolTable.PrintAllIdentifiers();
    }

    public void ProcessInput() {
        while (position < input.length()) {
            char currentChar = input.charAt(position);
            char nextChar = Peek();

            if (Character.isWhitespace(currentChar)) {
                SkipWhiteSpace();
            }
            else if (currentChar == '#') {

                if (nextChar == '#') {
                    ProcessSingleLineComment();
                }
                else {
                    ErrorHandler error = new ErrorHandler("Malformed Literal", lineNumber, columnNumber, Character.toString(currentChar), "Incorrect way to declare a comment.");
                    error.PrintError();
                    errors.add(error);
                }
            }
            else if (Character.isLetter(currentChar)) {
                //processes keyword, boolean and identifier in that order
                ProcessWord();
            }
            else if (Character.isDigit(currentChar) || ((currentChar == '-' || currentChar == '+') && Character.isDigit(nextChar))) {
                //processes float & int literals in that order
                ProcessNumber();
            }
            else if (input.charAt(position) == '\"') {
                //processes string literals
                ProcessString();
            }
            else if (input.charAt(position) == '\'') {
                //processes characters
                ProcessChar();
            }
            else if (currentChar == '$' || currentChar == '@' || currentChar == '^' || currentChar == '?') {
                //handle error
                ErrorHandler error = new ErrorHandler("Invalid Character", lineNumber, columnNumber, Character.toString(currentChar), "Character is of invalid type.");
                error.PrintError();
                errors.add(error);
                Advance();
            }
            else
                Advance();
        }
    }
}
