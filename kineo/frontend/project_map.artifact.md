# Project Map - Kineo Android Application

This document provides a comprehensive overview of the project structure, including all source files, resources, and configuration files.

## Project Structure Overview

```text
Kineo/
├── app/                        # Main application module
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/           # Java source code (Clean Architecture)
│   │   │   ├── res/            # Android resources (Layouts, Drawables, Values)
│   │   │   └── AndroidManifest.xml
│   │   └── build.gradle        # Module-level build configuration
├── build.gradle                # Project-level build configuration
├── settings.gradle             # Project settings
└── gradle.properties           # Gradle properties
```

## Source Code (Java)

### Core Application
- [KineoApplication.java](file:///C:/Users/forja/Desktop/Kineo_new/Kineo/app/src/main/java/com/dam/kineo/KineoApplication.java)

### Data Layer
Handles data persistence, remote API communication, and mapping.

- **Local (Room Database)**
    - [KineoDatabase.java](file:///C:/Users/forja/Desktop/Kineo_new/Kineo/app/src/main/java/com/dam/kineo/data/local/KineoDatabase.java)
    - **DAOs:** [BadgeDao.java](file:///C:/Users/forja/Desktop/Kineo_new/Kineo/app/src/main/java/com/dam/kineo/data/local/dao/BadgeDao.java), [ChallengeDao.java](file:///C:/Users/forja/Desktop/Kineo_new/Kineo/app/src/main/java/com/dam/kineo/data/local/dao/ChallengeDao.java), [FriendshipDao.java](file:///C:/Users/forja/Desktop/Kineo_new/Kineo/app/src/main/java/com/dam/kineo/data/local/dao/FriendshipDao.java), [RoutePointDao.java](file:///C:/Users/forja/Desktop/Kineo_new/Kineo/app/src/main/java/com/dam/kineo/data/local/dao/RoutePointDao.java), [StepSessionDao.java](file:///C:/Users/forja/Desktop/Kineo_new/Kineo/app/src/main/java/com/dam/kineo/data/local/dao/StepSessionDao.java)
    - **Entities:** [BadgeEntity.java](file:///C:/Users/forja/Desktop/Kineo_new/Kineo/app/src/main/java/com/dam/kineo/data/local/entity/BadgeEntity.java), [ChallengeEntity.java](file:///C:/Users/forja/Desktop/Kineo_new/Kineo/app/src/main/java/com/dam/kineo/data/local/entity/ChallengeEntity.java), [FriendshipEntity.java](file:///C:/Users/forja/Desktop/Kineo_new/Kineo/app/src/main/java/com/dam/kineo/data/local/entity/FriendshipEntity.java), [RoutePointEntity.java](file:///C:/Users/forja/Desktop/Kineo_new/Kineo/app/src/main/java/com/dam/kineo/data/local/entity/RoutePointEntity.java), [StepSessionEntity.java](file:///C:/Users/forja/Desktop/Kineo_new/Kineo/app/src/main/java/com/dam/kineo/data/local/entity/StepSessionEntity.java)
- **Remote (Retrofit)**
    - [KineoApiService.java](file:///C:/Users/forja/Desktop/Kineo_new/Kineo/app/src/main/java/com/dam/kineo/data/remote/KineoApiService.java)
    - [MockInterceptor.java](file:///C:/Users/forja/Desktop/Kineo_new/Kineo/app/src/main/java/com/dam/kineo/data/remote/MockInterceptor.java)
    - **DTOs:** [AuthResponse.java](file:///C:/Users/forja/Desktop/Kineo_new/Kineo/app/src/main/java/com/dam/kineo/data/remote/dto/AuthResponse.java), [BadgeDto.java](file:///C:/Users/forja/Desktop/Kineo_new/Kineo/app/src/main/java/com/dam/kineo/data/remote/dto/BadgeDto.java), [ChallengeDto.java](file:///C:/Users/forja/Desktop/Kineo_new/Kineo/app/src/main/java/com/dam/kineo/data/remote/dto/ChallengeDto.java), [FriendDto.java](file:///C:/Users/forja/Desktop/Kineo_new/Kineo/app/src/main/java/com/dam/kineo/data/remote/dto/FriendDto.java), [LeaderboardEntryDto.java](file:///C:/Users/forja/Desktop/Kineo_new/Kineo/app/src/main/java/com/dam/kineo/data/remote/dto/LeaderboardEntryDto.java)
- **Repositories (Implementations)**
    - [BadgeRepositoryImpl.java](file:///C:/Users/forja/Desktop/Kineo_new/Kineo/app/src/main/java/com/dam/kineo/data/repository/BadgeRepositoryImpl.java), [ChallengeRepositoryImpl.java](file:///C:/Users/forja/Desktop/Kineo_new/Kineo/app/src/main/java/com/dam/kineo/data/repository/ChallengeRepositoryImpl.java), [FriendRepositoryImpl.java](file:///C:/Users/forja/Desktop/Kineo_new/Kineo/app/src/main/java/com/dam/kineo/data/repository/FriendRepositoryImpl.java), [StepRepositoryImpl.java](file:///C:/Users/forja/Desktop/Kineo_new/Kineo/app/src/main/java/com/dam/kineo/data/repository/StepRepositoryImpl.java)

### Domain Layer
Contains business logic and model definitions.

- **Models:** [Badge.java](file:///C:/Users/forja/Desktop/Kineo_new/Kineo/app/src/main/java/com/dam/kineo/domain/model/Badge.java), [Challenge.java](file:///C:/Users/forja/Desktop/Kineo_new/Kineo/app/src/main/java/com/dam/kineo/domain/model/Challenge.java), [Friend.java](file:///C:/Users/forja/Desktop/Kineo_new/Kineo/app/src/main/java/com/dam/kineo/domain/model/Friend.java), [LeaderboardEntry.java](file:///C:/Users/forja/Desktop/Kineo_new/Kineo/app/src/main/java/com/dam/kineo/domain/model/LeaderboardEntry.java), [StepSession.java](file:///C:/Users/forja/Desktop/Kineo_new/Kineo/app/src/main/java/com/dam/kineo/domain/model/StepSession.java)
- **Use Cases:** [CheckBadgesUseCase.java](file:///C:/Users/forja/Desktop/Kineo_new/Kineo/app/src/main/java/com/dam/kineo/domain/usecase/CheckBadgesUseCase.java), [GetLeaderboardUseCase.java](file:///C:/Users/forja/Desktop/Kineo_new/Kineo/app/src/main/java/com/dam/kineo/domain/usecase/GetLeaderboardUseCase.java), [UpdateStepsUseCase.java](file:///C:/Users/forja/Desktop/Kineo_new/Kineo/app/src/main/java/com/dam/kineo/domain/usecase/UpdateStepsUseCase.java)

### UI Layer
Handles user interface, fragments, and ViewModels.

- **Main/Home:** [MainActivity.java](file:///C:/Users/forja/Desktop/Kineo_new/Kineo/app/src/main/java/com/dam/kineo/ui/MainActivity.java), [HomeFragment.java](file:///C:/Users/forja/Desktop/Kineo_new/Kineo/app/src/main/java/com/dam/kineo/ui/home/HomeFragment.java), [HomeViewModel.java](file:///C:/Users/forja/Desktop/Kineo_new/Kineo/app/src/main/java/com/dam/kineo/ui/home/HomeViewModel.java)
- **Auth:** [LoginActivity.java](file:///C:/Users/forja/Desktop/Kineo_new/Kineo/app/src/main/java/com/dam/kineo/ui/auth/LoginActivity.java), [RegisterActivity.java](file:///C:/Users/forja/Desktop/Kineo_new/Kineo/app/src/main/java/com/dam/kineo/ui/auth/RegisterActivity.java), [AuthViewModel.java](file:///C:/Users/forja/Desktop/Kineo_new/Kineo/app/src/main/java/com/dam/kineo/ui/auth/AuthViewModel.java)
- **Social:** [SocialFragment.java](file:///C:/Users/forja/Desktop/Kineo_new/Kineo/app/src/main/java/com/dam/kineo/ui/social/SocialFragment.java), [SocialViewModel.java](file:///C:/Users/forja/Desktop/Kineo_new/Kineo/app/src/main/java/com/dam/kineo/ui/social/SocialViewModel.java)
- **Map:** [MapaFragment.java](file:///C:/Users/forja/Desktop/Kineo_new/Kineo/app/src/main/java/com/dam/kineo/ui/map/MapaFragment.java), [MapViewModel.java](file:///C:/Users/forja/Desktop/Kineo_new/Kineo/app/src/main/java/com/dam/kineo/ui/map/MapViewModel.java)
- **Adapters:** [BadgePreviewAdapter.java](file:///C:/Users/forja/Desktop/Kineo_new/Kineo/app/src/main/java/com/dam/kineo/ui/home/BadgePreviewAdapter.java), [ChallengesAdapter.java](file:///C:/Users/forja/Desktop/Kineo_new/Kineo/app/src/main/java/com/dam/kineo/ui/social/ChallengesAdapter.java), [FriendsAdapter.java](file:///C:/Users/forja/Desktop/Kineo_new/Kineo/app/src/main/java/com/dam/kineo/ui/social/FriendsAdapter.java)

### Background Services
- [StepCounterService.java](file:///C:/Users/forja/Desktop/Kineo_new/Kineo/app/src/main/java/com/dam/kineo/service/StepCounterService.java)

## Resources (res)

### Layouts (XML)
- **Activities:** [activity_main.xml](file:///C:/Users/forja/Desktop/Kineo_new/Kineo/app/src/main/res/layout/activity_main.xml), [activity_login.xml](file:///C:/Users/forja/Desktop/Kineo_new/Kineo/app/src/main/res/layout/activity_login.xml), [activity_register.xml](file:///C:/Users/forja/Desktop/Kineo_new/Kineo/app/src/main/res/layout/activity_register.xml)
- **Fragments:** [fragment_home.xml](file:///C:/Users/forja/Desktop/Kineo_new/Kineo/app/src/main/res/layout/fragment_home.xml), [fragment_social.xml](file:///C:/Users/forja/Desktop/Kineo_new/Kineo/app/src/main/res/layout/fragment_social.xml), [fragment_map.xml](file:///C:/Users/forja/Desktop/Kineo_new/Kineo/app/src/main/res/layout/fragment_map.xml)
- **Items:** [item_badge_preview.xml](file:///C:/Users/forja/Desktop/Kineo_new/Kineo/app/src/main/res/layout/item_badge_preview.xml), [item_challenge.xml](file:///C:/Users/forja/Desktop/Kineo_new/Kineo/app/src/main/res/layout/item_challenge.xml), [item_friend.xml](file:///C:/Users/forja/Desktop/Kineo_new/Kineo/app/src/main/res/layout/item_friend.xml)

### Values
- [colors.xml](file:///C:/Users/forja/Desktop/Kineo_new/Kineo/app/src/main/res/values/colors.xml)
- [strings.xml](file:///C:/Users/forja/Desktop/Kineo_new/Kineo/app/src/main/res/values/strings.xml)
- [themes.xml](file:///C:/Users/forja/Desktop/Kineo_new/Kineo/app/src/main/res/values/themes.xml)

### Navigation & Menus
- [nav_graph.xml](file:///C:/Users/forja/Desktop/Kineo_new/Kineo/app/src/main/res/navigation/nav_graph.xml)
- [bottom_nav_menu.xml](file:///C:/Users/forja/Desktop/Kineo_new/Kineo/app/src/main/res/menu/bottom_nav_menu.xml)
