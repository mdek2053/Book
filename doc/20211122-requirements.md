# Functional Requirements

### **Must have**

* Employees shall book a room

    * for unlimited period

    * only between opening hours

    * with a reason for meeting

* Opening hours of the room shall be determined by building

* Rooms shall have

    * maximum capacity

    * equipment

    * id

* Id shall be characterized by building number followed by room number

* Users shall have access to the system and its functionality by authenticating themselves

* Users shall have accounts with:

    * NetID

    * password

* Users shall search for availability of specific room

* Users shall see their reservations with time-slots and corresponding rooms 

* Buildings shall have

    * id

    * open hours

    * name

### **Should have**

* Users shall modify their reservations

* Users shall search for rooms matching specific characteristics

    * number of people

    * type of equipment

    * time-slot availability

    * building

* Employees shall:

    * not make a reservation more than two weeks in advance

    * not reserve two or more rooms in the same time slot

    * not cancel or edit reservations made by other users

    * not make a reservation for a room that is under maintenance

* Secretaries shall book multiple rooms at the same time

* Secretaries shall modify reservations made by members of research group they work for

* Secretaries shall add employees to research groups

* Secretaries while making reservations shall specify:

    * employee for which reservation is made

    * purpose

* Admin shall edit and cancel any reservation

* Admin shall provide explanation for modifying reservation

* Users shall indicate room should be under maintenance if any equipment doesn’t work properly

* Admin shall change the status to "under maintenance" or “available”

* Room that are "under maintenance" shall be unavailable for booking

### **Could have**

* Users shall indicate which equipment is not working while indicating room should be under maintenance

* Rooms "under maintenance" shall optionally have estimated availability time

* Secretaries shall see all reservations made by their research group

* Admin shall see all the reservations

* Admin shall see all reported faults

* Admin shall be able to resolve a reported fault

### **Won’t have**

* System shall be connected to Single-Sign-On

* Graphical User Interface

# Non-functional

* System should be modular

* System should rely on API

* System should make use of microservices

* System should be written in Java 11

* System must be build with Spring Boot and Gradle

* NetID and password serve as credentials

* The password shall be stored safely using Spring Security

