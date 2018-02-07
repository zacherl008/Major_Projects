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
public class Sheep extends Animal implements Edible{
    public Sheep(){}
    
    public Sheep(Acre myAcre) {
       currentAcre = myAcre;
    }

    @Override
    public int getNutrition() {
        return 10;
    }
    
    @Override
    public String toString(){
        return "Sheep";
    }
    
   
    public void graze(){
        Grass meal = currentAcre.getGrass();
        if(meal != null){
            eat(meal);
        }   
    }
    
    @Override
    public void eat(Edible meal){
        if(meal instanceof Grass){
            hunger -= meal.getNutrition(); 
            if(hunger < 0){
              hunger = 0;
            }
        }
        else{
            System.out.println("Yuck, you want me to eat what??");
        }
    }

    @Override
    public int lifeSpan() {
        return 10;
    }
}
