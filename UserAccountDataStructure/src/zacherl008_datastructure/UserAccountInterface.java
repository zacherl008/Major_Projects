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
