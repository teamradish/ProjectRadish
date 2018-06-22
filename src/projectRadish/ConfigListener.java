package projectRadish;

public interface ConfigListener
{
    /**
     * A method invoked when the config is loaded.
     */
    void configLoaded();

    /**
     * A method invoked when the config is saved.
     */
    void configSaved();
}
