Service to Transfer of Funds between two Accounts
    
    As a base requirement the service also allows for the creation of accounts.
    
Accounts    
    
    Create Account : curl -X POST http://localhost:8085/account -d '{"initialBalance":1050.0}'
    Get an Account : curl -X GET http://localhost:8085/account/{accountId}
    Get All accounts: curl -X GET http://localhost:8085/account/all
    Get Account transactions : GET Accounts curl -X GET http://localhost:8085/account/{accountId}/transactions
    
Bonus:To Create Accounts quickly use
 
    curl -X POST http://localhost:8085/account/quickSetup/{noOfAccounts}

Transfer Funds
     
    Initiate Transfer: curl -X POST http://localhost:8085/funds/transfer -d '{"fromAccountId": "44b6f059-686e-433c-a971-4f4c6d50d5bb","toAccountId": "ced6f81e-4c9a-4ed2-8cdc-70f88ee16b01","amount": 1.0}'
    
Application Uses 

    Dropwizard
    Lombak

Running the Application :

    java -jar fundstransfer.jar server funds-transfer.yml

    
 
