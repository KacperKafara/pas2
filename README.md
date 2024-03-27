# Movie Rental
The "Movie Rental" app, built with Spring Boot 3.1.5 and MongoDB, allows users to browse and rent movies for specific durations. It's a RESTful application, enabling easy communication via HTTP. Key features include user registration, browsing movies and renting. Moderators can manage the movie and rent collections and administrators the users collection.

Frontend application built with REACT: https://github.com/KacperKafara/pasSPA
---
Default accounts:
- Administrator
  - Login: admin
  - Password: password
- Moderator
  - Login: mod
  - Password: password
- Client
  - Logind: client1
  - Password: password
## How to run
- Clone repository
```
git clone https://github.com/KacperKafara/pasREST.git
```
- Run docker containers with MongoDB
```
cd docker
docker compose up -d
```
 - Run Spring Boot application
```
cd ..
./mvnw spring-boot:run
```
---
Developers:
- Kamil Cieślak https://github.com/kamill10
- Kacper Kafara https://github.com/KacperKafara
- Konrad Koza https://github.com/konradkoza
