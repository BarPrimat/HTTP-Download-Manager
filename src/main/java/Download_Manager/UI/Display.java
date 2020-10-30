package Download_Manager.UI;

public class Display {
    private static boolean isUseUI = true;
    private static MainMenu mainMenu = null;

    public static void print(String textToPrint) {
        if (!isUseUI) System.out.println(textToPrint);
    }

    public static void printError(String textToPrint) {
        if (!isUseUI) System.err.println(textToPrint);
    }

    public static void updatePercentToDisplay(int percent){
        if (!isUseUI) System.out.println("Downloaded " + percent + "%");
        else{
            if(mainMenu != null) mainMenu.setProgressBar(percent);
        }
    }

    public static void setMainMenu(MainMenu newMainMenu){
        mainMenu = newMainMenu;
    }
}
