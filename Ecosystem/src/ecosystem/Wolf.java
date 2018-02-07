/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ecosystem;

import java.util.ArrayList;

/**
 *
 * @author kzacherl
 */
public class Wolf extends Animal{

    Wolf(Acre myAcre) {
        currentAcre = myAcre;
    }

    @Override
    public String toString(){
        return "Wolf";
    }

    public void hunt(){
       for(int i = 0; i < currentAcre.getInhabs().size(); i++){
            if((currentAcre.getInhabs().get(i).toString()).equals("Sheep")){
                Edible meal = (Edible)currentAcre.getInhabs().get(i);
                currentAcre.getInhabs().remove(i);
                eat(meal);
                break;
            }
       }
       
    }
    
    @Override
    public void eat(Edible meal){
        if(meal instanceof Sheep)
        hunger -= meal.getNutrition(); 
        if(hunger < 0){
            hunger = 0;
        }
    }

    @Override
    public int lifeSpan() {
        return 5;
    }
    
}
