/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zacherl008._sorters;

import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author kzacherl
 */
public class MakeArray <E> {
    public MakeArray(){
        
    }
    
    public Object[] makeArray (){
        h.ps("Please the length of your array: ");
        Scanner scanner = new Scanner(System.in);
        int length = scanner.nextInt();
        Object[] arg = new Object[length];

        for(int i = 0; i < length; i++){
            h.ps("Please enter one item at a time for your array: ");
            String str = scanner.next();
            try{
               Integer input = Integer.parseInt(str);
               arg[i] = input;
            }
            catch(java.lang.NumberFormatException ex){
                if (str.length() > 1){
                    arg[i] = str;
                }
                else{
                    Character input = str.charAt(0);
                    arg[i] = str;
                }
            }
        }
        return arg;
    }
}