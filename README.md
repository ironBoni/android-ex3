To open the Server's code - 
Clone and **Go to the Main folder, and run Main.sln**.
**Set as startup project (the "AspWebApi"), and Start without Debugging.**
then, **Set the MVC as startup project (the "AspNetMvc"), and Start without Debugging.**
<br/>

**React - npm install in "react" directory**<br/>
go to the "react" directory.<br/>
run the following commands:
1. npm install @microsoft/signalr
2. npm install react-bootstrap
in the **"react" directory** - run **npm start**.

**ASP.NET MVC (Parts 0-2)**: <br/>
(Assuming you already started (in that order) the : 1. WebAPI project. 2. MVC. 3. React as mentioned in the beggining).
**To see the rating page go to http://localhost:5266/Ratings**
(or go to http://localhost:3000 and you can choose it from the nav bar)

**ASP.NET WebAPI (Part 3) - localhost:5186** <br/>
**First, you should sign in in order to get a token from the server.**
Meaning, send a **POST request to http://localhost:5186/api/Login**
**in the body:
{
    "username": "noam",
    "password": "Np1234"
}**
**You will get a JWT token in the response. 
This token will by sent by the client in the header
"Authorization": "Bearer blablah...token..."**

Methods you can perform:<br/>
Login:<br/>
GET /api/login/{username} - returns its token.
POST /api/Login - example is above.

Register:<br/>
POST /api/Register - in the body:<br/>
{
    "id": "shlomo",
    "name": "Shlomo Levin",
    "password": "Np1234",
    "profileImage": "/profile/hadar.jpg",
    "server": "localhost:5186"
}

Invitations, Transfer, Contacts:
and every API request as mentioned in the exercise.

**SignalR - Part4**
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
