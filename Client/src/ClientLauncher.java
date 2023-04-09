public class ClientLauncher {
    public final static int PORT = 1337;
    public static void main(String[] args) {

        Client_simple clientSimple;
        try {
            clientSimple = new Client_simple(PORT) ;
            clientSimple.charger();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}