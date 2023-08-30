1.Name of project: track-mail-api-spring-boot-rest-hibernate-postgresql

2.Launch of project: run TrackMailApplication.class

3.Launch of jar-file: java -jar track-mail-api-spring-boot-rest-hibernate-postgresql-2.5.2.jar

4.Port of the project: http://localhost:8081

5.Start page: http://localhost:8081

6.Swagger documentation: http://localhost:8081/v2/api-docs

7.name/password/email/index/address of users:

Den/1234/den_mogilev@yopmail.com/212008/Mogilev, Lenin str.4-50,
Peter/4321/peter_mogilev@yopmail.com/750406/Singapore, Sembawang Drive 406-5',
Asya/5678/asya_mogilev@yopmail.com/212030/Mogilev, Lenin str. 4-74,
Jimmy/P@ssword1/jimmy_mogilev@yopmail.com/745555/Delhi, Bugis str. 17-98,
Maricel/221182/maricel_mogilev@yopmail.com/745406/Bacalod, Flower str. 27-16

8.Configuration: resources/application.properties

9.Database scripts: resources/data.sql

10.Database PostgreSQL connection:

Name: dbase@localhost
User: denmit
Password: 1981
Port: 5432

11.Rest controllers:

UserController:
registerUser(POST): http://localhost:8081 + body;
authenticationUser(POST): http://localhost:8081/auth + body

MailingController:
register(POST): http://localhost:8081/mailings + body;
arriveToPostOffice(PUT): http://localhost:8081/mailings/{mailingId}/arriveToPostOffice/{postOfficeId};
leavePostOffice(PUT): http://localhost:8081/mailings/{mailingId}/leavePostOffice/{postOfficeId};
receiveByRecipient(PUT): http://localhost:8081/mailings/{mailingId}/receiveByRecipient;
getAll(GET): http://localhost:8081/mailings;
getById(GET): http://localhost:8081/mailings/{mailingId};
getAllHistoryByMailingId(GET): http://localhost:8081/mailings/{mailingId}/history;

PostOfficeController:
save(POST): http://localhost:8081/post-offices + body;
getAll(GET): http://localhost:8081/post-offices;
getById(GET): http://localhost:8081/post-offices/{id};

12.Launch of all the tests:
EditConfiguration -> JUnit -> name:mvn test -> All In Directory: track-mail-api-spring-boot-rest-hibernate-postgresql\src\test ->
Environment variables : clean test

Controller tests: MailingControllerTest, PostOfficeControllerTest, UserControllerTest
Converter tests: MailingConverterImplTest
Dao tests: MailingDAOImplTest, PostOfficeDAOImplTest
ServiceTests: MailingServiceImplTest, PostOfficeServiceImplTest

