/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ecosystem;

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author kzacherl
*/
public class Acre{
    public Acre(){
        populate();
        initMeadow();
    }
    
    //
   
    //reset in reproduce function
    //incrememnt in eat or migrate
    //change how many times has eaten per turn
    //increment up when migrate
    //set max grass
    
    //cyclesPerClick  text box and allows user to input a number
    //trycatch number format error
    //change random amount of wolves and sheep to user input
   
    private int maxAnimals = 20;
    //here or in world?
    private ArrayList<Animal> inhabs = new ArrayList();
    //this is the first acre
    //can make a 2d array with acres in them and easier to link

    private Acre north = null;
    private Acre west = null;
    private ArrayList<Grass> meadow = new ArrayList();
    private int meadowCapacity = 20;
    private Acre east = null;
    private Acre south = null;

    
    public boolean isFull(){
        int numInhabs = getNumInhabs();
        if(numInhabs >= maxAnimals){
            return true;
        }
        else{
            return false;
        }
    }
    
    public int getNumInhabs(){
        return inhabs.size();
    }
   
    public ArrayList<Animal> getInhabs(){
        return inhabs;
    }
    
    public int numGrass(){
        return meadow.size();
    }
    
    public int numSheep(){
        int numSheep = 0;
        for(int i = 0; i < inhabs.size(); i++){
            if((inhabs.get(i).toString()).equals("Sheep")){
                numSheep++;
            }
        }
        //h.pl("Num of sheep is " + numSheep);
        return numSheep;
    }
    
    public int numWolves(){
        int numWolves = 0;
        for(int i = 0; i < inhabs.size(); i++){
            if(inhabs.get(i).toString().equals("Wolf")){
                numWolves++;
            }
        }
        //h.pl("Num of wolves is " + numWolves);
        return numWolves;
    }
    public void populate(){
        //Sheep from 3-5
        int numSheep = (int)((Math.random() * 3) + 2);
        //System.out.println("Num of sheep is " + numSheep);
        
        //Wolves from 2-3
        int numWolves = (int)((Math.random() * 2) + 1);
        //System.out.println("Num of wolves is " + numWolves);
        
        //adding the randomly generated number of wolves and sheeps to inhabs
        for (int j = 0; j < numSheep + 1; j++){
            inhabs.add(new Sheep(this));
        }
    
        for (int q = 0; q < numWolves + 1; q++){
            inhabs.add(new Wolf(this));
        }
        
        //randomly arranges animals
        Collections.shuffle(inhabs);
        
    }
    
    private void initMeadow(){
        //Random amount of grass between 10 and 20
        int grass = (int)(Math.floor(Math.random() * 10) + 10);  
        for(int i = 0; i < grass; i++){
            meadow.add(new Grass());
        }    
    }
    
    public Grass getGrass(){
        if(!meadow.isEmpty()){
            return meadow.remove(meadow.size() - 1);
        }
        else{
            return null;
        }
    }
    
    public void growGrass(){
        //Adding 5 grass per turn
        for(int q = 0; q < 5; q++){
            if(meadow.size() != meadowCapacity){
                meadow.add(new Grass());
            }
        }    
    }
    
    public void setNorth(Acre neighbor){
        north = neighbor;
    } 
    
    public void setEast(Acre neighbor){
        east = neighbor;
    }
    
    public void setSouth(Acre neighbor){
        south = neighbor;
    }
 
    public void setWest(Acre neighbor){
        west = neighbor;
    }
    
    public Acre north(){
        return north;
    }

    public Acre south(){
        return south;
    }

    public Acre east(){
        return east;
    }

    public Acre west(){
        return west;
    }

    public void removeAnimal(Animal me){
        inhabs.remove(me);
    }

    public void addAnimal(Animal me){
        inhabs.add(me);
    }

    public void resetRepro(){
        for(int i = 0; i < inhabs.size(); i++){
            Animal currentAnimal = inhabs.get(i);
            currentAnimal.reproduced = false;
        }
    }
    public void loopAnimals() {
        for(int i = 0; i < inhabs.size(); i++){
            Animal currentAnimal = inhabs.get(i);
            currentAnimal.age++;
            currentAnimal.hunger = currentAnimal.hunger + 3;
            if(currentAnimal.age == currentAnimal.lifeSpan()){
                currentAnimal.die();
            }
            if(currentAnimal instanceof Sheep){
                ((Sheep)currentAnimal).graze();
            }
            else if((currentAnimal instanceof Wolf)){
                ((Wolf)currentAnimal).hunt();
            }
            if(currentAnimal.hunger > 15){
                currentAnimal.die();
            }
            else if(currentAnimal.hunger > 5){
                currentAnimal.migrate();
            }
            else if(currentAnimal.hunger < 5 && currentAnimal instanceof Sheep && currentAnimal.reproduced == false){
                currentAnimal.reproduce();
            }
            else if(currentAnimal.hunger < 2 && currentAnimal instanceof Wolf && currentAnimal.reproduced == false){
                currentAnimal.reproduce();
            }
        }
    }
}
