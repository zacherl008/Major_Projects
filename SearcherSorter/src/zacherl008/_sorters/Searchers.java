/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zacherl008._sorters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 *
 * @author kzacherl
 */
public class Searchers <E> {
    private FindTarget findTarget = new FindTarget();
    //functions from sorter into searcher instead of into main
    //use E arrays for things that dont happen during runtime
    
    public Searchers(){
    
    }
  
    public int binarySearch (E[] arg){
        Object target = findTarget.findTarget();
        target = (E)(target);
        h.ps("Beginning binary search. \n");
        int low = 0;
        int index = 0;
        int high = arg.length - 1;
        
        while (high >= low){
            int middle = (low + high) / 2;
            try{
                if (((Comparable)(target)).compareTo(arg[middle]) > 0){
                    low = middle + 1;
                } 
                else if (((Comparable)(target)).compareTo(arg[middle]) < 0){
                    high = middle - 1;
                }
                else{
                    index = middle;
                    h.ps("Your target, " + target + ", is at index " + index + ". \n");
                    return index;
                }
            }
            catch(java.lang.ClassCastException e){
                if (((Comparable)(target.toString())).compareTo(arg[middle].toString()) > 0){
                    low = middle + 1;
                } 
                else if (((Comparable)(target.toString())).compareTo(arg[middle].toString()) < 0){
                    high = middle - 1;
                }
                else{
                    index = middle;
                    h.ps("Your target, " + target + ", is at index " + index + ". \n");
                    return index;
                }
            }
        }
        h.ps("Your target is not here. ");
        h.ps("Would you like to search again? Please enter yes or no: ");
        Scanner scan = new Scanner(System.in);
        String reply = scan.next();
        if(reply.equals("yes")){
            binarySearch(arg);
        }        
        return -1;
    }
    
    public int linearSearch (E[] arg){
        Object target = findTarget.findTarget();
        h.ps("Beginning linear search. \n");
        for (int i =0; i<arg.length; i++){
            if (arg[i] == target){
                int index = i + 1;
                h.ps("Your target, " + target + ", is at index " + index + ". \n");
                return index;
            }   
        }    
        h.ps("Your target is not here.");
        return -1;    
    }
    
    public int jumpSearch (E[] arg){
        Sorter sort = new Sorter();
        Object [] arg2 = sort.selSorter(arg);
        //Object target = 0;
        Object target = findTarget.findTarget();
        target = (E)(target);
        h.ps("Beginning jump search. \n");
        int n = arg2.length;
        int step = (int)Math.floor(Math.sqrt(n));
        int prev = 0;
        
        while(((Comparable)(arg2[Math.min(step, n)-1])).compareTo(((Comparable)(target))) < 0){
            prev = step;
            step += (int)Math.floor(Math.sqrt(n));
            if(prev >= n){
                h.ps("Your target is not here.");
                return -1;
            }
        }
        
        while(((Comparable)(arg2[prev])).compareTo(((Comparable)(target))) < 0){
            prev++;
            if(prev == Math.min(step, n)){
                h.ps("Your target is not here.");
                return -1;
            }
        }
        
        if(arg2[prev] == target){
            h.ps("Your target, " + target + ", is at index " + prev + ". \n");
            return prev;
        }
        h.ps("Your target is not here.");
        return -1;
    }
    
}
