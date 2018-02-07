/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zacherl008._sorters;
import java.lang.Comparable;
import java.util.ArrayList;
/**
 *
 * @author zacherl008
 */
public class Sorter <E>{
    
    public Sorter(){}
    public char[] bubSort(char[] cArr){
        boolean hasSwapped = true;
    
        while(hasSwapped){
            hasSwapped = false;
            for(int i = 0; i < cArr.length - 1; i++){
                if(cArr[i] > cArr[i+1]){
                    char temp = cArr[i];
                    cArr[i] = cArr[i+1];
                    cArr[i+1] = temp;
                    hasSwapped = true;
                }   
            }
        }
        return cArr;
    }
    
    public int[] bubSort(int[] elements){
        boolean hasSwapped = true;
        while(hasSwapped){
            hasSwapped = false;
            for(int i = 0; i < elements.length - 1; i++){
                if(elements[i] > elements[i+1]){
                    int temp = elements[i];
                    elements[i] = elements[i+1];
                    elements[i+1] = temp;
                    hasSwapped = true;
                }
            }
        }
        return elements;
    }
    
    public boolean sortPossible(E[] arg){
        h.ps("The array you provided is: ");
        h.pa(arg);
        boolean notComparable = false;
        int q = 0;
        ArrayList elements = new ArrayList();
        for (int r = 0; r < arg.length; r++){
            elements.add(arg[r]);
        }
        while (!notComparable && q != elements.size()){
            if(elements.get(q) instanceof Comparable){
                q++;
            }
            else{
                notComparable = true;
                h.ps("Your array contains at least one element that is not comparable. \n");
                return false;
            }
            if(q == elements.size() && notComparable == false){
                h.ps("Your array is fully comparable.\n");
                return true;
            }
        }
        return false;
    }
    
    public E[] selSorter(E[] arg){
        int q = 0;
        ArrayList elements = new ArrayList();
        for (int r = 0; r < arg.length; r++){
            elements.add(arg[r]);
        }
        Object smallest;
        int smallestIndex;
        for (int currentIndex = 0; currentIndex < elements.size(); currentIndex++){
            smallest = elements.get(currentIndex);
            smallestIndex = currentIndex;
            for (int i = currentIndex + 1; i < elements.size(); i++){
                try{
                    if (((Comparable)(smallest)).compareTo(elements.get(i)) > 0){
                        smallest = elements.get(i);
                        smallestIndex = i;
                    }
                }catch(java.lang.ClassCastException e){
                    if (((Comparable)(smallest.toString())).compareTo(elements.get(i).toString()) > 0){
                        smallest = elements.get(i);
                        smallestIndex = i;
                    }
                }
            }    
            if (smallestIndex == currentIndex){
                continue;
            }
            else{ 
                Object temp = elements.get(currentIndex);
                elements.set(currentIndex, elements.get(smallestIndex));
                elements.set(smallestIndex, temp);
            }
        } 
        h.ps("Your sorted array is: ");
        h.ps(elements + "\n"); 
        arg = (E[])(elements.toArray());
        return(arg);
    }
}
