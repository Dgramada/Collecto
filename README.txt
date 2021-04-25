First, using Eclipse or IDEA IntelliJ to import the file. 
If you are using the Eclipse you may not see the colour on the board. You can click the help button —> install new software —> add —> input http://www.mihai-nita.net/eclipse —> finish
Then you can handle the colour problem.

For local game: You will find a class called Collecto in folder gamedesign, run this class you will see a board in console and you can play it!

For Server-Client game: You can find a class CollectoServer in folder server. Run this class and input a port number. Then you need to find a class CollectoClient in folder client.
Run CollectoClient and input user name and host number and the port. When another client join the queue you will start the game. When a board appear on console the game is begin.
Example of Server: 
Welcome to the Abalone Server! Starting...
Please enter the server port.
4114
Attempting to open a socket on port 4114...
Server started at port 4114

Example of Client:
Input your name: 
bob
Input the host: 
localhost
Input the port: 
4114
Connecting to the port 4114 and address localhost/127.0.0.1
Input your command: 

Input user name: 
bob
Welcome to the Collecto: username
Input player name: 
bob

QUEUE

After the board appear:
MOVE~3
MOVE4~12

https://mihai-nita.net/java/