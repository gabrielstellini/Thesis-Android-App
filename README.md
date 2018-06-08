# SEHM-Client

SEHM - or Stress Eating Health Management - is a project aimed at reducing stress eating habits. 

The android app in this repo collects data with a Microsoft Band 2, and sends it to the server for processing (linked [here](https://github.com/gabrielstellini/Thesis-Server)). 

This then calculates a score based on several factors, mainly:

 1. stress levels
 2. calories consumed 
 3. time the food was eaten

Users can then view each other's scores and 

## Getting started

### Prerequisites
This android app is the front end to the thesis-server (linked [here](https://github.com/gabrielstellini/Thesis-Server)). 

This project makes use of an OAuth solution provided by [Auth0](https://auth0.com/).  In order to authenticate the client, you need to update the server and client keys.

The app was built for android 5.0 lollipop, however it was also tested on Android 6.0 Marshmallow and Android 8.0

### Installing

 1. Pair a Microsoft Band 2 to the phone
 2. Clone the repo and update the `BASE_API_URL`  in the `APIService`
 3. Build the app and run it on your phone.

### License
This project is Licensed under the MIT License.
