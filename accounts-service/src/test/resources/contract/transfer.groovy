import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "Should return user with new balance"

    request {
        method 'POST'
        url '/accounts/transfer'
        headers {
            header 'Accept': 'application/json'
            header 'Content-Type': 'application/json'  // <-- ДОБАВИТЬ ЭТО!
        }
        body([
                login: 'test',
                target: 'alan',
                amount: 1000
        ])
    }

    response {
        status 200
        headers {
            header 'Content-Type': 'application/json'
        }
        body(

                login: 'alan',
                name: 'Алан Дзагоев',
                birthdate: '1998-05-20',
                balance: 4000

        )
    }
}
