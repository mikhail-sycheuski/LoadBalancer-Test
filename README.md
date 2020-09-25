# iptiQ-LoadBalancer-Test

<!-- PROJECT LOGO -->
<br />
<p align="center">
  <a href="https://github.com/mikhail-sycheuski/iptiQ-LoadBalancer-Test/blob/master/docs/images/baby-image.png">
    <img src="images/logo.png" alt="Logo" width="80" height="80">
  </a>

  <h3 align="center">Childish LoadBalancer</h3>

  <p align="center">
    Implementation of the test task for iptiQ!
    <br />
  </p> 
</p>

<p><b>Task description:</b></p>
<p>
  A load balancer is a component that, once invoked, it distributes incoming requests to a list of registered providers and return the value obtained from one of the registered providers to the original caller. For simplicity we will consider both the load balancer and the provider having a public method named get().
  <br />
  <br />
  <i>Every component described in the exercise is a piece of software of the same codebase. You don’t need to build a “runnable” application, there is no need to create any real server or rest service, no need to build any real network-based interaction, there should be no framework within the codebase. Simulating real world scenario means however that it has to be working properly and effectively in all scenario that can happen in real life (eg. handling parallel requests, managing edge cases etc.)</i>
  <br />
  <br />
  <b>Steps:</b>
  <ul>
      <li>
          Generate provider
      </li>
      <li>
          Register a list of providers
      </li>
      <li>
          Random invocation
      </li>        
      <li>
          Round Robin invocation
      </li>
      <li>
          Manual node exclusion / inclusion
      </li>
      <li>
          Heart beat checker
      </li>
      <li>
          Improving Heart beat checker
      </li> 
      <li>
          Cluster Capacity Limit
      </li>               
  </ul>
</p>



<!-- ABOUT THE PROJECT -->
## About The Project

This implementation is based on ny understanding of the task requirements and following assumptions:
* No libraries and frameworks should be used
* The application should have possibility to run a showcase
* All concurrency scenarios should be covered
* List of registered providers is static and dynamic registration/deregistration of providers is not possible
* Application should be built following main good design principles and practises

<!-- GETTING STARTED -->
## Getting Started

To run this application you need no more than just apply gradle sync compile java app and run i.

This is an example of how you may give instructions on setting up your project locally.
To get a local copy up and running follow these simple example steps.

### Prerequisites

In order to build the application please run in console being in the project root directory:
```sh
gradlew clean build
```

In order to run application please use standard java command.

The list of supported configuration options is:
* [int] -DproviderParallelismLevel - [DEFAULT: 3]
* [int] -DnumberOfProviders - [DEFAULT: 10]
* [int] -DhealthCheckIntervalMS - [DEFAULT: 1000]
* [int] -DnumberOfClientsToSimulate - [DEFAULT: 35]
* [int] -DclientRequestIntervalMS - [DEFAULT: 500]
* [int] -DclientRequestProcessingDelayMS - [DEFAULT: 1000]
* ['RO'|'RR'] -DloadBalancingType - [DEFAULT: 'RO']

### Authors

Mikhail Sycheuski



