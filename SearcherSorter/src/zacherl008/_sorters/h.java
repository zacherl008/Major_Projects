/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zacherl008._sorters;

import java.util.ArrayList;

/**
 *
 * @author zacherl008
 */
public class h {
    
    public static void ps(Object obj){
        System.out.print(obj.toString());
    }
    
    public static void pa(Object [] objArr){
        ps("| ");
        for(int i = 0; i < objArr.length; i++){
            ps(objArr[i] + " | ");
        }
        ps("\n");
    }
    
    public static void pa(char[] cArr){
        ps("| ");
        for(int i = 0; i < cArr.length; i++){
            ps(cArr[i] + " | ");
        }
        ps("\n");
    }
    
    public static void pa(int[] intArr){
        ps("| ");
        for(int i = 0; i < intArr.length; i++){
            ps(intArr[i] + " | ");
        }
        ps("\n");
    }
    
    public static void pa(Integer[] intArr){
       ps("| ");
       for(int i = 0; i < intArr.length; i++){
            ps(intArr[i] + " | ");
        } 
       ps("\n");
    }
    
    public static void pa(ArrayList[] intArr){
       ps("| ");
       for(int i = 0; i < intArr.length; i++){
            ps(intArr[i] + " | ");
        } 
       ps("\n");
    }
}

