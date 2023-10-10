package advisor;

import advisor.Controller.MusicController;
import advisor.Model.UserDTO;
import advisor.View.MusicView;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {

        String accessServer = Constants.accessServer;
        String serverPath = Constants.serverPath;
        int pageNumber = 5;

        if (args.length > 0 && args[0].equals("-access") && args[2].equals("-resource") && args[4].equals("-page")) {
            accessServer = args[1];
            serverPath = args[3];
            pageNumber = Integer.parseInt(args[5]);
        }

        UserDTO user = new UserDTO();
        MusicView view = new MusicView();
        MusicController controller = new MusicController(user, view);


        Scanner scanner = new Scanner(System.in);
        String userAuth = scanner.nextLine();
        while(!userAuth.equals("auth") && !userAuth.equals(Constants.exit)){
            controller.incorrectResponse();
            userAuth = scanner.nextLine();
        }

        if (userAuth.equals(Constants.exit)) {
            controller.userAccessExit();
        }
        else {
            controller.getAuthCode();
            controller.getAccessToken(controller.getUserAuthCode(),accessServer);

            String userString = scanner.nextLine();

            String[] words = userString.split(" ",2);

            while (!userString.equals(Constants.exit)) {
                switch(words.length){
                    case 1:
                        switch (userString){
                            case "new" -> controller.getNewList(serverPath, pageNumber);
                            case "featured" -> controller.getFeaturedMusic(serverPath,pageNumber);
                            case "categories" -> controller.getCategories(serverPath, pageNumber);

                        }
                    case 2:
                        if(words[0].equals("playlists")){
                            String secondWord = words[1];
                            controller.getPlaylists(secondWord, serverPath, pageNumber);
                        }
                }
                userString = scanner.nextLine();
            }
            controller.exit();
        }
    }
}


