package Download_Manager.UI;

public class Display {
    private static boolean isUseUI = true;
    private static MainMenu mainMenu = null;

    public static void print(String textToPrint) {
        if (!isUseUI) Display.print(textToPrint);
    }

    public static void printError(String textToPrint) {
        if (!isUseUI) Display.printError(textToPrint);
        else if(mainMenu != null) mainMenu.someProblemHappened(textToPrint);
    }

    public static void updatePercentToDisplay(int percent){
        if (!isUseUI) Display.print("Downloaded " + percent + "%");
        else{
            if(mainMenu != null) {
                mainMenu.setProgressBar(percent);
                Display.print(percent + "%");
            }
        }
    }

    public static void setMainMenu(MainMenu newMainMenu){
        mainMenu = newMainMenu;
    }

    public static boolean getIsUseUI(){
        return isUseUI;
    }

    public static void setIsUseUI(boolean newValue){
        isUseUI = newValue;
    }
}
