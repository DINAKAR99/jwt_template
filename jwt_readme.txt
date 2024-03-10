1.login -get jwt token and refresh token

while generate new token we verify te user is real or not 

so use   doAuthenticate(req.getUsername(), req.getPassword()); 

it creates a token which is a mix of username and pass called usernbamepassauthenticate token

  it uses authmanager.authenticate  and under the hood it calls 
userdetailservice and verifies the given details

2. carry jwt token to evry request and hit the filter and get verified 

3.jwt entry point is for exeception handling

auth provider just consist of custom user detail service and encoder

auth manager is made from authconifgurations

in filter we check for the header and verify the credentials 
we validate the token also in the filterand we set in securty context holdr