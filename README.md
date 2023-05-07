# Visionary - Java Based Desktop Drawing App

---

### Project Description

This project is made for CS-UY 3913 (Java)\
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
        - When shift is pressed, the shape being drawn will be automatically formatted (rectangle becomes square, oval becomes circle, etc.)
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
        - A page displaying all drawings belonged to the user sorted by the last modified date will be shown, in which user can choose to edit any of them, and sync the changes as well
3. Register/Login System:
    - User must register an account to use the app
    - Once properly entered the username and password, user is able to log in, and see all of their drawings on "cloud" server
4. Server:
    - Communicate with client using socket
        - Each request will create a new thread, allowing server the process multiple request concurrently
    - Session management
        - Once user logged in, a unique session token will be sent to user, and user is able to do any authenticated operations with the session token
        - Each session token will be expired after 30 days
    - User File Management:
        - User's drawing is stored on server machine's file system, with image's hash as primary search key
5. Security:
    - Password is securely hashed before storing into the database
    - SQL statement is carefully composed to avoid SQL injection attack
    - all images stored on server machine is encrypted with AES CTR mode on client side, making the image content access exclusive to client/user
    - User's secret key is generated with user's password and salt using Password-Based Key Derivation Function 2 (pbkdf2) with HMAC-SHA256, where the salt is stored on the server side, and password is only known by the user (password stored on server is not the original password), allowing user to decrypt their own cloud drawings but no one else could do that
    - Example(left: user's drawing; right: encrypted image stored on server):\
      <img src="https://user-images.githubusercontent.com/53324229/236570351-95bdc8ec-1576-4de6-a983-5d8cad9282ca.png" width="250">
      <img src="https://user-images.githubusercontent.com/53324229/236570411-4e855474-c8bf-45ba-a599-3761e53eacb3.png" width="250">

### Project Showcase

<img src="https://user-images.githubusercontent.com/53324229/236704196-98764bbc-74a4-49f6-8fa4-4b59c554ab60.png" width="300">
<img src="https://user-images.githubusercontent.com/53324229/236704204-9ffb3e4a-1c0e-4604-b142-035ef720674e.png" width="300">

### Example Drawings Created From the App

<img src="https://user-images.githubusercontent.com/53324229/236704244-d2f5b603-d669-42cf-92a4-8708df78238c.png" width="200">
<img src="https://user-images.githubusercontent.com/53324229/236704247-eb7c0acc-16a0-4701-91f3-a966b45acc25.png" width="200">
<img src="https://user-images.githubusercontent.com/53324229/236704248-1c816487-559c-4acf-893c-6ac10a4c9afb.png" width="200">

### External Packages Used

1. mariadb: JDBC driver
2. password4j: used for securely hash the password
