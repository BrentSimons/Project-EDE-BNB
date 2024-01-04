In this file you will be able to see some examples for all our endpoints, at the bottom of each endpoint there will also be a screenshot of a complete request made in Postman.

# Public Endpoints


### 1. GET /public/bnb?name=Hugo

This is a public endpoint that can be used to get an overview of all our bnbs. 

The optional `name` parameter can be used to filter through the bnbs in our system.

If multiple bnbs contain the text of the parameter within their name, they will all be returned.

Example result:

```json
[
    {
        "name": "Hugo's Bnb Ter Dolen",
        "roomsIdList": [ 1, 2 ]
    },
    {
        "name": "Hugo's Bnb Geel",
        "roomsIdList": [ 3, 4 ]
    }
]
```

### 2. GET /public/rooms/available?id=\<bnbId\>

This is a public endpoint that can be used by the client to check if (and which) rooms from a certain bnb are available.

Required body:

```json
{
    "roomCodes": [],
    "startDate": "2024-05-7",
    "endDate": "2024-05-11",
    "size": 3
}
```

The list of `roomCodes` is left empty, this list will automatically be filled with the roomCodes that belong to the given `bnbId` in the url.

The `startDate` and `endDate` are used to check if the rooms are available during a certain period. This way the client can choose when they want to go on a vacation to the given bnb and thus they can easily check if and which rooms are available during that period. The rooms that are not free during the entirety of this period will still be returned but with the value `available: false`.

The `size` parameter can be used to filter rooms which are too small for your group, note that when a room is too small it will not be returned. This way the client can indicate how many people will need to stay in the bnb, only rooms that are big enough (also bigger) will be returned.

Example result:
```json
[
    {
        "roomCode": "Room1_Bnb1",
        "available": false
    },
    {
        "roomCode": "Room2_Bnb1",
        "available": true
    },
    {
        "roomCode": "Room3_Bnb1",
        "available": true
    }
]
```

### 3. GET /public/room/available?roomCode=<Room2_Bnb1>&months=<3>

This is a public endpoint that can be used to check whether a specific room is available within the next month(s).

The `roomCode` parameter is required and specifies which room you want to check availability for.

The `months` parameter is optional, it has default value of 1 but can be changed to any integer. 

Example result:

```json
[
    {
        "startDate": "2023-12-28",
        "endDate": "2024-03-09"
    },
    {
        "startDate": "2024-03-14",
        "endDate": "2024-03-18"
    },
    {
        "startDate": "2024-03-20",
        "endDate": "2024-03-28"
    }
]
```

As you can see in this result the room is available for 3 periods. This means that there are 2 reservations planned for this room (the gaps between the available periods that are returned).



# <br>Authenticated Endpoints

## Bnb

### 1. GET /bnb/all
Returns all bnbs, this  method also has the option to add `?name=<name>` to the url to filter on name. Take a look at /public/bnb at the top of this file for more info.

### 2. GET /bnb/\<id\>
Returns a single bnb that has the given `id`.

### 3. POST /bnb
Create a new bnb.

Required body:
```json
{
    "name": "The Slumburger ",
    "roomCodes": [
        "Room1",
        "Room2",
        "Room3"
    ],
    "city": "Geel",
    "postcode": "2440",
    "address": "Antwerpseweg 117"
}
```

### 4. PUT /bnb/\<id\>
You add the `id` of the bnb you want to edit to the url.

Besides an `id` in the url, this request also needs a body with data for the updated bnb, all fields that are not explicitly assigned a value will be ignored. In the following example only `name` will be updated:
```json
{
    "name": "Only name will be changed",
    "city": null,
    "postcode": null,
    "address": null
}
```

### 5. DELETE /bnb/\<id\>
Delete the bnb that corresponds to the given `id`.



## Room

### 1. GET /room/all
Returns all rooms.

### 2. GET /room/\<id\>
Returns a single room that has the given `id`.

### 3. POST /room
Create a new room.

Required body:
```json
{
    "roomRequest": {
        "roomCode": "Room1_Bnb1",
        "name": "Room 1",
        "size": 3
    },
    "bnbId": 2
}
```

### 4. PUT /room/\<id\>
You have to add the `id` of the room you want to edit to the url.

Besides an `id` in the url, this request also needs a body with data for the updated room, all fields that are not explicitly assigned a value will be ignored. In the following example `name` and `size` will be updated:
```json
{
    "roomCode": null,
    "name": "Dubbele kamer",
    "size": 6
}
```

### 5. DELETE /room/\<id\>
Delete the room that corresponds to the given `id`.



## Reservation

### 1. GET /reservation/all
Returns all reservations.

### 2. GET /reservation/\<id\>
Returns a single reservation that has the given `id`.

### 3. POST /reservation
Create a new reservation.

Required body:
```json
{
    "personId": "2",
    "roomCode": "Room2_Bnb2",
    "startDate": "2024-03-15",
    "endDate": "2024-03-17"
}
```

### 4. PUT /reservation/\<id\>
You have to add the `id` of the reservation you want to edit to the url.

Besides an `id` in the url, this request also needs a body with data for the updated reservation, all fields that are not explicitly assigned a value will be ignored. In the following example only `endDate` will be updated:
```json
{
    "personId": null,
    "roomCode": null,
    "startDate": null,
    "endDate": "2024-03-17"
}
```

### 5. DELETE /reservation/\<id\>
Delete the reservation that corresponds to the given `id`.



## Person

### 1. GET /person/all
Returns all persons.

### 2. GET /person/\<id\>
Returns a single person that has the given `id`.

### 3. POST /person
Create a new person.

Required body:
```json
{
    "firstName": "Adam",
    "lastName": "Johns",
    "dateOfBirth": "2013-09-25",
    "accountNumber": "1000",
    "contact": {
        "phoneNumber": "0589 69 14 32",
        "emailAddress": "adamjohns23@heavenmail.com",
        "address": "AngelStreet 26 Godtown"
    }
}
```

### 4. PUT /person/\<id\>
You have to add the `id` of the person you want to edit to the url.

Besides an `id` in the url, this request also needs a body with data for the updated person, all fields that are not explicitly assigned a value will be ignored. In the following example `lastName` and `phoneNumber` will be updated:
```json
{
    "firstName": null,
    "lastName": "Burrows",
    "dateOfBirth": null,
    "accountNumber": null,
    "contact": {
        "phoneNumber": "0589 69 14 90",
        "emailAddress": null,
        "address": null
    }
}
```

### 5. DELETE /person/\<id\>
Delete the person that corresponds to the given `id`.