Service to Transfer of Funds between 2 Accounts
    As a base requirement the service also allows for the creation of accounts.
Accounts    
    Create Account : curl -X POST http://localhost:8085/account -d '{"initialBalance":1050.0}'
    GET Account : curl -X GET http://localhost:8085/account/{accountId}
    GET Accounts curl =X GET http://localhost:8085/account/all
Transfer Funds
    POST request 
        curl -X POST http://localhost:8085/funds/transfer -d '{"fromAccountId": "44b6f059-686e-433c-a971-4f4c6d50d5bb","toAccountId": "ced6f81e-4c9a-4ed2-8cdc-70f88ee16b01","amount": 1.0}'

Application Uses 
    Dropwizard
    Lombak

Running the Application :
    java -jar fundstransfer-1.0-SNAPSHOT.jar server funds-transfer.yml

To DO's
    1. Logging
    2. Passing Accounting Events
    3. Keeping a transaction Store
    
 
