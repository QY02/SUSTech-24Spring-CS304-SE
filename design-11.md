# Campus Events and Entertainment Center

## Team 11

|**Name**|**Student ID**|
|:----|:----|
| 李冰   | 12110141   |
| 张颖萍 | 12111947   |
| 李华炫 | 12112045   |
| 陶毅诚 | 12112003   |
| 秦尧   | 12112016   |

# **Architectural Design**

![](/assets/Architectural_Design.jpg)

**Description：**

### Reason to Use This Architecture

This architecture follows a multi-tiered pattern, sometimes referred to as an N-tier architecture, where the presentation, application processing, and data management functions are physically separated. This allows for better modularity and flexibility in the development process.

### Natural Language Description:

1. **User Interface Layer:**
This layer is where users interact with the system through various user interface components. Components such as "User Register and Login," "Event Overview," and "Recommendations" facilitate user actions like creating an account, browsing events, and receiving personalized event suggestions. Other components like "Interactive Campus" might allow for a virtual exploration of a campus or event space, while "CSS Style Design" ensures consistent styling across all UI components.

2. **Service Interface Layer:**
This layer serves as the intermediary for communication between the front-end and the server's business logic. It's comprised of RESTful APIs like the "Event REST API" and "User REST API" which define how the UI layer sends requests and receives responses related to events and user data. For example, the "Event REST API" would manage calls for event details, whereas the "Interaction REST API" handles user interactions such as comments and replies.

3. **Business Logic Layer:**
The core computational logic of the application resides here. Services such as the "History Subscription Service" manage user activity history, and the "Comment Interaction Service" handles the logic for user comments and interactions on events. The "AI Recommendation System" likely uses machine learning to provide personalized event recommendations to users, while the "Notification Service" manages alerts and notifications sent to users.

4. **Data Access Layer:**
This layer provides access to the data stored in the database. It contains "mappers" like the "Event Mapper" and "User Mapper," which convert data between the database and the business services. These mappers ensure that data queries and transactions are performed effectively, abstracting the underlying database queries from the rest of the application.

5. **Database Layer:**
At this level, we have the actual databases, such as "Event," "Interaction," and "User," which are repositories for storing and retrieving all data relevant to the application. These databases are the persistent storage mechanisms that hold everything from user profiles to event details and interaction records.

6. **Middleware**/**Cross-Cutting Concerns:**
In addition to the main layers, there are services that address system-wide concerns like "Token Authentication" for security, "Redis Buffer" for caching, "Message Queue" using RabbitMQ for handling asynchronous message passing, "Logging Service" for system monitoring, and "Exception Handling" to manage errors across the application.

Each layer in this architecture is designed to handle specific aspects of the application, from user interaction to data management, with cross-cutting concerns providing essential support services throughout the architecture.


# UI Design
### HomePage


![](/assets/首页、本人申报、参加、收藏、浏览历史.png)

### Profile


![](/assets/个人页面.png)

### Apply for new evnets


![](/assets/申请活动.png)
![](/assets/申请活动(2).png)
![](/assets/申请活动(3).png)
![](/assets/申请活动(4).png)

### Reserve seats


![](/assets/9.png)
![](/assets/10.png)
![](/assets/11.png)
![](/assets/12.png)
![](/assets/13.png)

### Review event applications


![](/assets/审核.png)

### Notifications


![](/assets/通知页.png)

### Chat room


![](/assets/聊天室.png)

### News feed


![](/assets/动态页.png)
