# SEM - Booking system (team 11b)
A room booking system developed as part of the [TU Delft](https://tudelft.nl) *Software Engineering Methods* course, by
team **11B**.

## Usage

### Requirements
The project only requires JDK for Java 11.

#### Windows
Java development kits must be downloaded and installed from the official 
[Java downloads page](https://www.oracle.com/java/technologies/downloads) (scroll down for old Java versions).

#### RPM-based linux
Linux that uses the RPM package system (Fedora, RHEL, CentOS) may install all requirements using the following command:
```shell
$ sudo dnf install java-11-openjdk-devel
```

### Building
To build everything simply run the following in project root:
```shell
$ gradlew build
```

### Running
To start a service run the following in project root:
```shell
# for admin
$ gradlew services:admin:run

# for core
$ gradlew services:core:run

# for identity
$ gradlew services:identity:run
```

It is highly recommended using a profile. Each service has two profiles:

 * `dev` for development environment (local database, better diagnostics output, etc.)
 * `prod` for production environment (external database, less diagnostics output, more lightweight, etc.)

A profile can be specified by supplying the `-Dspring.profiles.active=<profile>` argument on launch.

## Authors
Members of the development team include:

 * [Aleksandra Andrasz](mailto:A.Andrasz@student.tudelft.nl)
 * [Ciprian Stanciu](mailto:G.C.Stanciu@student.tudelft.nl)
 * [Matyáš Pokorný](mailto:M.Pokorny@student.tudelft.nl) (gpg key `4941449FACB209FEE0C5613BA2DE505F3A6C5266`)
 * [Mike Raave](mailto:M.P.Raave@student.tudelft.nl)
 * [Milan de Koning](mailto:M.deKoning-1@student.tudelft.nl)
 * [Oanh Tran Chau Kieu](mailto:TranChauKieuOanh@student.tudelft.nl)