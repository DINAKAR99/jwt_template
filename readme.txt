hey this is for the  jwt template that we can use it for the jwt security in our project 

steps.
1.login user gets jwt token 
2. user shows jwt token every time
3. if jwt expired , we use refresh token to get new jwt token, refresh token will be saved in the db
4. if credentials wrogn show wromg credentials

we need jwt request to send username and password

jwt response has jwt , refresh and user (optional)

jwt service has all methods 

jwt filter checks for token evry time

jwt entry pouint is exception handler to show access denied


