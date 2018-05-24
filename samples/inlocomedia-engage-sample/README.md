InLocoMedia SDK GeoEngagement Sample Project
===

## Getting Started
* Open on your Android Studio
	* You will need to import the whole sample projects as a single project and the Engage Sample is one of it's modules
* Get your Engage Credentials
	* Open your [account](https://dashboard.inlocoengage.com) and open the [applications tab](https://dashboard.inlocoengage.com/apps)
	* Open the Engage Experience App and copy your app id: *xxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx*
	* Now open your gradle.properties file and fill with your app id anywhere on the file.
	```
		ENGAGE_SAMPLE_APPLICATION_ID xxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx
	```
	* If you do not have a gradle.properties file, fill free to update directly on the engage build.config file located at *inlocomedia-android-sdk-public/samples/inlocomedia-engage-sample/build.gradle*. You just need to change the following line
	```
	ext {
    	<!-- engageApplicationId = getPropertyIfAvailable('ENGAGE_SAMPLE_APPLICATION_ID') Replace this --> 
    	engageApplicationId = "\"xxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx\""
	}
	```
* After opening
   * Grant all the permissions the app will request
   
> This documentation is expected to be updated soon. Enjoy.