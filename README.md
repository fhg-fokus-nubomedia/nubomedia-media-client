# NUBOMEDIA Media Client


This project is part of the [NUBOMEDIA](http://www.nubomedia.eu/) research initiative.

This repository includes the open source implementation of the “org.kurento.client.internal.KmsProvider” interface. 
This KMSProvider connects to the  Virtual Network Function Manager (VNFM) on the NUBOMEDIA cloud repository which is responsible for managing the lifecycle of the media components (KMS) to obtain the KMS URL dynamically.


If your application is going to use the Kurento Media Server (KMS) on the NUBOMEDIA cloud repository then you need to add this library as dependency to your project. 


### How to set the environment Properties
On your test environment you need to create a configuration file on your *HOME* directory within a folder .kurento called *config.properties*. 
The content of the file should have the provider property set, which looks like this ```kms.url.provider=de.fhg.fokus.nubomedia.kmc.KmsUrlProvider```


### How to add KMC+ library 
If you are using Maven, here is an example of including the jar file.
	
```
<dependencies>
...
<dependency>
	<groupId>de.fhg.fokus.nubomedia</groupId>
	<artifactId>nubomedia-media-client</artifactId>
	<version>1.0-SNAPSHOT</version>
</dependency>
```
### Instantiating Kurento Media Client
Normally, you would instantiate the KMC with the method KurentoClient.create(Properties properties). For example 
``` KurentoClient.create(System.getProperty("kms.ws.uri", DEFAULT_KMS_WS_URI)); ```

However, with this library, you will need to instantiate the KMC using instead this method ```KurentoClient.create()```. This way, the URL of the KMS will be discovered with the following procedure:
* If there are a system property with the value “kms.url”, its value will be returned.
* If the file “~/.kurento/config.properties” doesn’t exist, the default value “ws://127.0.0.1:8888/kurento” will be returned.
* If the file “~/.kurento/config.properties” exists:
* If the property “kms.url” exists in the file, its value will be returned. For example, if the file has the following content ``` kms.url: ws://4.4.4.4:9999/kurento ```. The value ```ws://4.4.4.4:9999/kurento``` will be returned.

If the property ```kms.url.provider``` exists in the file, it should contain the name of a class that will be used to obtain the kms url. In this case,  your config.properties file MUST have the following content ```kms.url.provider: de.fhg.fokus.nubomedia.kmc.KmsUrlProvider```
The class “de.fhg.fokus.nubomedia.kmc.KmsUrlProvider” from the library will be instantiated with its default constructor. This class implements the interface ```org.kurento.client.internal.KmsProvider```. This interface has the following methods:
* ```String reserveKms(String id) throws NotEnoughResourcesException; ```
* ```String reserveKms(String id, int loadPoints) throws NotEnoughResourcesException; ```
* ```void releaseKms(String id); ```
The method ```reserveKms()``` will be invoked and its value returned. If ```NotEnoughResourcesException``` exception is thrown, it will be thrown in ```KurentoClient.create()``` method.

Issue tracker
-------------

Issues and bug reports should be posted to the [GitHub Issue List](https://github.com/fhg-fokus-nubomedia/kurento-client-extended/issues)

Licensing and distribution
--------------------------

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

Support and Contribution
-------------------------

Need some support, wish to contribute? Then get in contact with us via our [mailinglist](mailto:nubomedia@fokus.fraunhofer.de)!
