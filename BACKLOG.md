# Feature
Fetching the supplies for the shop
## Item
The user of the Cash Register and Seller App should be able to access all the necessary data that the Cash Register server receives from the Main server.
### Task
* Create a User model
* Create a Product model
* Create a Branch model
* Create a CashRegister model
* Create a Role model
* Create a RoleName model
* Implement a method for retrieving data from the Main server
* Push the code
* Make a pull request

# Feature
User login
## Item
The user of the Cash Register desktop application and the Seller mobile application should log in to it's account via the Cash Register server, which obtains all access data of the employees from the main server, in order to check the data before granting access to ensure connection security.
* Implement LoginRequest
* Implement LoginResponse
* Implement UserController
* Implement UserService
* Implement UserRepository
* Security and configuration
* Push the code
* Make a pull request

# Feature
View information of all products
## Item
The user of the Cash Register and Seller App should have an overview of information of all products via the Cash Register server.
### Task
* Implement ProductController
* Implement ProductService
* Implement ProductRepository
* Implement methods for information retrieval about all products from the database
* Push the code
* Make a pull request

# Feature
Filtered information overview of all products
## Item
The user of the Cash Register desktop application and the Seller mobile application should be able to filter the list of all items in the branch, about which information is stored by the Cash Register server, in order to quickly and easily access data on currently required items.
### Task
* Implement the FilterRequest class
* Implement a method within ProductService that returns a list of products based on filterRequest
* Implement a Product Controller method that returns filtered products
* Do push code
* Make a pull request

# Feature
Saving orders created by the Seller App
## Item
The user of the Seller mobile application should be able to save the order.
### Task
* Implement Receipt model
* Implement Receipt service
* Implement Receipt repository
* Implement POST method to save order
* Implement order save in the form of a receipt
* Return the response to the client
* Push the code
* Make a pull request

# Feature
View all receipts created by the Seller App
## Item
The user of the Cash Register application should be able to view all receipts created by the Seller App.
### Task
* Implement a GET method for fetching receipts
* Check if the receipt was created by the mobile app
* Implement aquirement of the receipt
* Return the response to the client
* Push the code
* Make a pull request

# Feature
View all receipts
## Item
The user of the Cash Register application should be able to view all receipts.
### Task
* Implement a GET method for fetching receipts
* Implement aquirement of the receipt
* Return the response to the client
* Push the code
* Make a pull request

# Feature
Receipt saving
## Item
The user of the Cash Register application should be able to save the receipt.
### Task
* Implement a POST method to save receipts
* Check if the account was created by the mobile app
* Implement a check to see if the receipt has already been saved
* Implement receipt saving
* Return the response to the client
* Push the code
* Make a pull request

# Feature
Delete receipt
## Item
The user of the Cash Register application should be able to delete a receipt that will not be paid.
### Task
* Implement a DELETE method to delete receipts
* Check if the receipt has already been deleted
* Implement receipt deletion
* Return the response to the client
* Push the code
* Make a pull request

# Feature
Receipt payment
## Item
The user should be able to pay the receipt using PayApp.
### Task
* Receive the recepits from the Cash Register App that has to be pais.
* Check payment method
* Sending the receipt to the Main server
* Response check from the Main server
* Send a response to the application whether the receipt has been paid
* Saving the receipt
* Push the code
* Make a pull request

# Feature
Daily transaction report view.
## Item
The Cash Register App user should be able to view all receipts issued that day through that Cash Register.
### Task
* Create an end point
* Implement a method for returning all receipts issued in the last 24 hours
* Push the code
* Make a pull request

# Feature
Saving notifications
## Item
The guest user of the Seller App should be able to send a message to the waiter.
### Task
* Implement Notification model
* Implement Notification controller
* Implement Notification service
* Implement Notification Repository
* Create an end point
* Implement a method to save all messages sent by the Seller App.
* Push the code
* Make a pull request

# Feature
Sending notifications
## Item
The Seller App and Cash Register App should be able to view messages from the Guest user.
### Task
* Create an end point
* Implement a method for sending all messages that have not been sent to one of the Apps that should receive those messages.
* Push the code
* Make a pull request

# Feature
Order editing
## Item
The Seller App and Cash Register App should be able to edit orders that are not paid.
### Task
* Create an end point
* Implement a method that checks whether the receipt has been paid, and if not allows it to be updated in the database.
* Puth the code
* Make a pull request

# Feature
Review of unfinished orders
## Item
The Seller App should be able to view orders that are not closed.
### Task
* Create end point
* Implement a method that returns a list of all orders that have not been closed.
* Push the code
* Make a pull request

# Feature
Overview of products ingredients
## Item
The Seller App should be able to see all product ingredients.
### Task
* Create an end point
* Expand Product model (add Description and Barcode)
* Implement a method that returns a list of all products.
* Push the code
* Make a pull request

# Feature
Fetching Cash Register information
## Item
The Cash Register should be able to access all the information about itself necessary to generate a QR code.
### Task
* Create an end point
* Implement CashRegister model
* Implement CashRegister controller
* Implement CashRegister service
* Implement CAshRegister repository
* Create an end point
* Implement a method to fetch all data about a particular Cash Register stored in the database
* Push the code
* Make a pull request

# Feature
Change password
## Item
Seller mobile App and Cash Register should be able to find out when the user's password has been changed by the administrator, and in that case the user should be immediately redirected to change its own password for security after the login.
### Task
* Expand the User model with information about the temporary password and reset token
* Create a route to which a request is sent with the user's mail
* Implement email verification and send the reset token to the mail
* Create html email form
* Implement the route to which the security token is sent to verify it
* Implement token verification methods
* Implement a route to save the new password
* Implement methods to change the password and store it in the database
* Send new information about the User to the Main server
* Push the code
* Make a pull request

# Feature
Asynchronous message reception
## Item
The user of the Cash Register application and the Seller mobile application should be able to receive messages sent by the Guest user asynchronously.
### Task
* Create a configuration class for WebSocket support
* Add an endpoint for websocket connections
* Add a topic to which clients need to subscribe to listen to notifications
* Extend the method for sending notifications by broadcasting messages that have been sent
* Push the code
* Make a pull request

# Feature
Receipt overview with VAT included
## Item
 The Cash Register App user should be able to view the receipt with product's VAT information.
### Task
* Expand product model with VAT information
* In addition to obtaining product data from the Main server, also collect it's VAT information
* Push the code
* Make a pull request

# Feature
Graphical table overview
## Item
The user of the Seller App and the Cash Register App should be able to see the arrangement of tables/chairs/… in the restaurant/shop/…
### Task
* Create Table model
* Create Table controller
* Create Table service
* Create Table repository
* Implement a method for obtaining the shop's table quantity information from the Main server
* Implement a route to fetch table information
* Push the code
* Make a pull request

# Feature
Product filtering
## Item
The Seller App user should be able to filter all products by it's ingredients.
### Task
* Implement a method to retrieve all products from the database
* Push the code
* Make a pull request

# Feature
Application configuration settings
## Item
The Seller Mobile App and Cash Register should be able to obtain information about it's configuration settings (language, time).
### Task
* Obtain configuration information from the Main server
* Implement the route through which the Cash Register server obtains the configuration information it needs
* Implement the route through which the Seller App obtains the configuration information it needs
* Push the code
* Make a pull request
