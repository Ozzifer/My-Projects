/*
Course:     CS4310: Operating Systems
Project:    Program 2
Due:        March, 25th 2019
By:         Cayce Osborn
 */

package onePerson;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.Scanner;


public class OnePerson extends Thread
{
    static int counter =0;
    int exitCount = 0;
    int value = 3;
    int id = 0;
    int gender;
    int time;
    static int departureNum =0;
    int max = 3;
    int genVal = -1;
    int manLineLength = 0;
    int womanLineLength = 0;
    static Semaphore bathroomStalls = new Semaphore(3);
    private static Lock rRLock = new ReentrantLock();
    private static Lock lockOnArrival = new ReentrantLock() ;
    private static Lock lockOnEntry = new ReentrantLock() ;
    private static Lock lockOnDeparture = new ReentrantLock() ;
    private static Lock inUse = new ReentrantLock(false) ;
    private static Condition genInUseCond = inUse.newCondition();
    static private Queue<OnePerson> initialQ = new LinkedList<>();
    static private Queue<OnePerson> womanQ = new LinkedList<>();
    static private Queue<OnePerson> manQ = new LinkedList<>();
    Random rand = new Random();
    public int lockHimUp;
    public int identity;

    //default constructor
    public OnePerson(){
        id = counter++;
        if(Math.random()<= .60)
            gender = 1;
        else{
            gender = 0;
        }
        time = 5;
    }
 
    //creating an person object
    public OnePerson(int id,int gender, int time){
        id = counter++;
        if(Math.random()<= .60)
            gender = 1;
        else{
            gender = 0;
        }
        time = 5;
  
    }
    
    //to put a number to each person leaving the bathroom
    private synchronized int departureIndex(){
        departureNum = departureNum + 1;
        return departureNum;
    }
 
    //to identify which is which during System.out calls
    public String gen(int gender){
        String sex;
        if(gender == 0)
            sex = "man";
        else
            sex="woman";
        return sex;
    }

    
    //the gender currently inside of the bathroom
    public void inUse(int gender){
        genVal = gender;
    }
    
    
    //to be able to quickly swap the conditional value of the gender
    //currently inside of the bathroom
    public void switchUse(int gender){
        if(gender == 0){
            genVal = 1;
        }
        if(gender == 1){
            genVal = 0;
        }
    }
    
    
    //to allow the person to use the bathroom, or wait for a stall,
    //or for the opposite gender to finish up
    private void arrive(int id, int gender){
     /*   
            if(bathroomStalls.availablePermits()==0){
                genInUseCond.awaitUninterruptibly();
            }
            if(bathroomStalls.availablePermits()!=0){
                
                }
                if(value !=3 && gender == genVal){
                    value++;
                    if(genVal==gender&&bathroomStalls.availablePermits()!=0){
                       // rRLock.unlock();  
                        return;
                    }
                    if(genVal!=gender){
                        genInUseCond.awaitUninterruptibly();
                    }
            }
            
            if(gender == 0&&womanQ!=null&&bathroomStalls.availablePermits()==0)
            {
                genInUseCond.awaitUninterruptibly();
            }
            if(gender == 1&&manQ!=null&&bathroomStalls.availablePermits()==0)
            {
                genInUseCond.awaitUninterruptibly();
            }
            if(gender==0&&womanQ==null&&bathroomStalls.availablePermits()==0)
            {
                genInUseCond.awaitUninterruptibly();
            }
            if(gender==1&&manQ==null&&bathroomStalls.availablePermits()==0){
                genInUseCond.awaitUninterruptibly();
            }
            inUse(gender);
            if(gender==0&&womanQ==null&&bathroomStalls.availablePermits()>=1){
                genInUseCond.signal();
            }
            else if(gender == 1&&manQ==null&&bathroomStalls.availablePermits()>=1){
                genInUseCond.signal();
            }
        }
        finally{
            rRLock.unlock();        
        }*/
        
        lockOnArrival.lock();
        try {
            if(gender == 0)
            {
                manQ.add(this);
            }
            else{
                womanQ.add(this);
            }
            
            //if the bathroom is empty, enter, set the value of the bathroom
            //the the appropriate gender. Deduce the integer "value" by one
            //to indicate how many of the same sex may have entered simultaneously
            //prior to the opposing sex.
            if(genVal == -1){
                inUse(gender);
                if(gender == 0){
                    manQ.remove();
                }
                if(gender == 1){
                    womanQ.remove();
                }
                System.out.println(gen(gender)+id+": seeing if stall is empty...");
                bathroomStalls.acquire();
                value--;
                System.out.println(gen(gender)+id+": got the stall!");
                System.out.println(gen(gender)+id+": available stalls now: "+ bathroomStalls.availablePermits());
                    return;
            }
            
            if(genVal ==gender && bathroomStalls.availablePermits()!=0&&value!=0){
                System.out.println(gen(gender)+id+": seeing if stall is empty...");
                bathroomStalls.acquire();
                value--;
                System.out.println(gen(gender)+id+": got the stall!");
                System.out.println(gen(gender)+id+": available stalls now: "+ bathroomStalls.availablePermits());
                    return;
            }
            //if the gender accessing the bathroom is not the gender using the
            //bathroom, and the stalls aren't available, and the number of 
            //accessed stalls by the same gender isn't 0, 
            if(genVal !=gender && bathroomStalls.availablePermits()!=0&&value!=0){
                OnePerson.sleep(5000);
            }
            
            //if the gender accessing is NOT the same as the gender inside,
            //yet there are available permits, check to see if there is still a 
            //queue of the opposite sex waiting. If not, wait for the people 
            //inside to finish, then enter.
            if(genVal != gender &&bathroomStalls.availablePermits()!=0){
                if(gender == 0&&womanQ==null){
                    switchUse(gender);
                    value = 3;
                    OnePerson.sleep(5000);
                }
                if(gender == 1&&manQ==null){
                    switchUse(gender);
                    value=3;
                    OnePerson.sleep(5000);
                }
            }
            if(genVal == gender && value == 0){
                if(gender == 0&&womanQ!=null){
                    switchUse(gender);
                    value = 3;
                    inUse.lock();
                    OnePerson.sleep(5000);
                }
                if(gender == 1&&manQ!=null){
                    switchUse(gender);
                    value = 3;
                    inUse.lock();
                    OnePerson.sleep(5000);
                }
               
            }
            if(genVal==gender&&value!=0&&bathroomStalls.availablePermits()!=0){
                inUse(gender);
                System.out.println(gen(gender)+id+": seeing if stall is empty...");
                bathroomStalls.acquire();
                value--;
                System.out.println(gen(gender)+id+": got the stall!");
                System.out.println(gen(gender)+id+": available stalls now: "+ bathroomStalls.availablePermits());
                return;
            }
            
            
            }catch(InterruptedException e){
               e.printStackTrace();
            }finally{
               lockOnArrival.unlock();
            }
        }       
    
    
    private void useFacilities(int id,int gender,int time){
        try{
            System.out.println(gen(gender)+id+": is dropping a duece");
            this.sleep(5000);
        }catch(InterruptedException e)
        {  
        }finally{
        }
        //The usefacilities procedure should just delay for time seconds and print
            //out a debug message.
        //So, some type of delay of 5 seconds, then System.out.println()
    }
    
        //Depart is called to indicate that the person is ready to exit.
        //Depart should take steps to let additional people enter the restroom.
        // Prints out the departure index for 
        //  that person.
    private void depart(int id, int gender){
        lockOnDeparture.lock();
        try{
            
         System.out.println(gen(gender)+id+": feels refreshed!");
         bathroomStalls.release();
         exitCount++;
         if(exitCount == 3||bathroomStalls.availablePermits()==3){
             value = 3;
             exitCount = 0;
         }
         System.out.println(gen(gender)+id+": available stalls now: "+bathroomStalls.availablePermits());
         System.out.println(gen(gender)+id+"'s departure index is: "+departureIndex());
        
        }finally{
            lockOnDeparture.unlock();
            }
        
                
    }
    
    //setters and getters for gender and id.
    public int getPersonGender(){
        return gender;
    }

    
    public int getPersonId(){
        return id;
    }
    
    
    
    @Override
    public void run()
    {           
            arrive(id,gender);
            useFacilities(id,gender,time);
           try{
               
            rRLock.lock();
            depart(id,gender);
            }finally{
                rRLock.unlock();
            }
        
        }
    public static void main(String args[]) 
    {
        Scanner input = new Scanner(System.in);
        System.out.println("Please select an option: \n 1: Three groups of five "
                + "people with a delay of 10 seconds "
                + "between them.\n 2: Two groups of ten people with a delay of"
                + "10 seconds between them. \n 3: A group of twenty people "
                + "REALLY need to use the restroom, RIGHT NOW!");
        
        int choice = input.nextInt();
        switch(choice){
                case 1:
                    OnePerson people[] = new OnePerson[15];
                    for( int i = 0; i<15; i++)
                    {
                        OnePerson newPerson = new OnePerson();
                        newPerson.id = i;
                        initialQ.add(newPerson);
                        people[i] = newPerson;
                        System.out.print(people[i].getPersonId());
                        System.out.println(people[i].getPersonGender());
                        if(newPerson.gender == 0){
                            manQ.add(newPerson);
                        }
                        else{
                            womanQ.add(newPerson);
                        }
                    }
                    for(int i = 0; i<5;i++){
                        people[i].start();
                    }
                    try{
                    java.util.concurrent.TimeUnit.SECONDS.sleep(10);
                    }catch(InterruptedException e){
                    }
                    for(int i = 5; i<10;i++){
                        people[i].start();
                    }
                    try{
                    java.util.concurrent.TimeUnit.SECONDS.sleep(10);
                    }catch(InterruptedException e){
                    }
                    for(int i = 10;i<15;i++){
                        people[i].start();
                    }
                    break;
                case 2:
                    OnePerson people1[] = new OnePerson[20];
                    for(int i = 0; i<20; i++)
                    {
                        OnePerson newPerson = new OnePerson();
                        newPerson.id = i;
                        initialQ.add(newPerson);
                        people1[i] = newPerson;
                        System.out.print(people1[i].getPersonId());
                        System.out.println(people1[i].getPersonGender());
                        if(newPerson.gender == 0){
                            manQ.add(newPerson);
                        }
                        else{
                            womanQ.add(newPerson);
                        }
                    }
                    for(int i = 0; i<10;i++){
                        people1[i].start();
                    }
                    try{
                    java.util.concurrent.TimeUnit.SECONDS.sleep(10);
                    }catch(InterruptedException e){
                    }
                    for(int i = 10;i<20;i++){
                        people1[i].start();
                    }
                    break;
                    
                    
                case 3:
                    OnePerson people2[] = new OnePerson[20];
                    for(int i = 0; i<20; i++)
                    {
                        OnePerson newPerson = new OnePerson();
                        newPerson.id = i;
                        initialQ.add(newPerson);
                        people2[i] = newPerson;
                        System.out.print(people2[i].getPersonId());
                        System.out.println(people2[i].getPersonGender());
                        if(newPerson.gender == 0){
                            manQ.add(newPerson);
                        }
                        else{
                            womanQ.add(newPerson);
                        }
                    }
                    for(int i = 0; i<10;i++){
                        people2[i].start();
                    }
                    break;
        }
        
         
     
    }
}
