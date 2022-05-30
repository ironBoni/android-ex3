In this example we will use MariaDB as the Database server. 
To run this app on your computer please make sure MariaDB is installed. 
If not, please download it from here: https://mariadb.org/

To work with MariaDB using an Entity Framework, we will 
use the Pomelo Entity Framework. 
It is available as a NuGet package here: https://www.nuget.org/packages/Pomelo.EntityFrameworkCore.MySql
You can simply open the package manager (PM) console
(In Visual Studio 2022 -> View -> Other Windows -> Package Manager Console).

And enter the following command:
"Install-Package Pomelo.EntityFrameworkCore.MySql -Version 6.0.1"
(*without the quotation marks*)

Then, install the Microsoft Entity Framework Tools package
by entering the following command:
"Install-Package Microsoft.EntityFrameworkcore.Tools -version 6.0.1"

Finally create the database:
	1) First create a migration by typing: "add-migration InitialMigration" 
	   in the PM console. 
	2) Then apply the migration using: "update-database"

Note: We are working with .NET version 6.0.1 (same as the university server).
	  Please make sure it is installed. 

Password is Np1239
