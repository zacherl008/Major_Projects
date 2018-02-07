/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zacherl008_datastructure;

import java.util.ArrayList;

/**
 *
 * @author kzacherl
 */
public class h {
    public static void pl(Object obj){
        System.out.println(obj.toString());
    }
    
    public static void ps(Object obj){
        System.out.print(obj.toString());
    }
    public static void pa(Object [] arr){
        for(int i = 0; i < arr.length; i++){
            ps(arr[i] + " | ");
        }
        h.ps("\n");
    }
    public static void pal(ArrayList al){
        int i = 0;
        String str = "";
        for(; i < al.size()-1; i++){
            str += al.get(i) + " | ";
        }
        h.ps(str + al.get(i) + "\n");
        
    }
}
