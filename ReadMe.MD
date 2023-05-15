
<!-- ABOUT THE PROJECT -->
## About The Project


The project was built with clean architecture principals in mind, it has a 3 layer architecture which can be tested independently.

The layers are:
* Presentation - consisting of views and view models
* Data - Which as the repository layers and exposes data either from network or local disk/memory.
* Domain - Which has the business logic and all use cases are exposed as interfaces.

Unit test cases for the use cased and view models have been included
Use  

```sh
  ./gradlew test
  ```

### Caveats
As per requirement 3, we needed to load the last city searched, however right now it loads the weather at the current location, this can be changed however we need to add the use case for that. 


### Basic features
As the user brings up the app:
* Asks for user's permission for location and then, loads weather at current location
* User can start to type in the location and search for a US city, this will saved in Room db as history.
* User can get weather at that location again using the history or search for another city.
* Error scenarios handled, however it's not a perfect world, please feel free to report any issues or bugs.


### What can be improved
* Better user scenarios, like showing a message if the user types in a vague text for search.
* Currently as the user types it reads for the room db on every type, this is in efficient and a better in memory cache can be implemented to handle this.
* Use Compose for the UI
* Use detekt
* Use version catalog 
* Improve more on code quality
* Have instrumentation tests.
