/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ecosystem;

/**
 *
 * @author kzacherl
 */
public class Grass implements Edible {

    public String toString(){
        return "Grass";
    }

    @Override
    public int getNutrition() {
        return 1;
    }
    
}
