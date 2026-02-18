public class ErrorHandler {
    String errorType;
    int lineOfError;
    int columnOfError;
    String lexeme;
    String reason;

    ErrorHandler(String errorType, int lineOfError, int columnOfError, String lexeme, String reason) {
        this.errorType = errorType;
        this.lineOfError = lineOfError;
        this.columnOfError = columnOfError;
        this.lexeme = lexeme;
        this.reason = reason;
    }

    public void PrintError() {
        System.out.print("Error Type: " + errorType + ", Line: " + lineOfError + ", Col: " + columnOfError + ", Lexeme: " + lexeme + ", Reason: " + reason + "\n");
    }
}
