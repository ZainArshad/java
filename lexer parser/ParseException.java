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


public final class ParseException extends RuntimeException {

    private final int index;

    public ParseException(String message, int index) {
        super(message);
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
    
    public static void error(int a, String b)
    {
        System.out.println(b);
    }

}

