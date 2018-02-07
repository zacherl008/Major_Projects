/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zacherl008_datastructure;

/**
 *
 * @author kzacherl
 */
//toString, firstname, lastname, etc
//convert to single stream JSON
//hashtable
//object can hold data structure as well
//acre to grid box
//add section of grid box
//instantiate world so that acres exist
//go through grid (using x and y or add in order) 
//feed acres one by one in a 2d array loop to grid
//add each one to grid array
//if grid pane allows an x and y give i and j if they map the same way
//
//algorithm design and techniques
//p367, 370
//
//Greedy Algorithm
//local best can translate to a globally optimal solution
//coin change, selection sorting
//selection sort finds minimum and moves to beginning of array
//always makess choice that seems best at that moment
//coin change of 1,3,4 to make 6 would be 4,1,1 vs 3,3 optimal, small victories
//Decision is good at that point without caring about future consequences
//
//Divide & Conquer
//binary search, joining tables, merge sort
//binary search checks x vs middle, if x > middle run again for right half and opposite
//divides into subproblems of same type
//merge sort divides an array in half, sorts them, then combines
//
//Dynamic Programming
//memoization - optimization by storing the results of an expensive function and 
//saving the solution in a table for future inputs (cacheing)
//dynamic programming has an overlap of subproblems vs independent in d&c
//reduces exponential complexity to polynomial complexity 
//computer modeling of chemicals (graph compression)
//traveling salesman problem
//

public interface UserAccountInterface {
    //Interface with functions that will be implemented in the user information array class
    public String[] getArray();
    public void setArray(String password,String email, String CC, String zipcode, String birthdate);

    public String getPW();
    public void setPW(String pw);

    public String getUN();
    public void setUN(String un);

    public String getEmail();
    public void setEmail(String email);

    public String getCC();
    public void setCC(String cc);

    public String getZIP();
    public void setZIP(String zip);

    public String getBirthdate();
    public void setBirthdate(String bd);
        
}
