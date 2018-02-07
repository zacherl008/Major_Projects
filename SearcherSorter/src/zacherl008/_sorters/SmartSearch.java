/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zacherl008._sorters;

import java.util.ArrayList;

/**
 *
 * @author kzacherl
 */
public class SmartSearch <E>{
    
    public SmartSearch(){
    
    }
    
    public int doSmartSearch(){
        MakeArray arrayMaker = new MakeArray();
        Searchers searcher = new Searchers();
        Sorter sorter = new Sorter();
        int location;
        
        Object [] arg = arrayMaker.makeArray();
        
        boolean sortPossible = sorter.sortPossible(arg);
        
        if (sortPossible){
            Object [] sortedArray = sorter.selSorter(arg);
            location = searcher.binarySearch(sortedArray);
        }
        else{
            location = searcher.linearSearch(arg);
        }
    
        return location;
    }
}
