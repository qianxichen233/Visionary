

# Visionary - Java Based Drawing App
---
### Project Description
This project is made for CS-UY 3913 (Java)
The project implemented a full-stack desktop application that can help user to create their own drawing, with features including:
1. Various Painting Tools:
	- Pen Tool:
		- With optimized algorithm to achieve a smooth drawing line
	- Color Selector:
		- Can use two colors at the same time. Left mouse button corresponds to main color and right mouse button corresponds to secondary color.
		- 16 preset colors for convenience use
		- Also support choose arbitrary colors from color panel or entering color code
	- Size Tool:
		- Allow to change pen size
	- Shape drawer:
		- Allow users to draw accurate shapes
		- Supported shapes: line, rectangle, oval, triangle
	- Eraser:
		- Able to erase traces on the canvas
	- Color picker:
		- Allow users to pick colors directly from canvas
	- Filler:
		- Allow users to fill an enclosed area with color
		- Implemented with Flood Fill algorithm
	- Selector:
		- Able to select arbitrary rectangle area on the canvas
		- Once selected, user can move the selected area to anywhere they want
		- Allow user to copy the selected area, which saves the clip into user's clipboard and allow to paste anywhere even outside of the app
		- Also able to paste clips from clipboard to canvas, and move to anywhere on the canvas
	- Undo/Redo:
		- Allow users to undo or redo their changes, compatible with all tools above
		- Implemented the feature using a circular array with three pointers
	- Rename:
		- User can rename current file
2. Drawing File Management
	- Local file:
		- User can export their drawing to local machine
		- User can also import their previous work or other image file to the application
	- Remote file:
		- An option to save the drawing to "cloud" server is supported
		- User can sync their drawing with "cloud" server, and retrieve their drawing whenever they want
		- A page displaying all drawings belonged to the user sorted by the last modified date will be shown, in which user can choose to edit any of them, and async the changes as well
3. Register/Login System:
	- User must register an account to use the app
	- Once properly entered the username and password, user is able to log in, and see all of their drawings on "cloud" server
4. Server:
	- Communicate with client using socket
		- Each request will create a new thread, allowing server the process multiple request concurrently
	- Session management
		- Once user logged in, a unique session token will be sent to user, and user is able to do any authenticated operations with the session token
		- Each session token will be expired after 30 days
	- Security:
		- Password is securly hashed before storing into the database
		- SQL statement is carefully composed to avoid SQL injection attack
	- User File Management:
		- User's drawing is stored on server machine's file system, with image's hash as primary search key
### Project Showcase
### External Packages Used
1. mariadb: JDBC driver
2. password4j: used for securly hash the password
