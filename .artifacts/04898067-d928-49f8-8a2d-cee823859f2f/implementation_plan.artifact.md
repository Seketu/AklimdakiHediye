# Fix Add People Button in UserMainView

The "Add People" button in the home screen (`UserMainView`) logs a message but doesn't show the dialog because the logic to display `AddPeopleDialog` is commented out in `UserMainContent`.

## Proposed Changes

### Home Screen

#### [MODIFY] [UserMainView.kt](file:///C:/Users/avina/StudioProjects/AklimdakiHediye/app/src/main/java/com/reylortechnology/aklimdakihediye/Views/MainScreens/UserMainView.kt)
- Uncomment the `when(addPeoplePopUp)` block that shows `AddPeopleDialog` inside `UserMainContent`.

## Verification Plan

### Manual Verification
- Click the "+" (Add People) card on the home screen.
- Verify that the `AddPeopleDialog` appears correctly.
