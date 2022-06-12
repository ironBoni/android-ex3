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
In Main/Main.sln, <br/>
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
The first item in the view is the logged-in user <br/>
So, from the second item and on it's all his contacts. <br/>
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

**React Client** <br/>
Install packages in "react" directory<br/>
go to the "react" directory.<br/>
run the following command: **npm install**<br/>
it's equivalent to these commands: <br/>
1. npm install @microsoft/signalr
2. npm install react-bootstrap <br/>
After "npm install" in the **"react" directory** - run **npm start**.

Example to register user IDs: noam, ron, dan <br/>
The password is Np1234 <br/>
**React - use instructions**
1. Enter "react" folder and run "npm start".
2. enter username: "noam", and password: "Np1234". <br/>
3. To chat with Ron, click on his name in the left menu. <br/>
4. Enter a message and click Enter or on the send-button. <br/>
5. To make image bigger - click on it.
  (the profile image will become bigger if you click on it).
6. To logout - click on the top-right button.
7. To **add a contact** click on the icon - **man-with-plus** (left-top, next to profile image)
  You can add one of the following usernames: <br/>
  a) ran <br/> 
  b) yaniv <br/>
  c) yuval <br/>
  d) oren <br/>

11. To **register**, logout (step 8) (or go to http://localhost:3000/),  
  Click on Register in the sign in page,

  fill all the fields:
  the password has to be be at least 6 characters
  and must contain at least 1 Capital letter and 1 digit.
  Click Register. 

**React: Packages:** react-bootstrap, @microsoft/signalr,   

The Images' sources in imageSources.txt (in the "react" directory).