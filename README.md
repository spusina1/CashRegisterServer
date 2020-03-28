# Cash Register Server
## Team Alpha

### Team members
* [Rijad Fejzić](https://github.com/rfejzic1)
* [Samra Pušina](https://github.com/spusina1)
* [Amila Žigo](https://github.com/azigo12)
* [Ajdin Karahasanović](https://github.com/akarahasan3)
* [Samra Mujčinović](https://github.com/SamraMujcinovic)
* Nudžejma Zukorlić
* Deni Pencl

### About

The cash register server is a local server with which the cash register and the seller mobile application establish communication. This server is online with its own database. If the main server goes down, the cash registers should be able to work using the information from the local server. The cash register server gets all the necessary information from the main server (about products and users). When the user logs in to the desktop or the mobile application, the local server forwards the data to the main server, from which it receives an answer(affirmative if the user data is correct) after which the user-login is either allowed or denied. When the employee logs in, he has the ability to open a new fiscal bill and add/delete bill items. All the information about currently open bills which weren't payed for, and the bills that were generated with the help of the seller mobile application, are temporarily kept on the server until they are confirmed(payed for) or rejected. If the cash register employee closes the account after previously being payed, the information about the payment are saved in the database. The bills that are being payed with the help of the Pay App should immediately be sent to the main server, others - at the end of the day. During the work, when selling prodcuts, the information about their quantity is edited on the server. All successful transactions during the day and new data are sent to the main server.

