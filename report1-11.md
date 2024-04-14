# Campus Events and Entertainment Center

## Team 11

|**Name**|**Student ID**|
|:----|:----|
| 李冰   | 12110141   |
| 张颖萍 | 12111947   |
| 李华炫 | 12112045   |
| 陶毅诚 | 12112003   |
| 秦尧   | 12112016   |

## Part I. Project Proposal

### **Target Users or Clients:**

The target users of the proposed "Campus Events and Entertainment Center" system are students, faculty, and staff of SUSTech. 

### **Functionalities of the Proposed System**

The proposed system encompasses several key functionalities:

1. **Event Exploration:** Users can browse diverse event categories, view detailed event information including schedules and venues.
2. **Publish a New Event:** Allow event initiators to publish new events and their related information. Once approved by the administrator, make them available for other users to view.
3. **Booking and Reservation:** Users can make reservations, book seats, and purchase event tickets directly. Merchants manage ticket distribution and transactions through the platform.
4. **User Interaction:** Users can view and write reviews, communicate with others, and share event-related information like video clips and pictures.
5. **Event Recommendations:** The system offers personalized event suggestions based on user preferences and past interactions.
6. **Notifications:** Users receive timely notifications about upcoming events, newly added events, and any changes in event details.
7. **User Management:** The system governs varying user roles with distinct permissions, ensuring differential access to functionalities. 
Regarding non-functional requirements:

1. **Usability:** The system prioritizes an intuitive user interface and seamless navigation.
2. **Safety:** User data is securely stored and protected against unauthorized access.
3. **Performance:** The system is designed to handle a large number of concurrent users without significant performance degradation.
4. **Reliability:** Each transaction shall be guaranteed with reliabilty.
### **Expected Outcome:**

The expected outcome of the proposed system is to provide a centralized platform for the SUSTech community to discover, engage with, and participate in various campus events and entertainment activities. 

The system aims to enhance the overall experience of users by simplifying the process of accessing event information, making reservations, and facilitating communication and interaction among users.

### **Preliminary Requirement Analysis:**

#### **Functional Requirements:**

1. **Event Exploration:** Users can browse through various categories of events such as performances, lectures, competitions, and others. They can view detailed information about each event including schedules, venues, and maps.
-- Highlight: Support third-party map API for high-resolution location service

2. **Booking and Reservation:** Users can make reservations, book seats, and purchase tickets for events directly through the system. Merchants can complete the distribution of event tickets and transaction processing through the platform.
-- Highlight: AliPay sandbox, seatmap implementation, MQ for load balance

3. **User Interaction:** The system allows users to view and write reviews, communicate with other users, and share information such as video clips and pictures related to events.
-- Highlight: Video streaming

4. **Event Recommendations:** The system provides personalized event recommendations based on user preferences and past interactions.
-- Highlight: SOTA AI performance, ChatGPT API

5. **Notifications:** Users receive notifications and alerts about upcoming events, newly added events, and any changes in event details.
-- Highlight: Mail notification service

6. **User Management:** The system efficiently handles user access with secure login and registration processes. Users can manage their profiles and preferences. The system governs varying user roles with distinct permissions, ensuring differential access to functionalities.
-- Highlight: Two-factor authentication,  token-based authentication (Redis)

7. **Publish a New Event:** The platform facilitates event initiators in submitting comprehensive details via a user-friendly form, including title, description, date, time, location and so on. Administrators review submissions to ensure accuracy and appropriateness, providing feedback for necessary adjustments. Upon approval, events are seamlessly integrated for user viewing and participation, fostering community engagement.
-- Highlight: Logical and beautiful interface design

#### **Non-functional Requirements:**

1. **Usability:** The system should have an intuitive user interface and seamless navigation. The system should be easy to deploy. The system should support AI-based recommendation.
-- Highlight: Microservice design, Docker deployment

2. **Safety:** User data should be securely stored and protected against unauthorized access.
-- Highlight: Encryption

3. **Performance:** The system should be able to handle a large number of concurrent users accessing some critical services.
4. **Reliability:** Each transaction shall be guaranteed and rollback shall be performed when error occurs.
#### **Data Requirements:**

1. **Event Information:** Including event details, schedules, venues, and locations. 
Source: users upload and Internet based.

2. **User Data:** User profiles, preferences, booking history, and reviews.
Source: users filled in and adminstrator exported in.

3. **Multimedia Content:** Video clips, pictures, and other media shared by users.
Source: users upload and Internet based.

4. **API Retrieved Data:** This encompasses map data and other data retrieved from third-party APIs.
Source: Internet based.

#### **Technical Requirements:**

*Non final presentation, only for illustrative purposes, subject to actual conditions.*

**Operating Environment:** 

The system should be compatible with common web browsers.

**Tools:**

|**Tool**|**Notes**|
|:----|:----|
|GitHub|Code Cooperation && Task Decomposition |
|Lark|Doc Sharing|
|Shimo Doc|Doc Sharing|
|IntelliJ IDEA Ultimate Edition |IDE|
|WebStorm|IDE                                    |

**Technology Stack and Libraries:**

Frontend

|**Technical Framework and Library**|**Version**|**Notes**|
|:----|:----|:----|
|Vue                             |3.2.13  |Progressive Framework   |
|TDesign Vue Next             |0.9.0   |UI Library From Tencent |
|Axios    |1.6.2   |Network request library |
|Echarts                         |5.4.3|Visualization library   |

Backend

|**Technical Framework and Library**|**Version**|**Notes**|
|:----|:----|:----|
|Java|17|Programming Language       |
|Python|3.10|Programming Language (Recommendation System)|
|spring-boot                     |3.1.4|Web Application Framework      |
|mybatis-plus                    |3.5.3.2 |Database Interaction Framework |
|MySQL                           |8.1.0   |Relational Database            |
|RabbitMQ|3.1.3|Message Queue|
|Redis|7.2.1|Storage middleware|
|slf4j                           |2.0.9   |Logging plugins                |
|Maven                           |3.9.2   |Dependency management tools    |
|lombok                          |1.18.30 |Simplifying Java Code          |
|hutool                          |5.8.22  |Java tools                     |
|Swagger UI                      |2.2.15  |Generate API doc               |
|fastjson2                       |2.0.43  |JSON library                   |

## Part II. Task Decomposition & Planning

1. **Product Backlog**
   
![](/assets/product_backlog.png)

2. **Sprint Backlog**   

![](/assets/sprint_backlog.png)

3. **RoadMap**

![](/assets/roadmap1.png)
![](/assets/roadmap2.png)
![](/assets/roadmap3.png)
![](/assets/roadmap4.png)


## PART III. AI Usage

We used **ChatGPT(3.5)** to assist us to finish the proposal.

In the processes of proposing features, conducting the preliminary requirement analysis, writing user stories and assigning tasks, we all use AI to generate some content to use as reference and make some modifications to the content. By doing that, we can get some new ideas from AI and increase the efficiency. Also, making some modifications on our own can make sure the content is corrent and meet the requirements.

Here are some prompts and responses.

|**Prompt**|**Response**|
|:----|:----|
| 你是一个软件工程师，你要根据以下要求生成文档Your team should provide a written report that propose your team project. The report should include the following information.Project OverviewYou should briefly describe the proposed project. In particular, you will describe:Who are the target users or clients of the proposed system? What are the functionalities of the proposed system? What is the expected outcome or overall goal of the proposed system?Preliminary Requirement AnalysisYou should provide a preliminary requirement analysis of the proposed system, which includes:Functional requirements: list the features of the proposed system (see below). Non-functional requirements: e.g., usability, safety, security, performance, etc. Data requirements: e.g., which data is required? How to get the data? Technical requirements: e.g., the operating environment and tech stack that you'll use for developing and executing the deliverables.The proposed system should have at least 5 distinct, notable features.项目题目是：Campus Events and Entertainment CenterPlease design and implement a "Campus Events and Entertainment Center" system, in which users can view information for SUSTech performances (e.g., musical concerts), distinguish lectures (e.g., by Turing Award recipients), competitions (football match, coding skill competitions), and other events. Users could also book tickets, make reservations, or write reviews. Specifically,Users can explore specific information about various events, including categories, maps, venues, etc. Users can make reservations, book seats, and purchase tickets for various events. The system also supports user reviews, communication, assistance, and (real-time) information sharing (e.g., video clips, pictures, etc.) for events and activitiesThe system can provide event recommendations ... | **Project Overview:** **Target Users or Clients:**The target users of the proposed "Campus Events and Entertainment Center" system are students, faculty, staff, and visitors of the Southern University of Science and Technology (SUSTech).  **Functionalities:**1. **Event Exploration:** Users can browse through various categories of events such as performances, lectures, competitions, and others. They can view detailed information about each event including schedules, venues, and maps.2. **Booking and Reservation:** Users can make reservations, book seats, and purchase tickets for events directly through the system.3. **User Interaction:** The system allows users to write reviews, communicate with other users, and share real-time information such as video clips and pictures related to events.4. **Event Recommendations:** The system provides personalized event recommendations based on user preferences and past interactions.5. **Notification and Alerts:** Users receive notifications and alerts about upcoming events, newly added events, and any changes in event details. **Expected Outcome:**The expected outcome of the proposed system is to provide a centralized platform for the SUSTech community to discover, engage with, and participate in various campus events and entertainment activities. The system aims to enhance the overall experience of users by simplifying the process of accessing event information, making reservations, and facilitating communication and interaction among users. **Preliminary Requirement Analysis:** **Functional Requirements:**1. Event Exploration Interface2. Booking and Reservation System3. User Review and Communication Features4. Event Recommendation System5. Notification and Alert Mechanism **Non-functional Requirements:**1. **Usability:** The system should have an intuitive user interface and seamless navigation.2. **Safety:** User data should be securely stored and protected against unauthorized access.3. **Performance:** The system should be able to handle a large number of concurrent users without significant performance degradation.4. **Scalability:** The system should be scalable to accommodate future growth in user base and event listings.5. **Accessibility:** The system should be accessible to users with disabilities and comply with accessibility standards. **Data Requirements:**1. Event Information: Including event details, schedules, venues, and maps.2. User Data: User profiles, preferences, booking history, and reviews.3. Multimedia Content: Video clips, pictures, and other media shared by users. **Technical Requirements:**1. **Operating Environment:** The system should be compatible with common web browsers and mobile devices.2. **Tech Stack:** Frontend development using HTML, CSS, and JavaScript. Backend development using a framework such as Django or Flask with Python. Database management using SQL or NoSQL databases.3. **Integration:** Integration with third-party services for payment processing, notification delivery, and event recommendations. By fulfilling these requirements, the "Campus Events and Entertainment Center" system will provide a comprehensive solution for managing and accessing campus events, enhancing the overall experience for users at SUSTech. |

For proposing features and conducting the preliminary requirement analysis, we inspired by the AI-generated responses so we've broadened our thinking. Thus, we can have a more comprehensive analysis.

For writing user stories and assigning tasks, the AI-generated responses act as some great examples so we can get started faster.

The reason why we don't directly reuse the AI-generated responses is that while AI-generated responses may offer valuable insights and ideas, we prefer to use them as a reference and then articulate our own viewpoints and responses based on our understanding and creativity, ensuring that the answers are aligned with our own perspectives and style of expression. Additionally, by rephrasing the responses in our own words, we can better digest and absorb the information, thereby deepening our understanding of the question. Moreover, AI-generated responses may contain false and illusion information, so we have to go over and distinguish the responses.

