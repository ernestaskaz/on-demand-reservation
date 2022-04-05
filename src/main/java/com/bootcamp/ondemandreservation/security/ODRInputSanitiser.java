package com.bootcamp.ondemandreservation.security;

public class ODRInputSanitiser {

    /**
     * from  https://cheatsheetseries.owasp.org/cheatsheets/Input_Validation_Cheat_Sheet.html
     * As such, the best way to validate email addresses is to perform some basic initial validation, and then pass the address to the mail server and catch the exception if it rejects it. This means that any the application can be confident that its mail server can send emails to any addresses it accepts. The initial validation could be as simple as:
     *
     *     The email address contains two parts, separated with an @ symbol.
     *     The email address does not contain dangerous characters (such as backticks, single or double quotes, or null bytes).
     *         Exactly which characters are dangerous will depend on how the address is going to be used (echoed in page, inserted into database, etc).
     *     The domain part contains only letters, numbers, hyphens (-) and periods (.).
     *     The email address is a reasonable length:
     *         The local part (before the @) should be no more than 63 characters.
     *         The total length should be no more than 254 characters.
     *
     * @return
     */
    public static boolean likelyIsEmail(String email){
        if(email==null){
            return false;
        }
        int len=email.length();//so we can see in debugger
        if(len>254){
            return false;
        }
        int indexOfAtSymbol=email.indexOf("@");
        if(indexOfAtSymbol<1||indexOfAtSymbol>63){
            return false;
        }
        /*
        should be enough for basic "Bobby Tables" JPA/SQL injection ( "Robert'); DROP TABLE USERS") scenario
         */
        if(email.indexOf("\u0000")>0||email.indexOf("\\")>0
                ||email.indexOf("`")>0||email.indexOf("\'")>0
                ||email.indexOf("'")>0||email.indexOf("\r")>0
                ||email.indexOf("\n")>0){
            return false;
        }
        String domain=email.substring(indexOfAtSymbol+1);

        return domain.matches("[A-Za-z0-9\\-\\.]+");

    }
}
