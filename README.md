# Banque-Misr-Task
### App screenshots
<p align="center">
  <img src="img/app1.png" height="400" width="200">
  <img src="img/app2.png" height="400" width="200">
  <img src="img/app3.png" height="400" width="200">
  <img src="img/app4.png" height="400" width="200">
  <img src="img/app5.png" height="400" width="200">
  <img src="img/app6.png" height="400" width="200">
</p>



### Built With:
• Kotlin <br />
• MVVM Architecture <br />
• Dependency Injection - Dagger-Hilt <br />
• Kotlin Coroutines <br />
• Retrofit <br />
• ROOM <br />
• Live Data <br />
• Shared Preference <br />
• Recycler View + DiffUtil <br />
• View Binding <br />
• Shimmer Effect <br />
• Picasso <br />
• Unit Test <br />


### MVVM Code Architecture:

<p align="left">
  <img src="img/arch.png" height="400" width="350">
</p>


### Requriments:

●	The app should support a minimum SDK version of 23 <br />
●	The app should fetch the trending repositories from the provided public API and display it to the users  <br />
●	While the data is being fetched, the app should show a loading state . Shimmer animation is optional <br />
●	If the app is not able to fetch the data, then it should show an error state to the user with an option to retry again  <br />
●	All the items in the list should be in their collapsed state by default and can be expanded on being tapped  <br />
●	Tapping any item will expand it to show more details and collapse any already expanded item. Tapping the same item in expanded state should collapse it <br />
●	The app should be able to handle configuration changes (like rotation) <br />
●	The app should have 100% offline support. Once the data is fetched successfully from remote, it should be stored locally and served from cache thereafter till the cache is not expired <br />
●	The cached data should only be valid for a duration of 2 hour after that the app should attempt to refresh the data from remote and purge the cache if successful <br />
●	The app should give a pull-to-refresh option to the user to force fetch data from remote  <br />


