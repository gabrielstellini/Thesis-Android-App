# SEHM-Client

SEHM - or Stress Eating Health Management - is a project aimed at reducing stress eating habits. 

The android app in this repo collects data with a Microsoft Band 2, and sends it to the server for processing (linked [here](https://github.com/gabrielstellini/Thesis-Server)). 

This then calculates a score based on several factors, mainly:

 1. stress levels
 2. calories consumed 
 3. time the food was eaten

Other features include viewing a food eaten history, as well as viewing other users' scores. The full writeup on the project can be found [in my dissertation](https://github.com/gabrielstellini/Thesis-Android-App/blob/master/Thesis%20writeup%20-%20final.pdf)

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

<details>
  <summary>Screenshots</summary>
  

![Screenshot_2018-04-13-15-50-41](https://user-images.githubusercontent.com/16836689/227718195-ab37ece3-77f5-4799-8710-338700e71011.png)
![Screenshot_2018-04-13-15-50-54](https://user-images.githubusercontent.com/16836689/227718198-32abd2f5-a4eb-4b15-aac1-5ba49ab9e2aa.png)
![Screenshot_2018-04-13-15-51-12](https://user-images.githubusercontent.com/16836689/227718217-020f4685-fb8a-4a2f-835b-dee49c9b68c8.png)
![Screenshot_2018-04-13-15-51-56](https://user-images.githubusercontent.com/16836689/227718250-02ad89cc-8cd5-4857-8ef6-af0bfc79d863.png)
![Screenshot_2018-04-13-15-52-51](https://user-images.githubusercontent.com/16836689/227718278-83615dcf-0b4b-4f72-a053-2810529ebf74.png)
![Screenshot_2018-04-13-15-54-08](https://user-images.githubusercontent.com/16836689/227718236-7435494e-56eb-4495-823f-a549ea5c2289.png)
![Screenshot_2018-04-06-12-30-41](https://user-images.githubusercontent.com/16836689/227718304-8fdd9a2f-3e82-4c44-8f29-805402971fde.png)
</details>


