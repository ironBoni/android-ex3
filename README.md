To open the Server's code - 
Clone and **Go to the Main folder, and open Main.sln**.

**Database - Migrations, Load Data**
1. Delete the Migrations folder in the AspWebApi project.
2. run in the Packange Manager Console the commands: 
   * add-migration init <br/>
   * update-database <br/>
   
3. Load data - Open HeidiSQL (in Windows):
   * username: root
   * password: Np1239
   * port: 3306
   * Click Open
   * Go to the Database folder (in the repo)
   * In the HeidiSQL - Click on File -> Run SQL File 
   * Choose the file in Database\Final_DeleteAndInsert.sql
   
   Now all the data is loaded to the DB.
<br/>

**Run the server** <br/>
In Main/Main.sln,
Set as startup project (the "AspWebApi"), and Start without Debugging.

**Android - Install (Pixel - 2 API 28)**<br/>
Run the application.<br/>

**Register:**<br/>
  * In the Login screen, Click on Register.
  * Enter the details and upload image from the device
  * For simplicity, choose the password Np1234 <br/>
    (if the didn't choose the image, it will be the default).
 * Press **register**.

**Login:**<br/>
  * Enter user Id (The one you registered with,
   but we prefer "ron" - since it has contacts and friends he can chat with).
  * Enter password. (Np1234).
  * Press Login.

**Contacts List - Main Activity**
The first item in the view is the logged-in user
So, from the second item and on it's all his contacts.
Click on a contact in order to chat with him.

**Conversation Activity**
You can chat in this activity with the user.

**Settings**
  * In the main activity (contacts list), click on the 3-vertical-dots (⋮).
  * Click on Settings.
  * Click on *Change Color Theme* to change the theme.
  * You can enter server (e.g. localhost:5186) and *Save Settings*.

**Add Contact Activity**
  * In the main activity (contacts list), click on the floating-button <br/>
    in the bottom-right corner (man-with-plus icon).
  *  Enter the contacts details: <br/>
     For example: <br/>
     User Id - ran </br>
     Nickname - Ran Levy <br/>
     Server - localhost:5186 <br/>
  * Click on ADD CONTACT.
    (If you will scroll down to the bottom you will see the new contact).
  
**Firebase**
   * To simulate it, open Postman (or react client from execrise 2 - link is down below).
   * Assuming that you loaded the DB Data as mentioned, and Noam appears in ron's contacts
     (Otherwise, add it to the contacts).
     
   * In the Postman, send Transfer:
     POST to http://localhost:5186/api/transfer/
     with the body:
     {
      "from": "noam",
      "to": "ron",
      "content": "Good morning!!!"
      }
      
  * You can notice you got notification in 3 ways: <br/>
      * A notification if you scroll down the bar.
        ("New message: from noam", "Good Morning!!").
      * An update in the last message in the contacts list. 
      * An update in the conversation activity (if you click on Noam, you will see the 
        last message is what that was sent by the firebase).
      
**Landscape screens**
  * Go the the Contacts List (The main activity, that shows all the contacts).
  * Enable rotating the phone in the bar above (2 buttons right to the Wi-Fi button)
  * In the emulator click on Rotate Right.
  * Give it a few seconds.
  * Now the screen shows both the contacts, and the relevant chat in the same activity.
  * Click on a contact and you will see the chat with him.

The client can send and receive message from other clients that are 
registered in the server. Furthermore, the android client can send and receive message from other clients.

The application saves local DB (Room) - for chats, contacts, etc.
