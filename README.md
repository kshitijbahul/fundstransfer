Service to Transfer of Funds between two Accounts
    
    As a base requirement the service also allows for the creation of accounts.
    
Accounts    
    
    Create Account : curl -X POST http://localhost:8129/account  -H 'Content-Type: application/json' -d '{"initialBalance":1050.0}'
    Get an Account : curl -X GET http://localhost:8129/account/{accountId}  -H 'Content-Type: application/json'
    Get All accounts: curl -X GET http://localhost:8129/account/all  -H 'Content-Type: application/json'
    Get Account transactions : GET Accounts -X GET http://localhost:8129/account/{accountId}/transactions  -H 'Content-Type: application/json'
    
Bonus:To Create Accounts quickly use
 
    curl  -X POST http://localhost:8129/account/quickSetup/{noOfAccounts} -H 'Content-Type: application/json'

Transfer Funds
     
    Initiate Transfer: curl  -X POST http://localhost:8129/funds/transfer -H 'Content-Type: application/json' -d '{"fromAccountId": "44b6f059-686e-433c-a971-4f4c6d50d5bb","toAccountId": "ced6f81e-4c9a-4ed2-8cdc-70f88ee16b01","amount": 1.0}'
    Get Transfer Status: curl -X GET http://localhost:8129/funds/transfer/{requestId} -H 'Content-Type: application/json' 
    Get All transfer Requests: curl -X GET http://localhost:8129/funds/transfer -H 'Content-Type: application/json' 
Application Uses 

    Dropwizard
    Lombak
    Dropwizard Guicey
    Mockito
    
Building and Running the Application :
    
    run : mvn clean install
    run : java -jar target/fundstransfer.jar server funds-transfer.yml
To Run the Application straight away download the jar `fundstransfer.jar`

    run : java -jar fundstransfer.jar server funds-transfer.yml
    
Application uses port `8129` , it can be changed by changing the `port` property in `funds-transfer.yml`
    
 
