import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "Should return user with new balance"

    request {
        method 'GET'
        url '/accounts/user'
        headers {
            header 'Accept': 'application/json'
            header 'Content-Type': 'application/json'  // <-- ДОБАВИТЬ ЭТО!
        }
    }

    response {
        status 200
        headers {
            header 'Content-Type': 'application/json'
        }
        body(

                login: 'test',
                name: 'Иван Иванов',
                birthdate: '1998-05-20',
                balance: 1500

        )
    }
}
