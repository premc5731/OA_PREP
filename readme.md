# OA Prep: AI-Powered Assessment Generator

OA Prep is a full-stack web application designed to help users prepare for technical assessments and interviews. It leverages the power of Google's Gemini API to dynamically generate a set of multiple-choice questions (MCQs) on any user-specified topic.

## Key Features

* **Dynamic Question Generation:** Get a fresh set of questions every time you click "Generate."
* **Customizable Topics:** Enter any subject you want to study, from "Java" to "Data Structures," "SQL," "Operating Systems," or "Network Security."
* **Adjustable Parameters:** Specify the number of questions you want for a focused study session.
* **Clean & Responsive UI:** A simple and intuitive React interface for a seamless user experience.
* **Secure API Handling:** All API calls to the Gemini API are handled securely by the Spring Boot backend, ensuring your API key is never exposed to the client.

## Tech Stack

* **Frontend:**
    * [React](https://reactjs.org/)
    * [Axios](https://axios-http.com/) (for API requests)
    * HTML5 & CSS3 (or your preferred styling library like Material-UI, Tailwind, etc.)
* **Backend:**
    * [Spring Boot](https://spring.io/projects/spring-boot) (Java 17+)
    * Spring Web
    * Jackson (for JSON parsing)
* **API:**
    * [Google Gemini API](https://ai.google.dev/docs)

## Architecture

The application follows a classic decoupled client-server model:

**React (Client)** ➡️ **HTTP Request** ➡️ **Spring Boot (Server)** ➡️ **API Call** ➡️ **Gemini API**

1.  The user interacts with the **React** frontend (running in their browser).
2.  On clicking "Generate," React sends a `POST` request (containing the topic and question count) to the Spring Boot backend.
3.  The **Spring Boot** backend receives the request, constructs a secure prompt, and sends it to the **Gemini API**, including the private API key.
4.  The Gemini API returns a response (ideally a well-formatted JSON string).
5.  The Spring Boot backend parses this response and forwards it as clean JSON to the React client.
6.  React updates its state and renders the list of questions for the user.

## Getting Started

Follow these instructions to get a copy of the project up and running on your local machine for development and testing.

### Prerequisites

You will need the following tools installed on your system:
* [Git](https://git-scm.com/)
* [Java JDK 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) or later
* [Apache Maven](https://maven.apache.org/) (or Gradle)
* [Node.js (v16+) & npm](https://nodejs.org/)

### 1. Configuration (Gemini API Key)

Before launching the backend, you must set up your Gemini API key.

1.  Obtain your API key from the [Google AI Studio](https://ai.google.dev/tutorials/setup).
2.  Navigate to the Spring Boot backend's resource folder: `backend/src/main/resources/`
3.  Open the `application.properties` file.
4.  Add the following line, replacing `YOUR_API_KEY_HERE` with your actual key:

    ```properties
    gemini.api.key=YOUR_API_KEY_HERE
    ```

### 2. Clone the Repository

Clone this repository to your local machine:

```bash
git clone [https://github.com/your-username/oa-prep.git](https://github.com/your-username/oa-prep.git)
cd oa-prep