# Smartcloud instance prices API

**Important: Do NOT fork this repository if you want to submit a solution.**

Imagine we run our infrastructure on a fictional cloud provider, Smartcloud. As their machine instance prices fluctuate all the time, Smartcloud provides an API for us to retrieve their prices in real time. This helps us in managing our cost.

# Requirements

Implement an API for fetching and returning machine instance prices from Smartcloud.

```
GET /prices?kind=sc2-micro
{"kind":"sc2-micro","amount":0.42}, ... (omitted)
```

This project scaffold provides an end-to-end implementation of an API endpoint which returns some dummy data. You should try to follow the same code structure.

You should implement `SmartcloudPriceService` to call the [smartcloud](https://hub.docker.com/r/smartpayco/smartcloud) endpoint and return the price data. Note that the smartcloud service has usage quota and may return error occassionally to simulate unexpected errors. Please make sure your service is able to handle the constraint and errors gracefully.

You should also include a README file to document:-
1. Any assumptions you make
1. Any design decisions you make
1. Instruction on how to run your code

You should use git and make small commits with meaningful commit messages as you implement your solution.

# Setup

Follow the instruction at [smartcloud](https://hub.docker.com/r/smartpayco/smartcloud) to run the Docker container on your machine.

Clone or download this project onto your machine and run

```
$ sbt run
```

The API should be running on your port 8080.

# How to submit

Please push your code to a public repository and submit the link via email. Please do not fork this repository.

# Any assumptions you make
1. User use application with this API as following
   1. They load the list of instances
   2. when they select some instance in list, application will show the page after calling price API
2. This API server must have high availability
3. But also, This API server must have fast response
4. Multiple user use this application at the same time.
5. This API server is not updated often
6. The list of instances will be updated
   - It is possible for application to send the wrong instance name to API server even if we have assumption #1

# Any design decisions you make
1. We save token in environment variables
   - From the aspect of security, It is not safe when the token is saved in source file
2. We use cache for high availability
   - We will return the price information if we know previous price
     - It means that we need to return Not only price but also the date that we get the price from smartcloud API
   - We also respond to client quickly
     1. Cache is created on process but not thread-safe
        - Cache works fine from assumption #6
        - Conflict can occur from assumption #4
     2. After we call API, saving the price information in cache will be executed asynchronously

# Instruction on how to run your code in local
1. Run server
```dtd
$ sbt run
```
