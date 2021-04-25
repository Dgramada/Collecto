# Example game 
In this document, the networking messages that are exchanged between a particular client and server during the execution of a game are shown. Note that, while both client and server support extensions, none of them are used since none of them are in common. 

The messages are shown below. The sentences in between (in *italics*) indicate the phase of the connection or a different event happening which is not in the log.   
The client is called Alice, her opponent in the game will be Bob. The server was made by Charlie. Only messages between Alice and the server are shown. 

*Start of initialization sequence*
- Client -> server: `HELLO~Masterpiece of Alice~RANK~CRYPT`
- Server -> client: `HELLO~Charlie's domain~AUTH`
- Client -> server: `LOGIN~Alice`
- Server -> client: `LOGIN`
- Client -> server: `QUEUE`

*After some time, Bob also joins and queues for a game*
- Server -> client: `NEWGAME~5~3~4~2~5~3~6~4~6~3~4~3~1~2~5~3~2~1~2~6~5~4~1~4~0~4~1~4~5~6~2~1~5~6~2~3~1~5~4~6~5~3~6~3~6~2~1~2~1~Bob~Alice`

*After some time, Bob has sent a move to the server*
- Server -> client: `MOVE~2` *(the move by Bob)*
- Client -> server: `MOVE~25` *(the move by Alice, after some time)*
- Server -> client: `MOVE~25` *(the server confirming the previous move, almost immediately)*
- Server -> client: `MOVE~10` *(the next move by Bob, after some time)*
*The pattern above continues for some time, until Alice does the last possible move*
- Client -> server: `MOVE~18~2` 
- Server -> client: `MOVE~18~2`
- Server -> client: `GAMEOVER~VICTORY~Bob` *(Bob has earned the most points)*
- Client -> server: `QUEUE` *(Alice wants to play a next game)*
- Connection is broken *(Alice quits out of rage after realizing exactly how silly she just lost)* 