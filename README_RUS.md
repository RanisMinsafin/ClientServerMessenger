# Как запустить "Клиент-Серверный Мессенджер"

Прежде чем продолжить, убедитесь, что у вас установлены Java и Maven на вашей системе.

## Сборка проектов

Склонируйте репозитории и перейдите в директории проектов:

    git clone <repository URL>
    cd SocketServer

Соберите проект SocketServer с использованием Maven:

    mvn clean package

Запустите SocketServer:

    java -jar target/SocketServer-1.0-SNAPSHOT-jar-with-dependencies.jar --port=8080

Перейдите в директорию SocketClient:

    cd ../SocketClient

Соберите часть проекта SocketClient с использованием Maven:

    mvn clean package

Запустите SocketClient:

    java -jar target/SocketClient-1.0-SNAPSHOT-jar-with-dependencies.jar --server-port=8080

## О приложении

### Действия пользователя:

1.  Создание чат-комнаты: Пользователи могут создать новую чат-комнату.
2.  Выбор чат-комнаты: Пользователи могут выбрать и присоединиться к существующей чат-комнате.
3.  Отправка сообщения: Пользователи могут отправлять сообщения в выбранной чат-комнате.
4.  Покинуть чат-комнату: Пользователи могут выйти из текущей чат-комнаты.

### История сообщений

При повторном входе в приложение отображаются последние 30 сообщений из предыдущей посещенной комнаты.

### Пример взаимодействия:

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