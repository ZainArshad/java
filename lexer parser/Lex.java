/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lex;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author z
 */
public class Lex {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Scanner myObj = new Scanner(System.in);  // Create a Scanner object
        
        System.out.println("Enter text");

        String code = myObj.nextLine();
        Lexer start= new Lexer(code);
        //start.scanTokens();
        List<Token> tokens = new ArrayList<>();
        tokens=start.lex(code);
    for (int counter = 0; counter < tokens.size(); counter++) { 		      
          System.out.println(tokens.get(counter)); 
    }
   
  }
    
}
