# Functional Requirements

## Must have

- The system shall keep track of buildings.
- Each building shall have a building number.
- Each building shall have its own opening hours.
- The system shall keep track of the meeting rooms.
- Each meeting room shall be identified by the number of the building it is located in and its own room number unique to the building.
- Each meeting room shall have a maximum capacity.
- Each meeting room shall be open only during the opening hours of the building it is in.
- The system shall keep track of room reservations.
- Users shall log in using their NetID and their password.
- Users shall be able to check the availability of a specific meeting room.
- Users shall be able to inspect their own reservations with time slots and meeting rooms.
- Users shall be able to reserve rooms for themselves provided that they state the purpose of the reservation.
- Users shall not be able to reserve rooms more than two weeks in advance.
- Users shall not be able to have more than one room reserved for a particular point in time.
- Users shall not be able to reserve rooms that are under maintenance or not open.
- Users may additionally be system admins.
- System admins shall be able to add rooms and buildings to the system.

## Should have

- Each meeting room shall be either available or under maintenance.
- Each meeting room shall have its own equipment.
- Users shall be able to search for meeting rooms.
- Users shall be able to filter their search using attributes such as capacity, available equipment, availability and building.
- Users shall be able to edit or cancel their own reservations and only their own reservations.
- Users shall be able to indicate if the equipment in a meeting room is not working or a meeting room requires maintenance.
- Users may additionally be secretaries for a group of users (research group).
- Secretaries shall be able to reserve meeting rooms for members of their research group provided they state the purpose of the reservation.
- Secretaries shall be able to edit or cancel reservations for members of their research group.
- System admins shall be able to edit or cancel other users&#39; reservations provided they offer an explanation for this.
- System admins shall be able to change the status of a meeting room from available to under maintenance or from under maintenance to available.

## Could have

- Users shall be able to indicate which equipment is not working.
- Meeting rooms under maintenance shall have an estimated availability time.
- Secretaries shall be able to see all reservations made by their research group.
- Admin shall see all the reservations.
- Admin shall see all reported faults.
- Admin shall be able to resolve a reported fault.

## Won&#39;t have

- System shall be connected to the TU Delft Single Sign On.
- System shall have a graphical user interface.

# Non-functional Requirements

- System shall be modular.
- System shall rely on API.
- System shall make use of microservices.
- System shall be written in Java 11.
- System shall be built with Spring Boot and Gradle.
- System shall use Spring Security for authentication.