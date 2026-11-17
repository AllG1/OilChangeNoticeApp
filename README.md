# OilChangeNoticeApp
A simple app that tells me whether today is an oil change day.


## Why I started this project
I work at a restaurant where we change the fryer oil every two days.
I keep forgetting whether today is an oil change day or not.
To be honest, Google Calendar would be an easier and faster way to track this.
But I wanted to try vibe coding with Copilot, and I need a simple widget anyway.


## Tech Stack
I'm using tools I rarely work with:
 - Android Studio
 - Kotlin


## Algorithm
The app compares dates to decide what today is.

```
if ((today - lastOilChangeDate) % 2 == 0) → Oil change day
else → Filter day
```


## Features
 - **Adjustable oil change date**: I can reset the base date when the schedule shifts. For example, if the restaurant is closed on a holiday and we change the oil early.
 - **1x1 home screen widget**: A simple oil icon. When the icon is colored, it's an oil change day. When it's gray, it's a filter day.
