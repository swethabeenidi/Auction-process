Bidding system
==============

Yieldlab is a technology service provider, connecting suppliers (those who
have space to show ads, e.g. on their websites) to bidders (those who actually
want to show ads). The core process is to listen for requests, gather metadata
and bids, and afterwards to determine who is winning. This challenge is about
setting up tests for a simplified version of this core process as its own application.

Contents of this document:

- [1 The Auctioning Service](#1-the-auctioning-service)
  - [1.1 Incoming Requests](#11-incoming-requests)
  - [1.2 Bid Requests](#12-bid-requests)
  - [1.3 Bid Response](#13-bid-response)
  - [1.4 Auction Response](#14-auction-response)
  - [1.5 Configuration](#15-configuration)
- [2 The Task: Test the application](#3-the-task-test-the-application)
  - [2.1 Prerequisites](#31-prerequisites)
  - [2.2 Start the Docker containers](#32-start-the-docker-containers)
  - [2.3 Start the application](#33-start-the-application)
  - [2.4 Run the test](#34-run-the-test)
- [3 Task Submission](#4-task-submission)


## 1 The Auctioning Service

The RTB application behaves as follows:

For every incoming request as described in [1], bid requests are sent out as
described in [2] to a configurable number of bidders [5]. Responses from these
bidders as described in [3] are processed. The highest bidder wins, and
payload is sent out as described in [4].

Incoming and outgoing communication are done over HTTP. Message formats
are described below.


Please stay with commonly known frameworks for easier reviewing and explaining
afterwards.

[1]: #1-incoming-requests
[2]: #2-bid-requests
[3]: #3-bid-response
[4]: #4-auction-response

### 1.1 Incoming Requests

The application listens to incoming HTTP requests on port 8080.

An incoming request is of the following format:

    http://localhost:8080/[id]?[key=value,...]

The URL will contain an ID to identify the ad for the auction, and a number of
query-parameters.

### 1.2 Bid Requests

The application forwards incoming bid requests by sending a corresponding
HTTP POST request to each of the configured bidders with the body in the
following JSON format:

```json
{
	“id”: $id,
	“attributes” : {
		“$key”: “$value”,
		…
	}
}
```

The property `attributes` contains all incoming query-parameters.
Multi-value parameters need not be supported.

### 1.3 Bid Response

The bidders' response will contain details of the bid, with `id` and `bid`
values in a numeric format:

```json
{
	"id" : $id,
	"bid": bid,
	"content": "the string to deliver as a response"
}
```

### 1.4 Auction Response

The response for the auction must be the `content` property of the winning bid,
with some tags that can be mentioned in the content.

For now, only `$price$` must be supported, denoting the final price of the bid.

### 1.5 Configuration

The application accepts a the following configuration
parameter:

| Parameter | Meaning                                                  |
|-----------|----------------------------------------------------------|
| `bidders` | a comma-separated list of URLs denoting bidder endpoints |


## 2 The Task: Test the application

Write unit and integration tests for the auctioning system, checking at least the following criteria:

* highest bid always wins:
 * if there are two or more bids with the same (highest) bid amount, the winner is randomly chosen between them
 * if there are no valid bids, the auctioning system will answer with `No valid bids`
* the `$price$` macro 
 * is replaced regardless of its location in the bids adtag
 * always contains the winning bid value

Please write code that you would want to maintain in production as well, or
document all exceptions to this rule and give reasons as to why you made those
exceptions.

The tests must be set up to be run automatically while building the application.

### 2.1 Verifying the setup

**NOTE:** This setup is not intended to be the basis for your submission.
Please implement a test suite that you would feel comfortable maintaining.

For verification of the general setup, there is a simple shell script provided that
executes an end-to-end test and verifies the test results. That shell script
requires the `curl` and `diff` binaries to be in your `PATH`.

First, a set of bidders is required that will respond to bidding requests
sent out by your application. For this test suite, we will be using a
pre-built [Docker][what-is-docker] image that will be started several times
with sligthly different configuration values.

So, here is a list of the requirements:

- Docker ([official installation docs][install-docker])
- A shell (or you'll need to carry out the tests manually)
- `diff` (e.g. from [GNU Diffutils][diffutils] package)
- `curl` ([official download link][curl-dl])

[what-is-docker]: https://www.docker.com/what-docker
[install-docker]: https://docs.docker.com/engine/installation/
[diffutils]: https://www.gnu.org/software/diffutils/
[curl-dl]: https://curl.haxx.se/download.html

### 2.2 Start the Docker containers

To start the test environment, either use the script `test-setup.sh` or run the
following commands one after the other from your shell:

```sh
docker run --rm -e server.port=8081 -e biddingTrigger=a -e initial=150 -p 8081:8081 yieldlab/recruiting-test-bidder &
docker run --rm -e server.port=8082 -e biddingTrigger=b -e initial=250 -p 8082:8082 yieldlab/recruiting-test-bidder &
docker run --rm -e server.port=8083 -e biddingTrigger=c -e initial=500 -p 8083:8083 yieldlab/recruiting-test-bidder &
```

This will set up three bidders on localhost, opening ports 8081, 8082 and 8083.

### 2.3 Start the application

You can run the application from your shell via:
```sh
mvn spring-boot:run
```

You can use the following configuration parameters to connect to these bidders
from the application:

| Parameter | Value                                                                 |
|-----------|-----------------------------------------------------------------------|
| `bidders` | `http://localhost:8081, http://localhost:8082, http://localhost:8083` |

This is configurable in the `application.yml` file.

### 2.4 Run the test

To run the test, execute the shell script `run-test.sh`. The script expects
your application to listen on `localhost:8080`. It will issue a number of bid
requests to the application and verify the responses to these requests. If
the application doesn't respond correctly, it will print out a diff between
the expected and the actual results.

## 3 Task Submission

When you the application passes your test suite, please send the code and instructions on
how to build and run it back to Yieldlab.
