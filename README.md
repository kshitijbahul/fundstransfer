Service to Transfer of Funds between two Accounts
    
    As a base requirement the service also allows for the creation of accounts.
    
Accounts    
    
    Create Account : curl  -H 'Content-Type: application/json' -X POST http://localhost:8129/account -d '{"initialBalance":1050.0}'
    Get an Account : curl -H 'Content-Type: application/json' -X GET http://localhost:8129/account/{accountId}
    Get All accounts: curl -H 'Content-Type: application/json' -X GET http://localhost:8129/account/all
    Get Account transactions : GET Accounts curl -H 'Content-Type: application/json' -X GET http://localhost:8129/account/{accountId}/transactions
    
Bonus:To Create Accounts quickly use
 
    curl -H 'Content-Type: application/json' -X POST http://localhost:8129/account/quickSetup/{noOfAccounts}

Transfer Funds
     
    Initiate Transfer: curl -H 'Content-Type: application/json' -X POST http://localhost:8129/funds/transfer -d '{"fromAccountId": "44b6f059-686e-433c-a971-4f4c6d50d5bb","toAccountId": "ced6f81e-4c9a-4ed2-8cdc-70f88ee16b01","amount": 1.0}'
    
Application Uses 

    Dropwizard
    Lombak
    Dropwizard Guicey
    Mockito
    
Building and Running the Application :
    
    run : mvn clean install
    run : java -jar target/fundstransfer.jar server funds-transfer.yml
    
Application uses port `8129` , it can be changed by changing the `port` property in `funds-transfer.yml`
    
 
