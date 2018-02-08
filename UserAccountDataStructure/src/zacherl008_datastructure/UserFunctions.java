/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zacherl008_datastructure;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Scanner;

/**
 *
 * @author kzacherl
 */
public class UserFunctions{
    HashMap myHashMap = new HashMap();
    
    //remove this in final version
    public void testAdd(){
        UserAccount account = new UserAccount("olive", "william", "dfjshfdsfdsf", "5434321434", "54346", "06/30/2003");
        myHashMap.put("olive", account.getArray());
    }
    
    public void addAccount(){
        System.out.println("Making new account...");
        Scanner scan = new Scanner(System.in);

        System.out.print("Please enter a username: ");
        String username = scan.next();
        //function to check if username is unique
        if(! existsUN(username)){
            h.ps("This username is in use. Please try again: ");
            username = scan.next();
            if(! existsUN(username)){
                h.pl("You are dumb. Goodbye.");
                System.exit(0);   
            }
        }

        System.out.print("Please enter a password: ");
        String password  = scan.next();
        
        System.out.print("Please enter an email: ");
        String email  = scan.next();
        
        System.out.print("Please enter a credit card: ");
        String CC = scan.next();
        //regular expression to check if credit card is numeric with 8-19 digits
        if(! CC.matches("\\d{8,19}")){
            h.ps("Please enter a credit card: ");
            CC = scan.next();
            if(! CC.matches("\\d{8,19}")){
                h.pl("You are dumb. Goodbye.");
                System.exit(0);
            }
        }
        
        System.out.print("Please enter a 5 digit zipcode: ");
        String zipcode  = scan.next();
        //regular expression to check if zipcode is numeric with 5 digits
        if(! zipcode.matches("\\d{5}")){
            h.ps("Please enter a 5 digit zipcode: ");
            zipcode  = scan.next();
            if(! zipcode.matches("\\d{5}")){
                h.pl("You are dumb. Goodbye.");
                System.exit(0);
            }
        }
     
        System.out.print("Please enter your date of birth in MM/DD/YYYY format: ");
        String birthdate = scan.next();
        //regular expression to check if birthdate is in MM/DD/YYYY format
        if(! birthdate.matches("\\d{2}/\\d{2}/\\d{4}")){
            h.ps("Please enter your date of birth in MM/DD/YYYY format: ");
            birthdate = scan.next();
            if(! birthdate.matches("\\d{2}/\\d{2}/\\d{4}")){
                h.pl("You are dumb. Goodbye.");
                System.exit(0);
            }
        }
       
        //creates a new UserAccount object and then adds this information to the hashmap
        //with the username as the key and the other information (added to an array
        //in the user account class constructor) as the value
        UserAccount account = new UserAccount(username, password, email, CC, zipcode, birthdate);
        myHashMap.put(username, account.getArray());       
    }
    
    public void accessAccount(String username, String password){
        if(myHashMap.containsKey(username)){
            String[] value = (String[])myHashMap.get(username);
            if(value[0].equals(password)){
                h.pl("Your credit card on file is: " + value[2]);
                h.pl("Your zipcode on file is: " + value[3]);
                h.pl("Your email on file is: " + value[1]);
                h.pl("Your date of birth on file is: " + value[4]);
            }
            else{
                h.pl("Incorrect username or password. Please retry");
            }
        }
        else{
            h.pl("Username not found. Please retry.");
        }
    }
    
    public void removeAccount(String username, String password){
        if(myHashMap.containsKey(username)){
            String[] value = (String[])myHashMap.get(username);
            if(value[0].equals(password)){
                h.ps("This will delete your account and all information. Are you sure?");
                Scanner scan = new Scanner(System.in);
                String reply = scan.next();
                if(reply.equals("yes")){
                    myHashMap.remove(username);    
                    h.pl("Your account has been removed.");
                }
                else{
                    h.pl("Your account has not been removed.");
                }
            } 
            else{
                h.pl("Incorrect username or password. Please retry");
            }
        }
        else{
            h.pl("Username not found. Please retry.");
        }
    }
    
    public boolean existsUN(String username){
        if(myHashMap.containsKey(username)){
            return false;
        }
        return true;
    }
    
    public boolean existsUNP(String username, String password){
        if(myHashMap.containsKey(username)){
            String[] value = (String [])myHashMap.get(username);
            if(value[0].equals(password)){
                return true;
            }
        }
        return false;
    }
    
    //retrieves entry from hashmap and checks password before editing information
    public boolean editZIP(String username, String password, String zipcode){
        if(myHashMap.containsKey(username)){
            if(zipcode.matches("\\d{5}")){
                String [] value = (String [])myHashMap.get(username);
                if(value[0].equals(password)){
                    value[3] = zipcode;
                    myHashMap.replace(username, value);
                    h.pl("Your zipcode has been updated.");
                    return true;

                }
                else{
                    h.pl("Incorrect username or password. Please retry. ");
                    return false;
                }
            }
            h.pl("Please retry with a correctly formatted zipcode.");
            return false;
        }    
        h.pl("Username not found. Please retry.");
        return false;
    }
    
    //retrieves entry from hashmap and checks password before editing information
    public boolean editCC(String username, String password, String CC){
        if(myHashMap.containsKey(username)){
            if(CC.matches("\\d{8,19}")){
                String [] value = (String [])myHashMap.get(username);
                if(value[0].equals(password)){
                    value[2] = CC;
                    myHashMap.replace(username, value);
                    h.pl("Your credit card information has been updated.");
                    return true;
                }
                else{
                    h.pl("Incorrect username or password. Please retry. ");
                    return false;
                }
            }
            h.pl("Please retry with a correctly formatted credit card number.");
            return false;
        }
        h.pl("Username not found. Please retry.");
        return false;
    }
    
    //retrieves entry from hashmap and checks password before editing information
    public boolean editEmail(String username, String password, String email){
        if(myHashMap.containsKey(username)){
            String [] value = (String [])myHashMap.get(username);
            if(value[0].equals(password)){
                value[1] = email;
                myHashMap.replace(username, value);
                h.pl("Your email has been updated.");
                return true;
            }
            else{
                h.pl("Incorrect username or password. Please retry. ");
                return false;
            }
        }
        h.pl("Username not found. Please retry.");
        return false;
    }
    
    //adds 18 years to the user's DOB and then compares it to the current date.
    //if the current data is after the user's 18th birthday then it returns true
    public boolean ofAge18(String username){
        if(myHashMap.containsKey(username)){
            String[] value = (String [])myHashMap.get(username);
            String dob = value[4];
            String [] date = dob.split("/");
            int year = Integer.parseInt(date[2]);
            int month = Integer.parseInt(date[0]);
            int day = Integer.parseInt(date[1]);
            Calendar userDOB = Calendar.getInstance();
            //subtracts one from month since calendar package starts at 0
            userDOB.set(year, month - 1, day);
            userDOB.roll(Calendar.YEAR, 18);
            Calendar now = Calendar.getInstance();
            if(now.after(userDOB)){
                return true;
            }
            else{
                return false;
            }
        }
        h.pl("Username not found. Please retry.");
        return false;
    }
    
    //Checks if user is 21
    public boolean ofAge21(String username){
        if(myHashMap.containsKey(username)){
            String[] value = (String [])myHashMap.get(username);
            String dob = value[4];
            String [] date = dob.split("/");
            int year = Integer.parseInt(date[2]);
            int month = Integer.parseInt(date[0]);
            int day = Integer.parseInt(date[1]);
            Calendar userDOB = Calendar.getInstance();
            userDOB.set(year, month - 1, day);
            userDOB.roll(Calendar.YEAR, 21);
            Calendar now = Calendar.getInstance();
            if(now.after(userDOB)){
                return true;
            }
            else{
                return false;
            }
        }
        h.pl("Username not found. Please retry.");
        return false;
    }
    
    
    public HashMap returnHash(){
        return myHashMap;
    }
    
    public final class UserAccount implements UserAccountInterface{
        private String username;
        private String [] array = new String[5];
        
        public UserAccount(String username, String password,String email, String CC, String zipcode, String birthdate){
            setUN(username);
            setArray(password, email, CC, zipcode, birthdate);
        }
        
        //adds the user information to set locations in a string array of size 5
        @Override
        public void setArray(String password,String email, String CC, String zipcode, String birthdate){
            setPW(password);
            setEmail(email);
            setCC(CC);
            setZIP(zipcode);
            setBirthdate(birthdate);
        }
        @Override
        public String[] getArray(){
            return array;
        }
        
        //username is the only piece of user information not including in the array since
        //it is being used as the hashmap key with the array as the value and
        //would be redundant and use extra memory pointlessly
        @Override
        public String getUN(){
            return username;
        }
        @Override
        public void setUN(String un){
            this.username = un;
        }
        
        @Override
        public String getPW(){
            return array[0];
        }
        @Override
        public void setPW(String pw){
            this.array[0] = pw;
        }
        
        @Override
        public String getEmail(){
            return array[1];
        }
        @Override
        public void setEmail(String email){
            this.array[1] = email;
        }
        
        @Override
        public String getCC(){
            return array[2];
        }
        @Override
        public void setCC(String cc){
            this.array[2] = cc;
        }
        
        @Override
        public String getZIP(){
            return array[3];
        }
        @Override
        public void setZIP(String zip){
            this.array[3] = zip;
        }
        
        @Override
        public String getBirthdate(){
            return array[4];
        }
        
        @Override
        public void setBirthdate(String bd){
            this.array[4] = bd;
        }
           
    }
}
