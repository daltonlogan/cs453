public class ScannerTest
{

    /* Quick test of token extraction*/
    public static void testTokenExtraction( )
    {
        System.out.println( "*******************************************" );
        System.out.println( "Testing Token Extraction" );
        System.out.println( "*******************************************" );
        Scanner test = new Scanner();

        System.out.println( "Starting test 1" );
        String result = test.extractTokens( "123" );
        String expected = "|NUM: 123|";
        if ( ( !result.equals( expected ) ) ) throw new AssertionError();
        System.out.println( "Passed test 1" );

        System.out.println();

        System.out.println( "Starting test 2" );
        result = test.extractTokens( "+ 3 3" );
        expected = "|PLUS: +||NUM: 3||NUM: 3|";
        if ( ( !result.equals( expected ) ) ) throw new AssertionError();
        System.out.println( "Passed test 2" );

        System.out.println();

        System.out.println( "Starting test 3" );
        result = test.extractTokens( "+ - * / < >" );
        expected = "|PLUS: +||MINUS: -||MUL: *||DIV: /||LT: <||GT: >|";
        if ( ( !result.equals( expected ) ) ) throw new AssertionError();
        System.out.println( "Passed test 3" );

        System.out.println();

        System.out.println( "Starting test 4" );
        result = test.extractTokens( "<= - >=" );
        expected = "|LTE: <=||MINUS: -||GTE: >=|";
        if ( ( !result.equals( expected ) ) ) throw new AssertionError();
        System.out.println( "Passed test 4" );

        System.out.println();

        System.out.println( "*******************************************" );
        System.out.println( "Congrats: preliminary token extraction tests passed! Now make your own test cases " +
                                    "(this is only a subset of what we will test your code on)" );
        System.out.println( "*******************************************" );
        System.out.println();
        System.out.println();

        System.out.println( "*******************************************" );
        System.out.println( "Testing Custom Token Extraction" );
        System.out.println( "*******************************************" );

        System.out.println( "Starting test 1" );
        result = test.extractTokens( "<= < >    - - 324 >=" );
        expected = "|LTE: <=||LT: <||GT: >||MINUS: -||MINUS: -||NUM: 324||GTE: >=|";
        if ( ( !result.equals( expected ) ) ) throw new AssertionError();
        System.out.println( "Passed test 1" );

        System.out.println();

        System.out.println( "Starting test 2" );
        result = test.extractTokens( "< > @" );
        expected = "|LT: <||GT: >|";
        if ( ( !result.equals( expected ) ) ) throw new AssertionError();
        System.out.println( "Passed test 2" );

        System.out.println();

        System.out.println( "Starting test 3" );
        result = test.extractTokens( "1 1 23   < 4 *" );
        expected = "|NUM: 1||NUM: 1||NUM: 23||LT: <||NUM: 4||MUL: *|";
        if ( ( !result.equals( expected ) ) ) throw new AssertionError();
        System.out.println( "Passed test 3" );

        System.out.println();

        System.out.println( "Starting test 4" );
        result = test.extractTokens( "   -   - &  < " );
        expected = "|MINUS: -||MINUS: -|";
        if ( ( !result.equals( expected ) ) ) throw new AssertionError();
        System.out.println( "Passed test 4" );

        System.out.println();

        System.out.println( "Starting test 5" );
        result = test.extractTokens( "< < <= <>" );
        expected = "|LT: <||LT: <||LTE: <=||LT: <||GT: >|";
        if ( ( !result.equals( expected ) ) ) throw new AssertionError();
        System.out.println( "Passed test 5" );

        System.out.println();

        System.out.println( "Starting test 6" );
        result = test.extractTokens( "<32>+-" );
        expected = "|LT: <||NUM: 32||GT: >||PLUS: +||MINUS: -|";
        if ( ( !result.equals( expected ) ) ) throw new AssertionError();
        System.out.println( "Passed test 6" );

        System.out.println();

        System.out.println( "Starting test 7" );
        result = test.extractTokens( "55 + 2 - ( 2 )" );
        expected = "|NUM: 55||PLUS: +||NUM: 2||MINUS: -||LEFTPAREN: (||NUM: 2||RIGHTPAREN: )|";
        if ( ( !result.equals( expected ) ) ) throw new AssertionError();
        System.out.println( "Passed test 7" );

        System.out.println();

        System.out.println( "*******************************************" );
        System.out.println( "Testing Java Token Extraction" );
        System.out.println( "*******************************************" );

        System.out.println( "Starting test 1" );
        result = test.extractTokens( "test ; + 4" );
        expected = "|ID: test||SEMICOLON: ;||PLUS: +||NUM: 4|";
        if ( ( !result.equals( expected ) ) ) throw new AssertionError();
        System.out.println( "Passed test 1" );

        System.out.println();

        System.out.println( "Starting test 2" );
        result = test.extractTokens( "int firstInt = 5 + 5" );
        expected = "|INT: int||ID: firstInt||ASSIGN: =||NUM: 5||PLUS: +||NUM: 5|";
        if ( ( !result.equals( expected ) ) ) throw new AssertionError();
        System.out.println( "Passed test 2" );

        System.out.println();

        System.out.println( "Starting test 3" );
        result = test.extractTokens( "if ( 5 < 6 )" );
        expected = "|IF: if||LEFTPAREN: (||NUM: 5||LT: <||NUM: 6||RIGHTPAREN: )|";
        if ( ( !result.equals( expected ) ) ) throw new AssertionError();
        System.out.println( "Passed test 3" );

        System.out.println();

        System.out.println( "Starting test 4" );
        result = test.extractTokens( "{ 5 + 2 } >= 7" );
        expected = "|LEFTCURLY: {||NUM: 5||PLUS: +||NUM: 2||RIGHTCURLY: }||GTE: >=||NUM: 7|";
        if ( ( !result.equals( expected ) ) ) throw new AssertionError();
        System.out.println( "Passed test 4" );

        System.out.println();

        System.out.println( "Starting test 5" );
        result = test.extractTokens( "if while void int" );
        expected = "|IF: if||WHILE: while||VOID: void||INT: int|";
        if ( ( !result.equals( expected ) ) ) throw new AssertionError();
        System.out.println( "Passed test 5" );

        System.out.println();

        System.out.println( "Starting test 6" );
        result = test.extractTokens( "INT WHILE int while" );
        expected = "|ID: INT||ID: WHILE||INT: int||WHILE: while|";
        if ( ( !result.equals( expected ) ) ) throw new AssertionError();
        System.out.println( "Passed test 6" );

        System.out.println();

        System.out.println( "Starting test 7" );
        result = test.extractTokens( "test123" );
        expected = "|ID: test123|";
        if ( ( !result.equals( expected ) ) ) throw new AssertionError();
        System.out.println( "Passed test 7" );

        System.out.println();

        System.out.println( "Starting test 8" );
        result = test.extractTokens( "int123 = 123" );
        expected = "|ID: int123||ASSIGN: =||NUM: 123|";
        if ( ( !result.equals( expected ) ) ) throw new AssertionError();
        System.out.println( "Passed test 8" );

        System.out.println();

        System.out.println( "Starting test 9" );
        result = test.extractTokens( "5 == 5" );
        expected = "|NUM: 5||EQUALS: ==||NUM: 5|";
        if ( ( !result.equals( expected ) ) ) throw new AssertionError();
        System.out.println( "Passed test 9" );

        System.out.println();

        System.out.println( "Starting test 10" );
        result = test.extractTokens( "0 != 1" );
        expected = "|NUM: 0||NOTEQUALS: !=||NUM: 1|";
        if ( ( !result.equals( expected ) ) ) throw new AssertionError();
        System.out.println( "Passed test 10" );

        System.out.println();

        System.out.println( "Starting test 11" );
        result = test.extractTokens( "public private class" );
        expected = "|PUBLIC: public||PRIVATE: private||CLASS: class|";
        if ( ( !result.equals( expected ) ) ) throw new AssertionError();
        System.out.println( "Passed test 11" );

        System.out.println();

        System.out.println( "Starting test 12" );
        result = test.extractTokens( "&& ||" );
        expected = "|AND: &&||OR: |||";
        if ( ( !result.equals( expected ) ) ) throw new AssertionError();
        System.out.println( "Passed test 12" );

        System.out.println();

        System.out.println( "Starting test 13" );
        result = test.extractTokens( "|| &" );
        expected = "|OR: |||";
        if ( ( !result.equals( expected ) ) ) throw new AssertionError();
        System.out.println( "Passed test 13" );

        System.out.println();

        System.out.println( "Starting test 14" );
        result = test.extractTokens( "&& |" );
        expected = "|AND: &&|";
        if ( ( !result.equals( expected ) ) ) throw new AssertionError();
        System.out.println( "Passed test 14" );

        System.out.println();

        System.out.println( "*******************************************" );
        System.out.println( "Passed All Tests" );
        System.out.println( "*******************************************" );
    }

    public static void main( String[] args )
    {
        testTokenExtraction();
    }

}
