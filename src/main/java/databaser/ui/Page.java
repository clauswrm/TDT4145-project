package databaser.ui;

/**
 * Enum of all pages in the application that a user can navigate to.
 */
public enum Page {
    APPARATER("/Apparater.fxml", "Apparater"),
    DAGBOK("/Dagbok.fxml", "Dagbok"),
    MAIN_MENU("/MainMenu.fxml", "Hjem"),
    ØKT("/Økt.fxml", "Økt"),
    ØVELSER("/Øvelser.fxml", "Øvelse"),;

    private final String fxmlFile;
    private final String title;

    /**
     * Create a new page.
     *
     * @param fxmlFile filename of {@code .fxml} file.
     * @param title    Title for the page.
     */
    Page(String fxmlFile, String title) {
        this.fxmlFile = fxmlFile;
        this.title = UserInterface.appName + " - " + title;
    }

    public String fxmlPath() {
        return fxmlFile;
    }

    public String title() {
        return title;
    }
}