# BookListingApp
Udacity Android Basiscs Nanodegree (EU) lesson 4 project JSON API access and networking

# Book Listing App

## Project Overview
This project is a chance for you to combine and practice everything you learned in this section of the Nanodegree program. You will be making an app that lists books.

The goal is to design and create the structure of a Book Listing app which would allow a user to get a list of published books on a given topic. You will be using the google books api in order to fetch results and display them to the user.

### Why this project?
In the most recent portion of the Nanodegree program, you learned about the web and about how to get data from an api. You also learned how to parse that data and display it to a user. This project gives you the ability to practice those skills, which will be key in developing any app which makes use of a backend server, real time data, or interactions over the internet.

### What will I learn?
This project is about combining various ideas and skills we’ve been practicing throughout the course. They include:

* Fetching data from an API
* Using an AsyncTask
* Parsing a JSON response
* Creating a list based on that data and displaying it to the user.

## Build Your Project
For this project, you will be creating a book listing app. A user should be able to enter a keyword, press the search button, and recieve a list of published books which relate to that keyword.

To achieve this, you'll make use of the [Google Books API](https://developers.google.com/books/docs/v1/getting_started#intro). This is a well-maintained API which returns information in a JSON format.

We suggest first exploring the API and learning what information it returns given a particular query. An example query that we found useful was

  `https://www.googleapis.com/books/v1/volumes?q=android&maxResults=1`

Once you've explored the API, begin work in Android Studio. You'll want a simple layout initially, with an editable TextView and a 'search' button.

Then, you'll want to build the AsyncTask that queries the API. This is a complex step, so be sure to reference the course materials when needed.

Once you've queried the API, you'll need to parse the result. This will involve storing the information returned by the API in a custom class.

Finally, you'll use the List and Adapter pattern to populate a list on the user's screen with the information stored in the custom objects you wrote earlier.

## Additional Criteria
The intent of this project is to give you practice writing raw Java code using the necessary classes provided by the Android framework; therefore, the use of external libraries will not be permitted to complete this project.

---
#### Screenshots

<img src="https://dl.dropboxusercontent.com/s/shw6twqcptqca7c/Screenshot_1498846145.png" alt="" width=200/> <img src="https://dl.dropboxusercontent.com/s/vju5mayz1qhcfjb/Screenshot_1498846150.png" alt="" width=200/> <img src="https://dl.dropboxusercontent.com/s/v3at5j8a5l5zm1f/Screenshot_1498846165.png" alt="" width=200/> <img src="https://dl.dropboxusercontent.com/s/0u15kw9s7hxzg3b/Screenshot_1498846194.png" alt="" width=200/> <img src="https://dl.dropboxusercontent.com/s/nnjspt2o5fe3fzv/Screenshot_1498846210.png" alt="" width=400/> <img src="https://dl.dropboxusercontent.com/s/vyami368wu82m3l/Screenshot_1498846215.png" alt="" width=400/>
