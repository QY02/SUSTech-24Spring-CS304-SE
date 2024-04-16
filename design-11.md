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

1. **Database Layer**:

   - Contains two database entities: `Event` and `User`, with a third entity, `Interaction`, connecting to the `User` entity.

2. **Data Access Layer**:

   - Includes `Event Mapper` for accessing `Event` and `EventDetail`, `Interaction Mapper` for managing interactions like `EntityAttachmentRelation` and `Comment`, and `User Mapper` for user-related data such as `User`, `History` and `Favorite`.

3. **Business (Logic) Layer**:

   - Features multiple services including `History`, `Subscription`, `Comment`, `Interaction`, `Event`, `Third Party Map`, `AI Recommendation System`, `LLM System`, `File Storage System`, `AliPay Sandbox`, `Notification`, `User`, and `Seat`.

4. **Service Interface Layer**:

   - This layer shows `Event REST API`, `Interaction REST API` and `User REST API`, which provide endpoints for interaction between users and the system.

5. **User Interface Layer**:

   - Comprised of various UI components for features such as `User Register and Login`, `Event Overview`, `Event Recommendations`, `Event Publishment`, `Interactive Campus`, `Event Details`, `Review and Rate`, `User Profile Management`, `Reservation and Booking`, `Navigation Layout`, `UI Process`, and `CSS Style`.

6. **Middleware**:
   - There are also some middleware and additional components including `Token Authentication`, `Redis Buffer`, `Message Queue (Rabbit MQ)`, `Logging Service`, `Permission Control`, and `Exception Handling`.

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
