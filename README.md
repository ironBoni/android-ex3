To open the Server's code - 
Clone and **Go to the Main folder, and run Main.sln**.

**Database - Migrations, Load Data**
1. Delete the Migrations folder in the AspWebApi project.
2. run in the Packange Manager Console the commands: 
   * add-migration init <br/>
   * update-database <br/>
3. Open HeidiSQL (in Windows):
   * username: root
   * password: Np1239
   * Click Open
   * Make sure DB named pomelodb doesn't exists. Otherwise, drop it.
   * Go to the Database folder (in the repo)
   * In the HeidiSQL - Click on File -> Run SQL File 
   * Choose the file in Database\Final_DeleteAndInsert.sql
   
   Now all the data is loaded to the DB.
<br/>

**Run the server**
In Main/Main.sln,
Set as startup project (the "AspWebApi"), and Start without Debugging.

**Android - Install (Pixel - 2 API 28)**<br/>
Run the application.<br/>

**Register:**<br/>
Enter the details and upload image from the device
For simplicity, choose the password Np1234
(if the didn't choose the image willn be the default)
Press **register**.

**Login:**<br/>
1. Enter user Id (The one you registered with or ron).
2. Enter password. (Np1234).
3. Press Login.

**Chats - Main Activity**
The first item in the view is the logged-in user
So, from the second item and on it's all his contacts.
Click on a contact in order to chat with him.

**Conversation Activity**
You can chat in this activity with the user.

**Firebase**
**in the client (npm start in "react" folder)
open two tabs,
in the first login with: "noam", "Np1234"
in the second login with: "dan", "Np1234"**
you can send message from one to the second,
and see that it wil get it immediately by the server ChatHub.

**React - general use instructions**
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
**ASP.Net: Packages:**
"AspWebApi" project's Packages:
Microsoft.AspNetCore.Authentication.JwtBearer
Microsoft.AspNetCore.Identity.EntityFrameworkCore
Microsoft.AspNetCore.SignalR
Microsoft.AspNetCore.SignalR.Client
Microsoft.AspNetCore.SignalR.Core
Microsoft.IdentityModel.Tokens
System.IdentityModel.Tokens.Jwt

"AspNetMvc" project's Packages:
Microsoft.EntityFrameworkCore.SqlServer
Microsoft.EntityFrameworkCore.Tools
  
The Images (profile images, button-images), Audio (Hatikva.mp3), 
Video (Hatikva.mp4) sources in imageSources.txt (in the "react" directory).
