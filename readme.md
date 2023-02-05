To launch the application run following in root directory:
1. *mvn clean install* 
2. *docker build --tag=jwtee_image .*
3. *docker run -p 8080:8080 --name jwtee jwtee_image*

Application will be available at *localhost:8080*.