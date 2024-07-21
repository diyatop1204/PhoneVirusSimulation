# Mobile Phone Virus Simulation
This Mobile Phone Virus Simulation written in Java 11, simulates virus transmission between phone images using threads, with dynamic phone creation and synchronized repair management.

Phones can be dynamically added and infected with specific keys (refer to user instructions below), and will move randomly. They are displayed in different colors based on their state (healthy, infected, or moving to the repair shop), and the virus spreads to other phones within 20 pixels. Infected phones have a life span of 500 frames before removal, and only one phone can be in the repair shop at a time. 

Thread synchronization ensures safe management of phone behaviors and interactions.

# SET UP & USER INSTRUCTIONS:
1. Ensure Java 11 is downloaded on your system. (Download Java 11: https://www.oracle.com/nz/java/technologies/javase/jdk11-archive-downloads.html)
2. Download 'MobilePhoneVirusSimulation' folder.
3. Locate Terminal/ Command Prompt.
4. Navigate to 'MobilePhoneVirusSimulation' directory
   Use the cd command to navigate to the directory containing the MazeProgram folder.
5. Run ```javac -d bin src/*.java```
6. Then, ```java -cp bin VirusSimulation``` to start the program.

## SIMULATION INSTRUCTIONS:
- Press the UP Arrow Key (↑): Increases the number of phones in the simulation.
- Press the 'V' Key: Randomly infects one of the phones.
