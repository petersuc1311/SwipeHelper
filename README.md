# SwipeHelper
Small utility to display a background option when swiping in recyclerview.

Returns an ItemTouchHelper.SimpleCallback object which can be attached to your recyclerview. 
You can integrate your icon, background color and logic which should be exeuted when the swipe occures.
If you have a OnRefreshListener registered you can sync the swipe behavior with the crossinline argument onSwipeRefresherHandling
for a better UX behavior.
