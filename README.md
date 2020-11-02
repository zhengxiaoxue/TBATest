## Objective of the case 
Build an application with moving cranes on a straight track (ASC), 2 per track, and user interaction. 
1.	Instantiate between 1 and X pairs of cranes.
2.	Move either one of the cranes from any starting position on the track to any end position over the whole length of the track, apart from the first and the last positions (parking locations).  
3.	Move out the second crane of the way if that crane is in the way of the first crane.  
  4.  See where the vehicles are and how they are moving along the track.

## Technical tacks
1. Java8
2. Undertow, Restful API and WebSocket
3. HTML5 and JQuery
4. Junit4 and Mockito
5. Maven

## Architecture
 [!image](https://github.com/zhengxiaoxue/TBATest/blob/main/UI.png)
## Run

This project is maven based, to run this project you must have the environment with >=Java8 and Maven3.x, IntelliJ IDEA is highly recommended. 
Please make sure the ports 8081 and 8082 are available. Port 8081 is for Restful Service, 8082 is for WebSocket Server.

### Run from source code:
Run Main.class, open your browser, visit http:localhost:8081

### Run from command:
1). Go to the base directory run: maven clean assembly:assembly
You can use –Dmaven.test.skip to speed up
2). cd target/
3) java –jar TBATask.jar
OR
From this directory, run java –jar TBATask.jar

Then visit http://localhost:8081/index.html

### Design Notice:
When moving a crane, if there are other cranes in the way, , we have to move away these cranes. So I suppose all the cranes are same, when I move the crane I just move cranes to left or right together until one crane reached the target position and also keep positions previous having crane still having crane. Hope this suppose doesn’t violate the user case.





