# 7Hub

7Hub is a mobile application built using **Jetpack Compose** and **MVVM Architecture**. This application is designed to help Yeditepe University students select their courses more easily, access information about courses, instructors, and course materials within the app, and stay informed about club activities.

## Features

- **Authentication**: Secure login and user authentication.
- **Chat System**: Real-time messaging functionality.
- **Club Management**: Users can view and join clubs.
- **Course Review**: Allows users to review and rate courses.
- **UI Components**: Implements Material Design with Jetpack Compose.

## Learning Outcomes

This project helped improve my understanding and skills in:

- Using **Jetpack Compose** for modern UI development.
- Implementing **MVVM Architecture** effectively.
- Handling **Firestore Database** for real-time data management.
- Creating **reusable UI components** with Jetpack Compose.
- Managing **LiveData & ViewModels** efficiently.

## Tech Stack

- **Kotlin** - Programming language
- **Jetpack Compose** - UI Framework
- **MVVM Architecture** - Design pattern
- **Firestore** - Database
- **Coroutines & Flow** - Asynchronous programming
- **Hilt** - Dependency Injection

## Screenshots

### Splash Screen

| Splash Screen Animation |
| ----- |
| <img src="https://github.com/user-attachments/assets/0b63e059-aee4-4c58-926a-0354dd31402a" width="300" /> |


### Login and Reset

| Login | Reset |
| ----- | ----- |
| <img src="https://github.com/user-attachments/assets/2419001b-1715-4de7-bcd9-43bbdfc7b431" width="300"/> | <img src="https://github.com/user-attachments/assets/c84a4abc-135c-4de2-95da-1f2803df7cc3" width="300"/> |

### Home

| Home Screen |
| ----- |
| <img src="https://github.com/user-attachments/assets/a36b7a72-604f-44f6-bd36-53bc1d96cd12" width="300"/> |

### Clubs

| Club 1 |
| ------ |
| <img src="https://github.com/user-attachments/assets/5a42766a-d308-4ccf-8574-db1109e8b3ed" width="300"/> |

### Courses

| Course 1 | Course 2 |
| -------- | -------- |
| <img src="https://github.com/user-attachments/assets/668fb54e-bf66-4727-80d6-c3021f69a160" width="300"/> | <img src="https://github.com/user-attachments/assets/a287736a-fa9b-40b2-b7c6-8903f0ba31a1" width="300"/> |

### Reviews

| Review 1 | Review 2 |
| -------- | -------- |
| <img src="https://github.com/user-attachments/assets/267606a3-1e49-4b9e-9242-89dc46f4e1d4" width="300"/> | <img src="https://github.com/user-attachments/assets/5304bd62-e98b-47d5-ba9f-4a08f9056dcc" width="300"/> |

### Groups

| Groups | Create New Group |
| ------ | --------------- |
| <img src="https://github.com/user-attachments/assets/ae03095b-94a0-4498-9772-0e5b3bc136bd" width="300"/> | <img src="https://github.com/user-attachments/assets/73672c0d-fe33-4ba3-9638-f77773b16caa" width="300"/> |

### Chats

| Chat 1 | Chat 2 |
| ------ | ------ |
| <img src="https://github.com/user-attachments/assets/59e9a0cc-978f-49fa-882e-151355ff4089" width="300"/> | <img src="https://github.com/user-attachments/assets/187d6ba1-02f2-47e2-8e1b-a97b754819aa" width="300"/> |

### Account

| Account Screen |
| ----- |
| <img src="https://github.com/user-attachments/assets/3e869ab8-2a16-4eba-a78a-ec54fabf8b6f" width="300"/> |



## 📁 Project Structure
📦 7Hub  
┣ 📂 app  
┃ ┣ 📂 src  
┃ ┃ ┣ 📂 main  
┃ ┃ ┃ ┣ 📂 java/com/example/myapplication  
┃ ┃ ┃ ┃ ┣ 📂 Components  
┃ ┃ ┃ ┃ ┣ 📂 DataLayer/Models  
┃ ┃ ┃ ┃ ┃ ┣ 📄 ChatData.kt  
┃ ┃ ┃ ┃ ┃ ┣ 📄 ClubDataModel.kt  
┃ ┃ ┃ ┃ ┃ ┣ 📄 CourseModel.kt  
┃ ┃ ┃ ┃ ┃ ┣ 📄 DataRepository.kt  
┃ ┃ ┃ ┃ ┃ ┣ 📄 HelpDataModel.kt  
┃ ┃ ┃ ┃ ┃ ┣ 📄 InstructorData.kt  
┃ ┃ ┃ ┃ ┃ ┗ 📄 UserData.kt  
┃ ┃ ┃ ┃ ┣ 📂 ui.theme  
┃ ┃ ┃ ┃ ┃ ┣ 📄 Color.kt  
┃ ┃ ┃ ┃ ┃ ┣ 📄 Theme.kt  
┃ ┃ ┃ ┃ ┃ ┣ 📄 Type.kt  
┃ ┃ ┃ ┃ ┃ ┗ 📄 CustomShadowButton.kt  
┃ ┃ ┃ ┃ ┣ 📂 Views  
┃ ┃ ┃ ┃ ┃ ┣ 📂 AccountView  
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📄 AccountScreen.kt  
┃ ┃ ┃ ┃ ┃ ┃ ┗ 📄 AccountViewModel.kt  
┃ ┃ ┃ ┃ ┃ ┣ 📂 ClubsView  
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📄 ClubDetail.kt  
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📄 Clubs.kt  
┃ ┃ ┃ ┃ ┃ ┃ ┗ 📄 ClubDetailViewModel.kt  
┃ ┃ ┃ ┃ ┃ ┣ 📂 CourseView  
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📄 Course.kt  
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📄 CourseCard.kt  
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📄 CourseDetail.kt  
┃ ┃ ┃ ┃ ┃ ┃ ┗ 📄 CourseDetailViewModel.kt  
┃ ┃ ┃ ┃ ┃ ┣ 📂 HelpView  
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📄 HelpScreen.kt  
┃ ┃ ┃ ┃ ┃ ┃ ┗ 📄 HelpViewModel.kt  
┃ ┃ ┃ ┃ ┃ ┣ 📂 ChatList  
┃ ┃ ┃ ┃ ┃ ┃ ┣ 📄 ChatListScreen.kt  
┃ ┃ ┃ ┃ ┃ ┃ ┗ 📄 ChatListViewModel.kt  
┃ ┃ ┃ ┃ ┃ ┗ 📂 ... (Diğer View'lar)  
┃ ┃ ┃ ┃ ┣ 📄 MainActivity.kt  
┃ ┃ ┃ ┃ ┗ 📄 MyAppNavigation.kt  
┃ ┃ ┃ ┗ 📂 res  
┃ ┃ ┃ ┃ ┣ 📂 drawable  
┃ ┃ ┃ ┃ ┃ ┣ 📄 app_logo.png  
┃ ┃ ┃ ┃ ┃ ┣ 📄 ic_arrow_left.xml  
┃ ┃ ┃ ┃ ┃ ┣ 📄 ic_star_filled.xml  
┃ ┃ ┃ ┃ ┃ ┣ 📄 teacher_1.png  
┃ ┃ ┃ ┃ ┃ ┗ 📄 ... (20+ asset)  
┃ ┣ 📄 build.gradle  
┗ ┗ 📄 ... (Diğer root dosyalar)


### 🗂️ Key Directories
- **`DataLayer/Models`**: Data models and Firestore connections.
- **`ui.theme`**: Custom themes and Material Design components.
- **`Views`**: All screens and ViewModels (MVVM).
- **`res/drawable`**: Application icons, images, and XML assets.


---


## Installation

1. **Clone the repository**  
   Open your terminal and run the following command to clone the repository:  
   ```sh
   git clone https://github.com/yourusername/7Hub.git

2. **Navigate to the project directory**
    ```sh
    cd 7Hub

3. **Open the project in Android Studio**
- ```sh
  Open Android Studio
  Select "Open an Existing Project"
  Choose the 7Hub folder
4. **Sync Gradle and Run the App**

## Contact

For any questions or feedback, you can reach me at:

- 📧 Email: [gurkankaradas42@gmail.com](mailto:gurkankaradas42@gmail.com)
- 🐙 GitHub: [@imacapella](https://github.com/imacapella)
- 💼 LinkedIn: [Gürkan Karadaş](https://linkedin.com/in/gurkankaradas/)
