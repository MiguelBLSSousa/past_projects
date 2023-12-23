# Mod 4 Project

We have made a web application which allows users to find music lessons for a cheap price by connecting them to upcoming music teachers.
It allows users to sign up as either a teacher or a student or both, create a student or teacher profile and 
schedule lessons according to their role. You can also search for teachers on a map and book lessons with them. 
after booking you are also able to look at your own schedule where you are able to pay or cancel the lesson.



This readme file will go over the locations of files and where to find specific functions. 

The idea package contains the libraries and the artifacts. Underneath are the assignments we made throughout the module.
Inside our src package we have the code which comprises the web application. inside the java package we have our java code,
this consists of the java model, resources and the servlets inside the nl.utwente.notebridge package. Inside the model package
we have a class for all database classes.

inside the resources package we have an implementation of the restfull services.

The servlet package contains the servlet used to respond to requests.

The sql package contains the database file which consists of all our sql queries.

Inside the webapp are all front-end files, it contains the jsp and html files for all pages in the web application and the packages for the 
corresponding css and javascript.

This file also contains images used on the web application inside the img package and a json file containing the paths to the pictures of the instruments we use.

Our profile pictures and videos are saved inside our tomcat so that they will not get deleted every time we try to rebuild.