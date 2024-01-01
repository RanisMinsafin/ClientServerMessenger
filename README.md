# How to Run the "Client Server Messenger"
Before proceeding, ensure that you have Java and Maven installed on your system.

## Build the Projects

Clone the repositories and navigate to the project directories:

    git clone <repository URL>
    cd SocketServer

Build the SocketServer project using Maven:

    mvn clean package

Run SocketServer:

    java -jar target/SocketServer-1.0-SNAPSHOT-jar-with-dependencies.jar --port=8080

Navigate to the SocketClient directory:

    cd ../SocketClient

Build the SocketClient part of project using Maven:

    mvn clean package

Run SocketClient:

    java -jar target/SocketClient-1.0-SNAPSHOT-jar-with-dependencies.jar --server-port=8080

## About application

### User Actions:
1. Create a Chatroom: Users can create a new chatroom.
2. Choose a Chatroom: Users can select and join an existing chatroom.
3. Send a Message: Users can send messages within the chosen chatroom.
4. Leave a Chatroom: Users can exit the current chatroom.

### Message History

When a user re-enters the application, the last 30 messages from the previously visited room are displayed.

### Example Interaction:
```
Hello from Server!
1. signIn
2. SignUp
3. Exit
> 1
Enter username:
> Marsel
Enter password:
> qwerty007
1.	Create room
2.	Choose room
3.	Exit
> 2
Rooms:
1. First Room
2. SimpleRoom
3. JavaRoom
4. Exit
> 3
Java Room ---
JavaMan: Hello!
> Hello!
Marsel: Hello!
> Exit
You have left the chat.
```