# Overview

This is a play scala application used for controlling Philips Hue Lights.
  
I wrote this application because I didn't like the default yellow colour of the bulbs when they lost power and it's not a configurable option on these lights.

Once you get it started the app will poll the Hue bridge for updates and if any of the lights are in a default (yellow light) setting then it will update the lights to the energize state (white light) setting. Generally this can take anywhere between 3-10 seconds, depending on the polling setting.

I also store the previous state so it's possible to detect if a light has been turned on or is now reachable and restore it to it's previous setting, I'm considering doing this as a future improvement. 
     
# Architecture

It's a bit over-architected since I'm experimenting with Akka and Play in this project. I used activator to generate a skeleton project.

The application consists of, 
- Hue Service that connects to a Hue bridge and sets a configurable heartbeat time.
- Hue Actor that receives heartbeat update requests with a list of all the lights.
- Hue Actor that stores all the previous light states and changes the default colour of the lights.
- Hue Controller for controlling the lights.
- No Unit Tests :( TODO

# Configuration (Environment Variables)

You can update these variables in the conf/application.conf file directly or use the environment variables listed below.

Environment Variable Name | Description | Example Value
------------- | -------------- | --------------
HUE_HOST | IP Address of the Hue Bridge | 10.0.0.1
HUE_USER_ID | Authenticated token obtained from the Hue Bridge | sdfkjha9d8798F7sdf-asdfDFyuj
HUE_HEARTBEAT_MS | Heartbeat polling time in ms | 2000
PLAY_CRYPTO_SECRET | Play Crypto Secret | sdfkjha9d8798F7sdf

# Build Artifact

```$> sbt clean dist```

This should create a deployable zip file containing an embedded web server.  

# Development

You can sign up for a Philips API developer account if you want their web based documentation. Or you can head on over to their github repo https://github.com/PhilipsHue/PhilipsHueSDK-Java-MultiPlatform-Android for some examples.

To get started simply fill in all the required configuration variables in conf/application.conf and start your embedded web container. There's not a lot to the project and it should be really easy to jump in and add your own changes.

Tests are coming soon!?

## Dependencies
- Scala
- Java
- Sbt

## Start Embedded Web Container

```$> sbt start```

This should start the embedded web container available at http://localhost:9000/lights/list
