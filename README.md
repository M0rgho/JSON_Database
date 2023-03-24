# Json database
It stores and handles data locally in json format. It allows complex keys and values.
The key is either a string or a list of string that coresponds to the path of the information in the database.
Client server communictaion is done using Java Sockets.
---
## Functions
### Set
```json
{"type":"set","key":["person1", "inside1", "inside2"], "value": {"id1": 12, "id2": 14}}
```
Will result in following structure regardless of existence of the root objects:
```json
{
    ... ,
    "person1": {
        "inside1": {
            "inside2" : {
                "id1": 12,
                "id2": 14
            }
        }
    },
    ...
}
```
### Get
Simillar to set
```json
{"type":"get","key":["person1", "inside1"}}
```
### Delete
It removes only the value of the last key in the path list
```json
{"type":"delete","key":["person1", "inside1", "inside2"]}
```
Only the contents of "inside2" are deleted
### Exit
Closes the server
```json
{"type":"exit"}
```

## Server Response
Server responds in the form:
```json
{"response":"OK/ERROR","reason":"Error reason", "value": "used for get"}
```
---
Made by Mikołaj Maślak