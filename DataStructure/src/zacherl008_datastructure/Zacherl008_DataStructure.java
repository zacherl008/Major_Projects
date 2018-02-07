/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zacherl008_datastructure;

import java.util.HashMap;

/**
 *
 * @author kzacherl
 */
public class Zacherl008_DataStructure {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args){
        
        ///
        //Use the hardcoded testAdd() function (first one in the UserFunctions class to test
        //out all functions besides adding an account. This keeps you from having
        //to enter an email, CC, zip, username, password, and DOB each time you run
        //the program.
        //
                
        UserFunctions user = new UserFunctions();
        //user.addAccount();
        user.testAdd();
        
        HashMap hash = user.returnHash();
        h.ps(user.ofAge18("olive"));

        //String [] value = (String [])hash.get("olive");
        //h.pa(value);
        
        //user.editCC("olive", "william", "349832894343");
        //String [] value2 = (String [])hash.get("olive");
        //h.pa(value2);

    }
    
}
