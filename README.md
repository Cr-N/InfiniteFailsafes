# Infinite Failsafes

This project is meant to make making failsafes for your FTC Autos easier.
See the [examples folder](https://github.com/Cr-N/InfiniteFailsafes/tree/main/Examples) to see how to implement a simple failsafe.

How to use it:

### Prerequisites

[FtcLib](https://docs.ftclib.org/ftclib/installation) is used for Command Scheduling and handiling. But you can modify this to your liking, this is only a template of how to manage Finite State Machines in an FTC Autonomous.

## 
Make each autonomous state a class of it's own which extends ```AutoState``` 

For example :
```java
public class MyState extends AutoState{
  
}
```

After that, you have a bunch of methods which will handle your State-switching logic and a bunch of methods to keep things organized:
Here are the methods you can use in a class which extends AutoState:
```java
    /**
     * Executes once on state entry.
     */
    public void onEnter(){};

    /**
     * Run continuously while in the state.
     */
    public void update(){};

    /**
     * Is checked every loop to see if all the actions related to the state are done.
     * @return true if the actions have finished, false if the actions have NOT finished
     */
    public boolean isFinished(){
        return endState; // finishes instantly
    };

    /**
     * Executes this once after isFinished return true
     */
    public void end(){};

    /**
     * Decide what state to come after the current one.
     * @return
     */
    public AutoState getNextState(){return null;};

```

### Quality Of Life Features

#### Command States Map (m_cmds)
Each AutoState has a Map which can store the status of any scheduled command you might want your robot to run.
Each command has these states: 
```java
INITIALIZED,
EXECUTING,
FINISHED
```
This allows you to basically track the status of each command and make decisions based on that in your ```update()``` method.

For example, in the [```MoveServoAroundState class```](https://github.com/Cr-N/InfiniteFailsafes/blob/main/Examples/MoveServoAroundState.java), we check if the ```TESTING_FAILSAFE``` command is in the ```EXECUTING``` and make a failsafe based on some sensor input (in this case, if the Magnetic Limit Swithch detects a magnet *or* if the Color/Distance sensor reads a distance less than 20 mm)

#### EndState Boolean Flag

In the AutoState class, there is a boolean flag called ```endState``` which is used by the ```isFinished()``` method. This is meant to be used as a flag which determines when your robot has finished a state. By defaut, it is false, meaning that states will run forever by default unless you specifically set ```endState``` to ```false```.
You can see an example of using the ```endStat``` flag in the [GoToHomeFailsafe class](https://github.com/Cr-N/InfiniteFailsafes/blob/main/Examples/GoToHomeFailsafe.java). 

Made by Cr-N of team #20965 BIT-MO
Follow us on:

- [Instagram](https://www.instagram.com/bitmo_robotics/) 
- [TikTok](https://www.tiktok.com/@bitmo_robotics)
- [Facebook](https://www.facebook.com/profile.php?id=100078726120191&__cft__[0]=AZXiuSxHPL1XO2EMJh-P9F0iY52bltwnCPstmCDY-snTj0NPNLn13C80LxWvjlnFhkase7OhRFGmYgA5DJ06Rs9bw73bLOUgap1O8XQr0LLT6aZynWX7TVXvoHIBro60mzc86pvjzPuik1G3CvuDYElrh5dk8A2u5kxNzwHxGOJP3FKjUb2RMf1qAay7di8yC70&__tn__=%3C%3C%2CP-R)
