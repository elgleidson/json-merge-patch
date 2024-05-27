http://localhost:8080/swagger-ui.html

### Step 1: create a person only with personal details:

```shell
curl -X 'POST' \
  'http://localhost:8080/people' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "personalDetails": {
    "firstName": "Freddy",
    "lastName": "Krueger",
    "dateOfBirth": "1984-11-09"
  }
}'
```
_Response_:
```json
794b32b2-b187-47b3-9b65-62014acc332a
```

**Check the result:**
```shell
curl -X 'GET' 'http://localhost:8080/people/794b32b2-b187-47b3-9b65-62014acc332a'
```
_Response:_
```json
{
  "id": "794b32b2-b187-47b3-9b65-62014acc332a",
  "personalDetails": {
    "firstName": "Freddy",
    "lastName": "Krueger",
    "dateOfBirth": "1984-11-09"
  },
  "address": null,
  "contact": null
}
```

### Adding address:

```shell
curl -X 'PATCH' \
  'http://localhost:8080/people/794b32b2-b187-47b3-9b65-62014acc332a' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "address": {
    "address": "1428 Elm Street",
    "city": "Los Angeles",
    "postCode": "LA1234"
  }
}
'
```

**Check the result:**
```shell
curl -X 'GET' 'http://localhost:8080/people/794b32b2-b187-47b3-9b65-62014acc332a'
```
_Response:_
```json
{
  "id": "794b32b2-b187-47b3-9b65-62014acc332a",
  "personalDetails": {
    "firstName": "Freddy",
    "lastName": "Krueger",
    "dateOfBirth": "1984-11-09"
  },
  "address": {
    "address": "1428 Elm Street",
    "city": "Los Angeles",
    "postCode": "LA1234"
  },
  "contact": null
}
```

### Adding contact details
```shell
curl -X 'PATCH' \
  'http://localhost:8080/people/794b32b2-b187-47b3-9b65-62014acc332a' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "contact": {
    "email": "freddy.krueger@nightmare.com",
    "phoneNumber": "12345678"
  }
}
'
```

**Check the result:**
```shell
curl -X 'GET' 'http://localhost:8080/people/794b32b2-b187-47b3-9b65-62014acc332a'
```
_Response:_
```json
{
  "id": "794b32b2-b187-47b3-9b65-62014acc332a",
  "personalDetails": {
    "firstName": "Freddy",
    "lastName": "Krueger",
    "dateOfBirth": "1984-11-09"
  },
  "address": {
    "address": "1428 Elm Street",
    "city": "Los Angeles",
    "postCode": "LA1234"
  },
  "contact": {
    "email": "freddy.krueger@nightmare.com",
    "phoneNumber": "12345678"
  }
}
```

### Removing the contact details
```shell
curl -X 'PATCH' \
  'http://localhost:8080/people/794b32b2-b187-47b3-9b65-62014acc332a' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "contact": null
}
'
```
**Check the result:**
```shell
curl -X 'GET' 'http://localhost:8080/people/794b32b2-b187-47b3-9b65-62014acc332a'
```
_Response:_
```json
{
  "id": "794b32b2-b187-47b3-9b65-62014acc332a",
  "personalDetails": {
    "firstName": "Freddy",
    "lastName": "Krueger",
    "dateOfBirth": "1984-11-09"
  },
  "address": {
    "address": "1428 Elm Street",
    "city": "Los Angeles",
    "postCode": "LA1234"
  },
  "contact": null
}
```

## Updating only the city
```shell
curl -X 'PATCH' \
  'http://localhost:8080/people/794b32b2-b187-47b3-9b65-62014acc332a' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "address": {
    "city": "New York"
  }
}
'
```
**Check the result:**
```shell
curl -X 'GET' 'http://localhost:8080/people/794b32b2-b187-47b3-9b65-62014acc332a'
```
_Response:_
```json
{
  "id": "794b32b2-b187-47b3-9b65-62014acc332a",
  "personalDetails": {
    "firstName": "Freddy",
    "lastName": "Krueger",
    "dateOfBirth": "1984-11-09"
  },
  "address": {
    "address": "1428 Elm Street",
    "city": "New York",
    "postCode": "LA1234"
  },
  "contact": null
}
```

### Adding invalid email
```shell
curl -X 'PATCH' \
  'http://localhost:8080/people/794b32b2-b187-47b3-9b65-62014acc332a' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "contact": {
    "email": "invalid_email"
  }
}
'
```
_Response:_ 400 as it gets validated by `@Email` annotation.