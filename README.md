# Problem Overview:
The problem that the real estate office faces is keeping a tab on false excuses used by contractors to procrastinate a particular construction project precisely using weather conditions as a bate as there is no system that automates this process and verifies the same.

# Solution
This android application solves the problem of contractors making
false excuses regarding the weather and social conditions during an ongoing project with the ministry by automating the weather check using OpenWeatherMaps API. Monitoring the data was made easy
by data visualization methods such as charts and graphs.

 
# Use Case Diagrams

* Use case Diagram

![USE CASE DIAGRAM](https://github.com/Saanayy/ProgressMeter/blob/master/UseCaseDiagrams/UseCaseScenarios.jpg)

* Sequence Diagram

![Sequence Diagrams](https://github.com/Saanayy/ProgressMeter/blob/master/UseCaseDiagrams/SequenceDiagram.png)


# Technology Stack
## Frontend:
	 1. Android Studio 
	 2. Electron JS (Desktop application using Node.js)
## Backend: 
	1. Firebase
      a. Firebase Authentication: for user login and signup 
      b. Firebase Storage: for storing images and data related to the project 
      c. Cloud Fire Store : For providing real-time updates. 
## API’s Used: 
	1. Open weather API. 
	2. Google Chart API. 
	3. One signal API: for providing push notifications.
## Languages Used: 
	1. Java, XML,PHP,JavaScript
  
  
  # A brief idea about the project
    - There will be two apps- Admin(for JLL  personnel) and User Application(for Contractors).
    - The contractor(s) will handle the user app wherein he will upload a new project with project title, description and other details. The geolocation and the timestamp will be fetched automatically.
    - In the admin app, the admin will be able to see all contractor details with their project. Any update by the contractor regarding hindrances faced will be cross checked by data available through weather APIs or other sources of the specified location.
    - In case of any mismatch in the received and actual condition the admin as well as the user will be flagged for providing the correct data. 
    - One user will be able to manage many projects at a single time. 
    - The admin will be able to visualize the details of different projects by the use of charts and graphs.

  
  ## Liked it? [Have a very detailed look at the project here](https://docs.google.com/presentation/d/1GJO5dPwmf3f6KrzonLD9iR1Pbvo6pwGhKO9ZZD6P1No/edit?usp=sharing)
