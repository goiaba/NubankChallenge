This is the solution of a challenge proposed by Nubank Company as a step of their hiring process to a software engineer position.

## Overview

Giving a company that plans to reward its customers for inviting their friends, the application should:

- Read a file containing pairs of user identifiers, separated by comma. The first identifier refers to the user that is inviting. The second to the invited one.
- Have an endpoint that exposes the ranking of users by their scores
- Have an endpoint to create new invitations
- Detailed information is located in the doc directory, exercise.txt file

## Assumptions
Although a well defined list of requirements was provided, some doubts about the expected behavior arose. In order to solve them, some assumptions were made:

- Users that do not invited will not appear in the ranking
- All other users, including those with score = 0 will be in the ranking list
- If a user invites another user that already exists in the tree (witch means this user has already being invited before) its score does not change, but the scores of the users above it (in the path to the tree root) are updated

## Strategy

- MWTree implements the concept of an immutable multiway tree.
- InvitationTree is implemented in such a way that every time a new node is inserted, the score of each affected node is updated
- akka-http and an actor are used in the service layer in order to expose a data structure that can be accessed concurrently 

## Available endpoints

1 - GET /ranking
- Returns a decreasing list (by the score) of the users that have already invited someone.

2 - POST /invitation
- Inserts a new invitation in the data structure
- Content-Type: "application/json"
- Example of the http Body: { "invitee": 10, "inviter": 9 }
- Chrome web browser has a nice rest client addon called Postman that can be used to test the invitation functionality

## Todo list
- Implement logging functionality
- Improve tests in the service layer
- Externalize the definition of the input file (its path is hardcoded) 

## Running the app

    sbt run

## Running the tests

    sbt test
    
##### Running test with coverage
    sbt clean coverage test
    sbt coverageReport