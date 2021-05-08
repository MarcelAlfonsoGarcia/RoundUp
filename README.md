# RoundUp
RoundUp

How to build:
1. Get the code on your machine
2. Navigate to the root folder
3. With maven installed on your machine: https://maven.apache.org/install.html
	1. Run 'mvn clean install'
	2. All 48 DAL tests should pass at once and the Build should finish in success
4. To run tests individually, running them with Eclipse for Java is recommended

How to run:
1. Using maven, run 'mvn clean package'
2. Run the executable JAR using the following Java command:
    'java -jar target/RoundUp-0.0.1-SNAPSHOT.jar'
3. The server will start in localhost, likely using port 5000
4. The server is also already up and running on heroku at:
  https://round-up-2020.herokuapp.com/api/
You can add to events/1/ to the url to see event 1 as a JSON string or users/1/ to
see user 1 as a JSON string
