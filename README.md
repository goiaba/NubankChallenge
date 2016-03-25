This is the solution of a challenge proposed by Nubank Company as a step of their hiring process to a software engineer position.

## Overview

Giving a company that plans to reward its customers for inviting their friends, the application should:

* Read a file containing pairs of user identifiers, separated by comma. The first identifier refers to the user that is inviting. The second to the invited one.
* Have an endpoint that exposes the ranking of users by their scores
* Have an endpoint to create new invitations
* Detailed information is located in the doc directory, exercise.txt file

## Solution outline
The relation among a inviter and its invitees is easy to map with a multiway tree. This kind of tree allows each node to have a random number of children nodes, that are also considered as subtrees.

The tough part was figure out how to update the scores of each affected node. This is being done on every insertion in the tree. So, every time the ``insert`` method is called, a new node is created and all the affected nodes are updated. Since we are using an immutable tree, in order to improve performance, every non affected node is reused and a new tree is returned by the method.

### Strategy

* MWTree implements the concept of an immutable multiway tree.
* InvitationTree is implemented in such a way that every time a new node is inserted, the score of each affected node is updated
* akka-http and an actor are used in the service layer in order to expose a data structure that can be accessed concurrently 

## Assumptions
Although a well defined list of requirements was provided, some doubts about the expected behavior arose. In order to solve them, some assumptions were made:

* Users that do not invited will not appear in the ranking
* All other users, including those with score = 0 will be in the ranking list
* If a user invites another user that already exists in the tree (witch means this user has already being invited before) its score does not change, but the scores of the users above it (in the path to the tree root) are updated
* Invitations from nonexistent users are ignored

## Main tools

* Scala
* sbt
* akka-http
* akka-actor

## Available endpoints

* GET /ranking
    * Returns a decreasing list (by the score) of the users that have already invited someone.
* POST /invitation
    * Inserts a new invitation in the data structure
    * Content-Type: "application/json"
    * Example of the http payload: { "invitee": 10, "inviter": 9 }
    * Chrome web browser has a nice rest client addon called Postman that can be used to test the invitation functionality

## Todo list
* Implement logging functionality
* Improve tests in the service layer
* Figure out how to inject test configuration in the ServerBoot without changes to the application.conf file 

## Running the app

    sbt run

## Running the tests

    sbt test
    
The tests can also be run with code coverage

    sbt clean coverage test
    sbt coverageReport
