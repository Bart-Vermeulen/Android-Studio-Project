# Android-Studio-Project

VERMEULEN Barthélémy

This application afford to his user access to the Account Name, Amount of money and Currency of the money of an IBAN.
You enter the IBAN in a TextField, click on Access/Refresh and you obtain these three informations.

The requirements where:

- This application must be available offline.
- A refresh button allows the user to update its accounts.
- Access to the application is restricted 
- Exchanges with API must be secure ( with TLS)


To fullfil these requirements, my app instantiate the cache. So, if a request is made and the Network isn't available and the cache isn't empty the user will obtain information even if he's offline.
Also, the access button is also a refresh button so when the button is click on, the app will call the API to update its accounts.
The access to the app is restricted because when you open the app, a biometric login is asked to access to the app.
Finally, the exchanges with the API are executed with Volley, which uses the https protocol, so they are secure.
