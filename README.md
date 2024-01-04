# Enterprise Development Experience Project

## BNBs - Bed & Breakfast Management System

---

### Team Members:

- Brent Simons
- Siebe Michiels

### Description:

This repository contains our school project for the Enterprise Development Experience class. Our project, titled "BNBs - Bed & Breakfast Management System" is designed to demonstrate our understanding of enterprise-level application development using microservices architecture. We have implemented four microservices that collectively form a comprehensive management system for managing multiple Bed & Breakfast establishments. Our inspiration for this project came from the Bed and Breakfast owned by Brent's father.

### Microservices:

| API Name    | Description                               | Database |
|-------------|-------------------------------------------|----------|
| Bnb         | Bed and breakfast establishments          | MySQL    |
| Room        | Rooms available in a BnB establisment     | MongoDB  |
| Reservation | Reservations made under a room            | MySQL    |
| Person      | Customers that are recorded in the system | MongoDB  |

### ERD Model

![Our Entity Relationship Diagram](./erd.png)

## API Gateway

### API/Docker compose Diagram

![draw.io diagram of our Microservices Architecture](./APIDiagram.drawio.png)

In this diagram you can see an overview of our microservices architecture

### Endpoints:

<<<<<<< HEAD
| HTTP <br> method | Endpoint                                  |   Service   | Description                                                                                      |               Authorized                | Parameters/Body                      |
|------------------|-------------------------------------------|:-----------:|--------------------------------------------------------------------------------------------------|:---------------------------------------:|--------------------------------------|
| GET              | /public/bnb?name=                         |     Bnb     | Retrieve all bnb's that have the given parameter in their name                                   | $${\color{red}Red}$$  | `name`                               |
| GET              | /public/rooms/available?id=               |     Bnb     | Retrieve all rooms from given `bnbId` depending on the given filters (see endpoints_examples.md) | $${\color{green}Green}$$  | `bnbId` <br> `AvailableRoomRequest`  |
| GET              | /bnb/all?name=                            |     Bnb     | Same as /public/bnb?name=                                                                        | <span style="color: #47d147">Yes</span> | `name`                               |
| GET              | /bnb/{id}                                 |     Bnb     | Retrieve one  bnb with given `id`                                                                | <span style="color: #47d147">Yes</span> |                                      |
| POST             | /bnb                                      |     Bnb     | Create a new bnb                                                                                 | <span style="color: #47d147">Yes</span> | `BnbRequest`                         |
| PUT              | /bnb/{id}                                 |     Bnb     | Update the bnb with given `id`                                                                   | <span style="color: #47d147">Yes</span> | `BnbRequest`                         |
| DELETE           | /bnb/{id}                                 |     Bnb     | Delete the bnb with given `id`                                                                   | <span style="color: #47d147">Yes</span> | `BnbRequest`                         |
| PUT              | /bnb/addRoom?bnbId= &roomCode=            |     Bnb     | Add a new room with `roomCode` to bnb with given `bnbId`                                         | <span style="color: #47d147">Yes</span> | `bnbId` <br> `roomCode`              |
| PUT              | /bnb/removeRoom?bnbId= &roomCode=         |     Bnb     | Remove the room with `roomCode` from bnb with given `bnbId`                                      | <span style="color: #47d147">Yes</span> | `bnbId` <br> `roomCode`              |
| GET              | /public/room/available?roomCode= &months= |    Room     | Retrieve all periods during which a given room is available                                      | <span style="color: #ff1a1a">No</span>  | `roomCode` <br> `months` (default=1) |
| GET              | /room/all                                 |    Room     | Retrieve all rooms                                                                               | <span style="color: #47d147">Yes</span> |                                      |
| GET              | /room/{id}                                |    Room     | Retrieve one room with given `id`                                                                | <span style="color: #47d147">Yes</span> |                                      |
| POST             | /room                                     |    Room     | Create a new room, also add the room to the bnb with `bnbId` given in the request body           | <span style="color: #47d147">Yes</span> | `RoomWithBnbRequest`                 |
| PUT              | /room/{id}                                |    Room     | Update the room with given `id`                                                                  | <span style="color: #47d147">Yes</span> | `RoomRequest`                        |
| DELETE           | /room/{id}?bnbId=                         |    Room     | Delete the room with given `id`, also remove it from given `bnbId`                               | <span style="color: #47d147">Yes</span> | `bnbId`                              |
| GET              | /reservation/all                          | Reservation | Retrieve all reservations                                                                        | <span style="color: #47d147">Yes</span> |                                      |
| GET              | /reservation/{id}                         | Reservation | Retrieve one reservation with given `id`                                                         | <span style="color: #47d147">Yes</span> |                                      |
| POST             | /reservation                              | Reservation | Create a new reservation                                                                         | <span style="color: #47d147">Yes</span> | `ReservationRequest`                 |
| PUT              | /reservation/{id}                         | Reservation | Update the reservation with given `id`                                                           | <span style="color: #47d147">Yes</span> | `ReservationRequest`                 |
| DELETE           | /reservation/{id}                         | Reservation | Delete the reservation with given `id`                                                           | <span style="color: #47d147">Yes</span> |                                      |
| GET              | /person/all                               |   Person    | Retrieve all persons                                                                             | <span style="color: #47d147">Yes</span> |                                      |
| GET              | /person/{id}                              |   Person    | Retrieve one person with given `id`                                                              | <span style="color: #47d147">Yes</span> |                                      |
| POST             | /person                                   |   Person    | Create a new person                                                                              | <span style="color: #47d147">Yes</span> | `PersonRequest`                      |
| PUT              | /person/{id}                              |   Person    | Update the person with given `id`                                                                | <span style="color: #47d147">Yes</span> | `PersonRequest`                      |
| DELETE           | /person/{id}                              |   Person    | Delete the person with given `id`                                                                | <span style="color: #47d147">Yes</span> |                                      |


For more info about our endpoints please view the file [endpoint_examples.md](endpoint_examples.md)


