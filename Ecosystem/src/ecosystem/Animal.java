/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ecosystem;

//import ecosystem.World.Acre;

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author kzacherl
 */
public abstract class Animal {  
    protected boolean reproduced = false;
    protected int age = 0;
    protected int hunger = 0;
    protected Acre currentAcre = null;
    
    public Animal (){
    }
        
    public abstract int lifeSpan();
    
    public abstract void eat(Edible meal);
    
    public int getHunger(){
        return hunger;
    }
    
    public boolean reproduce() {  
        //if animal is old enough to reproduce, acre isn't full, and hasn't reproduced this cycle
        if(this.age >= 1 && !currentAcre.isFull() && !this.reproduced){      
            if(this instanceof Sheep){
                for(int i = 0; i < currentAcre.getNumInhabs(); i++){
                    Animal parent = currentAcre.getInhabs().get(i);
                    if(!currentAcre.getInhabs().get(i).reproduced && !this.reproduced && this.age >=1 && parent instanceof Wolf && parent != this){
                        Sheep veal = new Sheep(currentAcre);
                        veal.reproduced = true;
                        this.reproduced = true;
                        parent.reproduced = true;
                        currentAcre.getInhabs().add(veal);
                        return true;
                    }
                }        
            }
            else if(this instanceof Wolf){
                for(int i = 0; i < currentAcre.getInhabs().size(); i++){
                    Animal parent = currentAcre.getInhabs().get(i);
                    if(!currentAcre.getInhabs().get(i).reproduced && !this.reproduced && this.age >= 1 && parent instanceof Wolf && parent != this){
                        Wolf cub = new Wolf(currentAcre);
                        cub.reproduced = true;
                        this.reproduced = true;
                        parent.reproduced = true;
                        currentAcre.getInhabs().add(cub);
                        return true;
                    }                     
                }   
            }
        }
        return false;
    }

    //animal has access to acre and can move around
    int count = 1;
    public void migrate(){
        //Adding all possible directions to an array list and shuffling them 
        ArrayList<Integer> random = new ArrayList();
        for(int i = 1; i < 5; i++){
            random.add(i);
        }
        Collections.shuffle(random);
        Acre start = currentAcre;
        
        //only runs if all directions have not been tried
        if(count < 4){
            int rand = random.get(count);
            count++;
            
            //Moving directionally depending on which direction has been picked
            //1 north 2 east 3 south 4 west
            if(rand == 1 && currentAcre.north() != null && !currentAcre.north().isFull()){
                currentAcre = currentAcre.north();
                currentAcre.addAnimal(this);            
                start.removeAnimal(this);
            }
            else if(rand == 2 && currentAcre.east() != null && !currentAcre.east().isFull()){
                currentAcre = currentAcre.east();
                currentAcre.addAnimal(this);            
                start.removeAnimal(this);   
            }

            else if(rand == 3 && currentAcre.south() != null && !currentAcre.south().isFull()){
                currentAcre = currentAcre.south();
                currentAcre.addAnimal(this);            
                start.removeAnimal(this);
            }
            else if(rand == 4 && currentAcre.west() != null && !currentAcre.west().isFull()){
                currentAcre = currentAcre.west();
                currentAcre.addAnimal(this);            
                start.removeAnimal(this);
            }
            //if null and/or full, chooses a different direction
            else{
                migrate();
            }
        }    
    }
    public void die(){
        currentAcre.removeAnimal(this);
    }
}
