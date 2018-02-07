/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zacherl008._sorters;
import java.util.Scanner;

/**
 *
 * @author kzacherl
 */
public class FindTarget <E> {
    public FindTarget(){
        
    }
    public E findTarget(){
    h.ps("What would you like to search for? ");
        Scanner scan = new Scanner(System.in);
        String target = scan.next();
        try{
           Integer input = Integer.parseInt(target);
           return (E)input;
        }
        catch(java.lang.NumberFormatException ex){
            if (target.length() > 1){
                return (E)target;
            }
            else{
                Character input = target.charAt(0);
                return (E)target;
            }
        }
    }
}
